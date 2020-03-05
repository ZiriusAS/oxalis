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

package eu.sendregning.oxalis;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import no.difi.certvalidator.Validator;
import no.difi.oxalis.outbound.OxalisOutboundComponent;
import no.difi.vefa.peppol.common.model.*;

import java.io.*;
import java.net.URI;
import java.security.cert.X509Certificate;
import no.difi.oxalis.api.outbound.TransmissionRequest;
import no.difi.oxalis.api.outbound.TransmissionResponse;
import no.difi.oxalis.api.outbound.Transmitter;
import no.difi.oxalis.sniffer.PeppolStandardBusinessHeader;
import no.difi.oxalis.sniffer.sbdh.SbdhWrapper;

/**
 * @author Steinar O. Cook
 * @author Nigel Parker
 * @author Thore Johnsen
 * @author erlend
 */
@Slf4j
public class CustomMain {

    private static CustomMain main = null;
    
    private static OxalisOutboundComponent oxalisOutboundComponent = null;
    
    private Boolean isTestEnvironment;
    private String oxalisUrl;
    private String oxalisCertificatePath;
    private final Tracer tracer;
    
    public Boolean getTestEnvironment() {
        return isTestEnvironment;
    }

    public void setTestEnvironment(Boolean isTestEnvironment) {
        this.isTestEnvironment = isTestEnvironment;
    }

    public String getOxalisAS4Url() {
        return oxalisUrl;
    }

    public void setOxalisAS4Url(String OxalisAs4Url) {
        this.oxalisUrl = OxalisAs4Url;
    }

    public String getOxalisCertificatePath() {
        return oxalisCertificatePath;
    }

    public void setOxalisCertificatePath(String OxalisCertificatePath) {
        this.oxalisCertificatePath = OxalisCertificatePath;
    }
    
    private CustomMain(Boolean testEnv, String OxalisUrl, String oxalisCertPath) {
        
        this.isTestEnvironment = testEnv;
        this.oxalisUrl = OxalisUrl;
        this.oxalisCertificatePath = oxalisCertPath;
        this.tracer = getOutBoundComponent().getInjector().getInstance(Tracer.class);
    }
    
    public static CustomMain getInstance(Boolean testEnv, String oxalisUrl, String oxalisCertPath) {
        
        if (main == null) {
            main = new CustomMain(testEnv, oxalisUrl, oxalisCertPath);
        }
        
        return main;
    }
    
    private static OxalisOutboundComponent getOutBoundComponent() {

        if (oxalisOutboundComponent == null) {
            oxalisOutboundComponent = new OxalisOutboundComponent();
        }
        return oxalisOutboundComponent;
    }
    
    public byte[] wrapPayLoadWithSBDH(byte[] content, PeppolStandardBusinessHeader effectiveStandardBusinessHeader) throws Exception {

        try (InputStream is = new ByteArrayInputStream(content)) {
            
            SbdhWrapper sbdhWrapper = new SbdhWrapper();
            return sbdhWrapper.wrap(is, effectiveStandardBusinessHeader.toVefa());
        } 
    }

    public PeppolStandardBusinessHeader createSBDH(String sender, String receiver, String documentType, String processType) throws Exception {

        PeppolStandardBusinessHeader sbdh = PeppolStandardBusinessHeader.createPeppolStandardBusinessHeaderWithNewDate();
        sbdh.setDocumentTypeIdentifier(DocumentTypeIdentifier.of(documentType));
        sbdh.setProfileTypeIdentifier(ProcessIdentifier.of(processType));
        sbdh.setSenderId(ParticipantIdentifier.of(sender));
        sbdh.setRecipientId(ParticipantIdentifier.of(receiver));
        return sbdh;
    }
    
    public String send(String filepath) throws Exception {

    	Span span = null;
        try {

            TransmissionParameters params = new TransmissionParameters(getOutBoundComponent());

            if (this.isTestEnvironment) {
                
                try (InputStream inputStream = new FileInputStream(this.oxalisCertificatePath)) {

                	X509Certificate certificate = Validator.getCertificate(inputStream);
                	params.setEndpoint(Endpoint.of(TransportProfile.PEPPOL_AS4_2_0, URI.create(this.oxalisUrl), certificate));
                }
            }

            File xmlPayloadFile = new File(filepath);

            TransmissionTask transmissionTask = new TransmissionTask(params, xmlPayloadFile);

            span = tracer.buildSpan("standalone").start();

            TransmissionRequest transmissionRequest = transmissionTask.createTransmissionRequest(span);
            Transmitter transmitter = getOutBoundComponent().getTransmitter();

            TransmissionResponse response = transmitter.transmit(transmissionRequest, span);

            return response.getTransmissionIdentifier().getIdentifier();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }  finally {
        	span.finish();
        }
    }
    
    public TransmissionResponse sendDocumentUsingFactory(InputStream inputStream) throws Exception {

        Span span = null;
        if (inputStream != null) {

            try {
                
                span = tracer.buildSpan("standalone").start();
            	TransmissionParameters params = new TransmissionParameters(getOutBoundComponent());
            		
            	TransmissionResponse transmissionResponse = params.getOxalisOutboundComponent()
        				.getTransmissionService()
        				.send(inputStream, params.getTag(), span);
        		
                return transmissionResponse;
            	
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                span.finish();
            }
        }         
        throw new IOException("Input Stream cloased or not available");
    }
}
