/*
 * Copyright 2010-2018 Norwegian Agency for Public Management and eGovernment (Difi)
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/community/eupl/og_page/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package no.difi.oxalis.outbound.transmission;

import com.google.inject.Inject;
import io.opentracing.Span;
import io.opentracing.Tracer;
import no.difi.oxalis.api.error.ErrorTracker;
import no.difi.oxalis.api.lang.OxalisTransmissionException;
import no.difi.oxalis.api.lookup.LookupService;
import no.difi.oxalis.api.model.Direction;
import no.difi.oxalis.api.outbound.*;
import no.difi.oxalis.api.statistics.StatisticsService;
import no.difi.oxalis.api.transmission.TransmissionVerifier;
import no.difi.oxalis.commons.mode.OxalisCertificateValidator;
import no.difi.oxalis.commons.tracing.Traceable;
import no.difi.vefa.peppol.common.code.Service;
import no.difi.vefa.peppol.common.model.Endpoint;
import no.difi.vefa.peppol.common.model.TransportProfile;
import no.difi.vefa.peppol.security.lang.PeppolSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes transmission requests by sending the payload to the requested destination.
 * Updates statistics for the transmission using the configured RawStatisticsRepository.
 * <p>
 * Will log an error if the recording of statistics fails for some reason.
 *
 * @author steinar
 * @author thore
 * @author erlend
 */
class DefaultTransmitter extends Traceable implements Transmitter {

    /**
     * Factory used to fetch implementation of required transport profile implementation.
     */
    private final MessageSenderFactory messageSenderFactory;

    /**
     * Service to report statistics when transmission is successfully transmitted.
     */
    private final StatisticsService statisticsService;

    private final TransmissionVerifier transmissionVerifier;

    private final LookupService lookupService;

    private final OxalisCertificateValidator certificateValidator;

    private final ErrorTracker errorTracker;
    
    private final static Logger log = LoggerFactory.getLogger(DefaultTransmitter.class);

    @Inject
    public DefaultTransmitter(MessageSenderFactory messageSenderFactory, StatisticsService statisticsService,
                              TransmissionVerifier transmissionVerifier, LookupService lookupService, Tracer tracer,
                              OxalisCertificateValidator certificateValidator, ErrorTracker errorTracker) {
        super(tracer);
        this.messageSenderFactory = messageSenderFactory;
        this.statisticsService = statisticsService;
        this.transmissionVerifier = transmissionVerifier;
        this.lookupService = lookupService;
        this.certificateValidator = certificateValidator;
        this.errorTracker = errorTracker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransmissionResponse transmit(TransmissionMessage transmissionMessage, Span root)
            throws OxalisTransmissionException {
        Span span = tracer.buildSpan("transmit").asChildOf(root).start();
        try {
            return perform(transmissionMessage, span);
        } finally {
            span.finish();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransmissionResponse transmit(TransmissionMessage transmissionMessage) throws OxalisTransmissionException {
        Span root = tracer.buildSpan("transmit").start();
        try {
            return perform(transmissionMessage, root);
        } finally {
            root.finish();
        }
    }

    private TransmissionResponse perform(TransmissionMessage transmissionMessage, Span root)
            throws OxalisTransmissionException {
        
        log.info("### Transmission Started ###");
        try {
            if (transmissionMessage == null)
                throw new OxalisTransmissionException("No transmission is provided.");

            log.info("### Transmission verfication begins ###");
            transmissionVerifier.verify(transmissionMessage.getHeader(), Direction.OUT);
            log.info("### Transmission verfication ended ###");

            TransmissionRequest transmissionRequest;
            if (transmissionMessage instanceof TransmissionRequest) {
                transmissionRequest = (TransmissionRequest) transmissionMessage;

                // Validate provided certificate
                if (transmissionRequest.getEndpoint().getCertificate() == null)
                    throw new OxalisTransmissionException("Certificate of receiving access point is not provided.");
                
                log.info("### certificate validation begins ###");
                certificateValidator.validate(Service.AP, transmissionRequest.getEndpoint().getCertificate(), root);
                log.info("### certificate validation ended ###");
            } else {
                // Perform lookup using header.
                Span span = tracer.buildSpan("Fetch endpoint information").asChildOf(root).start();
                Endpoint endpoint;
                try {
                    
                    log.warn("### LookUp started for the trasmission ###");
                    
                    endpoint = lookupService.lookup(transmissionMessage.getHeader(), span);
                    span.setTag("transport profile", endpoint.getTransportProfile().getIdentifier());
                    transmissionRequest = new DefaultTransmissionRequest(transmissionMessage, endpoint);
                } catch (OxalisTransmissionException e) {
                    span.setTag("exception", e.getMessage());
                    log.error("### LookUp ended with an error for the trasmission ###",e);
                    throw e;
                } finally {
                    span.finish();
                }
            }
            
            log.warn("### LookUp ended for the trasmission without any error ###");

            log.info("### Span begins ###");
            Span span = tracer.buildSpan("send message").asChildOf(root).start();
            TransmissionResponse transmissionResponse;
            try {
                TransportProfile transportProfile = transmissionRequest.getEndpoint().getTransportProfile();
                
                log.info("### MessageSender Trasmission begins ###");
                MessageSender messageSender = messageSenderFactory.getMessageSender(transportProfile);
                transmissionResponse = messageSender.send(transmissionRequest, span);
               log.info("### MessageSender Trasmission ended ###");
                
            } catch (OxalisTransmissionException e) {
                span.setTag("exception", e.getMessage());
                log.error("### Trasmission failed ###",e);
                throw e;
            } finally {
                span.finish();
                log.info("###Span finished###");
            }

            log.info("### Presist Trasmission details begins ###");
            statisticsService.persist(transmissionRequest, transmissionResponse, root);
            log.info("### Presist Trasmission details ended ###");

            return transmissionResponse;
        } catch (PeppolSecurityException e) {
            errorTracker.track(Direction.OUT, e, true);
            throw new OxalisTransmissionException("Unable to verify certificate of receiving access point.", e);
        } catch (OxalisTransmissionException e) {
            errorTracker.track(Direction.OUT, e, true);
            throw e;
        } catch (RuntimeException e) {
            errorTracker.track(Direction.OUT, e, false);
            throw e;
        }
    }
}
