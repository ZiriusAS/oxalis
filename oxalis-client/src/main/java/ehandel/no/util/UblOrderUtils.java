/*
 * @(#)UblOrderUtils.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ehandel.no.EHFConstants;
import ehandel.no.dto.AddressDTO;
import ehandel.no.dto.AllowanceChargeDTO;
import ehandel.no.dto.CurrencyDTO;
import ehandel.no.dto.CustomerDTO;
import ehandel.no.dto.DeliveryDTO;
import ehandel.no.dto.FileDTO;
import ehandel.no.dto.InvoiceLineItemDTO;
import ehandel.no.dto.OrderDTO;
import ehandel.no.dto.OrderResponseDTO;
import ehandel.no.dto.SubstitutedLineItemDTO;
import ehandel.no.dto.SupplierDTO;
import ehandel.no.order.AccountingCostCommonBasic;
import ehandel.no.order.AddressType;
import ehandel.no.order.AllowanceChargeReasonCodeCommonBasic;
import ehandel.no.order.AllowanceChargeReasonCommonBasic;
import ehandel.no.order.AllowanceChargeType;
import ehandel.no.order.AllowanceTotalAmountCommonBasic;
import ehandel.no.order.AmountCommonBasic;
import ehandel.no.order.AttachmentType;
import ehandel.no.order.BaseQuantityCommonBasic;
import ehandel.no.order.BuildingNumberCommonBasic;
import ehandel.no.order.ChargeIndicatorCommonBasic;
import ehandel.no.order.ChargeTotalAmountCommonBasic;
import ehandel.no.order.CityNameCommonBasic;
import ehandel.no.order.CommodityClassificationType;
import ehandel.no.order.CompanyIDCommonBasic;
import ehandel.no.order.ContactType;
import ehandel.no.order.CountrySubentityCodeCommonBasic;
import ehandel.no.order.CountryType;
import ehandel.no.order.CustomerPartyType;
import ehandel.no.order.CustomizationIDCommonBasic;
import ehandel.no.order.DeliveryTermsCommonAggregate;
import ehandel.no.order.DescriptionCommonBasic;
import ehandel.no.order.DocumentCurrencyCodeCommonBasic;
import ehandel.no.order.DocumentReferenceType;
import ehandel.no.order.DocumentTypeCommonBasic;
import ehandel.no.order.ElectronicMailCommonBasic;
import ehandel.no.order.EmbeddedDocumentBinaryObjectCommonBasic;
import ehandel.no.order.EndDateCommonBasic;
import ehandel.no.order.EndpointIDCommonBasic;
import ehandel.no.order.IDCommonBasic;
import ehandel.no.order.IdentificationCodeCommonBasic;
import ehandel.no.order.IssueDateCommonBasic;
import ehandel.no.order.IssueTimeCommonBasic;
import ehandel.no.order.ItemClassificationCodeCommonBasic;
import ehandel.no.order.ItemIdentificationType;
import ehandel.no.order.ItemPropertyType;
import ehandel.no.order.ItemType;
import ehandel.no.order.LineExtensionAmountCommonBasic;
import ehandel.no.order.LineIDCommonBasic;
import ehandel.no.order.LineItemType;
import ehandel.no.order.LineStatusCodeCommonBasic;
import ehandel.no.order.MonetaryTotalType;
import ehandel.no.order.NameCommonBasic;
import ehandel.no.order.NoteCommonBasic;
import ehandel.no.order.Order;
import ehandel.no.order.OrderLineCommonAggregate;
import ehandel.no.order.OrderLineReferenceCommonAggregate;
import ehandel.no.order.OrderReferenceCommonAggregate;
import ehandel.no.order.OrderResponse;
import ehandel.no.order.OrderResponseCodeCommonBasic;
import ehandel.no.order.PartyIdentificationCommonAggregate;
import ehandel.no.order.PartyLegalEntityCommonAggregate;
import ehandel.no.order.PartyNameCommonAggregate;
import ehandel.no.order.PartyType;
import ehandel.no.order.PayableAmountCommonBasic;
import ehandel.no.order.PayableRoundingAmountCommonBasic;
import ehandel.no.order.PercentCommonBasic;
import ehandel.no.order.PeriodType;
import ehandel.no.order.PostalZoneCommonBasic;
import ehandel.no.order.PostboxCommonBasic;
import ehandel.no.order.PriceAmountCommonBasic;
import ehandel.no.order.PriceType;
import ehandel.no.order.ProfileIDCommonBasic;
import ehandel.no.order.QuantityCommonBasic;
import ehandel.no.order.StreetNameCommonBasic;
import ehandel.no.order.SupplierPartyType;
import ehandel.no.order.TaxAmountCommonBasic;
import ehandel.no.order.TaxCategoryType;
import ehandel.no.order.TaxExclusiveAmountCommonBasic;
import ehandel.no.order.TaxInclusiveAmountCommonBasic;
import ehandel.no.order.TaxSchemeCommonAggregate;
import ehandel.no.order.TaxTotalType;
import ehandel.no.order.TelefaxCommonBasic;
import ehandel.no.order.TelephoneCommonBasic;
import ehandel.no.order.TotalTaxAmountCommonBasic;
import ehandel.no.order.UBLVersionIDCommonBasic;
import ehandel.no.order.ValueCommonBasic;
import ehandel.no.order.WebsiteURICommonBasic;
import ehandel.no.order.DeliveryType;
import ehandel.no.order.RegistrationNameCommonBasic;
import ehandel.no.order.StartDateCommonBasic;
import java.util.Date;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.XMLGregorianCalendar;
        

/**
 * The Class UblOrderUtils.
 * 
 * @author vasanthis
 * @since Oct 18, 2013
 */
public class UblOrderUtils {

    /**
     * Instantiates a new order utils.
     */
    public UblOrderUtils() {
    }

    /**
     * Generate ehf order xml.
     *
     * @param orderDTO the order dto
     * @param filePath the file path
     * @throws JAXBException the jAXB exception
     */
    public static void generateEHFOrderXML(OrderDTO orderDTO, String filePath)
            throws JAXBException {

        Order order = convertToOrder(orderDTO);

        Marshaller marshaller = getMarshaller();

        marshaller.marshal(order, new File(filePath));
    }

    /**
     * Generate ehf order xml.
     *
     * @param orderDTO the order dto
     * @return the byte[]
     * @throws JAXBException the jAXB exception
     */
    public static byte[] generateEHFOrderXML(OrderDTO orderDTO) throws JAXBException {

        Order order = convertToOrder(orderDTO);

        Marshaller marshaller = getMarshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(order, out);

        return out.toByteArray();
    }

    /**
     * Gets the eHF order.
     *
     * @param is the is
     * @return the eHF order
     * @throws JAXBException the jAXB exception
     */
    public static OrderDTO getEHFOrder(InputStream is) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Order.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        Order order = (Order) unmarshaller.unmarshal(is);

        return convertToEHFOrder(order);
    }

    /**
     * Convert to ehf order.
     *
     * @param order the order
     * @return the order dto
     */
    public static OrderDTO convertToEHFOrder(Order order) {

        OrderDTO orderDTO = null;

        if (order != null) {

            orderDTO = new OrderDTO();

            UBLVersionIDCommonBasic ublVersionIDCommonBasic = order.getUBLVersionID();
            if (ublVersionIDCommonBasic != null) {
                orderDTO.setUblVersionId(ublVersionIDCommonBasic.getValue());
            }

            CustomizationIDCommonBasic customizationIDCommonBasic = order.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                orderDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            ProfileIDCommonBasic profileIDCommonBasic = order.getProfileID();
            if (profileIDCommonBasic != null) {
                orderDTO.setProfileId(profileIDCommonBasic.getValue());
            }

            IDCommonBasic idCommonBasic = order.getID();
            if (idCommonBasic != null) {
                orderDTO.setOrderNo(idCommonBasic.getValue());
            }

            IssueDateCommonBasic issueDateCommonBasic = order.getIssueDate();
            if (issueDateCommonBasic != null) {
                orderDTO.setOrderIssueDate(ConversionUtils.asDate(issueDateCommonBasic.getValue()));
            }

            AccountingCostCommonBasic accountingCostCommonBasic = order.getAccountingCost();
            if (accountingCostCommonBasic != null) {
                orderDTO.setAccountingCost(accountingCostCommonBasic.getValue());
            }

            List<PeriodType> validityPeriods = order.getValidityPeriods();
            if (validityPeriods.size() > 0) {
                EndDateCommonBasic endDateCommonBasic = validityPeriods.size() > 0 ? validityPeriods.get(0).getEndDate() : null;
                orderDTO.setValidityEndDate(ConversionUtils.asDate(endDateCommonBasic.getValue()));
            }

            DocumentReferenceType documentReferenceType = order.getQuotationDocumentReference();
            if (documentReferenceType != null) {
                orderDTO.setQuotationDocumentReferenceId(documentReferenceType.getID().getValue());
            }

            List<DocumentReferenceType> documentReferenceTypes = order.getOrderDocumentReferences();
            if (documentReferenceTypes.size() > 0) {
                orderDTO.setOrderDocumentReferenceId(documentReferenceTypes.get(0).getID().getValue());
            }

            documentReferenceType = order.getOriginatorDocumentReference();
            if (documentReferenceType != null) {
                orderDTO.setOriginatorDocumentReferenceId(documentReferenceType.getID().getValue());
            }
            
            List<TaxTotalType> taxTotalTypes = order.getTaxTotals();
            
            if(taxTotalTypes != null && taxTotalTypes.size() > 0) {
                
                orderDTO.setTaxAmount(taxTotalTypes.get(0).getTaxAmount().getValue().doubleValue());
            }
            
            List<DocumentReferenceType> additionalDocumentReferences =
                    order.getAdditionalDocumentReferences();

            if (additionalDocumentReferences != null) {

                AttachmentType attachmentType = null;
                DocumentTypeCommonBasic documentTypeCommonBasic = null;
                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocumentBinaryObject = null;

                List<FileDTO> files = new ArrayList<FileDTO>();
                FileDTO fileDTO = null;

                for (DocumentReferenceType additionalDocumentReference : additionalDocumentReferences) {

                    documentTypeCommonBasic = additionalDocumentReference.getDocumentType();
                    fileDTO = new FileDTO();

                    if (documentTypeCommonBasic != null) {
                        fileDTO.setDocType(documentTypeCommonBasic.getValue());
                    }

                    attachmentType = additionalDocumentReference.getAttachment();
                    if (attachmentType != null) {
                        embeddedDocumentBinaryObject =
                                attachmentType.getEmbeddedDocumentBinaryObject();
                        if (embeddedDocumentBinaryObject != null) {
                            
                            fileDTO.setFileName(embeddedDocumentBinaryObject.getFilename());

                            fileDTO.setFileContent(embeddedDocumentBinaryObject.getValue());
                        }
                    }
                    files.add(fileDTO);
                }
                orderDTO.setFiles(files);
            }

            mapCurrency(order, orderDTO);
            mapOrderCustomer(order, orderDTO);
            mapOrderSupplier(order, orderDTO);
            mapAllowanceCharges(order, orderDTO);
            mapOrderLine(order, orderDTO);
        }
        return orderDTO;
    }

    public static void generateEHFOrderResponseXML(OrderResponseDTO orderResponseDTO, String filePath)
            throws JAXBException, Exception {

        OrderResponse orderResponse = convertToOrderResponse(orderResponseDTO);

        Marshaller marshaller = getMarshaller();

        marshaller.marshal(orderResponse, new File(filePath));
    }

    public static byte[] generateEHFOrderResponseXML(OrderResponseDTO orderResponseDTO) throws JAXBException, Exception {

        OrderResponse orderResponse = convertToOrderResponse(orderResponseDTO);

        Marshaller marshaller = getMarshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(orderResponse, out);

        return out.toByteArray();
    }

    public static OrderResponseDTO getEHFOrderResponse(InputStream is) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(OrderResponse.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        OrderResponse orderResponse = (OrderResponse) unmarshaller.unmarshal(is);

        return convertToEHFOrderResponse(orderResponse);
    }

    public static OrderResponseDTO convertToEHFOrderResponse(OrderResponse orderResponse) {

        OrderResponseDTO orderResponseDTO = null;

        if (orderResponse != null) {

            orderResponseDTO = new OrderResponseDTO();

            UBLVersionIDCommonBasic ublVersionIDCommonBasic = orderResponse.getUBLVersionID();
            if (ublVersionIDCommonBasic != null) {
                orderResponseDTO.setUblVersionId(ublVersionIDCommonBasic.getValue());
            }

            CustomizationIDCommonBasic customizationIDCommonBasic = orderResponse.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                orderResponseDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            ProfileIDCommonBasic profileIDCommonBasic = orderResponse.getProfileID();
            if (profileIDCommonBasic != null) {
                orderResponseDTO.setProfileId(profileIDCommonBasic.getValue());
            }

            IDCommonBasic idCommonBasic = orderResponse.getID();
            if (idCommonBasic != null) {
                orderResponseDTO.setOrderResponseNo(idCommonBasic.getValue());
            }

            List<NoteCommonBasic> notes = orderResponse.getNotes();
            if(notes.size() > 0) {
                NoteCommonBasic note = notes.get(0);
                orderResponseDTO.setNote(note.getValue());
            }

            IssueDateCommonBasic issueDateCommonBasic = orderResponse.getIssueDate();
            IssueTimeCommonBasic issueTimeCommonBasic = orderResponse.getIssueTime();
            if (issueDateCommonBasic != null) {
                XMLGregorianCalendar date = issueDateCommonBasic.getValue();
                XMLGregorianCalendar time = issueTimeCommonBasic.getValue();
                date.setTime(time.getHour(), time.getMinute(), time.getSecond());
                orderResponseDTO.setOrderResponseIssueDate(ConversionUtils.asDate(date));
            }

            mapCurrency(orderResponse, orderResponseDTO);
            mapOrderResponseCustomer(orderResponse, orderResponseDTO);
            mapOrderResponseSupplier(orderResponse, orderResponseDTO);
            mapOrderResponseLine(orderResponse, orderResponseDTO);
        }
        return orderResponseDTO;
    }

    /**
     * Convert to order.
     *
     * @param orderDTO the order dto
     * @return the order
     */
    private static Order convertToOrder(OrderDTO orderDTO) {

        Order order = null;

        if (orderDTO != null) {

            order = new Order();

            CustomizationIDCommonBasic customizationIDCommonBasic =
                    new CustomizationIDCommonBasic();
            customizationIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_ORDER_CUSTOMIZATION_ID.getValue());
            order.setCustomizationID(customizationIDCommonBasic);

            ProfileIDCommonBasic profileIDCommonBasic = new ProfileIDCommonBasic();
            profileIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_ORDER_PROFILE_ID.getValue());
            order.setProfileID(profileIDCommonBasic);

            IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(orderDTO.getOrderNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(orderDTO.getOrderNo());
                order.setID(idCommonBasic);
            }

            if (orderDTO.getOrderIssueDate() != null) {
                IssueDateCommonBasic issueDateCommonBasic = new IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(orderDTO.getOrderIssueDate()));
                order.setIssueDate(issueDateCommonBasic);
            }

            if (!StringUtils.isEmpty(orderDTO.getAccountingCost())) {
                AccountingCostCommonBasic accountingCostCommonBasic = new AccountingCostCommonBasic();
                accountingCostCommonBasic.setValue(orderDTO.getAccountingCost());
                order.setAccountingCost(accountingCostCommonBasic);
            }

            if (orderDTO.getValidityEndDate() != null) {
                PeriodType periodType = new PeriodType();
                EndDateCommonBasic endDateCommonBasic = new EndDateCommonBasic();
                endDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(orderDTO.getValidityEndDate()));
                periodType.setEndDate(endDateCommonBasic);
                order.getValidityPeriods().add(periodType);
            }

            DocumentReferenceType documentReferenceType = null;
            if (!StringUtils.isEmpty(orderDTO.getQuotationDocumentReferenceId())) {
                documentReferenceType = new DocumentReferenceType();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(orderDTO.getQuotationDocumentReferenceId());
                documentReferenceType.setID(idCommonBasic);
                order.setQuotationDocumentReference(documentReferenceType);
            }

            if (!StringUtils.isEmpty(orderDTO.getOrderDocumentReferenceId())) {
                documentReferenceType = new DocumentReferenceType();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(orderDTO.getOrderDocumentReferenceId());
                documentReferenceType.setID(idCommonBasic);
                order.getOrderDocumentReferences().add(documentReferenceType);
            }
            if (!StringUtils.isEmpty(orderDTO.getOriginatorDocumentReferenceId())) {
                documentReferenceType = new DocumentReferenceType();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(orderDTO.getOriginatorDocumentReferenceId());
                documentReferenceType.setID(idCommonBasic);
                order.setOriginatorDocumentReference(documentReferenceType);
            }

            List<FileDTO> files = orderDTO.getFiles();
            if (files != null && !files.isEmpty()) {

                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCommonBasic = null;
                documentReferenceType = null;
                DocumentTypeCommonBasic documentTypeCommonBasic = null;
                AttachmentType attachmentType = null;

                int i = 0;
                for (FileDTO fileDTO : files) {

                    i++;
                    embeddedDocBinObjCommonBasic = new EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(fileDTO.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(fileDTO.getMimeType());
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode(EHFConstants.PDF_CONTENT_TYPE.getValue());
                    }
                    embeddedDocBinObjCommonBasic.setValue(fileDTO.getFileContent());
                    embeddedDocBinObjCommonBasic.setFilename(fileDTO.getFileName());

                    documentReferenceType = new DocumentReferenceType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(EHFConstants.DOCUMENT.getValue() + i);
                    documentReferenceType.setID(idCommonBasic);

                    documentTypeCommonBasic = new DocumentTypeCommonBasic();
                    documentTypeCommonBasic.setValue(fileDTO.getDocType());
                    documentReferenceType.setDocumentType(documentTypeCommonBasic);

                    attachmentType = new AttachmentType();
                    attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocBinObjCommonBasic);
                    documentReferenceType.setAttachment(attachmentType);

                    order.getAdditionalDocumentReferences().add(documentReferenceType);
                }
                i = 0;
            }
        }

        mapCurrency(orderDTO, order);
        mapOrderCustomer(orderDTO, order);
        mapOrderSupplier(orderDTO, order);
        mapOriginatorCustomer(orderDTO, order);
        mapAllowanceCharges(orderDTO, order);
        mapOrderLine(orderDTO, order);

        return order;
    }

    /**
     * Map allowance charges.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapAllowanceCharges(OrderDTO orderDTO, Order order) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeType allowanceChargeType = null;

        List<AllowanceChargeDTO> allowanceCharges = orderDTO.getAllowanceCharges();
        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            double allowanceTotalAmount = 0d;
            double chargeTotalAmount = 0d;

            String currencyCode = "";
            CurrencyDTO currencyDTO = orderDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            for (AllowanceChargeDTO allowanceChargeDTO : allowanceCharges) {

                allowanceChargeType = new AllowanceChargeType();

                chargeIndicatorCommonBasic = new ChargeIndicatorCommonBasic();
                chargeIndicatorCommonBasic.setValue(allowanceChargeDTO.isChargeIndicator());
                allowanceChargeType.setChargeIndicator(chargeIndicatorCommonBasic);

                if (allowanceChargeDTO.getAllowanceChargeReason() != null) {

                    allowanceChargeReasonCommonBasic = new AllowanceChargeReasonCommonBasic();
                    allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                    allowanceChargeType.getAllowanceChargeReasons().add(allowanceChargeReasonCommonBasic);
                }

                if (allowanceChargeDTO.getAmount() != null) {
                    amountCommonBasic = new AmountCommonBasic();
                    amountCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getAmount()));
                    amountCommonBasic.setCurrencyID(currencyCode);
                    allowanceChargeType.setAmount(amountCommonBasic);

                    if (allowanceChargeDTO.isChargeIndicator()) {
                        chargeTotalAmount += allowanceChargeDTO.getAmount();
                    } else {
                        allowanceTotalAmount += allowanceChargeDTO.getAmount();
                    }
                }

                order.getAllowanceCharges().add(allowanceChargeType);
            }

            MonetaryTotalType anticipatedMonetaryTotal = new MonetaryTotalType();

            if (allowanceTotalAmount > 0) {
                AllowanceTotalAmountCommonBasic allowanceTotalAmountCB =
                        new AllowanceTotalAmountCommonBasic();
                allowanceTotalAmountCB.setValue(ConversionUtils.asBigDecimal(allowanceTotalAmount));
                allowanceTotalAmountCB.setCurrencyID(currencyCode);
                anticipatedMonetaryTotal.setAllowanceTotalAmount(allowanceTotalAmountCB);
            }

            if (chargeTotalAmount > 0) {
                ChargeTotalAmountCommonBasic chargeTotalAmountCB =
                        new ChargeTotalAmountCommonBasic();
                chargeTotalAmountCB.setValue(ConversionUtils.asBigDecimal(chargeTotalAmount));
                chargeTotalAmountCB.setCurrencyID(currencyCode);
                anticipatedMonetaryTotal.setChargeTotalAmount(chargeTotalAmountCB);
            }
            order.setAnticipatedMonetaryTotal(anticipatedMonetaryTotal);
        }
    }

    /**
     * Map allowance charges.
     *
     * @param order the order
     * @param orderDTO the order dto
     */
    private static void mapAllowanceCharges(Order order, OrderDTO orderDTO) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeDTO allowanceChargeDTO = null;
        List<TaxCategoryType> taxCategories = null;
        TaxCategoryType taxCategoryType = null;
        PercentCommonBasic percentCommonBasic = null;
        TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        List<AllowanceChargeType> allowanceCharges = order.getAllowanceCharges();

        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            for (AllowanceChargeType allowanceChargeType : allowanceCharges) {

                chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();
				allowanceChargeReasonCommonBasic = allowanceChargeType
						.getAllowanceChargeReasons().size() > 0 ? allowanceChargeType
						.getAllowanceChargeReasons().get(0) : null;
                amountCommonBasic = allowanceChargeType.getAmount();
                if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                    allowanceChargeDTO = new AllowanceChargeDTO();
                    allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                    allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeReasonCommonBasic.getValue());
                    allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                    taxCategories = allowanceChargeType.getTaxCategories();
                    if (taxCategories != null && !taxCategories.isEmpty()) {

                        taxCategoryType = taxCategories.size() > 0 ? taxCategories.get(0) : null;
                        percentCommonBasic = taxCategoryType.getPercent();
                        if (percentCommonBasic != null) {
                            allowanceChargeDTO.setTaxPercent(percentCommonBasic.getValue().doubleValue());
                        }
                        taxSchemeCommonAggregate = taxCategoryType.getTaxScheme();
                        if (taxSchemeCommonAggregate != null
                                && taxSchemeCommonAggregate.getID() != null) {
                            allowanceChargeDTO.setTaxType(taxSchemeCommonAggregate.getID().getValue());
                        }
                    }
                    orderDTO.getAllowanceCharges().add(allowanceChargeDTO);
                }
            }
        }
    }

    /**
     * Map currency.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapCurrency(OrderDTO orderDTO, Order order) {

        CurrencyDTO currencyDTO = orderDTO.getCurrencyDTO();

        if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
            DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                    new DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
            documentCurrencyCodeCommonBasic.setValue(currencyDTO.getCurrencyCode());
            order.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);
        }
    }

    private static void mapCurrency(OrderResponse orderResponse, OrderResponseDTO orderResponseDTO) {

        DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic = orderResponse.getDocumentCurrencyCode();
        
        if (documentCurrencyCodeCommonBasic != null) {
            CurrencyDTO currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getName());
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            orderResponseDTO.setCurrencyDTO(currencyDTO);
        }
    }

    /**
     * Map currency.
     *
     * @param order the order
     * @param orderDTO the order dto
     */
    private static void mapCurrency(Order order, OrderDTO orderDTO) {

        DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                order.getDocumentCurrencyCode();
        CurrencyDTO currencyDTO = null;

        if (documentCurrencyCodeCommonBasic != null) {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getValue());
            orderDTO.setCurrencyDTO(currencyDTO);
        }
    }

    /**
     * Gets the marshaller.
     *
     * @return the marshaller
     * @throws JAXBException the jAXB exception
     */
    private static Marshaller getMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Order.class);

        final Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, EHFConstants.CHAR_ENCODING.getValue());
        try {

            Object mapper = EHFInternalNamespacePrefixMapper.getInstance();
            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", mapper);

        } catch (PropertyException e) {

            Object mapper = EHFNamespacePrefixMapper.getInstance();
            marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", mapper);
        }

        return marshaller;
    }

    /**
     * Gets the buyer customer party.
     *
     * @param customerDTO the customer dto
     * @return the buyer customer party
     */
    private static CustomerPartyType getBuyerCustomerParty(CustomerDTO customerDTO, boolean isOrder) {

        PartyType party = new PartyType();
        PartyType payeePartyType = new PartyType();
        AddressType postalAddress = new AddressType();
        AddressType registrationAddress = new AddressType();
        CustomerPartyType customerPartyType = new CustomerPartyType();
        PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate = new PartyLegalEntityCommonAggregate();

        IDCommonBasic idCommonBasic = null;
        EndpointIDCommonBasic endpointIDCB = null;

        boolean isPartyLegalEntity = false;
        boolean isRegistrationAddress = true;

        String country = "";

        AddressDTO addressDTO = customerDTO.getAddressDTO();
        if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
            country = addressDTO.getCountryCode().toUpperCase();
        }

        if (!StringUtils.isEmpty(customerDTO.getEndpointId())) {
            endpointIDCB = new EndpointIDCommonBasic();
            endpointIDCB.setSchemeID(customerDTO.getEaID());
            endpointIDCB.setValue(customerDTO.getEndpointId());
            party.setEndpointID(endpointIDCB);
        }

        if (!StringUtils.isEmpty(customerDTO.getId())) {
            idCommonBasic = new IDCommonBasic();
            idCommonBasic.setValue(customerDTO.getId());
            PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                    new PartyIdentificationCommonAggregate();
            partyIdentificationCommonAggregate.setID(idCommonBasic);

            party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            payeePartyType.getPartyIdentifications().add(partyIdentificationCommonAggregate);
        }

        if (!StringUtils.isEmpty(customerDTO.getName())) {
            NameCommonBasic nameCommonBasic = new NameCommonBasic();
            nameCommonBasic.setValue(customerDTO.getName());
            PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
            partyName.setName(nameCommonBasic);

            if(isOrder) party.getPartyNames().add(partyName);
            payeePartyType.getPartyNames().add(partyName);
        }

        ContactType contactType = new ContactType();
        boolean isContactAvailable = false;

        if (!StringUtils.isEmpty(customerDTO.getTelePhone())) {
            TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
            telephoneCommonBasic.setValue(customerDTO.getTelePhone());
            contactType.setTelephone(telephoneCommonBasic);
            isContactAvailable = true;
        }

        if (!StringUtils.isEmpty(customerDTO.getEmail())) {
            ElectronicMailCommonBasic electronicMailCommonBasic =
                    new ElectronicMailCommonBasic();
            electronicMailCommonBasic.setValue(customerDTO.getEmail());
            contactType.setElectronicMail(electronicMailCommonBasic);
            isContactAvailable = true;
        }

        if (isOrder && isContactAvailable) {
            party.setContact(contactType);
        }

        if (isOrder && addressDTO != null) {

            boolean isAddressAvailable = false;

            if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                streetNameCommonBasic.setValue(addressDTO.getStreetName());
                postalAddress.setStreetName(streetNameCommonBasic);
                isAddressAvailable = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                PostalZoneCommonBasic postalZoneCommonBasic = new PostalZoneCommonBasic();
                postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                postalAddress.setPostalZone(postalZoneCommonBasic);
                isAddressAvailable = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                cityNameCommonBasic.setValue(addressDTO.getCityName());
                postalAddress.setCityName(cityNameCommonBasic);
                registrationAddress.setCityName(cityNameCommonBasic);
                isAddressAvailable = true;
                isRegistrationAddress = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {

                IdentificationCodeCommonBasic identificationCodeCommonBasic =
                        new IdentificationCodeCommonBasic();
                identificationCodeCommonBasic.setValue(country);
                identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                CountryType countryType = new CountryType();
                countryType.setIdentificationCode(identificationCodeCommonBasic);
                postalAddress.setCountry(countryType);
                registrationAddress.setCountry(countryType);
                isAddressAvailable = true;
                isRegistrationAddress = true;
            }

            if (isAddressAvailable) {
                party.setPostalAddress(postalAddress);
            }
        }

        if (isOrder && isRegistrationAddress) {
            partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
        }
        
        RegistrationNameCommonBasic registrationNameCommonBasic = new RegistrationNameCommonBasic();
        registrationNameCommonBasic.setValue(customerDTO.getName());
        partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCommonBasic);
        
        if (isPartyLegalEntity || isRegistrationAddress) {
            party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            payeePartyType.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
        }
        
        customerPartyType.setParty(party);
        return customerPartyType;
    }

    /**
     * Map order customer.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapOrderCustomer(OrderDTO orderDTO, Order order) {

        CustomerDTO customerDTO = orderDTO.getCustomerDTO();

        if (customerDTO != null) {
            CustomerPartyType customerPartyType = getBuyerCustomerParty(customerDTO,true);
            order.setBuyerCustomerParty(customerPartyType);
        }
    }

    /**
     * Map order customer.
     *
     * @param order the order
     * @param orderDTO the order dto
     */
    private static void mapOrderCustomer(Order order, OrderDTO orderDTO) {

        CustomerPartyType customerPartyType = order.getBuyerCustomerParty();
        CustomerDTO customerDTO = null;
        if (customerPartyType != null) {
            customerDTO = getCustomer(customerPartyType);

        }
        orderDTO.setCustomerDTO(customerDTO);
    }

    private static CustomerDTO getCustomer(CustomerPartyType customerPartyType) {
        CustomerDTO customerDTO = new CustomerDTO();
        AddressDTO addressDTO = null;

        PartyType party = customerPartyType.getParty();
        if (party != null) {

            EndpointIDCommonBasic endpointIDCB = party.getEndpointID();
            if (endpointIDCB != null) {
                customerDTO.setEndpointId(endpointIDCB.getValue());
                customerDTO.setEaID(endpointIDCB.getSchemeID());
            }

            IDCommonBasic idCommonBasic = null;
            for (PartyIdentificationCommonAggregate partyIdentificationCommonAggregate : party.getPartyIdentifications()) {
                idCommonBasic = partyIdentificationCommonAggregate.getID();
                if (idCommonBasic != null) {
                    customerDTO.setId(idCommonBasic.getValue());
                }
            }

            NameCommonBasic nameCommonBasic = null;
            for (PartyNameCommonAggregate partyNameCommonAggregate : party.getPartyNames()) {
                nameCommonBasic = partyNameCommonAggregate.getName();
                if (nameCommonBasic != null) {
                    customerDTO.setName(nameCommonBasic.getValue());
                }
            }

            CompanyIDCommonBasic companyIDCommonBasic = null;
            for (PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate : party.getPartyLegalEntities()) {
                companyIDCommonBasic = partyLegalEntityCommonAggregate.getCompanyID();
                if (companyIDCommonBasic != null) {
                    customerDTO.setOrganizationNo(companyIDCommonBasic.getValue());
                }
            }

            AddressType postalAddress = party.getPostalAddress();

            if (postalAddress != null) {

                addressDTO = new AddressDTO();

                idCommonBasic = postalAddress.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }

                BuildingNumberCommonBasic buildingNumberCommonBasic =
                        postalAddress.getBuildingNumber();
                if (buildingNumberCommonBasic != null) {
                    addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                }

                StreetNameCommonBasic streetNameCommonBasic = postalAddress.getStreetName();
                if (streetNameCommonBasic != null) {
                    addressDTO.setStreetName(streetNameCommonBasic.getValue());
                }

                PostalZoneCommonBasic postalZoneCommonBasic = postalAddress.getPostalZone();
                if (postalZoneCommonBasic != null) {
                    addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                }

                PostboxCommonBasic postboxCommonBasic = postalAddress.getPostbox();
                if (postboxCommonBasic != null) {
                    addressDTO.setPostalBox(postboxCommonBasic.getValue());
                }

                CityNameCommonBasic cityNameCommonBasic = postalAddress.getCityName();
                if (cityNameCommonBasic != null) {
                    addressDTO.setCityName(cityNameCommonBasic.getValue());
                }

                CountryType countryType = postalAddress.getCountry();
                if (countryType != null) {
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            countryType.getIdentificationCode();
                    if (identificationCodeCommonBasic != null) {
                        addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                    }
                }
            }

            WebsiteURICommonBasic websiteURICommonBasic = party.getWebsiteURI();
            if (websiteURICommonBasic != null) {
                addressDTO.setUrl(websiteURICommonBasic.getValue());
            }

            customerDTO.setAddressDTO(addressDTO);
        }

        ContactType contactType = party.getContact();
        if (contactType != null) {

            IDCommonBasic idCommonBasic = contactType.getID();
            if (idCommonBasic != null) {
                customerDTO.setContactId(idCommonBasic.getValue());
            }

            TelephoneCommonBasic telephoneCommonBasic = contactType.getTelephone();
            if (telephoneCommonBasic != null) {
                customerDTO.setTelePhone(telephoneCommonBasic.getValue());
            }

            TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
            if (telefaxCommonBasic != null) {
                customerDTO.setTeleFax(telefaxCommonBasic.getValue());
            }

            ElectronicMailCommonBasic electronicMailCommonBasic =
                    contactType.getElectronicMail();
            if (electronicMailCommonBasic != null) {
                customerDTO.setEmail(electronicMailCommonBasic.getValue());
            }
        }
        return customerDTO;
    }

    /**
     * Gets the supplier party type.
     *
     * @param supplierDTO the supplier dto
     * @return the supplier party type
     */
    private static SupplierPartyType getSupplierPartyType(SupplierDTO supplierDTO, boolean isOrder) {

        SupplierPartyType supplierPartyType = new SupplierPartyType();
        PartyType party = new PartyType();
        PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                new PartyLegalEntityCommonAggregate();
        AddressType registrationAddress = null;

        IDCommonBasic idCommonBasic = null;
        EndpointIDCommonBasic endpointIDCB = null;
        NameCommonBasic nameCommonBasic = null;

        String country = "";

        boolean isPartyLegalEntity = false;
        boolean isRegistrationAddress = false;

        AddressDTO addressDTO = supplierDTO.getAddressDTO();
        if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
            country = addressDTO.getCountryCode().toUpperCase();
        }

        if (!StringUtils.isEmpty(supplierDTO.getEndpointId())) {
            endpointIDCB = new EndpointIDCommonBasic();
            endpointIDCB.setSchemeID(supplierDTO.getEaID());
            endpointIDCB.setValue(supplierDTO.getEndpointId());
            party.setEndpointID(endpointIDCB);
        }

        if (!StringUtils.isEmpty(supplierDTO.getId())) {

            idCommonBasic = new IDCommonBasic();
            idCommonBasic.setValue(supplierDTO.getId());
            PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                    new PartyIdentificationCommonAggregate();
            partyIdentificationCommonAggregate.setID(idCommonBasic);

            party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
        }

        if (isOrder && !StringUtils.isEmpty(supplierDTO.getName())) {

            nameCommonBasic = new NameCommonBasic();
            nameCommonBasic.setValue(supplierDTO.getName());
            PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
            partyName.setName(nameCommonBasic);

            party.getPartyNames().add(partyName);
        }

        ContactType contactType = new ContactType();
        boolean isContactAvailable = false;

        if (!StringUtils.isEmpty(supplierDTO.getTelePhone())) {
            TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
            telephoneCommonBasic.setValue(supplierDTO.getTelePhone());
            contactType.setTelephone(telephoneCommonBasic);
            isContactAvailable = true;
        }

        if (!StringUtils.isEmpty(supplierDTO.getEmail())) {
            ElectronicMailCommonBasic electronicMailCommonBasic =
                    new ElectronicMailCommonBasic();
            electronicMailCommonBasic.setValue(supplierDTO.getEmail());
            contactType.setElectronicMail(electronicMailCommonBasic);
            isContactAvailable = true;
        }
        if (isOrder && isContactAvailable) {
            party.setContact(contactType);
        }

        if (isOrder && addressDTO != null) {

            AddressType postalAddress = new AddressType();
            registrationAddress = new AddressType();
            boolean isAddressAvailable = false;

            if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                streetNameCommonBasic.setValue(addressDTO.getStreetName());
                postalAddress.setStreetName(streetNameCommonBasic);
                isAddressAvailable = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                PostalZoneCommonBasic postalZoneCommonBasic = new PostalZoneCommonBasic();
                postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                postalAddress.setPostalZone(postalZoneCommonBasic);
                isAddressAvailable = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                cityNameCommonBasic.setValue(addressDTO.getCityName());
                postalAddress.setCityName(cityNameCommonBasic);
                registrationAddress.setCityName(cityNameCommonBasic);
                isAddressAvailable = true;
                isRegistrationAddress = true;
            }

            if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {

                IdentificationCodeCommonBasic identificationCodeCommonBasic =
                        new IdentificationCodeCommonBasic();
                identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                CountryType countryType = new CountryType();
                countryType.setIdentificationCode(identificationCodeCommonBasic);
                postalAddress.setCountry(countryType);
                registrationAddress.setCountry(countryType);
                isAddressAvailable = true;
                isRegistrationAddress = true;
            }

            if (isAddressAvailable) {
                party.setPostalAddress(postalAddress);
            }
        }

        CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();

        if (isOrder && !StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

            companyIDCommonBasic = new CompanyIDCommonBasic();
            companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(supplierDTO.getOrganizationNo()));
            companyIDCommonBasic.setSchemeID( EHFConstants.EHF_THREE_DOT_ZERO_ORDER_COMPANY_ID_SCHEME.getValue());
            companyIDCommonBasic.setSchemeName(EHFConstants.EHF_THREE_DOT_ZERO_ORDER_LEGAL_ENTITY_SCHEME_NAME.getValue());
            companyIDCommonBasic.setSchemeAgencyID(EHFConstants.LEGAL_ENTITY_SCHEME_SGENCY_ID.getValue());
            partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
            isPartyLegalEntity = true;
        }

        if (isOrder && isRegistrationAddress) {
            partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
        }
        
        RegistrationNameCommonBasic registrationNameCommonBasic = new RegistrationNameCommonBasic();
        registrationNameCommonBasic.setValue(supplierDTO.getName());
        partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCommonBasic);
        
        if (isPartyLegalEntity || isRegistrationAddress) {
            party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
        }
        supplierPartyType.setParty(party);
        return supplierPartyType;
    }

    /**
     * Map order supplier.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapOrderSupplier(OrderDTO orderDTO, Order order) {

        SupplierDTO supplierDTO = orderDTO.getSupplierDTO();

        if (supplierDTO != null) {

            SupplierPartyType supplierPartyType = getSupplierPartyType(supplierDTO,true);
            order.setSellerSupplierParty(supplierPartyType);
        }
    }

    private static SupplierDTO getSupplierDTO(SupplierPartyType supplierPartyType) {

        SupplierDTO supplierDTO = new SupplierDTO();
        AddressDTO addressDTO = null;

        PartyType party = supplierPartyType.getParty();

        if (party != null) {

            EndpointIDCommonBasic endpointIDCB = party.getEndpointID();
            if (endpointIDCB != null) {
                supplierDTO.setEndpointId(endpointIDCB.getValue());
                supplierDTO.setEaID(endpointIDCB.getSchemeID());
            }

            IDCommonBasic idCommonBasic = null;
            for (PartyIdentificationCommonAggregate partyIdentificationCommonAggregate : party.getPartyIdentifications()) {
                idCommonBasic = partyIdentificationCommonAggregate.getID();
                if (idCommonBasic != null) {
                    supplierDTO.setId(idCommonBasic.getValue());
                }
            }

            NameCommonBasic nameCommonBasic = null;
            for (PartyNameCommonAggregate partyNameCommonAggregate : party.getPartyNames()) {
                nameCommonBasic = partyNameCommonAggregate.getName();
                if (nameCommonBasic != null) {
                    supplierDTO.setName(nameCommonBasic.getValue());
                }
            }

            CompanyIDCommonBasic companyIDCommonBasic = null;
            for (PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate : party.getPartyLegalEntities()) {
                companyIDCommonBasic = partyLegalEntityCommonAggregate.getCompanyID();
                if (companyIDCommonBasic != null) {
                    supplierDTO.setOrganizationNo(companyIDCommonBasic.getValue());
                }
            }

            AddressType postalAddress = party.getPostalAddress();

            if (postalAddress != null) {

                addressDTO = new AddressDTO();

                idCommonBasic = postalAddress.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }

                BuildingNumberCommonBasic buildingNumberCommonBasic =
                        postalAddress.getBuildingNumber();
                if (buildingNumberCommonBasic != null) {
                    addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                }

                StreetNameCommonBasic streetNameCommonBasic = postalAddress.getStreetName();
                if (streetNameCommonBasic != null) {
                    addressDTO.setStreetName(streetNameCommonBasic.getValue());
                }

                PostalZoneCommonBasic postalZoneCommonBasic = postalAddress.getPostalZone();
                if (postalZoneCommonBasic != null) {
                    addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                }

                PostboxCommonBasic postboxCommonBasic = postalAddress.getPostbox();
                if (postboxCommonBasic != null) {
                    addressDTO.setPostalBox(postboxCommonBasic.getValue());
                }

                CityNameCommonBasic cityNameCommonBasic = postalAddress.getCityName();
                if (cityNameCommonBasic != null) {
                    addressDTO.setCityName(cityNameCommonBasic.getValue());
                }

                CountryType countryType = postalAddress.getCountry();
                if (countryType != null) {
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            countryType.getIdentificationCode();
                    if (identificationCodeCommonBasic != null) {
                        addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                    }
                }
            }

            WebsiteURICommonBasic websiteURICommonBasic = party.getWebsiteURI();
            if (websiteURICommonBasic != null) {
                addressDTO.setUrl(websiteURICommonBasic.getValue());
            }

            supplierDTO.setAddressDTO(addressDTO);
        }

        ContactType contactType = party.getContact();
        if (contactType != null) {

            IDCommonBasic idCommonBasic = contactType.getID();
            if (idCommonBasic != null) {
                supplierDTO.setContactId(idCommonBasic.getValue());
            }

            TelephoneCommonBasic telephoneCommonBasic = contactType.getTelephone();
            if (telephoneCommonBasic != null) {
                supplierDTO.setTelePhone(telephoneCommonBasic.getValue());
            }

            TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
            if (telefaxCommonBasic != null) {
                supplierDTO.setTeleFax(telefaxCommonBasic.getValue());
            }

            ElectronicMailCommonBasic electronicMailCommonBasic =
                    contactType.getElectronicMail();
            if (electronicMailCommonBasic != null) {
                supplierDTO.setEmail(electronicMailCommonBasic.getValue());
            }
        }
        return supplierDTO;
    }

    /**
     * Map order supplier.
     *
     * @param order the order
     * @param orderDTO the order dto
     */
    private static void mapOrderSupplier(Order order, OrderDTO orderDTO) {

        SupplierPartyType supplierPartyType = order.getSellerSupplierParty();
        SupplierDTO supplierDTO = null;
        if (supplierPartyType != null) {
            supplierDTO = getSupplierDTO(supplierPartyType);
        }
        orderDTO.setSupplierDTO(supplierDTO);
    }

    /**
     * Map originator customer.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapOriginatorCustomer(OrderDTO orderDTO, Order order) {

        CustomerDTO customerDTO = orderDTO.getCustomerDTO();

        if (customerDTO != null) {

            PartyType party = new PartyType();
            PartyType payeePartyType = new PartyType();

            CustomerPartyType customerPartyType = new CustomerPartyType();
            IDCommonBasic idCommonBasic = null;

            if (!StringUtils.isEmpty(customerDTO.getId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(customerDTO.getId());
                PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);

                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
                payeePartyType.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(customerDTO.getName())) {
                NameCommonBasic nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(customerDTO.getName());
                PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
                payeePartyType.getPartyNames().add(partyName);
            }

            ContactType contactType = new ContactType();
            boolean isContactAvailable = false;

            if (!StringUtils.isEmpty(customerDTO.getTelePhone())) {
                TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
                telephoneCommonBasic.setValue(customerDTO.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getEmail())) {
                ElectronicMailCommonBasic electronicMailCommonBasic =
                        new ElectronicMailCommonBasic();
                electronicMailCommonBasic.setValue(customerDTO.getEmail());
                contactType.setElectronicMail(electronicMailCommonBasic);
                isContactAvailable = true;
            }

            if (isContactAvailable) {
                party.setContact(contactType);
            }

            customerPartyType.setParty(party);
            order.setOriginatorCustomerParty(customerPartyType);
        }
    }

    /**
     * Map order line.
     *
     * @param orderDTO the order dto
     * @param order the order
     */
    private static void mapOrderLine(OrderDTO orderDTO, Order order) {

        List<InvoiceLineItemDTO> invoiceLineItemDTOs = orderDTO.getOrderLineItems();

        if (invoiceLineItemDTOs != null) {

            MonetaryTotalType monetaryTotalType = null;
            OrderLineCommonAggregate orderLine = null;
            LineItemType lineitem = null;
            IDCommonBasic idCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionAmount = null;
            QuantityCommonBasic quantityCommonBasic = null;
            TotalTaxAmountCommonBasic totalTaxAmountCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            ItemIdentificationType itemIdentificationType = null;
            ItemType itemType = null;
            TaxExclusiveAmountCommonBasic taxExclusiveAmountCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            PriceType price = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            LineIDCommonBasic lineIDCommonBasic = null;
            TaxCategoryType taxCategoryType = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxTotalType taxTotalType = null;
            ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            AllowanceChargeReasonCodeCommonBasic allowanceChargeReasonCommonBasic = null;
            AmountCommonBasic amountCommonBasic = null;
            PayableRoundingAmountCommonBasic payableRoundingAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            PayableAmountCommonBasic payableAmountCommonBasic = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = orderDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            List<AllowanceChargeDTO> allowanceCharges = null;
            int i = 0;

            for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItemDTOs) {

                if (invoiceLineItemDTO.getTotalExcTax() == null) {
                    continue;
                }

                orderLine = new OrderLineCommonAggregate();
                lineitem = new LineItemType();
                itemType = new ItemType();

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getNote())) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(invoiceLineItemDTO.getNote());
                    orderLine.getNotes().add(noteCommonBasic);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getId());
                    lineitem.setID(idCommonBasic);
                }

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceLineItemDTO.getProductNo());
                idCommonBasic.setSchemeID(EHFConstants.GTIN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getAccountingCode())) {
                    accountingCostCommonBasic = new AccountingCostCommonBasic();
                    accountingCostCommonBasic.setValue(invoiceLineItemDTO.getAccountingCode());
                    lineitem.setAccountingCost(accountingCostCommonBasic);
                }

                lineIDCommonBasic = new LineIDCommonBasic();
                lineIDCommonBasic.setValue(String.valueOf(++i));

                if (invoiceLineItemDTO.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(invoiceLineItemDTO.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(invoiceLineItemDTO.getTaxTypeIntName())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(invoiceLineItemDTO.getTaxTypeIntName());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    nameCommonBasic = new NameCommonBasic();
                    nameCommonBasic.setValue(invoiceLineItemDTO.getDescription());
                    itemType.setName(nameCommonBasic);
                    itemIdentificationType = new ItemIdentificationType();
                    itemIdentificationType.setID(idCommonBasic);
                    itemType.setSellersItemIdentification(itemIdentificationType);
                }

                if (invoiceLineItemDTO.getUnitPrice() != null) {

                    priceAmountCommonBasic = new PriceAmountCommonBasic();
                    priceAmountCommonBasic.setCurrencyID(currencyCode);
                    priceAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getUnitPrice()));
                    price = new PriceType();
                    price.setPriceAmount(priceAmountCommonBasic);

                    baseQuantityCommonBasic = new BaseQuantityCommonBasic();
                    baseQuantityCommonBasic.setValue(new BigDecimal(1));
                    baseQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    baseQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                    price.setBaseQuantity(baseQuantityCommonBasic);
                    lineitem.setPrice(price);
                }

                if (invoiceLineItemDTO.getQuantity() != null) {
                    quantityCommonBasic = new QuantityCommonBasic();
                    quantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    quantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                    quantityCommonBasic.setValue(BigDecimal.valueOf(invoiceLineItemDTO.getQuantity()));
                    lineitem.setQuantity(quantityCommonBasic);
                }

                if (invoiceLineItemDTO.getTotalExcTax() != null) {
                    lineExtensionAmount = new LineExtensionAmountCommonBasic();
                    lineExtensionAmount.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTotalExcTax()));
                    lineExtensionAmount.setCurrencyID(currencyCode);
                    lineitem.setLineExtensionAmount(lineExtensionAmount);
                }

                allowanceCharges = invoiceLineItemDTO.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    AllowanceChargeType allowanceChargeType = null;
                    for (AllowanceChargeDTO allowanceChargeDTO : allowanceCharges) {

                        allowanceChargeType = new AllowanceChargeType();

                        chargeIndicatorCommonBasic = new ChargeIndicatorCommonBasic();
                        chargeIndicatorCommonBasic.setValue(allowanceChargeDTO.isChargeIndicator());
                        allowanceChargeType.setChargeIndicator(chargeIndicatorCommonBasic);

                        if (allowanceChargeDTO.getAllowanceChargeReason() != null) {
                            allowanceChargeReasonCommonBasic =
                                    new AllowanceChargeReasonCodeCommonBasic();
                            allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                            allowanceChargeType.setAllowanceChargeReasonCode(allowanceChargeReasonCommonBasic);
                        }

                        if (allowanceChargeDTO.getAmount() != null) {
                            amountCommonBasic = new AmountCommonBasic();
                            amountCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getAmount()));
                            amountCommonBasic.setCurrencyID(currencyCode);
                            allowanceChargeType.setAmount(amountCommonBasic);
                        }

                        lineitem.getAllowanceCharges().add(allowanceChargeType);
                    }
                }

                lineitem.setItem(itemType);
                orderLine.setLineItem(lineitem);
                order.getOrderLines().add(orderLine);
            }

            monetaryTotalType = order.getAnticipatedMonetaryTotal();
            if (monetaryTotalType == null) {
                monetaryTotalType = new MonetaryTotalType();
                order.setAnticipatedMonetaryTotal(monetaryTotalType);;
            }

            BigDecimal taxExclusiveAmount = null;
            if (orderDTO.getTotalExcTax() != null) {

                lineExtensionAmount = new LineExtensionAmountCommonBasic();
                lineExtensionAmount.setValue(ConversionUtils.asBigDecimal(orderDTO.getTotalExcTax()));
                lineExtensionAmount.setCurrencyID(currencyCode);
                monetaryTotalType.setLineExtensionAmount(lineExtensionAmount);

                taxExclusiveAmount = ConversionUtils.asBigDecimal(orderDTO.getTotalExcTax());

                if (monetaryTotalType.getAllowanceTotalAmount() != null
                        && monetaryTotalType.getAllowanceTotalAmount().getValue() != null) {
                    taxExclusiveAmount =
                            taxExclusiveAmount.subtract(monetaryTotalType.getAllowanceTotalAmount().getValue());
                }

                if (monetaryTotalType.getChargeTotalAmount() != null
                        && monetaryTotalType.getChargeTotalAmount().getValue() != null) {
                    taxExclusiveAmount =
                            taxExclusiveAmount.add(monetaryTotalType.getChargeTotalAmount().getValue());
                }

                taxExclusiveAmountCommonBasic = new TaxExclusiveAmountCommonBasic();
                taxExclusiveAmountCommonBasic.setValue(taxExclusiveAmount);
                taxExclusiveAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountCommonBasic);
            }

            if (taxExclusiveAmount != null) {

                BigDecimal taxInclusiveAmount =
                        orderDTO.getTaxAmount() != null ? taxExclusiveAmount.add(ConversionUtils.asBigDecimal(orderDTO.getTaxAmount()))
                        : taxExclusiveAmount;

                payableRoundingAmountCommonBasic = new PayableRoundingAmountCommonBasic();
                if (orderDTO.getIsRoundPayableAmount()) {
                payableRoundingAmountCommonBasic.setValue(ConversionUtils.getRoundingAmount(taxInclusiveAmount));
                } else {
                    payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(-0.0d));
                }

                payableRoundingAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setPayableRoundingAmount(payableRoundingAmountCommonBasic);

                BigDecimal payableAmount =
                        taxInclusiveAmount.add(payableRoundingAmountCommonBasic.getValue());

                taxInclusiveAmountCommonBasic = new TaxInclusiveAmountCommonBasic();
                taxInclusiveAmountCommonBasic.setValue(payableAmount);
                taxInclusiveAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountCommonBasic);

                payableAmountCommonBasic = new PayableAmountCommonBasic();
                payableAmountCommonBasic.setValue(payableAmount);
                payableAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setPayableAmount(payableAmountCommonBasic);
            }

            order.setAnticipatedMonetaryTotal(monetaryTotalType);
        }
    }

    /**
     * Map order line.
     *
     * @param order the order
     * @param orderDTO the order dto
     */
    private static void mapOrderLine(Order order, OrderDTO orderDTO) {

        List<OrderLineCommonAggregate> orderLineItems = order.getOrderLines();
        if (orderLineItems != null) {

            InvoiceLineItemDTO invoiceLineItemDTO = null;

            ItemType itemType = null;
            ItemIdentificationType itemIdentificationType = null;
            PriceType price = null;
            MonetaryTotalType legalMonetaryTotal = null;
            IDCommonBasic idCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionAmount = null;
            AmountCommonBasic amountCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            TaxExclusiveAmountCommonBasic taxExclusiveAmount = null;
            AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
            ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            AllowanceChargeDTO allowanceChargeDTO = null;
            TaxCategoryType taxCategory = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            NoteCommonBasic noteCommonBasic = null;
            PayableAmountCommonBasic payableAmountB = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            QuantityCommonBasic quantityCommonBasic = null;

            List<TaxCategoryType> classifiedTaxCategories = null;
            List<TaxTotalType> taxTotals = null;
            List<AllowanceChargeType> allowanceCharges = null;
            List<TaxCategoryType> taxCategories = null;

            for (OrderLineCommonAggregate orderLine : orderLineItems) {

                invoiceLineItemDTO = new InvoiceLineItemDTO();
                LineItemType lineItem = orderLine.getLineItem();
                idCommonBasic = lineItem.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setId(idCommonBasic.getValue());
                }

                noteCommonBasic = orderLine.getNotes().size() > 0 ? orderLine.getNotes().get(0) : null;
                if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setNote(noteCommonBasic.getValue());
                }

                itemType = lineItem.getItem();
                if (itemType != null) {
                    
                    itemIdentificationType = itemType.getSellersItemIdentification();
                    if (itemIdentificationType != null) {
                        idCommonBasic = itemIdentificationType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setSellersIdentification(idCommonBasic.getValue());
                        }
                    }
                    
                    if(itemType.getName() != null) invoiceLineItemDTO.setProductName(itemType.getName().getValue());
                    
                }
               
                nameCommonBasic = itemType.getName();
                if (nameCommonBasic != null) {
                    invoiceLineItemDTO.setProductName(nameCommonBasic.getValue());
                }

                accountingCostCommonBasic = lineItem.getAccountingCost();
                if (accountingCostCommonBasic != null
                        && accountingCostCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setAccountingCode(accountingCostCommonBasic.getValue());
                }

                baseQuantityCommonBasic = lineItem.getPrice().getBaseQuantity();
                if (baseQuantityCommonBasic != null) {
                    invoiceLineItemDTO.setUnitCode(baseQuantityCommonBasic.getUnitCode());
                }

                classifiedTaxCategories = itemType.getClassifiedTaxCategories();
                for (TaxCategoryType taxCategoryType : classifiedTaxCategories) {

                    percentCommonBasic = taxCategoryType.getPercent();
                    if (percentCommonBasic != null && percentCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setTaxPercent(percentCommonBasic.getValue().doubleValue());
                    }

                    taxSchemeCommonAggregate = taxCategoryType.getTaxScheme();
                    if (taxSchemeCommonAggregate != null) {
                        idCommonBasic = taxSchemeCommonAggregate.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setTaxTypeIntName(idCommonBasic.getValue());
                        }
                    }
                }

                List<DescriptionCommonBasic> descriptions = itemType.getDescriptions();
                if (descriptions != null) {
                    for (DescriptionCommonBasic description : descriptions) {
                        if (description != null) {
                            invoiceLineItemDTO.setDescription(description.getValue());
                        }
                    }
                }

                price = lineItem.getPrice();
                if (price != null) {
                    priceAmountCommonBasic = price.getPriceAmount();
                    if (priceAmountCommonBasic != null && priceAmountCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setUnitPrice(priceAmountCommonBasic.getValue().doubleValue());
                    }
                }

                quantityCommonBasic = lineItem.getQuantity();
                if (quantityCommonBasic != null
                        && quantityCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setQuantity(quantityCommonBasic.getValue().doubleValue());
                }

                lineExtensionAmount = lineItem.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    invoiceLineItemDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                }

                taxTotals = lineItem.getTaxTotals();
                if (taxTotals != null) {

                    for (TaxTotalType taxTotalType : taxTotals) {
                        taxAmountCommonBasic = taxTotalType.getTaxAmount();
                        if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                        }
                    }
                }
                
                

                allowanceCharges = lineItem.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    for (AllowanceChargeType allowanceChargeType : allowanceCharges) {

                        chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();
                        amountCommonBasic = allowanceChargeType.getAmount();
                        if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                            allowanceChargeDTO = new AllowanceChargeDTO();
                            allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                            allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeType.getAllowanceChargeReasonCode().getValue());
                            allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                            taxCategories = allowanceChargeType.getTaxCategories();
                            if (taxCategories != null && !taxCategories.isEmpty()) {

                                taxCategory = taxCategories.size() > 0 ? taxCategories.get(0) : null;
                                percentCommonBasic = taxCategory.getPercent();
                                if (percentCommonBasic != null) {
                                    allowanceChargeDTO.setTaxPercent(percentCommonBasic.getValue().doubleValue());
                                }
                                taxSchemeCommonAggregate = taxCategory.getTaxScheme();
                                if (taxSchemeCommonAggregate != null
                                        && taxSchemeCommonAggregate.getID() != null) {
                                    allowanceChargeDTO.setTaxType(taxSchemeCommonAggregate.getID().getValue());
                                }
                            }
                            invoiceLineItemDTO.getAllowanceCharges().add(allowanceChargeDTO);
                        }
                    }
                }

                orderDTO.getOrderLineItems().add(invoiceLineItemDTO);
            }

            legalMonetaryTotal = order.getAnticipatedMonetaryTotal();
            if (legalMonetaryTotal != null) {

                taxExclusiveAmount = legalMonetaryTotal.getTaxExclusiveAmount();
                if (taxExclusiveAmount != null && taxExclusiveAmount.getValue() != null) {
                    orderDTO.setTotalExcTax(taxExclusiveAmount.getValue().doubleValue());
                }

                taxInclusiveAmountCommonBasic = legalMonetaryTotal.getTaxInclusiveAmount();
				if (taxInclusiveAmountCommonBasic != null
						&& taxInclusiveAmountCommonBasic.getValue() != null) {
                    orderDTO.setTotalAmount(taxInclusiveAmountCommonBasic.getValue().doubleValue());
                }

                payableAmountB = legalMonetaryTotal.getPayableAmount();
				if (payableAmountB != null && payableAmountB.getValue() != null) {
                    orderDTO.setTotalAmount(payableAmountB.getValue().doubleValue());
                }
            }
        }
    }

    private static OrderResponse convertToOrderResponse(OrderResponseDTO orderResponseDTO) throws Exception {

        OrderResponse orderResponse = null;

        if (orderResponseDTO != null) {

            orderResponse = new OrderResponse();

            // Set customization id
            CustomizationIDCommonBasic customizationIDCommonBasic =
                    new CustomizationIDCommonBasic();
            customizationIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_ORDER_RESPONSE_CUSTOMIZATION_ID.getValue());
            orderResponse.setCustomizationID(customizationIDCommonBasic);

            // Set profile id
            ProfileIDCommonBasic profileIDCommonBasic = new ProfileIDCommonBasic();
            profileIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_ORDER_RESPONSE_PROFILE_ID.getValue());
            orderResponse.setProfileID(profileIDCommonBasic);

            // Set invoice no
            IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(orderResponseDTO.getOrderResponseNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(orderResponseDTO.getOrderResponseNo());
                orderResponse.setID(idCommonBasic);
            }

            // Set issue date
            if (orderResponseDTO.getOrderResponseIssueDate() != null) {
                IssueDateCommonBasic issueDateCommonBasic = new IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(orderResponseDTO.getOrderResponseIssueDate()));
                orderResponse.setIssueDate(issueDateCommonBasic);

                IssueTimeCommonBasic issueTimeCommonBasic = new IssueTimeCommonBasic();
                issueTimeCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendarTimeWithoutZone(orderResponseDTO.getOrderResponseIssueDate()));
                orderResponse.setIssueTime(issueTimeCommonBasic);
            }

            OrderResponseCodeCommonBasic orderResponseCodeCommonBasic = new OrderResponseCodeCommonBasic();
            orderResponseCodeCommonBasic.setListID(EHFConstants.ORDER_RESPONSE_CODE.getValue());
            orderResponseCodeCommonBasic.setValue(orderResponseDTO.getOrderResponseNo());

            orderResponse.setOrderResponseCode(orderResponseCodeCommonBasic);

            if (orderResponseDTO.getNote() != null) {
                NoteCommonBasic noteCommonBasic = new NoteCommonBasic();
                noteCommonBasic.setValue(orderResponseDTO.getNote());
                orderResponse.getNotes().add(noteCommonBasic);
            }

            OrderReferenceCommonAggregate orderReferenceCommonAggregate = new OrderReferenceCommonAggregate();
            idCommonBasic = new IDCommonBasic();
            idCommonBasic.setValue(orderResponseDTO.getOrderResponseNo());
            orderReferenceCommonAggregate.setID(idCommonBasic);
            orderResponse.getOrderReferences().add(orderReferenceCommonAggregate);
            
            DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic = new DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setName(orderResponseDTO.getCurrencyDTO().getCurrencyName());
            documentCurrencyCodeCommonBasic.setValue(orderResponseDTO.getCurrencyDTO().getCurrencyCode());
            orderResponse.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);

            mapOrderResponseSupplier(orderResponseDTO, orderResponse);
            mapOrderResponseCustomer(orderResponseDTO, orderResponse);
            mapOrderResponseLine(orderResponseDTO, orderResponse);
            
        }
        return orderResponse;
    }

    private static void mapOrderResponseSupplier(OrderResponseDTO orderResponseDTO, OrderResponse orderResponse) {

        SupplierDTO supplierDTO = orderResponseDTO.getSupplierDTO();

        if (supplierDTO != null) {

            SupplierPartyType supplierPartyType = getSupplierPartyType(supplierDTO,false);
            orderResponse.setSellerSupplierParty(supplierPartyType);
        }
    }

    private static void mapOrderResponseCustomer(OrderResponseDTO orderResponseDTO, OrderResponse orderResponse) {

        CustomerDTO customerDTO = orderResponseDTO.getCustomerDTO();

        if (customerDTO != null) {
            CustomerPartyType customerPartyType = getBuyerCustomerParty(customerDTO,false);
            orderResponse.setBuyerCustomerParty(customerPartyType);
        }
    }

    private static void mapOrderResponseSupplier(OrderResponse orderResponse, OrderResponseDTO orderResponseDTO) {

        SupplierPartyType supplierPartyType = orderResponse.getSellerSupplierParty();

        SupplierDTO supplierDTO = null;
        if (supplierPartyType != null) {
            supplierDTO = getSupplierDTO(supplierPartyType);
        }
        orderResponseDTO.setSupplierDTO(supplierDTO);
    }

    private static void mapOrderResponseCustomer(OrderResponse orderResponse, OrderResponseDTO orderResponseDTO) {

        CustomerPartyType customerPartyType = orderResponse.getBuyerCustomerParty();

        if (customerPartyType != null) {

            CustomerDTO customerDTO = new CustomerDTO();
            AddressDTO addressDTO = null;

            PartyType party = customerPartyType.getParty();
            if (party != null) {

                EndpointIDCommonBasic endpointIDCB = party.getEndpointID();
                if (endpointIDCB != null) {
                    customerDTO.setEndpointId(endpointIDCB.getValue());
                    customerDTO.setEaID(endpointIDCB.getSchemeID());
                }

                IDCommonBasic idCommonBasic = null;
                for (PartyIdentificationCommonAggregate partyIdentificationCommonAggregate : party.getPartyIdentifications()) {
                    idCommonBasic = partyIdentificationCommonAggregate.getID();
                    if (idCommonBasic != null) {
                        customerDTO.setId(idCommonBasic.getValue());
                    }
                }

                NameCommonBasic nameCommonBasic = null;
                for (PartyNameCommonAggregate partyNameCommonAggregate : party.getPartyNames()) {
                    nameCommonBasic = partyNameCommonAggregate.getName();
                    if (nameCommonBasic != null) {
                        customerDTO.setName(nameCommonBasic.getValue());
                    }
                }

                CompanyIDCommonBasic companyIDCommonBasic = null;
                for (PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate : party.getPartyLegalEntities()) {
                    companyIDCommonBasic = partyLegalEntityCommonAggregate.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        customerDTO.setOrganizationNo(companyIDCommonBasic.getValue());
                    }
                    if(partyLegalEntityCommonAggregate.getRegistrationName() != null) customerDTO.setName(partyLegalEntityCommonAggregate.getRegistrationName().getValue());
                }

                AddressType postalAddress = party.getPostalAddress();

                if (postalAddress != null) {

                    addressDTO = new AddressDTO();

                    idCommonBasic = postalAddress.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                        addressDTO.setGln(idCommonBasic.getValue());
                    }

                    BuildingNumberCommonBasic buildingNumberCommonBasic =
                            postalAddress.getBuildingNumber();
                    if (buildingNumberCommonBasic != null) {
                        addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                    }

                    StreetNameCommonBasic streetNameCommonBasic = postalAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    PostalZoneCommonBasic postalZoneCommonBasic = postalAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    PostboxCommonBasic postboxCommonBasic = postalAddress.getPostbox();
                    if (postboxCommonBasic != null) {
                        addressDTO.setPostalBox(postboxCommonBasic.getValue());
                    }

                    CityNameCommonBasic cityNameCommonBasic = postalAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    CountryType countryType = postalAddress.getCountry();
                    if (countryType != null) {
                        IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                }

                WebsiteURICommonBasic websiteURICommonBasic = party.getWebsiteURI();
                if (websiteURICommonBasic != null) {
                    addressDTO.setUrl(websiteURICommonBasic.getValue());
                }

                customerDTO.setAddressDTO(addressDTO);
            }

            ContactType contactType = party.getContact();
            if (contactType != null) {

                IDCommonBasic idCommonBasic = contactType.getID();
                if (idCommonBasic != null) {
                    customerDTO.setContactId(idCommonBasic.getValue());
                }

                TelephoneCommonBasic telephoneCommonBasic = contactType.getTelephone();
                if (telephoneCommonBasic != null) {
                    customerDTO.setTelePhone(telephoneCommonBasic.getValue());
                }

                TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
                if (telefaxCommonBasic != null) {
                    customerDTO.setTeleFax(telefaxCommonBasic.getValue());
                }

                ElectronicMailCommonBasic electronicMailCommonBasic =
                        contactType.getElectronicMail();
                if (electronicMailCommonBasic != null) {
                    customerDTO.setEmail(electronicMailCommonBasic.getValue());
                }
            }
            orderResponseDTO.setCustomerDTO(customerDTO);
        }
    }

    private static void mapOrderResponseLine(OrderResponseDTO orderResponseDTO, OrderResponse orderResponse) throws Exception {

        List<InvoiceLineItemDTO> invoiceLineItemDTOs = orderResponseDTO.getOrderLineItems();

        if (invoiceLineItemDTOs != null) {

            OrderLineCommonAggregate orderLine = null;
            LineItemType lineitem = null;
            LineItemType substitutedLineItem = null;
            IDCommonBasic idCommonBasic = null;
            QuantityCommonBasic quantityCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            ItemIdentificationType itemIdentificationType = null;
            ItemType itemType = null;
            ItemType substitutedItemType = null;
            NoteCommonBasic noteCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            PriceType price = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            LineIDCommonBasic lineIDCommonBasic = null;
            TaxCategoryType taxCategoryType = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            OrderLineReferenceCommonAggregate orderLineReferenceCommonAggregate = null;
            LineStatusCodeCommonBasic lineStatusCodeCommonBasic = null;
            CommodityClassificationType commodityClassificationType = null;
            ItemClassificationCodeCommonBasic itemClassificationCodeCommonBasic = null;
            ItemPropertyType itemPropertyType = null;
            ValueCommonBasic valueCommonBasic = null;
            DeliveryType deliveryType = null;
            PeriodType periodType = null;
            StartDateCommonBasic startDateCommonBasic = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = orderResponseDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItemDTOs) {

                if (invoiceLineItemDTO.getLineStatusCode() == null) {
                    throw new Exception("Order Line must have a response code");
                }

                orderLine = new OrderLineCommonAggregate();
                lineitem = new LineItemType();
                itemType = new ItemType();

                //set notes
                if (!StringUtils.isEmpty(invoiceLineItemDTO.getNote())) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(invoiceLineItemDTO.getNote());
                    lineitem.getNotes().add(noteCommonBasic);
                }

                //set line response code
                lineStatusCodeCommonBasic = new LineStatusCodeCommonBasic();
                lineStatusCodeCommonBasic.setValue(invoiceLineItemDTO.getLineStatusCode());
                lineStatusCodeCommonBasic.setListID(EHFConstants.ORDER_RESPONSE_CODE.getValue());
                lineitem.setLineStatusCode(lineStatusCodeCommonBasic);

                //line item id
                if (!StringUtils.isEmpty(invoiceLineItemDTO.getId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getId());
                    lineitem.setID(idCommonBasic);
                }

                //set quantity
                if (invoiceLineItemDTO.getQuantity() != null) {
                    quantityCommonBasic = new QuantityCommonBasic();
                    quantityCommonBasic.setValue(BigDecimal.valueOf(invoiceLineItemDTO.getQuantity()));
                    quantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    lineitem.setQuantity(quantityCommonBasic);
                }

                //set delivery information
                if (invoiceLineItemDTO.getDeliveryDTO() != null) {
                    deliveryType = new DeliveryType();
                    periodType = new PeriodType();
                    startDateCommonBasic = new StartDateCommonBasic();
                    startDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceLineItemDTO.getDeliveryDTO().getDeliveryDate()));
                    periodType.setStartDate(startDateCommonBasic);
                    deliveryType.setPromisedDeliveryPeriod(periodType);
                    lineitem.getDeliveries().add(deliveryType);
                }

                //set unit price
                if (invoiceLineItemDTO.getUnitPrice() != null) {

                    priceAmountCommonBasic = new PriceAmountCommonBasic();
                    priceAmountCommonBasic.setCurrencyID(currencyCode);
                    priceAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getUnitPrice()));
                    price = new PriceType();
                    price.setPriceAmount(priceAmountCommonBasic);

                    baseQuantityCommonBasic = new BaseQuantityCommonBasic();
                    baseQuantityCommonBasic.setValue(new BigDecimal(1));
                    baseQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    baseQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                    price.setBaseQuantity(baseQuantityCommonBasic);
                    lineitem.setPrice(price);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getProductName())) {
                    nameCommonBasic = new NameCommonBasic();
                    nameCommonBasic.setValue(invoiceLineItemDTO.getProductName());
                    itemType.setName(nameCommonBasic);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getSellersIdentification())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getSellersIdentification());
                    itemIdentificationType = new ItemIdentificationType();
                    itemIdentificationType.setID(idCommonBasic);
                    itemType.setSellersItemIdentification(itemIdentificationType);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getStandardIdentification())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getStandardIdentification());
                    idCommonBasic.setSchemeID(EHFConstants.ORDER_EHFV3_GTIN.getValue());
                    itemIdentificationType = new ItemIdentificationType();
                    itemIdentificationType.setID(idCommonBasic);
                    itemType.setStandardItemIdentification(itemIdentificationType);
                }

                if (invoiceLineItemDTO.getSubstitutedLineItem() != null) {

                    SubstitutedLineItemDTO substitutedLineItemDTO = invoiceLineItemDTO.getSubstitutedLineItem();
                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getId())) {
                        substitutedLineItem = new LineItemType();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(substitutedLineItemDTO.getId());
                        substitutedLineItem.setID(idCommonBasic);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getItemName())) {
                        substitutedItemType = new ItemType();
                        nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(substitutedLineItemDTO.getItemName());
                        substitutedItemType.setName(nameCommonBasic);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getSellersItemId())) {
                        itemIdentificationType = new ItemIdentificationType();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setSchemeID(EHFConstants.ORDER_EHFV3_GTIN.getValue());
                        idCommonBasic.setValue(substitutedLineItemDTO.getSellersItemId());
                        itemIdentificationType.setID(idCommonBasic);
                        substitutedItemType.setSellersItemIdentification(itemIdentificationType);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getStandardItemId())) {
                        itemIdentificationType = new ItemIdentificationType();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setSchemeID(EHFConstants.ORDER_EHFV3_GTIN.getValue());
                        idCommonBasic.setValue(substitutedLineItemDTO.getStandardItemId());
                        itemIdentificationType.setID(idCommonBasic);
                        substitutedItemType.setStandardItemIdentification(itemIdentificationType);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getItemClassificationCode())) {
                        commodityClassificationType = new CommodityClassificationType();
                        itemClassificationCodeCommonBasic = new ItemClassificationCodeCommonBasic();
                        itemClassificationCodeCommonBasic.setListID(EHFConstants.ORDER_RESPONSE_ITEM_CLASSIFICATION_CODE.getValue());
                        itemClassificationCodeCommonBasic.setValue(substitutedLineItemDTO.getItemClassificationCode());
                        commodityClassificationType.setItemClassificationCode(itemClassificationCodeCommonBasic);
                        substitutedItemType.getCommodityClassifications().add(commodityClassificationType);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getClassifiedTaxCategoryId())) {
                        taxCategoryType = new TaxCategoryType();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(substitutedLineItemDTO.getClassifiedTaxCategoryId());
                        taxCategoryType.setID(idCommonBasic);
                    }

                    if (substitutedLineItemDTO.getClassifiedTaxCategoryPercent() != null) {
                        percentCommonBasic = new PercentCommonBasic();
                        percentCommonBasic.setValue(BigDecimal.valueOf(substitutedLineItemDTO.getClassifiedTaxCategoryPercent()));
                        taxCategoryType.setID(idCommonBasic);
                        taxCategoryType.setPercent(percentCommonBasic);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getClassifiedTaxCategorySchemeId())) {
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(substitutedLineItemDTO.getClassifiedTaxCategorySchemeId());
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                        taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                        substitutedItemType.getClassifiedTaxCategories().add(taxCategoryType);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getItemPropertyName())) {
                        itemPropertyType = new ItemPropertyType();
                        nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(substitutedLineItemDTO.getItemPropertyName());
                        itemPropertyType.setName(nameCommonBasic);
                    }

                    if (!StringUtils.isEmpty(substitutedLineItemDTO.getItemPropertyValue())) {
                        valueCommonBasic = new ValueCommonBasic();
                        valueCommonBasic.setValue(substitutedLineItemDTO.getItemPropertyValue());
                        itemPropertyType.setValue(valueCommonBasic);
                        substitutedItemType.getAdditionalItemProperties().add(itemPropertyType);
                    }

                    substitutedLineItem.setItem(substitutedItemType);
                    orderLine.getSellerSubstitutedLineItems().add(substitutedLineItem);
                }

                if (!StringUtils.isEmpty(orderResponseDTO.getOrderLineReference())) {
                    orderLineReferenceCommonAggregate = new OrderLineReferenceCommonAggregate();
                    lineIDCommonBasic = new LineIDCommonBasic();
                    lineIDCommonBasic.setValue(orderResponseDTO.getOrderLineReference());
                    orderLineReferenceCommonAggregate.setLineID(lineIDCommonBasic);
                    orderLine.getOrderLineReferences().add(orderLineReferenceCommonAggregate);
                }

                lineitem.setItem(itemType);
                orderLine.setLineItem(lineitem);
                orderResponse.getOrderLines().add(orderLine);
            }
        }
    }

    private static void mapOrderResponseLine(OrderResponse orderResponse, OrderResponseDTO orderResponseDTO) {

        List<OrderLineCommonAggregate> orderLineItems = orderResponse.getOrderLines();
        if (orderLineItems != null) {

            InvoiceLineItemDTO invoiceLineItemDTO = null;

            ItemType itemType = null;
            ItemType substitutedItemType = null;
            ItemIdentificationType itemIdentificationType = null;
            PriceType price = null;
            IDCommonBasic idCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            QuantityCommonBasic quantityCommonBasic = null;
            LineStatusCodeCommonBasic lineStatusCodeCommonBasic = null;
            SubstitutedLineItemDTO substitutedLineItemDTO = null;
            List<LineItemType> substitutedLines = null;
            TaxCategoryType classifiedTaxCategory = null;
            List<CommodityClassificationType> commodityClassifications = null;
            CommodityClassificationType commodityClassification = null;

            for (OrderLineCommonAggregate orderLine : orderLineItems) {

                invoiceLineItemDTO = new InvoiceLineItemDTO();

                //set id
                LineItemType lineItem = orderLine.getLineItem();
                idCommonBasic = lineItem.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setId(idCommonBasic.getValue());
                }

                //set note
                noteCommonBasic = lineItem.getNotes().size() > 0 ? lineItem.getNotes().get(0) : null;
                if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setNote(noteCommonBasic.getValue());
                }

                //set response code
                lineStatusCodeCommonBasic = lineItem.getLineStatusCode();
                if (lineStatusCodeCommonBasic != null) {
                    invoiceLineItemDTO.setLineStatusCode(lineStatusCodeCommonBasic.getValue());
                }
                
                List<DeliveryType>  deliveryTypes = lineItem.getDeliveries();
                if(deliveryTypes != null && deliveryTypes.size() > 0) {
                    
                    DeliveryDTO deliveryDTO = new DeliveryDTO();
                    if(deliveryTypes.get(0).getPromisedDeliveryPeriod() != null && deliveryTypes.get(0).getPromisedDeliveryPeriod().getStartDate() != null) {
                        deliveryDTO.setDeliveryDate(ConversionUtils.asDate(deliveryTypes.get(0).getPromisedDeliveryPeriod().getStartDate().getValue()));
                    }
                    
                    invoiceLineItemDTO.setDeliveryDTO(deliveryDTO);
                }

                itemType = lineItem.getItem();

                if (itemType != null) {
                    
                    if(itemType.getName() != null) invoiceLineItemDTO.setProductName(itemType.getName().getValue());

                    //set unit code
                    baseQuantityCommonBasic = lineItem.getPrice().getBaseQuantity();
                    if (baseQuantityCommonBasic != null) {
                        invoiceLineItemDTO.setUnitCode(baseQuantityCommonBasic.getUnitCode());
                    }

                    //set description
                    List<DescriptionCommonBasic> descriptions = itemType.getDescriptions();
                    if (descriptions != null) {
                        for (DescriptionCommonBasic description : descriptions) {
                            if (description != null) {
                                invoiceLineItemDTO.setDescription(description.getValue());
                            }
                        }
                    }

                    //set unit price
                    price = lineItem.getPrice();
                    if (price != null) {
                        priceAmountCommonBasic = price.getPriceAmount();
                        if (priceAmountCommonBasic != null && priceAmountCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setUnitPrice(priceAmountCommonBasic.getValue().doubleValue());                            
                        }
                    }

                    //set quantity
                    quantityCommonBasic = lineItem.getQuantity();
                    if (quantityCommonBasic != null
                            && quantityCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setQuantity(quantityCommonBasic.getValue().doubleValue());
                    }

                    //set sellers item identification
                    itemIdentificationType = itemType.getSellersItemIdentification();
                    if (itemIdentificationType != null) {
                        invoiceLineItemDTO.setSellersIdentification(itemIdentificationType.getID().getValue());
                    }

                    //set standard item identification
                    itemIdentificationType = itemType.getStandardItemIdentification();
                    if (itemIdentificationType != null) {
                        invoiceLineItemDTO.setStandardIdentification(itemIdentificationType.getID().getValue());
                    }
                }

                substitutedLines = orderLine.getSellerSubstitutedLineItems();
                substitutedLineItemDTO = new SubstitutedLineItemDTO();

                if (substitutedLines.size() > 0) {
                    LineItemType substitutedLine = substitutedLines.get(0);
                    substitutedItemType = substitutedLine.getItem();
                    List<ItemPropertyType> itemProperties = substitutedItemType.getAdditionalItemProperties();
                    if (itemProperties.size() > 0) {
                        ItemPropertyType itemProperty = itemProperties.get(0);
                        substitutedLineItemDTO.setItemPropertyValue(itemProperty.getValue().getValue());
                        substitutedLineItemDTO.setItemPropertyName(itemProperty.getName().getValue());
                    }

                    List<TaxCategoryType> classifiedTaxCategories = substitutedItemType.getClassifiedTaxCategories();
                    if (classifiedTaxCategories.size() > 0) {
                        classifiedTaxCategory = classifiedTaxCategories.get(0);
                        substitutedLineItemDTO.setClassifiedTaxCategorySchemeId(classifiedTaxCategory.getTaxScheme().getID().getValue());
                        substitutedLineItemDTO.setClassifiedTaxCategoryPercent(classifiedTaxCategory.getPercent().getValue().doubleValue());
                        substitutedLineItemDTO.setClassifiedTaxCategoryId(classifiedTaxCategory.getID().getValue());
                    }

                    commodityClassifications = substitutedItemType.getCommodityClassifications();
                    if (commodityClassifications.size() > 0) {
                        commodityClassification = commodityClassifications.get(0);
                        substitutedLineItemDTO.setItemClassificationCode(commodityClassification.getItemClassificationCode().getValue());
                    }

                    substitutedLineItemDTO.setStandardItemId(substitutedItemType.getStandardItemIdentification().getID().getValue());
                    substitutedLineItemDTO.setSellersItemId(substitutedItemType.getSellersItemIdentification().getID().getValue());
                }
                substitutedLineItemDTO.setItemName(substitutedItemType.getName().getValue());
                substitutedLineItemDTO.setId(lineItem.getID().getValue());

                invoiceLineItemDTO.setSubstitutedLineItem(substitutedLineItemDTO);
                
                List<OrderLineReferenceCommonAggregate> orderLineReferences = orderLine.getOrderLineReferences();
                if (orderLineReferences.size() > 0) {
                    OrderLineReferenceCommonAggregate orderLineReference = orderLineReferences.get(0);
                    orderResponseDTO.setOrderLineReference(orderLineReference.getLineID().getValue());
                }
                orderResponseDTO.getOrderLineItems().add(invoiceLineItemDTO);
            }
        }
    }
}