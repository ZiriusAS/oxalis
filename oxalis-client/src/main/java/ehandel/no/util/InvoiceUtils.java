/*
 * @(#)InvoiceUtils.java
 *
 * Copyright (c) 2012, Zirius AS.
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
import ehandel.no.dto.BankAccountDTO;
import ehandel.no.dto.ContactPersonDTO;
import ehandel.no.dto.ContractDTO;
import ehandel.no.dto.CurrencyDTO;
import ehandel.no.dto.CustomerDTO;
import ehandel.no.dto.DeliveryDTO;
import ehandel.no.dto.FileDTO;
import ehandel.no.dto.InvoiceDTO;
import ehandel.no.dto.InvoiceLineItemDTO;
import ehandel.no.dto.InvoiceSettingDTO;
import ehandel.no.dto.ManufacturerPartyDTO;
import ehandel.no.dto.PayeeParty;
import ehandel.no.dto.SupplierDTO;
import ehandel.no.dto.TaxRepresentativeDTO;
import ehandel.no.dto.TaxSummaryDTO;
import ehandel.no.ehf.invoice.AccountingCostCommonBasic;
import ehandel.no.ehf.invoice.ActualDeliveryDateCommonBasic;
import ehandel.no.ehf.invoice.AdditionalStreetNameCommonBasic;
import ehandel.no.ehf.invoice.AddressType;
import ehandel.no.ehf.invoice.AllowanceChargeReasonCommonBasic;
import ehandel.no.ehf.invoice.AllowanceChargeType;
import ehandel.no.ehf.invoice.AllowanceTotalAmountCommonBasic;
import ehandel.no.ehf.invoice.AmountCommonBasic;
import ehandel.no.ehf.invoice.AttachmentType;
import ehandel.no.ehf.invoice.BaseQuantityCommonBasic;
import ehandel.no.ehf.invoice.BranchType;
import ehandel.no.ehf.invoice.BuildingNumberCommonBasic;
import ehandel.no.ehf.invoice.BuyerReferenceCommonBasic;
import ehandel.no.ehf.invoice.CalculationRateCommonBasic;
import ehandel.no.ehf.invoice.ChargeIndicatorCommonBasic;
import ehandel.no.ehf.invoice.ChargeTotalAmountCommonBasic;
import ehandel.no.ehf.invoice.CityNameCommonBasic;
import ehandel.no.ehf.invoice.CompanyIDCommonBasic;
import ehandel.no.ehf.invoice.ContactType;
import ehandel.no.ehf.invoice.CountrySubentityCodeCommonBasic;
import ehandel.no.ehf.invoice.CountryType;
import ehandel.no.ehf.invoice.CustomerPartyType;
import ehandel.no.ehf.invoice.CustomizationIDCommonBasic;
import ehandel.no.ehf.invoice.DeliveryType;
import ehandel.no.ehf.invoice.DescriptionCommonBasic;
import ehandel.no.ehf.invoice.DocumentCurrencyCodeCommonBasic;
import ehandel.no.ehf.invoice.DocumentReferenceType;
import ehandel.no.ehf.invoice.DocumentTypeCodeCommonBasic;
import ehandel.no.ehf.invoice.DocumentTypeCommonBasic;
import ehandel.no.ehf.invoice.DueDateCommonBasic;
import ehandel.no.ehf.invoice.ElectronicMailCommonBasic;
import ehandel.no.ehf.invoice.EmbeddedDocumentBinaryObjectCommonBasic;
import ehandel.no.ehf.invoice.EndDateCommonBasic;
import ehandel.no.ehf.invoice.EndpointIDCommonBasic;
import ehandel.no.ehf.invoice.ExchangeRateType;
import ehandel.no.ehf.invoice.FinancialAccountType;
import ehandel.no.ehf.invoice.FinancialInstitutionCommonAggregate;
import ehandel.no.ehf.invoice.IDCommonBasic;
import ehandel.no.ehf.invoice.IdentificationCodeCommonBasic;
import ehandel.no.ehf.invoice.Invoice;
import ehandel.no.ehf.invoice.InvoiceLineType;
import ehandel.no.ehf.invoice.InvoiceTypeCodeCommonBasic;
import ehandel.no.ehf.invoice.InvoicedQuantityCommonBasic;
import ehandel.no.ehf.invoice.IssueDateCommonBasic;
import ehandel.no.ehf.invoice.ItemIdentificationType;
import ehandel.no.ehf.invoice.ItemType;
import ehandel.no.ehf.invoice.LineExtensionAmountCommonBasic;
import ehandel.no.ehf.invoice.LineIDCommonBasic;
import ehandel.no.ehf.invoice.LocationType;
import ehandel.no.ehf.invoice.MathematicOperatorCodeCommonBasic;
import ehandel.no.ehf.invoice.MonetaryTotalType;
import ehandel.no.ehf.invoice.NameCommonBasic;
import ehandel.no.ehf.invoice.NoteCommonBasic;
import ehandel.no.ehf.invoice.OrderLineReferenceCommonAggregate;
import ehandel.no.ehf.invoice.OrderReferenceCommonAggregate;
import ehandel.no.ehf.invoice.PartyIdentificationCommonAggregate;
import ehandel.no.ehf.invoice.PartyLegalEntityCommonAggregate;
import ehandel.no.ehf.invoice.PartyNameCommonAggregate;
import ehandel.no.ehf.invoice.PartyTaxSchemeCommonAggregate;
import ehandel.no.ehf.invoice.PartyTypeCommonAggregate;
import ehandel.no.ehf.invoice.PayableAmountCommonBasic;
import ehandel.no.ehf.invoice.PayableRoundingAmountCommonBasic;
import ehandel.no.ehf.invoice.PaymentDueDateCommonBasic;
import ehandel.no.ehf.invoice.PaymentIDCommonBasic;
import ehandel.no.ehf.invoice.PaymentMeansCodeCommonBasic;
import ehandel.no.ehf.invoice.PaymentMeansCommonAggregate;
import ehandel.no.ehf.invoice.PaymentTermsType;
import ehandel.no.ehf.invoice.PercentCommonBasic;
import ehandel.no.ehf.invoice.PeriodType;
import ehandel.no.ehf.invoice.PostalZoneCommonBasic;
import ehandel.no.ehf.invoice.PostboxCommonBasic;
import ehandel.no.ehf.invoice.PriceAmountCommonBasic;
import ehandel.no.ehf.invoice.PriceTypeCommonAggregate;
import ehandel.no.ehf.invoice.ProfileIDCommonBasic;
import ehandel.no.ehf.invoice.RegistrationNameCommonBasic;
import ehandel.no.ehf.invoice.SourceCurrencyBaseRateCommonBasic;
import ehandel.no.ehf.invoice.SourceCurrencyCodeCommonBasic;
import ehandel.no.ehf.invoice.StartDateCommonBasic;
import ehandel.no.ehf.invoice.StreetNameCommonBasic;
import ehandel.no.ehf.invoice.SupplierPartyType;
import ehandel.no.ehf.invoice.TargetCurrencyBaseRateCommonBasic;
import ehandel.no.ehf.invoice.TargetCurrencyCodeCommonBasic;
import ehandel.no.ehf.invoice.TaxAmountCommonBasic;
import ehandel.no.ehf.invoice.TaxCategoryType;
import ehandel.no.ehf.invoice.TaxCurrencyCodeCommonBasic;
import ehandel.no.ehf.invoice.TaxExclusiveAmountCommonBasic;
import ehandel.no.ehf.invoice.TaxExemptionReasonCodeCommonBasic;
import ehandel.no.ehf.invoice.TaxExemptionReasonCommonBasic;
import ehandel.no.ehf.invoice.TaxInclusiveAmountCommonBasic;
import ehandel.no.ehf.invoice.TaxPointDateCommonBasic;
import ehandel.no.ehf.invoice.TaxSchemeCommonAggregate;
import ehandel.no.ehf.invoice.TaxSubtotalCommonAggregate;
import ehandel.no.ehf.invoice.TaxTotalType;
import ehandel.no.ehf.invoice.TaxableAmountCommonBasic;
import ehandel.no.ehf.invoice.TelefaxCommonBasic;
import ehandel.no.ehf.invoice.TelephoneCommonBasic;
import ehandel.no.ehf.invoice.TransactionCurrencyTaxAmountCommonBasic;
import ehandel.no.ehf.invoice.UBLVersionIDCommonBasic;
import ehandel.no.ehf.invoice.WebsiteURICommonBasic;
import java.util.regex.Pattern;
import javax.xml.bind.PropertyException;

/**
 * The Class InvoiceUtils.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public final class InvoiceUtils {

    private static String taxTypeIntName = "";
    
    private static JAXBContext context;
    
    public static JAXBContext getJAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(ehandel.no.ehf.invoice.Invoice.class);
        }
        return context;
    }
 
    public InvoiceUtils() {
    }

    /**
     * Marshalling invoice object to XML file.
     */
    public static void generateEHFInvoiceXML(InvoiceDTO invoiceDTO, String filePath)
            throws JAXBException {

        ehandel.no.invoice.Invoice invoice = convertToInvoice(invoiceDTO);

        Marshaller marshaller = getMarshaller();

        marshaller.marshal(invoice, new File(filePath));
    }

    /**
     * Marshalling invoice object to byte array.
     */
    public static byte[] generateEHFInvoiceXML(InvoiceDTO invoiceDTO) throws JAXBException {

        ehandel.no.invoice.Invoice invoice = convertToInvoice(invoiceDTO);

        Marshaller marshaller = getMarshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(invoice, out);

        return out.toByteArray();
    }

    /**
     * Method to get the marshaller for ehandel.no.invoice.Invoice.
     */
    private static Marshaller getMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.invoice.Invoice.class);

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

    private static Marshaller getV2Marshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.ehf.invoice.Invoice.class);

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
     * Gets the invoice version.
     * 
     * @param is
     *            the is
     * @return the invoice version
     * @throws JAXBException
     *             the jAXB exception
     */
   public static String getInvoiceVersion(InputStream is) throws JAXBException {

       final JAXBContext context = JAXBContext.newInstance(Invoice.class);
       final Unmarshaller unmarshaller = context.createUnmarshaller();
       Invoice invoice = (Invoice) unmarshaller.unmarshal(is);

       return getVersion(invoice);
   }

   private static String getVersion(Invoice invoice) {

       UBLVersionIDCommonBasic ublVersionIDCommonBasic =
               invoice.getUBLVersionID();
       return ublVersionIDCommonBasic.getValue();
   }

    /**
     * Method used to convert ehandel.no.invoice.Invoice wrapper to EHF invoice.
     */
    private static ehandel.no.invoice.Invoice convertToInvoice(InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.Invoice invoice = null;
        if (invoiceDTO != null) {

            invoice = new ehandel.no.invoice.Invoice();

            // Set UBL version ID
            ehandel.no.invoice.UBLVersionIDCommonBasic ublVersionIDCommonBasic =
                    new ehandel.no.invoice.UBLVersionIDCommonBasic();
            ublVersionIDCommonBasic.setValue(EHFConstants.UBL_VERSION_ID.getValue());
            invoice.setUBLVersionID(ublVersionIDCommonBasic);

            // Set customization id
            ehandel.no.invoice.CustomizationIDCommonBasic customizationIDCommonBasic =
                    new ehandel.no.invoice.CustomizationIDCommonBasic();
            customizationIDCommonBasic.setValue(EHFConstants.INVOICE_CUSTOMIZATION_ID.getValue());
            invoice.setCustomizationID(customizationIDCommonBasic);

            // Set profile id
            ehandel.no.invoice.ProfileIDCommonBasic profileIDCommonBasic =
                    new ehandel.no.invoice.ProfileIDCommonBasic();
            profileIDCommonBasic.setValue(EHFConstants.INVOICE_PROFILE_ID.getValue());
            invoice.setProfileID(profileIDCommonBasic);

            // Set invoice no
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(invoiceDTO.getInvoiceNo())) {
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getInvoiceNo());
                invoice.setID(idCommonBasic);
            }

            // Set issue date, tax point date and invoice period
            if (invoiceDTO.getIssueDate() != null) {
                ehandel.no.invoice.IssueDateCommonBasic issueDateCommonBasic =
                        new ehandel.no.invoice.IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                invoice.setIssueDate(issueDateCommonBasic);

                ehandel.no.invoice.TaxPointDateCommonBasic taxPointDateCommonBasic =
                        new ehandel.no.invoice.TaxPointDateCommonBasic();
                taxPointDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                invoice.setTaxPointDate(taxPointDateCommonBasic);

                ehandel.no.invoice.PeriodType periodType = new ehandel.no.invoice.PeriodType();
                ehandel.no.invoice.StartDateCommonBasic startDateCommonBasic =
                        new ehandel.no.invoice.StartDateCommonBasic();
                startDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                periodType.setStartDate(startDateCommonBasic);

                if (invoiceDTO.getPaymentDueDate() != null) {
                    ehandel.no.invoice.EndDateCommonBasic endDateCommonBasic =
                            new ehandel.no.invoice.EndDateCommonBasic();
                    endDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPaymentDueDate()));
                    periodType.setEndDate(endDateCommonBasic);
                }
                invoice.getInvoicePeriods().add(periodType);
            }

            // Set invoice type
            ehandel.no.invoice.InvoiceTypeCodeCommonBasic invoiceTypeCodeCommonBasic =
                    new ehandel.no.invoice.InvoiceTypeCodeCommonBasic();
            invoiceTypeCodeCommonBasic.setValue(EHFConstants.INVOICE_TYPE.getValue());
            invoice.setInvoiceTypeCode(invoiceTypeCodeCommonBasic);

            // Set order reference id
            if (!StringUtils.isEmpty(invoiceDTO.getPurchaseOrderNo())) {
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getPurchaseOrderNo());
                ehandel.no.invoice.OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                        new ehandel.no.invoice.OrderReferenceCommonAggregate();
                orderReferenceCommonAggregate.setID(idCommonBasic);
                invoice.setOrderReference(orderReferenceCommonAggregate);
            }

            List<FileDTO> files = invoiceDTO.getFiles();
            if (files != null && !files.isEmpty()) {

                ehandel.no.invoice.EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCommonBasic =
                        null;
                ehandel.no.invoice.DocumentReferenceType documentReferenceType = null;
                ehandel.no.invoice.DocumentTypeCommonBasic documentTypeCommonBasic = null;
                ehandel.no.invoice.AttachmentType attachmentType = null;

                int i = 0;
                for (FileDTO fileDTO : files) {

                    i++;
                    embeddedDocBinObjCommonBasic =
                            new ehandel.no.invoice.EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(fileDTO.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(ehandel.no.invoice.BinaryObjectMimeCodeContentType.fromValue(fileDTO.getMimeType()));
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode(ehandel.no.invoice.BinaryObjectMimeCodeContentType.APPLICATION_PDF);
                    }
                    embeddedDocBinObjCommonBasic.setValue(fileDTO.getFileContent());

                    documentReferenceType = new ehandel.no.invoice.DocumentReferenceType();

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(EHFConstants.DOCUMENT.getValue() + i);
                    documentReferenceType.setID(idCommonBasic);

                    documentTypeCommonBasic = new ehandel.no.invoice.DocumentTypeCommonBasic();
                    documentTypeCommonBasic.setValue(fileDTO.getDocType());
                    documentReferenceType.setDocumentType(documentTypeCommonBasic);

                    attachmentType = new ehandel.no.invoice.AttachmentType();
                    attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocBinObjCommonBasic);
                    documentReferenceType.setAttachment(attachmentType);

                    invoice.getAdditionalDocumentReferences().add(documentReferenceType);
                }
                i = 0;
            }

            String language =
                    (StringUtils.isEmpty(invoiceDTO.getLanguage()) ? EHFConstants.DEFAULT_LANGUAGE.getValue()
                            : invoiceDTO.getLanguage());
            invoiceDTO.setLanguage(language);

            if (invoiceDTO.getNotes() != null && !invoiceDTO.getNotes().isEmpty()) {
                ehandel.no.invoice.NoteCommonBasic noteCommonBasic = null;
                for (String note : invoiceDTO.getNotes()) {
                    noteCommonBasic = new ehandel.no.invoice.NoteCommonBasic();
                    noteCommonBasic.setValue(note);
                    noteCommonBasic.setLanguageID(language);
                    invoice.getNotes().add(noteCommonBasic);
                }
            }

            List<InvoiceLineItemDTO> invoiceLineItems = invoiceDTO.getInvoiceLineItems();
            if (invoiceLineItems != null && !invoiceLineItems.isEmpty()) {
                for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItems) {
                    if (invoiceLineItemDTO.getTotalExcTax() != null) {
                        taxTypeIntName =
                                invoiceLineItemDTO.getTaxTypeIntName() != null ? invoiceLineItemDTO.getTaxTypeIntName().toUpperCase()
                                        : "";
                        break;
                    }
                }
            }

            mapPayeeParty(invoiceDTO, invoice);
            mapSupplier(invoiceDTO, invoice);
            mapSupplierBankAccount(invoiceDTO, invoice);
            mapCustomer(invoiceDTO, invoice);
            mapCustomerBankAccount(invoiceDTO, invoice);
            mapDeliveryAddress(invoiceDTO, invoice);
            mapCurrency(invoiceDTO, invoice);
            mapExchangeRate(invoiceDTO, invoice);
            mapAllowanceCharges(invoiceDTO, invoice);
            mapInvoiceLineItems(invoiceDTO, invoice);
            mapTaxSummaries(invoiceDTO, invoice);
            mapPaymentTerms(invoiceDTO, invoice);
        }

        return invoice;
    }

    /**
     * Method used to map payee information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapPayeeParty(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        PayeeParty payeeParty = invoiceDTO.getPayeeParty();
        ehandel.no.invoice.PartyType party = new ehandel.no.invoice.PartyType();

        if (payeeParty != null) {

            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;

            String organizationNo = "";
            String country = "";

            AddressDTO addressDTO = payeeParty.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            } else {
               addressDTO = invoiceDTO.getSupplierDTO().getAddressDTO();
               country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(payeeParty.getId())) {

                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(payeeParty.getId());
                ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new ehandel.no.invoice.PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);
                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(payeeParty.getName())) {

                ehandel.no.invoice.NameCommonBasic nameCommonBasic =
                        new ehandel.no.invoice.NameCommonBasic();
                nameCommonBasic.setValue(payeeParty.getName());
                ehandel.no.invoice.PartyNameCommonAggregate partyName =
                        new ehandel.no.invoice.PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);
                party.getPartyNames().add(partyName);
            }

            if (!StringUtils.isEmpty(payeeParty.getOrganizationNo())) {
                organizationNo = payeeParty.getOrganizationNo();
                ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic =
                        new ehandel.no.invoice.CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(payeeParty.getOrganizationNo());
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.LEGAL_ENTITY_SCHEME_SGENCY_ID.getValue());
                ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                        new ehandel.no.invoice.PartyLegalEntityCommonAggregate();
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }
            invoice.setPayeeParty(party);
        }
    }

    /**
     * Method used to map supplier information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapSupplier(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();

        if (supplierDTO != null) {

            ehandel.no.invoice.SupplierPartyType supplierPartyType =
                    new ehandel.no.invoice.SupplierPartyType();
            ehandel.no.invoice.PartyType party = new ehandel.no.invoice.PartyType();
            ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new ehandel.no.invoice.PartyLegalEntityCommonAggregate();

            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.EndpointIDCommonBasic endpointIDCB = null;
            ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;

            String country = "";

            boolean isPartyLegalEntity = false;

            AddressDTO addressDTO = supplierDTO.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(supplierDTO.getEndpointId())) {
                endpointIDCB = new ehandel.no.invoice.EndpointIDCommonBasic();
                endpointIDCB.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                endpointIDCB.setValue(supplierDTO.getEndpointId());
                party.setEndpointID(endpointIDCB);
            }

            if (!StringUtils.isEmpty(supplierDTO.getId())) {

                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(supplierDTO.getId());
                ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new ehandel.no.invoice.PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);

                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getName())) {

                nameCommonBasic = new ehandel.no.invoice.NameCommonBasic();
                nameCommonBasic.setValue(supplierDTO.getName());
                ehandel.no.invoice.PartyNameCommonAggregate partyName =
                        new ehandel.no.invoice.PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
            }

            ehandel.no.invoice.ContactType contactType = new ehandel.no.invoice.ContactType();
            boolean isContactAvailable = false;
            if (!StringUtils.isEmpty(supplierDTO.getContactId())) {
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(supplierDTO.getContactId());
                contactType.setID(idCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getTelePhone())) {
                ehandel.no.invoice.TelephoneCommonBasic telephoneCommonBasic =
                        new ehandel.no.invoice.TelephoneCommonBasic();
                telephoneCommonBasic.setValue(supplierDTO.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getTeleFax())) {
                ehandel.no.invoice.TelefaxCommonBasic telefaxCommonBasic =
                        new ehandel.no.invoice.TelefaxCommonBasic();
                telefaxCommonBasic.setValue(supplierDTO.getTeleFax());
                contactType.setTelefax(telefaxCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getEmail())) {
                ehandel.no.invoice.ElectronicMailCommonBasic electronicMailCommonBasic =
                        new ehandel.no.invoice.ElectronicMailCommonBasic();
                electronicMailCommonBasic.setValue(supplierDTO.getEmail());
                contactType.setElectronicMail(electronicMailCommonBasic);
                isContactAvailable = true;
            }
            if (isContactAvailable) {
                party.setContact(contactType);
            }

            if (addressDTO != null) {

                ehandel.no.invoice.AddressType postalAddress = new ehandel.no.invoice.AddressType();
                boolean isAddressAvailable = false;

                if (!StringUtils.isEmpty(addressDTO.getGln())) {

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(addressDTO.getGln());
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    postalAddress.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new ehandel.no.invoice.BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    postalAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            new ehandel.no.invoice.StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    postalAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalBox())) {
                    ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                            new ehandel.no.invoice.PostboxCommonBasic();
                    postboxCommonBasic.setValue(addressDTO.getPostalBox());
                    postalAddress.setPostbox(postboxCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                            new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    postalAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    postalAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {

                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    postalAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountrySubentityCode())) {
                    ehandel.no.invoice.CountrySubentityCodeCommonBasic countrySubentityCodeCommonBasic =
                            new ehandel.no.invoice.CountrySubentityCodeCommonBasic();
                    countrySubentityCodeCommonBasic.setValue(addressDTO.getCountrySubentityCode());
                    postalAddress.setCountrySubentityCode(countrySubentityCodeCommonBasic);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    party.setPostalAddress(postalAddress);
                }
            }

            AddressDTO legalAddressDTO = supplierDTO.getLegalAddressDTO();
            ehandel.no.invoice.AddressType registrationAddress = null;
            boolean isRegistrationAddress = false;
            if (legalAddressDTO != null) {
                registrationAddress = new ehandel.no.invoice.AddressType();

                if (!StringUtils.isEmpty(legalAddressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(legalAddressDTO.getCityName());
                    registrationAddress.setCityName(cityNameCommonBasic);
                    isRegistrationAddress = true;
                }

                 if (!StringUtils.isEmpty(legalAddressDTO.getCountryCode())) {

                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(legalAddressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    registrationAddress.setCountry(countryType);
                    isRegistrationAddress = true;
                }
            }

            ehandel.no.invoice.PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new ehandel.no.invoice.PartyTaxSchemeCommonAggregate();
            ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic =
                    new ehandel.no.invoice.CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(supplierDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + taxTypeIntName);
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_TAX_SCHEME_AGENCY_ID.getValue());
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate =
                        new ehandel.no.invoice.TaxSchemeCommonAggregate();
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getLegalName())) {

                ehandel.no.invoice.RegistrationNameCommonBasic registrationNameCB =
                        new ehandel.no.invoice.RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(supplierDTO.getLegalName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic = new ehandel.no.invoice.CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(supplierDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                companyIDCommonBasic.setSchemeName(EHFConstants.LEGAL_ENTITY_SCHEME_NAME.getValue());
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.LEGAL_ENTITY_SCHEME_SGENCY_ID.getValue());
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }

            if (isRegistrationAddress) {
                partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
            }

            if (isPartyLegalEntity || isRegistrationAddress) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            supplierPartyType.setParty(party);
            invoice.setAccountingSupplierParty(supplierPartyType);
        }
    }

    /**
     * Method used to map supplier bank account information from ehandel.no.invoice.Invoice wrapper
     * to EHF ehandel.no.invoice.Invoice.
     */
    private static void mapSupplierBankAccount(InvoiceDTO invoiceDTO,
            ehandel.no.invoice.Invoice invoice) {

        SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();

        if (supplierDTO != null) {

            boolean isFinancialAddressAvailable = false;

            BankAccountDTO bankAccountDTO = supplierDTO.getBankAccountDTO();
            if (bankAccountDTO != null) {

                ehandel.no.invoice.FinancialAccountType financialAccountTypeForIBAN =
                        new ehandel.no.invoice.FinancialAccountType();
                ehandel.no.invoice.FinancialAccountType financialAccountTypeForBBAN =
                        new ehandel.no.invoice.FinancialAccountType();

                ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountNumber())) {

                    ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                    idCommonBasic.setValue(bankAccountDTO.getBankAccountNumber());
                    financialAccountTypeForBBAN.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountName())) {
                    nameCommonBasic = new ehandel.no.invoice.NameCommonBasic();
                    nameCommonBasic.setValue(bankAccountDTO.getBankAccountName());
                    financialAccountTypeForBBAN.setName(nameCommonBasic);
                }

                if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {

                    ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                    idCommonBasic.setValue(bankAccountDTO.getiBanNo());
                    financialAccountTypeForIBAN.setID(idCommonBasic);
                    financialAccountTypeForIBAN.setName(nameCommonBasic);
                }

                ehandel.no.invoice.FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                        new ehandel.no.invoice.FinancialInstitutionCommonAggregate();

                ehandel.no.invoice.BranchType branchType = null;
                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId())) {
                    branchType = new ehandel.no.invoice.BranchType();
                    ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(bankAccountDTO.getBankAccountId());
                    branchType.setID(idCommonBasic);
                } else if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountNumber())) {
                    branchType = new ehandel.no.invoice.BranchType();
                    ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(bankAccountDTO.getBankAccountNumber());
                    branchType.setID(idCommonBasic);
                }

                if (branchType != null) {
                    if (!StringUtils.isEmpty(bankAccountDTO.getBic())) {
                        ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBic());
                        idCommonBasic.setSchemeID(EHFConstants.BIC.getValue());
                        financialInstitutionCommonAggregate.setID(idCommonBasic);
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankName())) {
                        nameCommonBasic = new ehandel.no.invoice.NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
                    }

                    AddressDTO bankAddressDTO = bankAccountDTO.getBankAddressDTO();
                    if (bankAddressDTO != null) {

                        ehandel.no.invoice.AddressType bankAddress =
                                new ehandel.no.invoice.AddressType();

                        if (!StringUtils.isEmpty(bankAddressDTO.getBuildingNumber())) {
                            ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                                    new ehandel.no.invoice.BuildingNumberCommonBasic();
                            buildingNumberCommonBasic.setValue(bankAddressDTO.getBuildingNumber());
                            bankAddress.setBuildingNumber(buildingNumberCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getStreetName())) {
                            ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                                    new ehandel.no.invoice.StreetNameCommonBasic();
                            streetNameCommonBasic.setValue(bankAddressDTO.getStreetName());
                            bankAddress.setStreetName(streetNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalBox())) {
                            ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                                    new ehandel.no.invoice.PostboxCommonBasic();
                            postboxCommonBasic.setValue(bankAddressDTO.getPostalBox());
                            bankAddress.setPostbox(postboxCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalZone())) {
                            ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                                    new ehandel.no.invoice.PostalZoneCommonBasic();
                            postalZoneCommonBasic.setValue(bankAddressDTO.getPostalZone());
                            bankAddress.setPostalZone(postalZoneCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCityName())) {
                            ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                                    new ehandel.no.invoice.CityNameCommonBasic();
                            cityNameCommonBasic.setValue(bankAddressDTO.getCityName());
                            bankAddress.setCityName(cityNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCountryCode())) {

                            ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                    new ehandel.no.invoice.IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                            identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                            ehandel.no.invoice.CountryType countryType =
                                    new ehandel.no.invoice.CountryType();
                            countryType.setIdentificationCode(identificationCodeCommonBasic);
                            bankAddress.setCountry(countryType);
                            isFinancialAddressAvailable = true;
                        }

                        if (isFinancialAddressAvailable) {
                            financialInstitutionCommonAggregate.setAddress(bankAddress);
                        }
                    }
                    branchType.setFinancialInstitution(financialInstitutionCommonAggregate);
                    financialAccountTypeForBBAN.setFinancialInstitutionBranch(branchType);
                    if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {
                        financialAccountTypeForIBAN.setFinancialInstitutionBranch(branchType);
                    }
                }

                ehandel.no.invoice.PaymentMeansCommonAggregate paymentMeansCommonAggregateForBBAN =
                        new ehandel.no.invoice.PaymentMeansCommonAggregate();
                ehandel.no.invoice.PaymentMeansCommonAggregate paymentMeansCommonAggregateForIBAN =
                        new ehandel.no.invoice.PaymentMeansCommonAggregate();
                paymentMeansCommonAggregateForBBAN.setPayeeFinancialAccount(financialAccountTypeForBBAN);

                if (!StringUtils.isEmpty(invoiceDTO.getPaymentId())) {
                    ehandel.no.invoice.PaymentIDCommonBasic paymentIDCommonBasic =
                            new ehandel.no.invoice.PaymentIDCommonBasic();
                    paymentIDCommonBasic.setValue(invoiceDTO.getPaymentId());
                    paymentMeansCommonAggregateForBBAN.getPaymentIDs().add(paymentIDCommonBasic);
                    paymentMeansCommonAggregateForIBAN.getPaymentIDs().add(paymentIDCommonBasic);
                }

                invoice.getPaymentMeans().add(paymentMeansCommonAggregateForBBAN);
                if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {
                    paymentMeansCommonAggregateForIBAN.setPayeeFinancialAccount(financialAccountTypeForIBAN);
                    invoice.getPaymentMeans().add(paymentMeansCommonAggregateForIBAN);
                }
            }
        }
    }

    /**
     * Method used to map customer information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapCustomer(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            ehandel.no.invoice.PartyType party = new ehandel.no.invoice.PartyType();
            ehandel.no.invoice.AddressType postalAddress = new ehandel.no.invoice.AddressType();
            ehandel.no.invoice.CustomerPartyType customerPartyType =
                    new ehandel.no.invoice.CustomerPartyType();
            ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new ehandel.no.invoice.PartyLegalEntityCommonAggregate();

            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.EndpointIDCommonBasic endpointIDCB = null;

            boolean isPartyLegalEntity = false;

            String country = "";

            AddressDTO addressDTO = customerDTO.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(customerDTO.getEndpointId())) {
                endpointIDCB = new ehandel.no.invoice.EndpointIDCommonBasic();
                endpointIDCB.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                endpointIDCB.setValue(customerDTO.getEndpointId());
                party.setEndpointID(endpointIDCB);
            }

            if (!StringUtils.isEmpty(customerDTO.getId())) {
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(customerDTO.getId());
                ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new ehandel.no.invoice.PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);

                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(customerDTO.getName())) {
                ehandel.no.invoice.NameCommonBasic nameCommonBasic =
                        new ehandel.no.invoice.NameCommonBasic();
                nameCommonBasic.setValue(customerDTO.getName());
                ehandel.no.invoice.PartyNameCommonAggregate partyName =
                        new ehandel.no.invoice.PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
            }

            ehandel.no.invoice.ContactType contactType = new ehandel.no.invoice.ContactType();
            boolean isContactAvailable = false;
            if (!StringUtils.isEmpty(customerDTO.getContactId())) {
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(customerDTO.getContactId());
                contactType.setID(idCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getTelePhone())) {
                ehandel.no.invoice.TelephoneCommonBasic telephoneCommonBasic =
                        new ehandel.no.invoice.TelephoneCommonBasic();
                telephoneCommonBasic.setValue(customerDTO.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getTeleFax())) {
                ehandel.no.invoice.TelefaxCommonBasic telefaxCommonBasic =
                        new ehandel.no.invoice.TelefaxCommonBasic();
                telefaxCommonBasic.setValue(customerDTO.getTeleFax());
                contactType.setTelefax(telefaxCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getEmail())) {
                ehandel.no.invoice.ElectronicMailCommonBasic electronicMailCommonBasic =
                        new ehandel.no.invoice.ElectronicMailCommonBasic();
                electronicMailCommonBasic.setValue(customerDTO.getEmail());
                contactType.setElectronicMail(electronicMailCommonBasic);
                isContactAvailable = true;
            }

            if (isContactAvailable) {
                party.setContact(contactType);
            }

            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                if (!StringUtils.isEmpty(addressDTO.getGln())) {

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(addressDTO.getGln());
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    postalAddress.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new ehandel.no.invoice.BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    postalAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            new ehandel.no.invoice.StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    postalAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                            new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    postalAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalBox())) {
                    ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                            new ehandel.no.invoice.PostboxCommonBasic();
                    postboxCommonBasic.setValue(addressDTO.getPostalBox());
                    postalAddress.setPostbox(postboxCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    postalAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {

                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(country);
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    postalAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getUrl())) {
                    ehandel.no.invoice.WebsiteURICommonBasic websiteURICommonBasic =
                            new ehandel.no.invoice.WebsiteURICommonBasic();
                    websiteURICommonBasic.setValue(addressDTO.getUrl());
                    party.setWebsiteURI(websiteURICommonBasic);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    party.setPostalAddress(postalAddress);
                }
            }

            AddressDTO legalAddressDTO = customerDTO.getLegalAddressDTO();
            ehandel.no.invoice.AddressType registrationAddress = null;
            boolean isRegistrationAddress = false;
            if (legalAddressDTO != null) {
                registrationAddress = new ehandel.no.invoice.AddressType();

                if (!StringUtils.isEmpty(legalAddressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(legalAddressDTO.getCityName());
                    registrationAddress.setCityName(cityNameCommonBasic);
                    isRegistrationAddress = true;
                }

                 if (!StringUtils.isEmpty(legalAddressDTO.getCountryCode())) {

                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(legalAddressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    registrationAddress.setCountry(countryType);
                    isRegistrationAddress = true;
                }
            }

            ehandel.no.invoice.PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new ehandel.no.invoice.PartyTaxSchemeCommonAggregate();
            ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic =
                    new ehandel.no.invoice.CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(customerDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + taxTypeIntName);
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_TAX_SCHEME_AGENCY_ID.getValue());
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate =
                        new ehandel.no.invoice.TaxSchemeCommonAggregate();
                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            if (!StringUtils.isEmpty(customerDTO.getName())) {

                ehandel.no.invoice.RegistrationNameCommonBasic registrationNameCB =
                        new ehandel.no.invoice.RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(customerDTO.getName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {
                companyIDCommonBasic = new ehandel.no.invoice.CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(customerDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.LEGAL_ENTITY_SCHEME_SGENCY_ID.getValue());
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }

            if (isRegistrationAddress) {
                partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
            }
            if (isPartyLegalEntity || isRegistrationAddress) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            customerPartyType.setParty(party);
            invoice.setAccountingCustomerParty(customerPartyType);
        }
    }

    /**
     * Method used to map customer bank account information from ehandel.no.invoice.Invoice wrapper
     * to EHF ehandel.no.invoice.Invoice.
     */
    private static void mapCustomerBankAccount(InvoiceDTO invoiceDTO,
            ehandel.no.invoice.Invoice invoice) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            ehandel.no.invoice.BranchType branchType = null;
            ehandel.no.invoice.FinancialAccountType financialAccountType = null;
            ehandel.no.invoice.FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                    null;
            ehandel.no.invoice.PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            ehandel.no.invoice.PaymentMeansCodeCommonBasic paymentMeansCodeCommonBasic = null;

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;
            ehandel.no.invoice.AddressType bankAddress = null;

            boolean isFinancialInsAvailable = false;
            boolean isFinancialAddressAvailable = false;

            for (ehandel.no.invoice.PaymentMeansCommonAggregate paymentMeansCommonAggregate : invoice.getPaymentMeans()) {

                isFinancialInsAvailable = false;
                isFinancialAddressAvailable = false;

                if (paymentMeansCommonAggregate == null) {
                    paymentMeansCommonAggregate =
                            new ehandel.no.invoice.PaymentMeansCommonAggregate();
                    invoice.getPaymentMeans().add(paymentMeansCommonAggregate);
                }

                if (invoiceDTO.getPaymentDueDate() != null) {
                    paymentDueDateCommonBasic = new ehandel.no.invoice.PaymentDueDateCommonBasic();
                    paymentDueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPaymentDueDate()));
                    paymentMeansCommonAggregate.setPaymentDueDate(paymentDueDateCommonBasic);
                }

                paymentMeansCodeCommonBasic = new ehandel.no.invoice.PaymentMeansCodeCommonBasic();
                paymentMeansCodeCommonBasic.setValue(EHFConstants.PAYMENT_MEANS_CODE.getValue());
                paymentMeansCodeCommonBasic.setListID(EHFConstants.PAYMENT_MEANS_LIST_ID.getValue());
                paymentMeansCodeCommonBasic.setListAgencyID(EHFConstants.PAYMENT_MEANS_LIST_AGENCY_ID.getValue());
                paymentMeansCommonAggregate.setPaymentMeansCode(paymentMeansCodeCommonBasic);

                bankAccountDTO = customerDTO.getBankAccountDTO();
                if (bankAccountDTO != null) {

                    financialAccountType = new ehandel.no.invoice.FinancialAccountType();

                    ehandel.no.invoice.IDCommonBasic idCommonBasic = new ehandel.no.invoice.IDCommonBasic();

                    if (bankAccountDTO.getiBanNo() != null && !bankAccountDTO.getiBanNo().isEmpty()) {
                        idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                        idCommonBasic.setValue(bankAccountDTO.getiBanNo());
                    } else {
                        idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                        idCommonBasic.setValue(bankAccountDTO.getBankAccountNumber());
                    }
                    financialAccountType.setID(idCommonBasic);

                    financialInstitutionCommonAggregate =
                            new ehandel.no.invoice.FinancialInstitutionCommonAggregate();

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId())) {
                        idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBankAccountId());
                        branchType = new ehandel.no.invoice.BranchType();
                        branchType.setID(idCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBic())) {
                        idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBic());
                        idCommonBasic.setSchemeID(EHFConstants.BIC.getValue());
                        financialInstitutionCommonAggregate.setID(idCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankName())) {
                        ehandel.no.invoice.NameCommonBasic nameCommonBasic =
                                new ehandel.no.invoice.NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    bankAddressDTO = bankAccountDTO.getBankAddressDTO();

                    if (bankAddressDTO != null) {

                        bankAddress = new ehandel.no.invoice.AddressType();

                        if (!StringUtils.isEmpty(bankAddressDTO.getBuildingNumber())) {
                            ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                                    new ehandel.no.invoice.BuildingNumberCommonBasic();
                            buildingNumberCommonBasic.setValue(bankAddressDTO.getBuildingNumber());
                            bankAddress.setBuildingNumber(buildingNumberCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getStreetName())) {
                            ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                                    new ehandel.no.invoice.StreetNameCommonBasic();
                            streetNameCommonBasic.setValue(bankAddressDTO.getStreetName());
                            bankAddress.setStreetName(streetNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalBox())) {
                            ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                                    new ehandel.no.invoice.PostboxCommonBasic();
                            postboxCommonBasic.setValue(bankAddressDTO.getPostalBox());
                            bankAddress.setPostbox(postboxCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalZone())) {
                            ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                                    new ehandel.no.invoice.PostalZoneCommonBasic();
                            postalZoneCommonBasic.setValue(bankAddressDTO.getPostalZone());
                            bankAddress.setPostalZone(postalZoneCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCityName())) {
                            ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                                    new ehandel.no.invoice.CityNameCommonBasic();
                            cityNameCommonBasic.setValue(bankAddressDTO.getCityName());
                            bankAddress.setCityName(cityNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCountryCode())) {

                            ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                    new ehandel.no.invoice.IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                            identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                            ehandel.no.invoice.CountryType countryType =
                                    new ehandel.no.invoice.CountryType();
                            countryType.setIdentificationCode(identificationCodeCommonBasic);
                            bankAddress.setCountry(countryType);
                            isFinancialAddressAvailable = true;
                        }
                        if (isFinancialAddressAvailable) {
                            financialInstitutionCommonAggregate.setAddress(bankAddress);
                            isFinancialInsAvailable = true;
                        }
                    }

                    if (isFinancialInsAvailable && branchType != null) {
                        branchType.setFinancialInstitution(financialInstitutionCommonAggregate);
                        financialAccountType.setFinancialInstitutionBranch(branchType);
                    }

                    paymentMeansCommonAggregate.setPayerFinancialAccount(financialAccountType);
                }
            }
        }
    }

    /**
     * Method used to map delivery information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapDeliveryAddress(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        DeliveryDTO deliveryDTO = invoiceDTO.getDeliveryDTO();

        if (deliveryDTO != null) {

            ehandel.no.invoice.ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
            ehandel.no.invoice.DeliveryCommonAggregate deliveryCommonAggregate =
                    new ehandel.no.invoice.DeliveryCommonAggregate();
            if (deliveryDTO.getDeliveryDate() != null) {
                actualDeliveryDateCommonBasic =
                        new ehandel.no.invoice.ActualDeliveryDateCommonBasic();
                actualDeliveryDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(deliveryDTO.getDeliveryDate()));
                deliveryCommonAggregate.setActualDeliveryDate(actualDeliveryDateCommonBasic);
            }

            AddressDTO addressDTO = deliveryDTO.getDeliveryAddressDTO();
            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                ehandel.no.invoice.AddressType deliveryAddress =
                        new ehandel.no.invoice.AddressType();

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new ehandel.no.invoice.BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    deliveryAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            new ehandel.no.invoice.StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    deliveryAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getAdditionalStreetName())) {
                    ehandel.no.invoice.AdditionalStreetNameCommonBasic additionalStreetNameCommonBasic =
                            new ehandel.no.invoice.AdditionalStreetNameCommonBasic();
                    additionalStreetNameCommonBasic.setValue(addressDTO.getAdditionalStreetName());
                    deliveryAddress.setAdditionalStreetName(additionalStreetNameCommonBasic);
                    isAddressAvailable = true;
                }

                ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    deliveryAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {
                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    deliveryAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                ehandel.no.invoice.LocationType locationType =
                        new ehandel.no.invoice.LocationType();
                if (!StringUtils.isEmpty(addressDTO.getGln())) {
                    ehandel.no.invoice.IDCommonBasic idCommonBasic =
                            new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    locationType.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    locationType.setAddress(deliveryAddress);
                    deliveryCommonAggregate.setDeliveryLocation(locationType);
                }
            }
            invoice.getDeliveries().add(deliveryCommonAggregate);
        }
    }

    /**
     * Method used to map delivery information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice Line.
     */
    private static void mapDeliveryAddress(InvoiceDTO invoiceDTO,
            ehandel.no.invoice.InvoiceLineCommonAggregate invoiceLine) {

        DeliveryDTO deliveryDTO = invoiceDTO.getDeliveryDTO();

        if (deliveryDTO != null) {

            ehandel.no.invoice.ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
            ehandel.no.invoice.DeliveryCommonAggregate deliveryCommonAggregate =
                    new ehandel.no.invoice.DeliveryCommonAggregate();
            if (deliveryDTO.getDeliveryDate() != null) {
                actualDeliveryDateCommonBasic =
                        new ehandel.no.invoice.ActualDeliveryDateCommonBasic();
                actualDeliveryDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(deliveryDTO.getDeliveryDate()));
                deliveryCommonAggregate.setActualDeliveryDate(actualDeliveryDateCommonBasic);
            }

            AddressDTO addressDTO = deliveryDTO.getDeliveryAddressDTO();
            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                ehandel.no.invoice.AddressType deliveryAddress =
                        new ehandel.no.invoice.AddressType();

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new ehandel.no.invoice.BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    deliveryAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            new ehandel.no.invoice.StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    deliveryAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new ehandel.no.invoice.PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            new ehandel.no.invoice.CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    deliveryAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {
                    ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new ehandel.no.invoice.IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    ehandel.no.invoice.CountryType countryType =
                            new ehandel.no.invoice.CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    deliveryAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                ehandel.no.invoice.LocationType locationType =
                        new ehandel.no.invoice.LocationType();
                if (!StringUtils.isEmpty(addressDTO.getGln())) {
                    ehandel.no.invoice.IDCommonBasic idCommonBasic =
                            new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    locationType.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    locationType.setAddress(deliveryAddress);
                    deliveryCommonAggregate.setDeliveryLocation(locationType);
                }
            }
            invoiceLine.getDeliveries().add(deliveryCommonAggregate);
        }
    }

    /**
     * Method used to map currency information from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapCurrency(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();

        if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
            ehandel.no.invoice.DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                    new ehandel.no.invoice.DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setValue(currencyDTO.getCurrencyCode());
            documentCurrencyCodeCommonBasic.setName(currencyDTO.getCurrencyName());
            invoice.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);
        }
    }

    /**
     * Method used to map invoice exchange rate from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapExchangeRate(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        // Set exchange rate
        if (invoiceDTO.getExchangeRate() != null) {
            ehandel.no.invoice.CalculationRateCommonBasic calculationRateCommonBasic =
                    new ehandel.no.invoice.CalculationRateCommonBasic();
            calculationRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getExchangeRate()));
            ehandel.no.invoice.ExchangeRateType exchangeRateType =
                    new ehandel.no.invoice.ExchangeRateType();
            exchangeRateType.setCalculationRate(calculationRateCommonBasic);

            if (invoiceDTO.getCurrencyDTO() != null
                    && !StringUtils.isEmpty(invoiceDTO.getCurrencyDTO().getCurrencyCode())) {
                ehandel.no.invoice.SourceCurrencyCodeCommonBasic sourceCurrencyCodeCommonBasic =
                        new ehandel.no.invoice.SourceCurrencyCodeCommonBasic();
                sourceCurrencyCodeCommonBasic.setValue(invoiceDTO.getCurrencyDTO().getCurrencyCode());
                exchangeRateType.setSourceCurrencyCode(sourceCurrencyCodeCommonBasic);
            }

            if (invoiceDTO.getInvoiceCurrencyBaseRate() != null) {
                ehandel.no.invoice.SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                        new ehandel.no.invoice.SourceCurrencyBaseRateCommonBasic();
                sourceCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getInvoiceCurrencyBaseRate()));
                exchangeRateType.setSourceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic);
            }

            if (invoiceDTO.getBaseCurrencyDTO() != null
                    && !StringUtils.isEmpty(invoiceDTO.getBaseCurrencyDTO().getCurrencyCode())) {
                ehandel.no.invoice.TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                        new ehandel.no.invoice.TargetCurrencyCodeCommonBasic();
                targetCurrencyCodeCommonBasic.setValue(invoiceDTO.getBaseCurrencyDTO().getCurrencyCode());
                exchangeRateType.setTargetCurrencyCode(targetCurrencyCodeCommonBasic);
            }

            if (invoiceDTO.getBaseCurrencyBaseRate() != null) {
                ehandel.no.invoice.TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                        new ehandel.no.invoice.TargetCurrencyBaseRateCommonBasic();
                targetCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getBaseCurrencyBaseRate()));
                exchangeRateType.setTargetCurrencyBaseRate(targetCurrencyBaseRateCommonBasic);

                ehandel.no.invoice.MathematicOperatorCodeCommonBasic operator =
                        new ehandel.no.invoice.MathematicOperatorCodeCommonBasic();
                operator.setValue(EHFConstants.OP_MULTIPLY.getValue());
                exchangeRateType.setMathematicOperatorCode(operator);
            }
            invoice.setPricingExchangeRate(exchangeRateType);
        }
    }

    /**
     * Method used to map invoice line item details from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapInvoiceLineItems(InvoiceDTO invoiceDTO,
            ehandel.no.invoice.Invoice invoice) {

        List<InvoiceLineItemDTO> invoiceLineItemDTOs = invoiceDTO.getInvoiceLineItems();

        if (invoiceLineItemDTOs != null) {

            ehandel.no.invoice.ItemCommonAggregate item = null;
            ehandel.no.invoice.InvoiceLineCommonAggregate invoiceLine = null;
            ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            ehandel.no.invoice.TaxTotalCommonAggregate taxTotalCommonAggregate = null;
            ehandel.no.invoice.OrderLineReferenceCommonAggregate orderLineReferenceCommonAggregate =
                    null;
            ehandel.no.invoice.ItemIdentificationType sellersItemIdentification = null;
            ehandel.no.invoice.TaxCategoryType taxCategoryType = null;
            ehandel.no.invoice.PriceType price = null;
            ehandel.no.invoice.MonetaryTotalType monetaryTotalType = null;
            ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.InvoicedQuantityCommonBasic invoicedQuantityCommonBasic = null;
            ehandel.no.invoice.PayableAmountCommonBasic payableAmountCommonBasic = null;
            ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
            ehandel.no.invoice.PriceAmountCommonBasic priceAmountCommonBasic = null;
            ehandel.no.invoice.TaxAmountCommonBasic taxAmountCommonBasic = null;
            ehandel.no.invoice.LineExtensionAmountCommonBasic lineExtensionTaxAmount = null;
            ehandel.no.invoice.LineExtensionAmountCommonBasic lineExtensionTotalAmount = null;
            ehandel.no.invoice.TaxExclusiveAmountCommonBasic taxExclusiveAmountCommonBasic = null;
            ehandel.no.invoice.TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            ehandel.no.invoice.AccountingCostCommonBasic accountingCostCommonBasic = null;
            ehandel.no.invoice.LineIDCommonBasic lineIDCommonBasic = null;
            ehandel.no.invoice.ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            ehandel.no.invoice.AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic =
                    null;
            ehandel.no.invoice.AmountCommonBasic amountCommonBasic = null;
            ehandel.no.invoice.BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            ehandel.no.invoice.PayableRoundingAmountCommonBasic payableRoundingAmountCommonBasic =
                    null;
            ehandel.no.invoice.NoteCommonBasic noteCommonBasic = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            List<AllowanceChargeDTO> allowanceCharges = null;
            int i = 0;
            for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItemDTOs) {

                if (invoiceLineItemDTO.getTotalExcTax() == null) {
                    continue;
                }
                invoiceLine = new ehandel.no.invoice.InvoiceLineCommonAggregate();

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getId())) {
                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getId());
                    invoiceLine.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getNote())) {
                    noteCommonBasic = new ehandel.no.invoice.NoteCommonBasic();
                    noteCommonBasic.setValue(invoiceLineItemDTO.getNote());
                    noteCommonBasic.setLanguageID(invoiceDTO.getLanguage());
                    invoiceLine.setNote(noteCommonBasic);
                }

                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                idCommonBasic.setValue(invoiceLineItemDTO.getProductNo());
                idCommonBasic.setSchemeID(EHFConstants.GTIN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                sellersItemIdentification = new ehandel.no.invoice.ItemIdentificationType();
                sellersItemIdentification.setID(idCommonBasic);
                item = new ehandel.no.invoice.ItemCommonAggregate();
                item.setSellersItemIdentification(sellersItemIdentification);

                nameCommonBasic = new ehandel.no.invoice.NameCommonBasic();
                nameCommonBasic.setValue(invoiceLineItemDTO.getProductName());
                item.setName(nameCommonBasic);

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getAccountingCode())) {
                    accountingCostCommonBasic = new ehandel.no.invoice.AccountingCostCommonBasic();
                    accountingCostCommonBasic.setValue(invoiceLineItemDTO.getAccountingCode());
                    invoiceLine.setAccountingCost(accountingCostCommonBasic);
                }

                lineIDCommonBasic = new ehandel.no.invoice.LineIDCommonBasic();
                lineIDCommonBasic.setValue(String.valueOf(++i));
                orderLineReferenceCommonAggregate =
                        new ehandel.no.invoice.OrderLineReferenceCommonAggregate();
                orderLineReferenceCommonAggregate.setLineID(lineIDCommonBasic);
                invoiceLine.getOrderLineReferences().add(orderLineReferenceCommonAggregate);

                invoicedQuantityCommonBasic = new ehandel.no.invoice.InvoicedQuantityCommonBasic();
                invoicedQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                invoiceLine.setInvoicedQuantity(invoicedQuantityCommonBasic);

                if (invoiceLineItemDTO.getTaxPercent() != null) {

                    taxCategoryType = new ehandel.no.invoice.TaxCategoryType();

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(invoiceLineItemDTO.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new ehandel.no.invoice.PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(invoiceLineItemDTO.getTaxTypeIntName())) {

                        idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(invoiceLineItemDTO.getTaxTypeIntName());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate =
                                new ehandel.no.invoice.TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    item.getClassifiedTaxCategories().add(taxCategoryType);
                }

                if (invoiceLineItemDTO.getUnitPrice() != null) {

                    priceAmountCommonBasic = new ehandel.no.invoice.PriceAmountCommonBasic();
                    priceAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    priceAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getUnitPrice()));
                    price = new ehandel.no.invoice.PriceType();
                    price.setPriceAmount(priceAmountCommonBasic);

                    baseQuantityCommonBasic = new ehandel.no.invoice.BaseQuantityCommonBasic();
                    baseQuantityCommonBasic.setValue(new BigDecimal(1));
                    baseQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    price.setBaseQuantity(baseQuantityCommonBasic);
                    invoiceLine.setPrice(price);
                }

                if (invoiceLineItemDTO.getQuantity() != null) {
                    invoicedQuantityCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getQuantity()));
                    invoiceLine.setInvoicedQuantity(invoicedQuantityCommonBasic);
                }

                if (invoiceLineItemDTO.getTotalExcTax() != null) {
                    lineExtensionTaxAmount =
                            new ehandel.no.invoice.LineExtensionAmountCommonBasic();
                    lineExtensionTaxAmount.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTotalExcTax()));
                    lineExtensionTaxAmount.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    invoiceLine.setLineExtensionAmount(lineExtensionTaxAmount);
                }

                if (invoiceLineItemDTO.getTaxAmount() != null) {
                    taxAmountCommonBasic = new ehandel.no.invoice.TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTaxAmount()));
                    taxAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    taxTotalCommonAggregate = new ehandel.no.invoice.TaxTotalCommonAggregate();
                    taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                    invoiceLine.getTaxTotals().add(taxTotalCommonAggregate);
                }

                allowanceCharges = invoiceLineItemDTO.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    ehandel.no.invoice.AllowanceChargeType allowanceChargeType = null;
                    for (AllowanceChargeDTO allowanceChargeDTO : allowanceCharges) {

                        allowanceChargeType = new ehandel.no.invoice.AllowanceChargeType();

                        chargeIndicatorCommonBasic =
                                new ehandel.no.invoice.ChargeIndicatorCommonBasic();
                        chargeIndicatorCommonBasic.setValue(allowanceChargeDTO.isChargeIndicator());
                        allowanceChargeType.setChargeIndicator(chargeIndicatorCommonBasic);

                        if (allowanceChargeDTO.getAllowanceChargeReason() != null) {
                            allowanceChargeReasonCommonBasic =
                                    new ehandel.no.invoice.AllowanceChargeReasonCommonBasic();
                            allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                            allowanceChargeType.setAllowanceChargeReason(allowanceChargeReasonCommonBasic);
                        }

                        if (allowanceChargeDTO.getAmount() != null) {
                            amountCommonBasic = new ehandel.no.invoice.AmountCommonBasic();
                            amountCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getAmount()));
                            amountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                            allowanceChargeType.setAmount(amountCommonBasic);
                        }

                        if (allowanceChargeDTO.getTaxPercent() != null) {

                            taxCategoryType = new ehandel.no.invoice.TaxCategoryType();

                            idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                            idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(allowanceChargeDTO.getTaxPercent()));
                            idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                            idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                            taxCategoryType.setID(idCommonBasic);

                            percentCommonBasic = new ehandel.no.invoice.PercentCommonBasic();
                            percentCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getTaxPercent()));
                            taxCategoryType.setPercent(percentCommonBasic);

                            if (!StringUtils.isEmpty(allowanceChargeDTO.getTaxType())) {

                                idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                                idCommonBasic.setValue(allowanceChargeDTO.getTaxType());
                                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                                taxSchemeCommonAggregate =
                                        new ehandel.no.invoice.TaxSchemeCommonAggregate();
                                taxSchemeCommonAggregate.setID(idCommonBasic);
                            }

                            taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                            allowanceChargeType.getTaxCategories().add(taxCategoryType);
                        }

                        invoiceLine.getAllowanceCharges().add(allowanceChargeType);
                    }
                }

                mapDeliveryAddress(invoiceDTO, invoiceLine);
                invoiceLine.setItem(item);
                invoice.getInvoiceLines().add(invoiceLine);
            }

            monetaryTotalType = invoice.getLegalMonetaryTotal();
            if (monetaryTotalType == null) {
                monetaryTotalType = new ehandel.no.invoice.MonetaryTotalType();
                invoice.setLegalMonetaryTotal(monetaryTotalType);
            }

            BigDecimal taxExclusiveAmount = null;
            if (invoiceDTO.getTotalExcTax() != null) {

                lineExtensionTotalAmount = new ehandel.no.invoice.LineExtensionAmountCommonBasic();
                lineExtensionTotalAmount.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getTotalExcTax()));
                lineExtensionTotalAmount.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setLineExtensionAmount(lineExtensionTotalAmount);

                taxExclusiveAmount = ConversionUtils.asBigDecimal(invoiceDTO.getTotalExcTax());

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

                taxExclusiveAmountCommonBasic =
                        new ehandel.no.invoice.TaxExclusiveAmountCommonBasic();
                taxExclusiveAmountCommonBasic.setValue(taxExclusiveAmount);
                taxExclusiveAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountCommonBasic);
            }

            if (taxExclusiveAmount != null) {

                BigDecimal taxInclusiveAmount =
                        invoiceDTO.getTaxAmount() != null ? taxExclusiveAmount.add(ConversionUtils.asBigDecimal(invoiceDTO.getTaxAmount()))
                                : taxExclusiveAmount;

                payableRoundingAmountCommonBasic =
                        new ehandel.no.invoice.PayableRoundingAmountCommonBasic();
               if (invoiceDTO.getIsRoundPayableAmount()) {
                    if (invoiceDTO.getPayableRoundingAmount() != null) {
                        payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getPayableRoundingAmount()));
                    } else {
                        payableRoundingAmountCommonBasic.setValue(ConversionUtils.getRoundingAmount(taxInclusiveAmount));
                    }
               } else {
                    payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(-0.0d));
               }

                payableRoundingAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setPayableRoundingAmount(payableRoundingAmountCommonBasic);

                BigDecimal payableAmount =
                        taxInclusiveAmount.add(payableRoundingAmountCommonBasic.getValue());

                taxInclusiveAmountCommonBasic =
                        new ehandel.no.invoice.TaxInclusiveAmountCommonBasic();
                taxInclusiveAmountCommonBasic.setValue(payableAmount);
                taxInclusiveAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountCommonBasic);

                payableAmountCommonBasic = new ehandel.no.invoice.PayableAmountCommonBasic();
                payableAmountCommonBasic.setValue(payableAmount);
                payableAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setPayableAmount(payableAmountCommonBasic);
            }

            invoice.setLegalMonetaryTotal(monetaryTotalType);
        }
    }

    /**
     * Method used to map tax summary details from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapTaxSummaries(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        List<TaxSummaryDTO> taxSummaries = invoiceDTO.getTaxSummaries();

        if (taxSummaries != null) {

            ehandel.no.invoice.TaxCategoryType taxCategoryType = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
            ehandel.no.invoice.TaxAmountCommonBasic taxAmountCommonBasic = null;
            ehandel.no.invoice.TaxSubtotalCommonAggregate taxSubtotalCommonAggregate = null;
            ehandel.no.invoice.TaxableAmountCommonBasic taxableAmountCommonBasic = null;
            ehandel.no.invoice.TaxExemptionReasonCodeCommonBasic taxExemptionReasonCodeCommonBasic =
                    null;
            ehandel.no.invoice.TaxExemptionReasonCommonBasic taxExemptionReasonCommonBasic = null;
            ehandel.no.invoice.TaxTotalCommonAggregate taxTotalCommonAggregate = null;
            ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

            String currencyCode = null;
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            taxTotalCommonAggregate = new ehandel.no.invoice.TaxTotalCommonAggregate();
            for (TaxSummaryDTO taxSummary : taxSummaries) {

                taxSubtotalCommonAggregate = new ehandel.no.invoice.TaxSubtotalCommonAggregate();
                if (taxSummary.getTotalExcTax() != null) {
                    taxableAmountCommonBasic = new ehandel.no.invoice.TaxableAmountCommonBasic();
                    taxableAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTotalExcTax()));
                    taxableAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    taxSubtotalCommonAggregate.setTaxableAmount(taxableAmountCommonBasic);
                }

                if (taxSummary.getTaxAmount() != null) {
                    taxAmountCommonBasic = new ehandel.no.invoice.TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxAmount()));
                    taxAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    taxSubtotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                }

                if (taxSummary.getTaxPercent() != null) {

                    taxCategoryType = new ehandel.no.invoice.TaxCategoryType();

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(taxSummary.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new ehandel.no.invoice.PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(taxSummary.getTaxTypeIntName())) {

                        idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(taxSummary.getTaxTypeIntName());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate =
                                new ehandel.no.invoice.TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    if (taxSummary.getTaxPercent() == 0) {

                        if(taxSummary.getTaxExcemptionReasion() == null) {

                            throw new RuntimeException("TaxExcemptionReasion can't be null when tax percent is 0 inside \"tax summary\" ");
                        }

                        if (taxSummary.getTaxExcemptionReasionCode() != null) {
                            taxExemptionReasonCodeCommonBasic =
                                    new ehandel.no.invoice.TaxExemptionReasonCodeCommonBasic();
                            taxExemptionReasonCodeCommonBasic.setValue(taxSummary.getTaxExcemptionReasionCode());
                            taxCategoryType.setTaxExemptionReasonCode(taxExemptionReasonCodeCommonBasic);
                        }

                        taxExemptionReasonCommonBasic =
                                new ehandel.no.invoice.TaxExemptionReasonCommonBasic();
                        taxExemptionReasonCommonBasic.setValue(taxSummary.getTaxExcemptionReasion());
                        taxCategoryType.setTaxExemptionReason(taxExemptionReasonCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    taxSubtotalCommonAggregate.setTaxCategory(taxCategoryType);
                }

                if (taxSubtotalCommonAggregate != null) {
                    taxTotalCommonAggregate.getTaxSubtotals().add(taxSubtotalCommonAggregate);
                }
            }

            if (invoiceDTO.getTaxAmount() != null) {
                taxAmountCommonBasic = new ehandel.no.invoice.TaxAmountCommonBasic();
                taxAmountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getTaxAmount()));
                taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
            }
            invoice.getTaxTotals().add(taxTotalCommonAggregate);
        }
    }

    /**
     * Method used to map tax payment terms from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapPaymentTerms(InvoiceDTO invoiceDTO, ehandel.no.invoice.Invoice invoice) {

        ehandel.no.invoice.PaymentTermsCommonAggregate paymentTermsCommonAggregate = null;
        ehandel.no.invoice.NoteCommonBasic noteCommonBasic = null;

        List<InvoiceSettingDTO> invoiceSettingDTOs = invoiceDTO.getInvoiceSettingDTOs();
        if (invoiceSettingDTOs != null && invoiceSettingDTOs.size() > 0) {

            InvoiceSettingDTO invoiceSettingDTO = invoiceSettingDTOs.get(0);
            List<String> paymentTermsNotes = invoiceSettingDTO.getPaymentTermsNotes();
            if (paymentTermsNotes != null && !paymentTermsNotes.isEmpty()) {

                for (String paymentTermsNote : paymentTermsNotes) {
                    paymentTermsCommonAggregate =
                            new ehandel.no.invoice.PaymentTermsCommonAggregate();
                    noteCommonBasic = new ehandel.no.invoice.NoteCommonBasic();
                    noteCommonBasic.setValue(paymentTermsNote);
                    paymentTermsCommonAggregate.getNotes().add(noteCommonBasic);
                    invoice.getPaymentTerms().add(paymentTermsCommonAggregate);
                }
            }
        }
    }

    /**
     * Method used to map allowance charges from ehandel.no.invoice.Invoice wrapper to EHF
     * ehandel.no.invoice.Invoice.
     */
    private static void mapAllowanceCharges(InvoiceDTO invoiceDTO,
            ehandel.no.invoice.Invoice invoice) {

        ehandel.no.invoice.AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ehandel.no.invoice.ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        ehandel.no.invoice.AmountCommonBasic amountCommonBasic = null;
        ehandel.no.invoice.AllowanceChargeType allowanceChargeType = null;

        List<AllowanceChargeDTO> allowanceCharges = invoiceDTO.getAllowanceCharges();
        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            ehandel.no.invoice.TaxCategoryType taxCategoryType = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
            ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

            double allowanceTotalAmount = 0d;
            double chargeTotalAmount = 0d;

            String currencyCode = "";
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            for (AllowanceChargeDTO allowanceChargeDTO : allowanceCharges) {

                allowanceChargeType = new ehandel.no.invoice.AllowanceChargeType();

                chargeIndicatorCommonBasic = new ehandel.no.invoice.ChargeIndicatorCommonBasic();
                chargeIndicatorCommonBasic.setValue(allowanceChargeDTO.isChargeIndicator());
                allowanceChargeType.setChargeIndicator(chargeIndicatorCommonBasic);

                if (allowanceChargeDTO.getAllowanceChargeReason() != null) {
                    allowanceChargeReasonCommonBasic =
                            new ehandel.no.invoice.AllowanceChargeReasonCommonBasic();
                    allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                    allowanceChargeType.setAllowanceChargeReason(allowanceChargeReasonCommonBasic);
                }

                if (allowanceChargeDTO.getAmount() != null) {
                    amountCommonBasic = new ehandel.no.invoice.AmountCommonBasic();
                    amountCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getAmount()));
                    amountCommonBasic.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                    allowanceChargeType.setAmount(amountCommonBasic);

                    if (allowanceChargeDTO.isChargeIndicator()) {
                        chargeTotalAmount += allowanceChargeDTO.getAmount();
                    } else {
                        allowanceTotalAmount += allowanceChargeDTO.getAmount();
                    }
                }

                if (allowanceChargeDTO.getTaxPercent() != null) {

                    taxCategoryType = new ehandel.no.invoice.TaxCategoryType();

                    idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(allowanceChargeDTO.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new ehandel.no.invoice.PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(allowanceChargeDTO.getTaxType())) {

                        idCommonBasic = new ehandel.no.invoice.IDCommonBasic();
                        idCommonBasic.setValue(allowanceChargeDTO.getTaxType());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate =
                                new ehandel.no.invoice.TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    allowanceChargeType.getTaxCategories().add(taxCategoryType);
                }
                invoice.getAllowanceCharges().add(allowanceChargeType);
            }

            ehandel.no.invoice.MonetaryTotalType legalMonetaryTotal =
                    new ehandel.no.invoice.MonetaryTotalType();

            if (allowanceTotalAmount > 0) {
                ehandel.no.invoice.AllowanceTotalAmountCommonBasic allowanceTotalAmountCB =
                        new ehandel.no.invoice.AllowanceTotalAmountCommonBasic();
                allowanceTotalAmountCB.setValue(ConversionUtils.asBigDecimal(allowanceTotalAmount));
                allowanceTotalAmountCB.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                legalMonetaryTotal.setAllowanceTotalAmount(allowanceTotalAmountCB);
            }

            if (chargeTotalAmount > 0) {
                ehandel.no.invoice.ChargeTotalAmountCommonBasic chargeTotalAmountCB =
                        new ehandel.no.invoice.ChargeTotalAmountCommonBasic();
                chargeTotalAmountCB.setValue(ConversionUtils.asBigDecimal(chargeTotalAmount));
                chargeTotalAmountCB.setCurrencyID(ehandel.no.invoice.CurrencyCodeContentType.fromValue(currencyCode));
                legalMonetaryTotal.setChargeTotalAmount(chargeTotalAmountCB);
            }
            invoice.setLegalMonetaryTotal(legalMonetaryTotal);
        }
    }

    /**
     * Unmarshalling XML file to invoice object.
     */
    public static InvoiceDTO getEHFInvoice(File file) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.invoice.Invoice.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        ehandel.no.invoice.Invoice invoice =
                (ehandel.no.invoice.Invoice) unmarshaller.unmarshal(file);

        return convertToEHFInvoice(invoice);
    }

    /**
     * Method used to convert EHF ehandel.no.invoice.Invoice XML file to EHF
     * ehandel.no.invoice.Invoice object
     */
    public static InvoiceDTO getEHFInvoice(String filePath) throws JAXBException {

        File file = new File(filePath);
        return getEHFInvoice(file);
    }

    /**
     * Method used to convert EHF ehandel.no.invoice.Invoice XML file stream to EHF
     * ehandel.no.invoice.Invoice object
     */
    public static ehandel.no.invoice.Invoice getInvoice(InputStream is) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.invoice.Invoice.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        ehandel.no.invoice.Invoice invoice =
                (ehandel.no.invoice.Invoice) unmarshaller.unmarshal(is);
        return invoice;
    }

    public static InvoiceDTO getEHFInvoice(InputStream is) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.invoice.Invoice.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        ehandel.no.invoice.Invoice invoice =
                (ehandel.no.invoice.Invoice) unmarshaller.unmarshal(is);

        return convertToEHFInvoice(invoice);
    }

    /**
     * Method used to convert EHF invoice to ehandel.no.invoice.Invoice wrapper.
     */
    public static InvoiceDTO convertToEHFInvoice(ehandel.no.invoice.Invoice invoice) {

        InvoiceDTO invoiceDTO = null;
        if (invoice != null) {

            invoiceDTO = new InvoiceDTO();

            // Set UBL version id
            ehandel.no.invoice.UBLVersionIDCommonBasic ublVersionIDCommonBasic =
                    invoice.getUBLVersionID();
            if (ublVersionIDCommonBasic != null) {
                invoiceDTO.setUblExtensions(ublVersionIDCommonBasic.getValue());
            }

            // Set customization id
            ehandel.no.invoice.CustomizationIDCommonBasic customizationIDCommonBasic =
                    invoice.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                invoiceDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            // Set profile id
            ehandel.no.invoice.ProfileIDCommonBasic profileIDCommonBasic = invoice.getProfileID();
            if (profileIDCommonBasic != null) {
                invoiceDTO.setProfileID(profileIDCommonBasic.getValue());
            }

            // Set invoice no
            ehandel.no.invoice.IDCommonBasic idCommonBasic = invoice.getID();
            if (idCommonBasic != null) {
                invoiceDTO.setInvoiceNo(idCommonBasic.getValue());
            }

            // Set issue date
            ehandel.no.invoice.IssueDateCommonBasic issueDateCommonBasic = invoice.getIssueDate();
            if (issueDateCommonBasic != null) {
                invoiceDTO.setIssueDate(ConversionUtils.asDate(issueDateCommonBasic.getValue()));
            }

            // Set purchase no
            ehandel.no.invoice.OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                    invoice.getOrderReference();
            if (orderReferenceCommonAggregate != null) {
                idCommonBasic = orderReferenceCommonAggregate.getID();
                if (idCommonBasic != null) {
                    invoiceDTO.setPurchaseOrderNo(idCommonBasic.getValue());
                }
            }

            List<ehandel.no.invoice.DocumentReferenceType> additionalDocumentReferences =
                    invoice.getAdditionalDocumentReferences();
            if (additionalDocumentReferences != null) {

                ehandel.no.invoice.AttachmentType attachmentType = null;
                ehandel.no.invoice.DocumentTypeCommonBasic documentTypeCommonBasic = null;
                ehandel.no.invoice.EmbeddedDocumentBinaryObjectCommonBasic embeddedDocumentBinaryObject =
                        null;

                List<FileDTO> files = new ArrayList<FileDTO>();
                FileDTO fileDTO = null;
                String fileExt = "";

                for (ehandel.no.invoice.DocumentReferenceType documentReferenceType : additionalDocumentReferences) {

                    documentTypeCommonBasic = documentReferenceType.getDocumentType();
                    fileDTO = new FileDTO();

                    if (documentTypeCommonBasic != null) {
                        fileDTO.setDocType(documentTypeCommonBasic.getValue());
                    }

                    attachmentType = documentReferenceType.getAttachment();
                    if (attachmentType != null) {
                        embeddedDocumentBinaryObject =
                                attachmentType.getEmbeddedDocumentBinaryObject();
                        if (embeddedDocumentBinaryObject != null) {

                            fileDTO.setFileContent(embeddedDocumentBinaryObject.getValue());

                            if (embeddedDocumentBinaryObject.getMimeCode() != null) {
                                fileExt = embeddedDocumentBinaryObject.getMimeCode().value();
                                if (!StringUtils.isEmpty(fileExt) && fileExt.indexOf("/") != -1) {
                                    fileExt = fileExt.substring(fileExt.lastIndexOf("/") + 1);
                                } else {
                                    fileExt = EHFConstants.PDF.getValue();
                                }
                            }
                        }

                        idCommonBasic = documentReferenceType.getID();
                        if (idCommonBasic != null) {
                            fileDTO.setFileName(idCommonBasic.getValue()
                                    + EHFConstants.DOT.getValue() + fileExt);
                        }
                    }
                    files.add(fileDTO);
                }
                invoiceDTO.setFiles(files);
            }

            List<ehandel.no.invoice.NoteCommonBasic> notes = invoice.getNotes();
            if (notes != null && !notes.isEmpty()) {
                for (ehandel.no.invoice.NoteCommonBasic noteCommonBasic : notes) {
                    invoiceDTO.getNotes().add(noteCommonBasic.getValue());
                    invoiceDTO.setLanguage(noteCommonBasic.getLanguageID());
                }
            }

            mapPayeeParty(invoice, invoiceDTO);
            mapSupplier(invoice, invoiceDTO);
            mapSupplierBankAccount(invoice, invoiceDTO);
            mapCustomer(invoice, invoiceDTO);
            mapCustomerBankAccount(invoice, invoiceDTO);
            mapCurrency(invoice, invoiceDTO);
            mapExchangeRate(invoice, invoiceDTO);
            mapDeliveryAddress(invoice, invoiceDTO);
            mapInvoiceLineItems(invoice, invoiceDTO);
            mapTaxSummaries(invoice, invoiceDTO);
            mapPaymentTerms(invoice, invoiceDTO);
            mapAllowanceCharges(invoice, invoiceDTO);
        }

        return invoiceDTO;
    }

    /**
     * Method used to map payee information from EHF invoice to ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapPayeeParty(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.PartyType partyType = invoice.getPayeeParty();
        PayeeParty payeeParty = new PayeeParty();

        if (partyType != null) {

            List<ehandel.no.invoice.PartyIdentificationCommonAggregate> partyIdentifications =
                    partyType.getPartyIdentifications();
            if (partyIdentifications != null) {

                for (ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentification : partyIdentifications) {
                    if (partyIdentification.getID() != null) {
                        payeeParty.setId(partyIdentification.getID().getValue());
                    }
                }
            }

            List<ehandel.no.invoice.PartyNameCommonAggregate> partyNames = partyType.getPartyNames();
            if (partyNames != null) {
                for (ehandel.no.invoice.PartyNameCommonAggregate partyName : partyNames) {
                    ehandel.no.invoice.NameCommonBasic nameCommonBasic = partyName.getName();
                    if (nameCommonBasic != null) {
                        payeeParty.setName(nameCommonBasic.getValue());
                    }
                }
            }

            List<ehandel.no.invoice.PartyLegalEntityCommonAggregate> partyLegalEntities =
                    partyType.getPartyLegalEntities();
            if (partyLegalEntities != null) {
                for (ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntity : partyLegalEntities) {
                    ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic =
                            partyLegalEntity.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        payeeParty.setOrganizationNo(companyIDCommonBasic.getValue());
                    }
                }
            }

            ehandel.no.invoice.AddressType addressType = partyType.getPostalAddress();
            AddressDTO addressDTO = new AddressDTO();
            if (addressType != null && addressType.getCountry() != null) {
                 addressDTO.setCountryCode(addressType.getCountry().getIdentificationCode().getValue());
                 payeeParty.setAddressDTO(addressDTO);
            }
            invoiceDTO.setPayeeParty(payeeParty);
        }
    }

    /**
     * Method used to map supplier information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapSupplier(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.SupplierPartyType supplierPartyType =
                invoice.getAccountingSupplierParty();

        if (supplierPartyType != null) {

            SupplierDTO supplierDTO = new SupplierDTO();
            AddressDTO addressDTO = null;

            ehandel.no.invoice.PartyType party = supplierPartyType.getParty();

            if (party != null) {

                ehandel.no.invoice.EndpointIDCommonBasic endpointIDCB = party.getEndpointID();
                if (endpointIDCB != null) {
                    supplierDTO.setEndpointId(endpointIDCB.getValue());
                }

                ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
                for (ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentificationCommonAggregate : party.getPartyIdentifications()) {
                    idCommonBasic = partyIdentificationCommonAggregate.getID();
                    if (idCommonBasic != null) {
                        supplierDTO.setId(idCommonBasic.getValue());
                    }
                }

                ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
                for (ehandel.no.invoice.PartyNameCommonAggregate partyNameCommonAggregate : party.getPartyNames()) {
                    nameCommonBasic = partyNameCommonAggregate.getName();
                    if (nameCommonBasic != null) {
                        supplierDTO.setName(nameCommonBasic.getValue());
                    }
                }

                ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic = null;
                for (ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate : party.getPartyLegalEntities()) {
                    companyIDCommonBasic = partyLegalEntityCommonAggregate.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        supplierDTO.setOrganizationNo(companyIDCommonBasic.getValue());
                    }
                    if (partyLegalEntityCommonAggregate.getRegistrationName() != null) {
                        supplierDTO.setLegalName(partyLegalEntityCommonAggregate.getRegistrationName().getValue());
                    }
                    if (partyLegalEntityCommonAggregate.getRegistrationAddress() != null) {
                        AddressDTO legalAddressDTO = new AddressDTO();
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName() != null) {
                            legalAddressDTO.setCityName(partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName().getValue());
                        }
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry() != null) {
                            if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry().getIdentificationCode() != null) {
                                legalAddressDTO.setCountryCode(partyLegalEntityCommonAggregate.getRegistrationAddress()
                                        .getCountry().getIdentificationCode().getValue());
                            }
                        }
                        supplierDTO.setLegalAddressDTO(legalAddressDTO);
                    }
                }

                ehandel.no.invoice.AddressType postalAddress = party.getPostalAddress();

                if (postalAddress != null) {

                    addressDTO = new AddressDTO();

                    idCommonBasic = postalAddress.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                        addressDTO.setGln(idCommonBasic.getValue());
                    }

                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            postalAddress.getBuildingNumber();
                    if (buildingNumberCommonBasic != null) {
                        addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                    }

                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            postalAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                            postalAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                            postalAddress.getPostbox();
                    if (postboxCommonBasic != null) {
                        addressDTO.setPostalBox(postboxCommonBasic.getValue());
                    }

                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            postalAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    ehandel.no.invoice.CountryType countryType = postalAddress.getCountry();
                    if (countryType != null) {
                        ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                }

                ehandel.no.invoice.WebsiteURICommonBasic websiteURICommonBasic =
                        party.getWebsiteURI();
                if (websiteURICommonBasic != null) {
                    addressDTO.setUrl(websiteURICommonBasic.getValue());
                }

                supplierDTO.setAddressDTO(addressDTO);
            }

            ehandel.no.invoice.ContactType contactType = party.getContact();
            if (contactType != null) {

                ehandel.no.invoice.IDCommonBasic idCommonBasic = contactType.getID();
                if (idCommonBasic != null) {
                    supplierDTO.setContactId(idCommonBasic.getValue());
                }

                ehandel.no.invoice.TelephoneCommonBasic telephoneCommonBasic =
                        contactType.getTelephone();
                if (telephoneCommonBasic != null) {
                    supplierDTO.setTelePhone(telephoneCommonBasic.getValue());
                }

                ehandel.no.invoice.TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
                if (telefaxCommonBasic != null) {
                    supplierDTO.setTeleFax(telefaxCommonBasic.getValue());
                }

                ehandel.no.invoice.ElectronicMailCommonBasic electronicMailCommonBasic =
                        contactType.getElectronicMail();
                if (electronicMailCommonBasic != null) {
                    supplierDTO.setEmail(electronicMailCommonBasic.getValue());
                }
            }
            invoiceDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map supplier bank account information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapSupplierBankAccount(ehandel.no.invoice.Invoice invoice,
            InvoiceDTO invoiceDTO) {

        List<ehandel.no.invoice.PaymentMeansCommonAggregate> paymentMeans =
                invoice.getPaymentMeans();

        if (paymentMeans != null) {

            AddressDTO bankAddressDTO = null;

            ehandel.no.invoice.FinancialAccountType financialAccountType = null;
            ehandel.no.invoice.BranchType branchType = null;
            ehandel.no.invoice.AddressType bankAddress = null;
            ehandel.no.invoice.CountryType countryType = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
            ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic = null;
            ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
            ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic = null;
            ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic = null;
            ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic = null;
            ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
            ehandel.no.invoice.FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                    null;
            List<ehandel.no.invoice.PaymentIDCommonBasic> paymentIDs = null;
            BankAccountDTO bankAccountDTO = new BankAccountDTO();

            for (ehandel.no.invoice.PaymentMeansCommonAggregate paymentMeansCommonAggregate : paymentMeans) {

                paymentIDs = paymentMeansCommonAggregate.getPaymentIDs();
                if (paymentIDs != null && !paymentIDs.isEmpty()) {
                    invoiceDTO.setPaymentId(paymentIDs.get(0).getValue());
                }

                financialAccountType = paymentMeansCommonAggregate.getPayeeFinancialAccount();
                if (financialAccountType != null) {

                    idCommonBasic = financialAccountType.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null
                            && idCommonBasic.getSchemeID() != null) {

                        if (EHFConstants.IBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setiBanNo(idCommonBasic.getValue());
                        } else if (EHFConstants.BBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setBankAccountNumber(idCommonBasic.getValue());
                        }
                    }

                    nameCommonBasic = financialAccountType.getName();
                    if (nameCommonBasic != null) {
                        bankAccountDTO.setBankAccountName(nameCommonBasic.getValue());
                    }

                    branchType = financialAccountType.getFinancialInstitutionBranch();
                    if (branchType != null) {

                        idCommonBasic = branchType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            bankAccountDTO.setBankAccountId(idCommonBasic.getValue());
                        }

                        financialInstitutionCommonAggregate = branchType.getFinancialInstitution();
                        if (financialInstitutionCommonAggregate != null) {

                            idCommonBasic = financialInstitutionCommonAggregate.getID();
                            if (idCommonBasic != null) {
                                bankAccountDTO.setBic(idCommonBasic.getValue());
                            }

                            nameCommonBasic = financialInstitutionCommonAggregate.getName();
                            if (nameCommonBasic != null) {
                                bankAccountDTO.setBankName(nameCommonBasic.getValue());
                            }

                            bankAddress = financialInstitutionCommonAggregate.getAddress();
                            if (bankAddress != null) {

                                bankAddressDTO = new AddressDTO();

                                buildingNumberCommonBasic = bankAddress.getBuildingNumber();
                                if (buildingNumberCommonBasic != null) {
                                    bankAddressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                                }

                                streetNameCommonBasic = bankAddress.getStreetName();
                                if (streetNameCommonBasic != null) {
                                    bankAddressDTO.setStreetName(streetNameCommonBasic.getValue());
                                }

                                postalZoneCommonBasic = bankAddress.getPostalZone();
                                if (postalZoneCommonBasic != null) {
                                    bankAddressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                                }

                                postboxCommonBasic = bankAddress.getPostbox();
                                if (postboxCommonBasic != null) {
                                    bankAddressDTO.setPostalBox(postboxCommonBasic.getValue());
                                }

                                cityNameCommonBasic = bankAddress.getCityName();
                                if (cityNameCommonBasic != null) {
                                    bankAddressDTO.setCityName(cityNameCommonBasic.getValue());
                                }

                                countryType = bankAddress.getCountry();
                                if (countryType != null) {
                                    identificationCodeCommonBasic =
                                            countryType.getIdentificationCode();
                                    if (identificationCodeCommonBasic != null) {
                                        bankAddressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                                    }
                                }
                                bankAccountDTO.setBankAddressDTO(bankAddressDTO);
                            }
                        }
                    }
                }
            }

            SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();
            if (supplierDTO == null) {
                supplierDTO = new SupplierDTO();
            }
            supplierDTO.setBankAccountDTO(bankAccountDTO);
            invoiceDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map customer information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapCustomer(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.CustomerPartyType customerPartyType =
                invoice.getAccountingCustomerParty();

        if (customerPartyType != null) {

            CustomerDTO customerDTO = new CustomerDTO();
            AddressDTO addressDTO = null;

            ehandel.no.invoice.PartyType party = customerPartyType.getParty();
            if (party != null) {

                ehandel.no.invoice.EndpointIDCommonBasic endpointIDCB = party.getEndpointID();
                if (endpointIDCB != null) {
                    customerDTO.setEndpointId(endpointIDCB.getValue());
                }

                ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
                for (ehandel.no.invoice.PartyIdentificationCommonAggregate partyIdentificationCommonAggregate : party.getPartyIdentifications()) {
                    idCommonBasic = partyIdentificationCommonAggregate.getID();
                    if (idCommonBasic != null) {
                        customerDTO.setId(idCommonBasic.getValue());
                    }
                }

                ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
                for (ehandel.no.invoice.PartyNameCommonAggregate partyNameCommonAggregate : party.getPartyNames()) {
                    nameCommonBasic = partyNameCommonAggregate.getName();
                    if (nameCommonBasic != null) {
                        customerDTO.setName(nameCommonBasic.getValue());
                    }
                }

                ehandel.no.invoice.CompanyIDCommonBasic companyIDCommonBasic = null;
                for (ehandel.no.invoice.PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate : party.getPartyLegalEntities()) {
                    companyIDCommonBasic = partyLegalEntityCommonAggregate.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        customerDTO.setOrganizationNo(companyIDCommonBasic.getValue());
                    }

                    if (partyLegalEntityCommonAggregate.getRegistrationName() != null) {
                        customerDTO.setLegalName(partyLegalEntityCommonAggregate.getRegistrationName().getValue());
                    }
                    if (partyLegalEntityCommonAggregate.getRegistrationAddress() != null) {
                        AddressDTO legalAddressDTO = new AddressDTO();
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName() != null) {
                            legalAddressDTO.setCityName(partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName().getValue());
                        }
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry() != null) {
                            if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry().getIdentificationCode() != null) {
                                legalAddressDTO.setCountryCode(partyLegalEntityCommonAggregate.getRegistrationAddress()
                                        .getCountry().getIdentificationCode().getValue());
                            }
                        }
                        customerDTO.setLegalAddressDTO(legalAddressDTO);
                    }
                }

                ehandel.no.invoice.AddressType postalAddress = party.getPostalAddress();

                if (postalAddress != null) {

                    addressDTO = new AddressDTO();

                    idCommonBasic = postalAddress.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                        addressDTO.setGln(idCommonBasic.getValue());
                    }

                    ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic =
                            postalAddress.getBuildingNumber();
                    if (buildingNumberCommonBasic != null) {
                        addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                    }

                    ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic =
                            postalAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic =
                            postalAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic =
                            postalAddress.getPostbox();
                    if (postboxCommonBasic != null) {
                        addressDTO.setPostalBox(postboxCommonBasic.getValue());
                    }

                    ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic =
                            postalAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    ehandel.no.invoice.CountryType countryType = postalAddress.getCountry();
                    if (countryType != null) {
                        ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                }

                ehandel.no.invoice.WebsiteURICommonBasic websiteURICommonBasic =
                        party.getWebsiteURI();
                if (websiteURICommonBasic != null) {
                    addressDTO.setUrl(websiteURICommonBasic.getValue());
                }

                customerDTO.setAddressDTO(addressDTO);
            }

            ehandel.no.invoice.ContactType contactType = party.getContact();
            if (contactType != null) {

                ehandel.no.invoice.IDCommonBasic idCommonBasic = contactType.getID();
                if (idCommonBasic != null) {
                    customerDTO.setContactId(idCommonBasic.getValue());
                }

                ehandel.no.invoice.TelephoneCommonBasic telephoneCommonBasic =
                        contactType.getTelephone();
                if (telephoneCommonBasic != null) {
                    customerDTO.setTelePhone(telephoneCommonBasic.getValue());
                }

                ehandel.no.invoice.TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
                if (telefaxCommonBasic != null) {
                    customerDTO.setTeleFax(telefaxCommonBasic.getValue());
                }

                ehandel.no.invoice.ElectronicMailCommonBasic electronicMailCommonBasic =
                        contactType.getElectronicMail();
                if (electronicMailCommonBasic != null) {
                    customerDTO.setEmail(electronicMailCommonBasic.getValue());
                }
            }
            invoiceDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map customer bank account information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapCustomerBankAccount(ehandel.no.invoice.Invoice invoice,
            InvoiceDTO invoiceDTO) {

        List<ehandel.no.invoice.PaymentMeansCommonAggregate> paymentMeans =
                invoice.getPaymentMeans();

        if (paymentMeans != null) {

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;

            ehandel.no.invoice.FinancialAccountType financialAccountType = null;
            ehandel.no.invoice.BranchType branchType = null;
            ehandel.no.invoice.CountryType countryType = null;
            ehandel.no.invoice.AddressType bankAddress = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
            ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic = null;
            ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic = null;
            ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic = null;
            ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
            ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic = null;
            ehandel.no.invoice.PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
            ehandel.no.invoice.FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                    null;

            for (ehandel.no.invoice.PaymentMeansCommonAggregate paymentMeansCommonAggregate : paymentMeans) {

                paymentDueDateCommonBasic = paymentMeansCommonAggregate.getPaymentDueDate();
                if (paymentDueDateCommonBasic != null
                        && paymentDueDateCommonBasic.getValue() != null) {
                    invoiceDTO.setPaymentDueDate(ConversionUtils.asDate(paymentDueDateCommonBasic.getValue()));
                }

                financialAccountType = paymentMeansCommonAggregate.getPayerFinancialAccount();
                if (financialAccountType != null) {

                    bankAccountDTO = new BankAccountDTO();

                    idCommonBasic = financialAccountType.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null
                            && idCommonBasic.getSchemeID() != null) {

                        if (EHFConstants.IBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setiBanNo(idCommonBasic.getValue());
                        } else if (EHFConstants.BBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setBankAccountNumber(idCommonBasic.getValue());
                        }
                    }

                    branchType = financialAccountType.getFinancialInstitutionBranch();
                    if (branchType != null) {

                        idCommonBasic = branchType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            bankAccountDTO.setBankAccountId(idCommonBasic.getValue());
                        }

                        financialInstitutionCommonAggregate = branchType.getFinancialInstitution();
                        if (financialInstitutionCommonAggregate != null) {

                            idCommonBasic = financialInstitutionCommonAggregate.getID();
                            if (idCommonBasic != null) {
                                bankAccountDTO.setBic(idCommonBasic.getValue());
                            }

                            nameCommonBasic = financialInstitutionCommonAggregate.getName();
                            if (nameCommonBasic != null) {
                                bankAccountDTO.setBankName(nameCommonBasic.getValue());
                            }

                            bankAddress = financialInstitutionCommonAggregate.getAddress();
                            if (bankAddress != null) {

                                bankAddressDTO = new AddressDTO();

                                buildingNumberCommonBasic = bankAddress.getBuildingNumber();
                                if (buildingNumberCommonBasic != null) {
                                    bankAddressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                                }

                                streetNameCommonBasic = bankAddress.getStreetName();
                                if (streetNameCommonBasic != null) {
                                    bankAddressDTO.setStreetName(streetNameCommonBasic.getValue());
                                }

                                postalZoneCommonBasic = bankAddress.getPostalZone();
                                if (postalZoneCommonBasic != null) {
                                    bankAddressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                                }

                                postboxCommonBasic = bankAddress.getPostbox();
                                if (postboxCommonBasic != null) {
                                    bankAddressDTO.setPostalBox(postboxCommonBasic.getValue());
                                }

                                cityNameCommonBasic = bankAddress.getCityName();
                                if (cityNameCommonBasic != null) {
                                    bankAddressDTO.setCityName(cityNameCommonBasic.getValue());
                                }

                                countryType = bankAddress.getCountry();
                                if (countryType != null) {
                                    identificationCodeCommonBasic =
                                            countryType.getIdentificationCode();
                                    if (identificationCodeCommonBasic != null) {
                                        bankAddressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();
            if (customerDTO == null) {
                customerDTO = new CustomerDTO();
            }
            customerDTO.setBankAccountDTO(bankAccountDTO);
            invoiceDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map delivery information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapDeliveryAddress(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        List<ehandel.no.invoice.DeliveryCommonAggregate> deliveries = invoice.getDeliveries();

        DeliveryDTO deliveryDTO = null;
        AddressDTO addressDTO = null;

        ehandel.no.invoice.ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
        ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic = null;
        ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic = null;
        ehandel.no.invoice.AdditionalStreetNameCommonBasic additionalStreetNameCommonBasic = null;
        ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
        ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic = null;
        ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic = null;
        ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
        ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
        ehandel.no.invoice.CountryType countryType = null;
        ehandel.no.invoice.LocationType locationType = null;
        ehandel.no.invoice.AddressType deliveryAddress = null;

        for (ehandel.no.invoice.DeliveryCommonAggregate deliveryCommonAggregate : deliveries) {

            deliveryDTO = new DeliveryDTO();
            actualDeliveryDateCommonBasic = deliveryCommonAggregate.getActualDeliveryDate();
            if (actualDeliveryDateCommonBasic != null) {
                deliveryDTO.setDeliveryDate(ConversionUtils.asDate(actualDeliveryDateCommonBasic.getValue()));
            }

            locationType = deliveryCommonAggregate.getDeliveryLocation();
            if (locationType != null) {

                deliveryAddress = locationType.getAddress();
                addressDTO = new AddressDTO();
                if (deliveryAddress != null) {

                    buildingNumberCommonBasic = deliveryAddress.getBuildingNumber();
                    if (buildingNumberCommonBasic != null) {
                        addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                    }

                    streetNameCommonBasic = deliveryAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    additionalStreetNameCommonBasic = deliveryAddress.getAdditionalStreetName();
                    if (additionalStreetNameCommonBasic != null) {
                        addressDTO.setAdditionalStreetName(additionalStreetNameCommonBasic.getValue());
                    }

                    postalZoneCommonBasic = deliveryAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    postboxCommonBasic = deliveryAddress.getPostbox();
                    if (postboxCommonBasic != null) {
                        addressDTO.setPostalBox(postboxCommonBasic.getValue());
                    }

                    cityNameCommonBasic = deliveryAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    countryType = deliveryAddress.getCountry();
                    if (countryType != null) {
                        identificationCodeCommonBasic = countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                    deliveryDTO.setDeliveryAddressDTO(addressDTO);
                }

                idCommonBasic = locationType.getID();
                if (idCommonBasic != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }
            }

            invoiceDTO.setDeliveryDTO(deliveryDTO);
            break;
        }
    }

    /**
     * Method used to map delivery information from EHF ehandel.no.invoice.Invoice to
     * InvoiceLineItemDTO wrapper.
     */
    private static void mapDeliveryAddress(ehandel.no.invoice.Invoice invoice,
            InvoiceLineItemDTO invoiceLineItemDTO) {

        List<ehandel.no.invoice.DeliveryCommonAggregate> deliveries = invoice.getDeliveries();

        DeliveryDTO deliveryDTO = null;
        AddressDTO addressDTO = null;

        ehandel.no.invoice.ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
        ehandel.no.invoice.BuildingNumberCommonBasic buildingNumberCommonBasic = null;
        ehandel.no.invoice.StreetNameCommonBasic streetNameCommonBasic = null;
        ehandel.no.invoice.PostalZoneCommonBasic postalZoneCommonBasic = null;
        ehandel.no.invoice.PostboxCommonBasic postboxCommonBasic = null;
        ehandel.no.invoice.CityNameCommonBasic cityNameCommonBasic = null;
        ehandel.no.invoice.IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
        ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
        ehandel.no.invoice.CountryType countryType = null;
        ehandel.no.invoice.LocationType locationType = null;
        ehandel.no.invoice.AddressType deliveryAddress = null;

        for (ehandel.no.invoice.DeliveryCommonAggregate deliveryCommonAggregate : deliveries) {

            deliveryDTO = new DeliveryDTO();
            actualDeliveryDateCommonBasic = deliveryCommonAggregate.getActualDeliveryDate();
            if (actualDeliveryDateCommonBasic != null) {
                deliveryDTO.setDeliveryDate(ConversionUtils.asDate(actualDeliveryDateCommonBasic.getValue()));
            }

            locationType = deliveryCommonAggregate.getDeliveryLocation();
            if (locationType != null) {

                deliveryAddress = locationType.getAddress();
                addressDTO = new AddressDTO();
                if (deliveryAddress != null) {

                    buildingNumberCommonBasic = deliveryAddress.getBuildingNumber();
                    if (buildingNumberCommonBasic != null) {
                        addressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                    }

                    streetNameCommonBasic = deliveryAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    postalZoneCommonBasic = deliveryAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    postboxCommonBasic = deliveryAddress.getPostbox();
                    if (postboxCommonBasic != null) {
                        addressDTO.setPostalBox(postboxCommonBasic.getValue());
                    }

                    cityNameCommonBasic = deliveryAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    countryType = deliveryAddress.getCountry();
                    if (countryType != null) {
                        identificationCodeCommonBasic = countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                    deliveryDTO.setDeliveryAddressDTO(addressDTO);
                }

                idCommonBasic = locationType.getID();
                if (idCommonBasic != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }
            }

            invoiceLineItemDTO.setDeliveryDTO(deliveryDTO);
            break;
        }
    }

    /**
     * Method used to map customer currency information from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapCurrency(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                invoice.getDocumentCurrencyCode();
        CurrencyDTO currencyDTO = null;

        if (documentCurrencyCodeCommonBasic != null) {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getValue());
            invoiceDTO.setCurrencyDTO(currencyDTO);
        }
    }

    /**
     * Method used to map invoice exchange rate from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapExchangeRate(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        // Set exchange rate
        ehandel.no.invoice.ExchangeRateType exchangeRateType = invoice.getPricingExchangeRate();
        if (exchangeRateType != null) {
            ehandel.no.invoice.CalculationRateCommonBasic calculationRateCommonBasic =
                    exchangeRateType.getCalculationRate();
            if (calculationRateCommonBasic != null && calculationRateCommonBasic.getValue() != null) {
                invoiceDTO.setExchangeRate(calculationRateCommonBasic.getValue().doubleValue());
            }

            ehandel.no.invoice.SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                    exchangeRateType.getSourceCurrencyBaseRate();
            if (sourceCurrencyBaseRateCommonBasic != null
                    && sourceCurrencyBaseRateCommonBasic.getValue() != null) {
                invoiceDTO.setInvoiceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }

            ehandel.no.invoice.TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                    exchangeRateType.getTargetCurrencyCode();
            if (targetCurrencyCodeCommonBasic != null
                    && targetCurrencyCodeCommonBasic.getValue() != null) {
                CurrencyDTO baseCurrencyDTO = new CurrencyDTO();
                baseCurrencyDTO.setCurrencyCode(targetCurrencyCodeCommonBasic.getValue());
                invoiceDTO.setBaseCurrencyDTO(baseCurrencyDTO);
            }

            ehandel.no.invoice.TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                    exchangeRateType.getTargetCurrencyBaseRate();
            if (targetCurrencyBaseRateCommonBasic != null
                    && targetCurrencyBaseRateCommonBasic.getValue() != null) {
                invoiceDTO.setBaseCurrencyBaseRate(targetCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }
        }
    }

    /**
     * Method used to map invoice line item details from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapInvoiceLineItems(ehandel.no.invoice.Invoice invoice,
            InvoiceDTO invoiceDTO) {

        List<ehandel.no.invoice.InvoiceLineCommonAggregate> invoiceLineItems =
                invoice.getInvoiceLines();
        if (invoiceLineItems != null) {

            InvoiceLineItemDTO invoiceLineItemDTO = null;

            ehandel.no.invoice.ItemCommonAggregate itemCommonAggregate = null;
            ehandel.no.invoice.ItemIdentificationType itemIdentificationType = null;
            ehandel.no.invoice.PriceType price = null;
            ehandel.no.invoice.MonetaryTotalType legalMonetaryTotal = null;
            ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
            ehandel.no.invoice.NameCommonBasic nameCommonBasic = null;
            ehandel.no.invoice.InvoicedQuantityCommonBasic invoicedQuantityCommonBasic = null;
            ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
            ehandel.no.invoice.PriceAmountCommonBasic priceAmountCommonBasic = null;
            ehandel.no.invoice.LineExtensionAmountCommonBasic lineExtensionAmount = null;
            ehandel.no.invoice.AmountCommonBasic amountCommonBasic = null;
            ehandel.no.invoice.TaxAmountCommonBasic taxAmountCommonBasic = null;
            ehandel.no.invoice.TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            ehandel.no.invoice.AccountingCostCommonBasic accountingCostCommonBasic = null;
            ehandel.no.invoice.TaxExclusiveAmountCommonBasic taxExclusiveAmount = null;
            ehandel.no.invoice.AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic =
                    null;
            ehandel.no.invoice.ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            AllowanceChargeDTO allowanceChargeDTO = null;
            ehandel.no.invoice.TaxCategoryType taxCategory = null;
            ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            ehandel.no.invoice.NoteCommonBasic noteCommonBasic = null;
            ehandel.no.invoice.PayableRoundingAmountCommonBasic payableRoundingAmountCB = null;
            ehandel.no.invoice.PayableAmountCommonBasic payableAmountB = null;

            List<ehandel.no.invoice.TaxCategoryType> classifiedTaxCategories = null;
            List<ehandel.no.invoice.TaxTotalCommonAggregate> taxTotals = null;
            List<ehandel.no.invoice.AllowanceChargeType> allowanceCharges = null;
            List<ehandel.no.invoice.TaxCategoryType> taxCategories = null;

            for (ehandel.no.invoice.InvoiceLineCommonAggregate invoiceLine : invoiceLineItems) {

                invoiceLineItemDTO = new InvoiceLineItemDTO();

                idCommonBasic = invoiceLine.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setId(idCommonBasic.getValue());
                }

                noteCommonBasic = invoiceLine.getNote();
                if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setNote(noteCommonBasic.getValue());
                }

                itemCommonAggregate = invoiceLine.getItem();
                if (itemCommonAggregate != null) {
                    itemIdentificationType = itemCommonAggregate.getSellersItemIdentification();
                    if (itemIdentificationType != null) {
                        idCommonBasic = itemIdentificationType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setProductNo(idCommonBasic.getValue());
                        }
                    }
                }

                nameCommonBasic = itemCommonAggregate.getName();
                if (nameCommonBasic != null) {
                    invoiceLineItemDTO.setProductName(nameCommonBasic.getValue());
                }

                accountingCostCommonBasic = invoiceLine.getAccountingCost();
                if (accountingCostCommonBasic != null
                        && accountingCostCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setAccountingCode(accountingCostCommonBasic.getValue());
                }

                invoicedQuantityCommonBasic = invoiceLine.getInvoicedQuantity();
                if (invoicedQuantityCommonBasic != null) {
                    invoiceLineItemDTO.setUnitCode(invoicedQuantityCommonBasic.getUnitCode());
                }

                classifiedTaxCategories = itemCommonAggregate.getClassifiedTaxCategories();
                for (ehandel.no.invoice.TaxCategoryType taxCategoryType : classifiedTaxCategories) {

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

                List<ehandel.no.invoice.DescriptionCommonBasic> descriptions =
                        itemCommonAggregate.getDescriptions();
                if (descriptions != null) {
                    for (ehandel.no.invoice.DescriptionCommonBasic description : descriptions) {
                        if (description != null) {
                            invoiceLineItemDTO.setDescription(description.getValue());
                        }
                    }
                }

                price = invoiceLine.getPrice();
                if (price != null) {
                    priceAmountCommonBasic = price.getPriceAmount();
                    if (priceAmountCommonBasic != null && priceAmountCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setUnitPrice(priceAmountCommonBasic.getValue().doubleValue());
                    }
                }

                invoicedQuantityCommonBasic = invoiceLine.getInvoicedQuantity();
                if (invoicedQuantityCommonBasic != null
                        && invoicedQuantityCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setQuantity(invoicedQuantityCommonBasic.getValue().doubleValue());
                }

                lineExtensionAmount = invoiceLine.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    invoiceLineItemDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                    invoiceLineItemDTO.setTotalAmount(lineExtensionAmount.getValue().doubleValue());
                }

                taxTotals = invoiceLine.getTaxTotals();
                if (taxTotals != null) {

                    for (ehandel.no.invoice.TaxTotalCommonAggregate taxTotalCommonAggregate : taxTotals) {
                        taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
                        if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                            invoiceLineItemDTO.setTotalAmount(invoiceLineItemDTO.getTotalExcTax() + invoiceLineItemDTO.getTaxAmount());
                        }
                    }
                }

                allowanceCharges = invoiceLine.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    for (ehandel.no.invoice.AllowanceChargeType allowanceChargeType : allowanceCharges) {

                        chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();
                        allowanceChargeReasonCommonBasic =
                                allowanceChargeType.getAllowanceChargeReason();
                        amountCommonBasic = allowanceChargeType.getAmount();
                        if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                            allowanceChargeDTO = new AllowanceChargeDTO();
                            allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                            allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeReasonCommonBasic.getValue());
                            allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                            taxCategories = allowanceChargeType.getTaxCategories();
                            if (taxCategories != null && !taxCategories.isEmpty()) {

                                taxCategory = taxCategories.get(0);
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

                mapDeliveryAddress(invoice, invoiceLineItemDTO);
                invoiceDTO.getInvoiceLineItems().add(invoiceLineItemDTO);
            }

            legalMonetaryTotal = invoice.getLegalMonetaryTotal();
            if (legalMonetaryTotal != null) {

                taxExclusiveAmount = legalMonetaryTotal.getTaxExclusiveAmount();
                if (taxExclusiveAmount != null && taxExclusiveAmount.getValue() != null) {
                    invoiceDTO.setTotalExcTax(taxExclusiveAmount.getValue().doubleValue());
                }

                taxInclusiveAmountCommonBasic = legalMonetaryTotal.getTaxInclusiveAmount();
                if (taxInclusiveAmountCommonBasic != null
                        && taxInclusiveAmountCommonBasic.getValue() != null) {
                    invoiceDTO.setTotalAmount(taxInclusiveAmountCommonBasic.getValue().doubleValue());
                }

                payableRoundingAmountCB = legalMonetaryTotal.getPayableRoundingAmount();
                if (payableRoundingAmountCB != null && payableRoundingAmountCB.getValue() != null) {
                    invoiceDTO.setPayableRoundingAmount(payableRoundingAmountCB.getValue().doubleValue());
                }

                payableAmountB = legalMonetaryTotal.getPayableAmount();
                if (payableAmountB != null && payableAmountB.getValue() != null) {
                    invoiceDTO.setPayableAmount(payableAmountB.getValue().doubleValue());
                }
            }
        }
    }

    /**
     * Method used to map tax summary details from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapTaxSummaries(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        List<ehandel.no.invoice.TaxTotalCommonAggregate> taxTotals = invoice.getTaxTotals();
        List<TaxSummaryDTO> taxSummaries = new ArrayList<TaxSummaryDTO>();
        List<ehandel.no.invoice.TaxSubtotalCommonAggregate> taxSubtotals = null;

        TaxSummaryDTO taxSummary = null;

        ehandel.no.invoice.IDCommonBasic idCommonBasic = null;
        ehandel.no.invoice.TaxableAmountCommonBasic taxableAmountCommonBasic = null;
        ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
        ehandel.no.invoice.TaxAmountCommonBasic taxAmountCommonBasic = null;
        ehandel.no.invoice.TaxCategoryType taxCategoryType = null;
        ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        for (ehandel.no.invoice.TaxTotalCommonAggregate taxTotalCommonAggregate : taxTotals) {

            taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
            if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                invoiceDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
            }

            taxSubtotals = taxTotalCommonAggregate.getTaxSubtotals();
            for (ehandel.no.invoice.TaxSubtotalCommonAggregate taxSubtotalCommonAggregate : taxSubtotals) {

                taxSummary = new TaxSummaryDTO();

                taxableAmountCommonBasic = taxSubtotalCommonAggregate.getTaxableAmount();
                if (taxableAmountCommonBasic != null && taxableAmountCommonBasic.getValue() != null) {
                    taxSummary.setTotalExcTax(taxableAmountCommonBasic.getValue().doubleValue());
                }

                taxAmountCommonBasic = taxSubtotalCommonAggregate.getTaxAmount();
                if (taxableAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                    taxSummary.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                }

                taxCategoryType = taxSubtotalCommonAggregate.getTaxCategory();
                if (taxCategoryType != null) {

                    percentCommonBasic = taxCategoryType.getPercent();
                    if (percentCommonBasic != null && percentCommonBasic.getValue() != null) {
                        final double taxPercent = percentCommonBasic.getValue().doubleValue();
                        taxSummary.setTaxPercent(taxPercent);
                        if(taxPercent == 0 ) {
                            taxSummary.setTaxExcemptionReasion(taxCategoryType.getTaxExemptionReason() != null 
                                    ? taxCategoryType.getTaxExemptionReason().getValue() : null);
                            taxSummary.setTaxExcemptionReasionCode(taxCategoryType.getTaxExemptionReasonCode() != null
                                    ? taxCategoryType.getTaxExemptionReasonCode().getValue() : null);
                        }

                    }

                    taxSchemeCommonAggregate = taxCategoryType.getTaxScheme();
                    if (taxSchemeCommonAggregate != null) {
                        idCommonBasic = taxSchemeCommonAggregate.getID();
                        if (idCommonBasic != null) {
                            taxSummary.setTaxTypeIntName(idCommonBasic.getValue());
                        }
                    }
                }
                taxSummaries.add(taxSummary);
            }
        }
        invoiceDTO.setTaxSummaries(taxSummaries);
    }

    /**
     * Method used to map tax payment terms from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapPaymentTerms(ehandel.no.invoice.Invoice invoice, InvoiceDTO invoiceDTO) {

        InvoiceSettingDTO invoiceSettingDTO = null;
        List<InvoiceSettingDTO> invoiceSettingDTOs = null;
        List<ehandel.no.invoice.NoteCommonBasic> notes = null;

        List<ehandel.no.invoice.PaymentTermsCommonAggregate> paymentTerms =
                invoice.getPaymentTerms();
        if (paymentTerms != null && !paymentTerms.isEmpty()) {

            invoiceSettingDTOs = new ArrayList<InvoiceSettingDTO>();
            invoiceSettingDTO = new InvoiceSettingDTO();
            for (ehandel.no.invoice.PaymentTermsCommonAggregate paymentTermsCommonAggregate : paymentTerms) {

                notes = paymentTermsCommonAggregate.getNotes();
                if (notes != null && !notes.isEmpty()) {
                    for (ehandel.no.invoice.NoteCommonBasic noteCommonBasic : notes) {
                        if (noteCommonBasic.getValue() != null) {
                            invoiceSettingDTO.getPaymentTermsNotes().add(noteCommonBasic.getValue());
                        }
                    }
                }
            }
            invoiceSettingDTOs.add(invoiceSettingDTO);
            invoiceDTO.getInvoiceSettingDTOs().addAll(invoiceSettingDTOs);
        }
    }

    /**
     * Method used to map allowance charges from EHF ehandel.no.invoice.Invoice to
     * ehandel.no.invoice.Invoice wrapper.
     */
    private static void mapAllowanceCharges(ehandel.no.invoice.Invoice invoice,
            InvoiceDTO invoiceDTO) {

        ehandel.no.invoice.AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ehandel.no.invoice.ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        ehandel.no.invoice.AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeDTO allowanceChargeDTO = null;
        List<ehandel.no.invoice.TaxCategoryType> taxCategories = null;
        ehandel.no.invoice.TaxCategoryType taxCategoryType = null;
        ehandel.no.invoice.PercentCommonBasic percentCommonBasic = null;
        ehandel.no.invoice.TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        List<ehandel.no.invoice.AllowanceChargeType> allowanceCharges =
                invoice.getAllowanceCharges();

        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            for (ehandel.no.invoice.AllowanceChargeType allowanceChargeType : allowanceCharges) {

                chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();
                allowanceChargeReasonCommonBasic = allowanceChargeType.getAllowanceChargeReason();
                amountCommonBasic = allowanceChargeType.getAmount();
                if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                    allowanceChargeDTO = new AllowanceChargeDTO();
                    if (chargeIndicatorCommonBasic != null) {
                        allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                    }
                    if (allowanceChargeReasonCommonBasic != null) {
                        allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeReasonCommonBasic.getValue());
                    }

                    allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                    taxCategories = allowanceChargeType.getTaxCategories();
                    if (taxCategories != null && !taxCategories.isEmpty()) {

                        taxCategoryType = taxCategories.get(0);
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
                    invoiceDTO.getAllowanceCharges().add(allowanceChargeDTO);
                }
            }
        }
    }

    public static boolean isValidInvoiceAmount(InvoiceDTO invoiceDTO) {

        double totalExcAmt = 0d;
        double discountAmount = 0d;
        for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceDTO.getInvoiceLineItems()) {

            if (invoiceLineItemDTO.getAllowanceCharges() != null
                    && !invoiceLineItemDTO.getAllowanceCharges().isEmpty()) {
                discountAmount = invoiceLineItemDTO.getAllowanceCharges().get(0).getAmount();
            }
            totalExcAmt +=
                    (invoiceLineItemDTO.getUnitPrice() * invoiceLineItemDTO.getQuantity())
                            - discountAmount;

        }
        return (totalExcAmt == invoiceDTO.getTotalExcTax());
    }

    public static void generateEHFV2InvoiceXML(InvoiceDTO invoiceDTO, String filePath)
            throws JAXBException {

        Invoice invoice = convertToEHFV2Invoice(invoiceDTO);

        Marshaller marshaller = getV2Marshaller();

        marshaller.marshal(invoice, new File(filePath));
    }

    /**
     * Marshalling invoice object to byte array.
     */
    public static byte[] generateEHFV2InvoiceXML(InvoiceDTO invoiceDTO) throws JAXBException {

        Invoice invoice = convertToEHFV2Invoice(invoiceDTO);

        Marshaller marshaller = getV2Marshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(invoice, out);

        return out.toByteArray();
    }
    
     public static InvoiceDTO getEHFV2Invoice(InputStream is) throws Exception {


        final Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
        ehandel.no.ehf.invoice.Invoice invoice =
                (ehandel.no.ehf.invoice.Invoice) unmarshaller.unmarshal(is);

        return convertToEHFV2Invoice(invoice);
    }

    /**
     * Method used to convert Invoice wrapper to EHF invoice.
     */
    private static Invoice convertToEHFV2Invoice(InvoiceDTO invoiceDTO) {

        Invoice invoice = null;
        if (invoiceDTO != null) {

            invoice = new Invoice();

            // Set UBL version ID
            UBLVersionIDCommonBasic ublVersionIDCommonBasic = new UBLVersionIDCommonBasic();
            ublVersionIDCommonBasic.setValue(EHFConstants.UBL_VERSION_ID_TWO_DOT_ONE.getValue());
            invoice.setUBLVersionID(ublVersionIDCommonBasic);

            // Initialize customization id and profile id
            CustomizationIDCommonBasic customizationIDCommonBasic =
                    new CustomizationIDCommonBasic();
            ProfileIDCommonBasic profileIDCommonBasic = new ProfileIDCommonBasic();
            // Set customization id and profile id
            if(invoiceDTO.getIsCreditNoteAgaintInvoice()) {
                customizationIDCommonBasic.setValue(EHFConstants.INVOICE_CREDITNOTE_TWO_DOT_ONE_CUSTOMIZATION_ID_FOR_INVOICE.getValue());
                profileIDCommonBasic.setValue(EHFConstants.INVOICE_CREDIT_TWO_DOT_ONE_NOTE_PROFILE_ID.getValue());
            } else {
                customizationIDCommonBasic.setValue(EHFConstants.INVOICE_TWO_DOT_ONE_CUSTOMIZATION_ID.getValue());
                profileIDCommonBasic.setValue(EHFConstants.INVOICE_TWO_DOT_ONE_PROFILE_ID.getValue());
            }
            
            invoice.setCustomizationID(customizationIDCommonBasic);
            invoice.setProfileID(profileIDCommonBasic);

            // Set invoice no
            IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(invoiceDTO.getInvoiceNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getInvoiceNo());
                invoice.setID(idCommonBasic);
            }

            // Set issue date, tax point date and invoice period
            if (invoiceDTO.getIssueDate() != null) {
                IssueDateCommonBasic issueDateCommonBasic = new IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                invoice.setIssueDate(issueDateCommonBasic);

                TaxPointDateCommonBasic taxPointDateCommonBasic = new TaxPointDateCommonBasic();
                taxPointDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                invoice.setTaxPointDate(taxPointDateCommonBasic);

                PeriodType periodType = new PeriodType();
                StartDateCommonBasic startDateCommonBasic = new StartDateCommonBasic();
                startDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                periodType.setStartDate(startDateCommonBasic);

                if (invoiceDTO.getPaymentDueDate() != null) {
                    EndDateCommonBasic endDateCommonBasic = new EndDateCommonBasic();
                    endDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPaymentDueDate()));
                    periodType.setEndDate(endDateCommonBasic);
                }
                invoice.getInvoicePeriods().add(periodType);
            }

            // Set invoice type
            InvoiceTypeCodeCommonBasic invoiceTypeCodeCommonBasic =
                    new InvoiceTypeCodeCommonBasic();
            invoiceTypeCodeCommonBasic.setListID(EHFConstants.INVOICE_TYPE_CODE_LIST_ID.getValue());
            invoiceTypeCodeCommonBasic.setValue(EHFConstants.INVOICE_TYPE.getValue());
            invoice.setInvoiceTypeCode(invoiceTypeCodeCommonBasic);

            // Set order reference id
            if (!StringUtils.isEmpty(invoiceDTO.getPurchaseOrderNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getPurchaseOrderNo());
                OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                        new OrderReferenceCommonAggregate();
                orderReferenceCommonAggregate.setID(idCommonBasic);
                invoice.setOrderReference(orderReferenceCommonAggregate);
            }

            //set contract
            ContractDTO contractDTO = invoiceDTO.getContractDTO();
            if (contractDTO != null) {
                DocumentReferenceType documentReferenceType = new DocumentReferenceType();
                if (!StringUtils.isEmpty(contractDTO.getContractId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(contractDTO.getContractId());
                    documentReferenceType.setID(idCommonBasic);
                }
                if (contractDTO.getContractType() != null) {
                    DocumentTypeCodeCommonBasic documentTypeCode = new DocumentTypeCodeCommonBasic();
                    documentTypeCode.setListID(EHFConstants.INVOICE_TYPE_CODE_LIST_ID.getValue());
                    documentTypeCode.setValue(String.valueOf(contractDTO.getContractType()));
                    documentReferenceType.setDocumentTypeCode(documentTypeCode);
                }
                if (!StringUtils.isEmpty(contractDTO.getDocumentType())) {
                    DocumentTypeCommonBasic documentType = new DocumentTypeCommonBasic();
                    documentType.setValue(contractDTO.getDocumentType());
                    documentReferenceType.setDocumentType(documentType);
                }
                invoice.getContractDocumentReferences().add(documentReferenceType);
            }

            List<FileDTO> files = invoiceDTO.getFiles();
            if (files != null && !files.isEmpty()) {

                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCommonBasic = null;
                DocumentReferenceType documentReferenceType = null;
                DocumentTypeCommonBasic documentTypeCommonBasic = null;
                AttachmentType attachmentType = null;

                int i = 0;
                for (FileDTO fileDTO : files) {

                    i++;
                    embeddedDocBinObjCommonBasic = new EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(fileDTO.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(fileDTO.getMimeType());
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode("application/pdf");
                    }
                    embeddedDocBinObjCommonBasic.setValue(fileDTO.getFileContent());

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

                    invoice.getAdditionalDocumentReferences().add(documentReferenceType);
                }
                i = 0;
            }

            String language =
                    (StringUtils.isEmpty(invoiceDTO.getLanguage()) ? EHFConstants.DEFAULT_LANGUAGE.getValue()
                            : invoiceDTO.getLanguage());
            invoiceDTO.setLanguage(language);

            if (invoiceDTO.getNotes() != null && !invoiceDTO.getNotes().isEmpty()) {
                NoteCommonBasic noteCommonBasic = null;
                for (String note : invoiceDTO.getNotes()) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(note);
                    noteCommonBasic.setLanguageID(language);
                    invoice.getNotes().add(noteCommonBasic);
                }
            }

            List<InvoiceLineItemDTO> invoiceLineItems = invoiceDTO.getInvoiceLineItems();
            if (invoiceLineItems != null && !invoiceLineItems.isEmpty()) {
                for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItems) {
                    if (invoiceLineItemDTO.getTotalExcTax() != null) {
                        taxTypeIntName =
                                invoiceLineItemDTO.getTaxTypeIntName() != null ? invoiceLineItemDTO.getTaxTypeIntName().toUpperCase()
                                        : "";
                        break;
                    }
                }
            }

            mapEHFV2PayeeParty(invoiceDTO, invoice);
            mapEHFV2Supplier(invoiceDTO, invoice);
            mapEHFV2SupplierBankAccount(invoiceDTO, invoice);
            mapEHFV2Customer(invoiceDTO, invoice);
            mapEHFV2CustomerBankAccount(invoiceDTO, invoice);
            mapEHFV2DeliveryAddress(invoiceDTO, invoice);
            mapEHFV2Currency(invoiceDTO, invoice);
            mapEHFV2ExchangeRate(invoiceDTO, invoice);
            mapEHFV2AllowanceCharges(invoiceDTO, invoice);
            mapEHFV2InvoiceLineItems(invoiceDTO, invoice);
            mapEHFV2TaxSummaries(invoiceDTO, invoice);
            mapEHFV2PaymentTerms(invoiceDTO, invoice);
            mapEHFV2TaxRepresentativeParty(invoiceDTO, invoice);
        }

        return invoice;
    }

    /**
     * Method used to map payee information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2PayeeParty(InvoiceDTO invoiceDTO, Invoice invoice) {

        PayeeParty payeeParty = invoiceDTO.getPayeeParty();

        if (payeeParty != null) {

            PartyTypeCommonAggregate party = new PartyTypeCommonAggregate();
            IDCommonBasic idCommonBasic = null;

            String organizationNo = "";
            String country = "";

            AddressDTO addressDTO = payeeParty.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            } else {
               addressDTO = invoiceDTO.getSupplierDTO().getAddressDTO();
               country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(payeeParty.getId())) {

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(payeeParty.getId());
                idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_IDENTIFICATION_SCHEME_AGENCY_ID.getValue());
                PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);
                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(payeeParty.getName())) {

                NameCommonBasic nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(payeeParty.getName());
                PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
            }

            if (!StringUtils.isEmpty(payeeParty.getOrganizationNo())) {
                organizationNo = payeeParty.getOrganizationNo();
                CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(payeeParty.getOrganizationNo());
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                        new PartyLegalEntityCommonAggregate();
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }
            invoice.setPayeeParty(party);
        }
    }

    /**
     * Method used to map supplier information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2Supplier(InvoiceDTO invoiceDTO, Invoice invoice) {

        SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();

        if (supplierDTO != null) {

            SupplierPartyType supplierPartyType = new SupplierPartyType();
            PartyTypeCommonAggregate party = new PartyTypeCommonAggregate();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();

            IDCommonBasic idCommonBasic = null;
            EndpointIDCommonBasic endpointIDCB = null;
            NameCommonBasic nameCommonBasic = null;

            String country = "";

            boolean isPartyLegalEntity = false;

            //set contact name
            ContactPersonDTO contactPersonDTO = supplierDTO.getContactPersonDTO();
            if (contactPersonDTO != null) {

                ContactType contactType = new ContactType();
                nameCommonBasic = new NameCommonBasic();
                idCommonBasic = new IDCommonBasic();
                TelephoneCommonBasic telephone = new TelephoneCommonBasic();
                TelefaxCommonBasic teleFax = new TelefaxCommonBasic();
                ElectronicMailCommonBasic email = new ElectronicMailCommonBasic();

                if (!StringUtils.isEmpty(contactPersonDTO.getId())) {
                    idCommonBasic.setValue(contactPersonDTO.getName());
                    contactType.setID(idCommonBasic);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getName())) {
                    nameCommonBasic.setValue(contactPersonDTO.getName());
                    contactType.setName(nameCommonBasic);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getTelephone())) {
                    telephone.setValue(contactPersonDTO.getTelephone());
                    contactType.setTelephone(telephone);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getTeleFax())) {
                    teleFax.setValue(contactPersonDTO.getTeleFax());
                    contactType.setTelefax(teleFax);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getEmail())) {
                    email.setValue(contactPersonDTO.getEmail());
                    contactType.setElectronicMail(email);
                }
                supplierPartyType.setSellerContact(contactType);
            }

            AddressDTO addressDTO = supplierDTO.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(supplierDTO.getEndpointId())) {
                endpointIDCB = new EndpointIDCommonBasic();
                endpointIDCB.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                endpointIDCB.setValue(removeCountryCodeAndMVAFromEndPointId(supplierDTO.getEndpointId()));
                party.setEndpointID(endpointIDCB);
            }

            if (!StringUtils.isEmpty(supplierDTO.getId())) {

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(supplierDTO.getId());
                idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_IDENTIFICATION_SCHEME_AGENCY_ID.getValue());
                PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);

                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getName())) {

                nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(supplierDTO.getName());
                PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
            }

            ContactType contactType = new ContactType();
            boolean isContactAvailable = false;
            if (!StringUtils.isEmpty(supplierDTO.getContactId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(supplierDTO.getContactId());
                contactType.setID(idCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getTelePhone())) {
                TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
                telephoneCommonBasic.setValue(supplierDTO.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getTeleFax())) {
                TelefaxCommonBasic telefaxCommonBasic = new TelefaxCommonBasic();
                telefaxCommonBasic.setValue(supplierDTO.getTeleFax());
                contactType.setTelefax(telefaxCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getEmail())) {
                ElectronicMailCommonBasic electronicMailCommonBasic =
                        new ElectronicMailCommonBasic();
                electronicMailCommonBasic.setValue(supplierDTO.getEmail());
                contactType.setElectronicMail(electronicMailCommonBasic);
                isContactAvailable = true;
            }
            if (isContactAvailable) {
                party.setContact(contactType);
            }

            if (addressDTO != null) {

                AddressType postalAddress = new AddressType();
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
                    isAddressAvailable = true;
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
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    party.setPostalAddress(postalAddress);
                }
            }

            AddressDTO legalAddressDTO = supplierDTO.getLegalAddressDTO();
            AddressType registrationAddress = null;
            boolean isRegistrationAddress = false;
            if (legalAddressDTO != null) {
                registrationAddress = new AddressType();

                if (!StringUtils.isEmpty(legalAddressDTO.getCityName())) {
                    CityNameCommonBasic cityNameCommonBasic =
                            new CityNameCommonBasic();
                    cityNameCommonBasic.setValue(legalAddressDTO.getCityName());
                    registrationAddress.setCityName(cityNameCommonBasic);
                    isRegistrationAddress = true;
                }

                 if (!StringUtils.isEmpty(legalAddressDTO.getCountryCode())) {

                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(legalAddressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    registrationAddress.setCountry(countryType);
                    isRegistrationAddress = true;
                }
            }

            PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new PartyTaxSchemeCommonAggregate();
            CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(removeCountryCodeAndMVAFromEndPointId(supplierDTO.getOrganizationNo()) + EHFConstants.MVA.getValue()));
                companyIDCommonBasic.setSchemeID(country + ":" + taxTypeIntName);
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable && !invoiceDTO.getIsInvoiceCompanyTaxFree()) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getName())) {

                RegistrationNameCommonBasic registrationNameCB = new RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(supplierDTO.getName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(supplierDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                companyIDCommonBasic.setSchemeName(EHFConstants.LEGAL_ENTITY_SCHEME_NAME.getValue());
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }

            if (isRegistrationAddress) {
                partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
            }

            if (isPartyLegalEntity || isRegistrationAddress) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            supplierPartyType.setParty(party);
            invoice.setAccountingSupplierParty(supplierPartyType);
        }
    }

    /**
     * Method used to map supplier bank account information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2SupplierBankAccount(InvoiceDTO invoiceDTO, Invoice invoice) {

        SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();

        if (supplierDTO != null) {

            boolean isFinancialAddressAvailable = false;
            boolean isFinancialInstitutionAvailable = false;

            BankAccountDTO bankAccountDTO = supplierDTO.getBankAccountDTO();
            if (bankAccountDTO != null) {

                FinancialAccountType financialAccountTypeForIBAN = new FinancialAccountType();
                FinancialAccountType financialAccountTypeForBBAN = new FinancialAccountType();

                NameCommonBasic nameCommonBasic = null;

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountNumber())) {

                    IDCommonBasic idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                    idCommonBasic.setValue(getBBAN(bankAccountDTO.getBankAccountNumber()));
                    financialAccountTypeForBBAN.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountName())) {
                    nameCommonBasic = new NameCommonBasic();
                    nameCommonBasic.setValue(bankAccountDTO.getBankAccountName());
                    financialAccountTypeForBBAN.setName(nameCommonBasic);
                }

                if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {

                    IDCommonBasic idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                    idCommonBasic.setValue(getIBAN(bankAccountDTO.getiBanNo()));
                    financialAccountTypeForIBAN.setID(idCommonBasic);
                    financialAccountTypeForIBAN.setName(nameCommonBasic);
                }

                FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                        new FinancialInstitutionCommonAggregate();

                BranchType branchType = null;

                // commented out because <cbc:ID> is not required

                /*if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId())) {
                    branchType = new BranchType();
                    IDCommonBasic idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(bankAccountDTO.getBankAccountId());
                    branchType.setID(idCommonBasic);
                } else if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountNumber())) {
                    branchType = new BranchType();
                    IDCommonBasic idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(getBBAN(bankAccountDTO.getBankAccountNumber()));
                    branchType.setID(idCommonBasic);
                }*/

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId()) ||
                       !StringUtils.isEmpty(bankAccountDTO.getBankAccountNumber())) {

                    branchType = new BranchType();

                //if (branchType != null) {
                    if (!StringUtils.isEmpty(bankAccountDTO.getBic())) {
                        IDCommonBasic idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBic());
                        idCommonBasic.setSchemeID(EHFConstants.BIC.getValue());
                        financialInstitutionCommonAggregate.setID(idCommonBasic);
                        isFinancialInstitutionAvailable = true;
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankName())) {
                        nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
                        isFinancialInstitutionAvailable = true;
                    }

                    AddressDTO bankAddressDTO = bankAccountDTO.getBankAddressDTO();
                    if (bankAddressDTO != null) {

                        AddressType bankAddress = new AddressType();

                        if (!StringUtils.isEmpty(bankAddressDTO.getBuildingNumber())) {
                            BuildingNumberCommonBasic buildingNumberCommonBasic =
                                    new BuildingNumberCommonBasic();
                            buildingNumberCommonBasic.setValue(bankAddressDTO.getBuildingNumber());
                            bankAddress.setBuildingNumber(buildingNumberCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getStreetName())) {
                            StreetNameCommonBasic streetNameCommonBasic =
                                    new StreetNameCommonBasic();
                            streetNameCommonBasic.setValue(bankAddressDTO.getStreetName());
                            bankAddress.setStreetName(streetNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalBox())) {
                            PostboxCommonBasic postboxCommonBasic = new PostboxCommonBasic();
                            postboxCommonBasic.setValue(bankAddressDTO.getPostalBox());
                            bankAddress.setPostbox(postboxCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalZone())) {
                            PostalZoneCommonBasic postalZoneCommonBasic =
                                    new PostalZoneCommonBasic();
                            postalZoneCommonBasic.setValue(bankAddressDTO.getPostalZone());
                            bankAddress.setPostalZone(postalZoneCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCityName())) {
                            CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                            cityNameCommonBasic.setValue(bankAddressDTO.getCityName());
                            bankAddress.setCityName(cityNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCountryCode())) {

                            IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                    new IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                            identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                            CountryType countryType = new CountryType();
                            countryType.setIdentificationCode(identificationCodeCommonBasic);
                            bankAddress.setCountry(countryType);
                            isFinancialAddressAvailable = true;
                        }

                        if (isFinancialAddressAvailable) {
                            financialInstitutionCommonAggregate.setAddress(bankAddress);
                        }
                    }

                    if (isFinancialInstitutionAvailable) {
                    branchType.setFinancialInstitution(financialInstitutionCommonAggregate);
                    }
                    financialAccountTypeForBBAN.setFinancialInstitutionBranch(branchType);
                    if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {
                        financialAccountTypeForIBAN.setFinancialInstitutionBranch(branchType);
                    }
                }

                PaymentMeansCommonAggregate paymentMeansCommonAggregateForBBAN =
                        new PaymentMeansCommonAggregate();
                PaymentMeansCommonAggregate paymentMeansCommonAggregateForIBAN =
                        new PaymentMeansCommonAggregate();
                paymentMeansCommonAggregateForBBAN.setPayeeFinancialAccount(financialAccountTypeForBBAN);

                if (!StringUtils.isEmpty(invoiceDTO.getPaymentId())) {
                    PaymentIDCommonBasic paymentIDCommonBasic = new PaymentIDCommonBasic();
                    paymentIDCommonBasic.setValue(invoiceDTO.getPaymentId());
                    paymentMeansCommonAggregateForBBAN.getPaymentIDs().add(paymentIDCommonBasic);
                    paymentMeansCommonAggregateForIBAN.getPaymentIDs().add(paymentIDCommonBasic);
                }

                invoice.getPaymentMeans().add(paymentMeansCommonAggregateForBBAN);
                if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {
                    paymentMeansCommonAggregateForIBAN.setPayeeFinancialAccount(financialAccountTypeForIBAN);
                    invoice.getPaymentMeans().add(paymentMeansCommonAggregateForIBAN);
                }
            }
        }
    }

    /** 
     * Method used to remove the country code from the end point id.
     * @param endPointId
     * @return 
     */
    private static String removeCountryCodeAndMVAFromEndPointId(String endPointId) {

        endPointId = endPointId.replaceAll("[^0-9:]", "");
        String[] endPointArr = endPointId.split(":");
        if(endPointArr.length == 2) {
            return endPointArr[1];
        }
        return endPointId;
    }
    
    private static String getBBAN(String bban) {

        return bban.replaceAll("[^0-9]", "");
    }
    
    private static String getIBAN(String iban) {        
        
        String countryCode = iban.substring(0, Math.min(iban.length(), 2));
        Pattern ptn = Pattern.compile("[A-Z]*");
        if(!ptn.matcher(countryCode).matches()) {
            throw new RuntimeException("Country code in IBAN number should contains A-Z only");
        }
        
        String IBAN = iban.substring(2);        
        return countryCode + IBAN.replaceAll("[^0-9]", "");        
    }

    /**
     * Method used to map customer information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2Customer(InvoiceDTO invoiceDTO, Invoice invoice) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            PartyTypeCommonAggregate party = new PartyTypeCommonAggregate();
            AddressType postalAddress = new AddressType();
            CustomerPartyType customerPartyType = new CustomerPartyType();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();

            IDCommonBasic idCommonBasic = null;
            EndpointIDCommonBasic endpointIDCB = null;

            boolean isPartyLegalEntity = false;

            String country = "";

            //set contact name
            ContactPersonDTO contactPersonDTO = customerDTO.getContactPersonDTO();
            if (contactPersonDTO != null) {

                ContactType contactType = new ContactType();
                NameCommonBasic nameCommonBasic = new NameCommonBasic();
                idCommonBasic = new IDCommonBasic();
                TelephoneCommonBasic telephone = new TelephoneCommonBasic();
                TelefaxCommonBasic teleFax = new TelefaxCommonBasic();
                ElectronicMailCommonBasic email = new ElectronicMailCommonBasic();

                if (!StringUtils.isEmpty(contactPersonDTO.getId())) {
                    idCommonBasic.setValue(contactPersonDTO.getName());
                    contactType.setID(idCommonBasic);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getName())) {
                    nameCommonBasic.setValue(contactPersonDTO.getName());
                    contactType.setName(nameCommonBasic);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getTelephone())) {
                    telephone.setValue(contactPersonDTO.getTelephone());
                    contactType.setTelephone(telephone);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getTeleFax())) {
                    teleFax.setValue(contactPersonDTO.getTeleFax());
                    contactType.setTelefax(teleFax);
                }
                if (!StringUtils.isEmpty(contactPersonDTO.getEmail())) {
                    email.setValue(contactPersonDTO.getEmail());
                    contactType.setElectronicMail(email);
                }
                customerPartyType.setBuyerContact(contactType);
            }

            AddressDTO addressDTO = customerDTO.getAddressDTO();
            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            }

            if (!StringUtils.isEmpty(customerDTO.getEndpointId())) {
                endpointIDCB = new EndpointIDCommonBasic();
                endpointIDCB.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                endpointIDCB.setValue(removeCountryCodeAndMVAFromEndPointId(customerDTO.getEndpointId()));
                party.setEndpointID(endpointIDCB);
            }
            if (!StringUtils.isEmpty(customerDTO.getId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(customerDTO.getId());
                idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_IDENTIFICATION_SCHEME_AGENCY_ID.getValue());
                PartyIdentificationCommonAggregate partyIdentificationCommonAggregate =
                        new PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);

                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(customerDTO.getName())) {
                NameCommonBasic nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(customerDTO.getName());
                PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
                partyName.setName(nameCommonBasic);

                party.getPartyNames().add(partyName);
            }

            ContactType contactType = new ContactType();
            boolean isContactAvailable = false;
            if (!StringUtils.isEmpty(customerDTO.getContactId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(customerDTO.getContactId());
                contactType.setID(idCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getTelePhone())) {
                TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
                telephoneCommonBasic.setValue(customerDTO.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getTeleFax())) {
                TelefaxCommonBasic telefaxCommonBasic = new TelefaxCommonBasic();
                telefaxCommonBasic.setValue(customerDTO.getTeleFax());
                contactType.setTelefax(telefaxCommonBasic);
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

            if (addressDTO != null) {

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
                    isAddressAvailable = true;
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
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getUrl())) {
                    WebsiteURICommonBasic websiteURICommonBasic = new WebsiteURICommonBasic();
                    websiteURICommonBasic.setValue(addressDTO.getUrl());
                    party.setWebsiteURI(websiteURICommonBasic);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    party.setPostalAddress(postalAddress);
                }
            }

            AddressDTO legalAddressDTO = customerDTO.getLegalAddressDTO();
            AddressType registrationAddress = null;
            boolean isRegistrationAddress = false;
            if (legalAddressDTO != null) {
                registrationAddress = new AddressType();

                if (!StringUtils.isEmpty(legalAddressDTO.getCityName())) {
                    CityNameCommonBasic cityNameCommonBasic =
                            new CityNameCommonBasic();
                    cityNameCommonBasic.setValue(legalAddressDTO.getCityName());
                    registrationAddress.setCityName(cityNameCommonBasic);
                    isRegistrationAddress = true;
                }

                if (!StringUtils.isEmpty(legalAddressDTO.getCountryCode())) {

                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(legalAddressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    registrationAddress.setCountry(countryType);
                    isRegistrationAddress = true;
                }
            }

            PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new PartyTaxSchemeCommonAggregate();
            CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(removeCountryCodeAndMVAFromEndPointId(customerDTO.getOrganizationNo()) + EHFConstants.MVA.getValue()));
                companyIDCommonBasic.setSchemeID(country + ":" + taxTypeIntName);
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            if (!StringUtils.isEmpty(customerDTO.getName())) {

                RegistrationNameCommonBasic registrationNameCB = new RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(customerDTO.getName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {
                companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(customerDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + EHFConstants.SCHEME_ID.getValue());
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }

            if (isRegistrationAddress) {
                partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
            }
            if (isPartyLegalEntity || isRegistrationAddress) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            customerPartyType.setParty(party);
            invoice.setAccountingCustomerParty(customerPartyType);
        }
    }

    /**
     * Method used to map customer bank account information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2CustomerBankAccount(InvoiceDTO invoiceDTO, Invoice invoice) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            BranchType branchType = null;
            FinancialAccountType financialAccountType = null;
            FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate = null;
            //IDCommonBasic idCommonBasic = null;
            PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            PaymentMeansCodeCommonBasic paymentMeansCodeCommonBasic = null;

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;
            AddressType bankAddress = null;

            boolean isFinancialInsAvailable = false;
            boolean isFinancialAddressAvailable = false;

            for (PaymentMeansCommonAggregate paymentMeansCommonAggregate : invoice.getPaymentMeans()) {

                isFinancialInsAvailable = false;
                isFinancialAddressAvailable = false;

                if (paymentMeansCommonAggregate == null) {
                    paymentMeansCommonAggregate = new PaymentMeansCommonAggregate();
                    invoice.getPaymentMeans().add(paymentMeansCommonAggregate);
                }

                if (invoiceDTO.getPaymentDueDate() != null) {
                    paymentDueDateCommonBasic = new PaymentDueDateCommonBasic();
                    paymentDueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPaymentDueDate()));
                    paymentMeansCommonAggregate.setPaymentDueDate(paymentDueDateCommonBasic);
                }

                paymentMeansCodeCommonBasic = new PaymentMeansCodeCommonBasic();
                paymentMeansCodeCommonBasic.setValue(EHFConstants.PAYMENT_MEANS_CODE.getValue());
                paymentMeansCodeCommonBasic.setListID(EHFConstants.PAYMENT_MEANS_TWO_DOT_ONE_LIST_ID.getValue());
                paymentMeansCodeCommonBasic.setListAgencyID(EHFConstants.PAYMENT_MEANS_LIST_AGENCY_ID.getValue());
                paymentMeansCommonAggregate.setPaymentMeansCode(paymentMeansCodeCommonBasic);

                bankAccountDTO = customerDTO.getBankAccountDTO();
                if (bankAccountDTO != null) {

                    financialAccountType = new FinancialAccountType();

                    IDCommonBasic idCommonBasic = new IDCommonBasic();

                    if (bankAccountDTO.getiBanNo() != null && !bankAccountDTO.getiBanNo().isEmpty()) {
                        idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                        idCommonBasic.setValue(getIBAN(bankAccountDTO.getiBanNo()));
                    } else {
                        idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                        idCommonBasic.setValue(getBBAN(bankAccountDTO.getBankAccountNumber()));
                    }
                    financialAccountType.setID(idCommonBasic);

                    financialInstitutionCommonAggregate = new FinancialInstitutionCommonAggregate();

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId())) {
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBankAccountId());
                        branchType = new BranchType();
                        branchType.setID(idCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBic())) {
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBic());
                        idCommonBasic.setSchemeID(EHFConstants.BIC.getValue());
                        financialInstitutionCommonAggregate.setID(idCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankName())) {
                        NameCommonBasic nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    bankAddressDTO = bankAccountDTO.getBankAddressDTO();

                    if (bankAddressDTO != null) {

                        bankAddress = new AddressType();

                        if (!StringUtils.isEmpty(bankAddressDTO.getBuildingNumber())) {
                            BuildingNumberCommonBasic buildingNumberCommonBasic =
                                    new BuildingNumberCommonBasic();
                            buildingNumberCommonBasic.setValue(bankAddressDTO.getBuildingNumber());
                            bankAddress.setBuildingNumber(buildingNumberCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getStreetName())) {
                            StreetNameCommonBasic streetNameCommonBasic =
                                    new StreetNameCommonBasic();
                            streetNameCommonBasic.setValue(bankAddressDTO.getStreetName());
                            bankAddress.setStreetName(streetNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalBox())) {
                            PostboxCommonBasic postboxCommonBasic = new PostboxCommonBasic();
                            postboxCommonBasic.setValue(bankAddressDTO.getPostalBox());
                            bankAddress.setPostbox(postboxCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalZone())) {
                            PostalZoneCommonBasic postalZoneCommonBasic =
                                    new PostalZoneCommonBasic();
                            postalZoneCommonBasic.setValue(bankAddressDTO.getPostalZone());
                            bankAddress.setPostalZone(postalZoneCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCityName())) {
                            CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                            cityNameCommonBasic.setValue(bankAddressDTO.getCityName());
                            bankAddress.setCityName(cityNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCountryCode())) {

                            IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                    new IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                            identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                            CountryType countryType = new CountryType();
                            countryType.setIdentificationCode(identificationCodeCommonBasic);
                            bankAddress.setCountry(countryType);
                            isFinancialAddressAvailable = true;
                        }
                        if (isFinancialAddressAvailable) {
                            financialInstitutionCommonAggregate.setAddress(bankAddress);
                            isFinancialInsAvailable = true;
                        }
                    }

                    if (isFinancialInsAvailable && branchType != null) {
                        branchType.setFinancialInstitution(financialInstitutionCommonAggregate);
                        financialAccountType.setFinancialInstitutionBranch(branchType);
                    }

                    paymentMeansCommonAggregate.setPayerFinancialAccount(financialAccountType);
                }
            }
        }
    }

    /**
     * Method used to map delivery information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2DeliveryAddress(InvoiceDTO invoiceDTO, Invoice invoice) {

        DeliveryDTO deliveryDTO = invoiceDTO.getDeliveryDTO();

        if (deliveryDTO != null) {

            ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
            DeliveryType deliveryCommonAggregate = new DeliveryType();
            if (deliveryDTO.getDeliveryDate() != null) {
                actualDeliveryDateCommonBasic = new ActualDeliveryDateCommonBasic();
                actualDeliveryDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(deliveryDTO.getDeliveryDate()));
                deliveryCommonAggregate.setActualDeliveryDate(actualDeliveryDateCommonBasic);
            }

            AddressDTO addressDTO = deliveryDTO.getDeliveryAddressDTO();
            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                AddressType deliveryAddress = new AddressType();

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    deliveryAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getAdditionalStreetName())) {
                    AdditionalStreetNameCommonBasic additionalStreetNameCommonBasic =
                            new AdditionalStreetNameCommonBasic();
                    additionalStreetNameCommonBasic.setValue(addressDTO.getAdditionalStreetName());
                    deliveryAddress.setAdditionalStreetName(additionalStreetNameCommonBasic);
                    isAddressAvailable = true;
                }

                PostalZoneCommonBasic postalZoneCommonBasic = null;
                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    deliveryAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    deliveryAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                LocationType locationType = new LocationType();
                if (isAddressAvailable) {
                    locationType.setAddress(deliveryAddress);
                    deliveryCommonAggregate.setDeliveryLocation(locationType);
                }
            }
            invoice.getDeliveries().add(deliveryCommonAggregate);
        }
    }

    /**
     * Method used to map delivery information from Invoice wrapper to EHF Invoice Line.
     */
    private static void mapEHFV2DeliveryAddress(InvoiceDTO invoiceDTO, InvoiceLineType invoiceLine) {

        DeliveryDTO deliveryDTO = invoiceDTO.getDeliveryDTO();

        if (deliveryDTO != null) {

            ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
            DeliveryType deliveryCommonAggregate = new DeliveryType();
            if (deliveryDTO.getDeliveryDate() != null) {
                actualDeliveryDateCommonBasic = new ActualDeliveryDateCommonBasic();
                actualDeliveryDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(deliveryDTO.getDeliveryDate()));
                deliveryCommonAggregate.setActualDeliveryDate(actualDeliveryDateCommonBasic);
            }

            AddressDTO addressDTO = deliveryDTO.getDeliveryAddressDTO();
            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                AddressType deliveryAddress = new AddressType();

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    deliveryAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                PostalZoneCommonBasic postalZoneCommonBasic = null;
                if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                    postalZoneCommonBasic = new PostalZoneCommonBasic();
                    postalZoneCommonBasic.setValue(addressDTO.getPostalZone());
                    deliveryAddress.setPostalZone(postalZoneCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                    CityNameCommonBasic cityNameCommonBasic = new CityNameCommonBasic();
                    cityNameCommonBasic.setValue(addressDTO.getCityName());
                    deliveryAddress.setCityName(cityNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID_ORDER.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    deliveryAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                LocationType locationType = new LocationType();
                if (isAddressAvailable) {
                    locationType.setAddress(deliveryAddress);
                    deliveryCommonAggregate.setDeliveryLocation(locationType);
                }
            }
            invoiceLine.getDeliveries().add(deliveryCommonAggregate);
        }
    }

    /**
     * Method used to map currency information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2Currency(InvoiceDTO invoiceDTO, Invoice invoice) {

        CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
        CurrencyDTO baseCurrencyDTO = invoiceDTO.getBaseCurrencyDTO();

        if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
            DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                    new DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
            documentCurrencyCodeCommonBasic.setValue(currencyDTO.getCurrencyCode());
            documentCurrencyCodeCommonBasic.setName(currencyDTO.getCurrencyName());
            invoice.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);

            if (baseCurrencyDTO != null && !StringUtils.isEmpty(baseCurrencyDTO.getCurrencyCode())) {
                if (!(currencyDTO.getCurrencyCode().equalsIgnoreCase(baseCurrencyDTO.getCurrencyCode()))) {
                    TaxCurrencyCodeCommonBasic taxCurrencyCodeCommonBasic =
                            new TaxCurrencyCodeCommonBasic();
                    taxCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
                    taxCurrencyCodeCommonBasic.setValue(baseCurrencyDTO.getCurrencyCode());
                    taxCurrencyCodeCommonBasic.setName(baseCurrencyDTO.getCurrencyName());
                    invoice.setTaxCurrencyCode(taxCurrencyCodeCommonBasic);
                }
            }
        }
    }

    /**
     * Method used to map invoice exchange rate from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2ExchangeRate(InvoiceDTO invoiceDTO, Invoice invoice) {

        // Set exchange rate
        if (invoiceDTO.getExchangeRate() != null) {
            CalculationRateCommonBasic calculationRateCommonBasic =
                    new CalculationRateCommonBasic();
            calculationRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getExchangeRate()));
            ExchangeRateType exchangeRateType = new ExchangeRateType();
            exchangeRateType.setCalculationRate(calculationRateCommonBasic);

            if (invoiceDTO.getCurrencyDTO() != null
                    && !StringUtils.isEmpty(invoiceDTO.getCurrencyDTO().getCurrencyCode())) {
                SourceCurrencyCodeCommonBasic sourceCurrencyCodeCommonBasic =
                        new SourceCurrencyCodeCommonBasic();
                sourceCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
                sourceCurrencyCodeCommonBasic.setValue(invoiceDTO.getCurrencyDTO().getCurrencyCode());
                exchangeRateType.setSourceCurrencyCode(sourceCurrencyCodeCommonBasic);
            }

            if (invoiceDTO.getInvoiceCurrencyBaseRate() != null) {
                SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                        new SourceCurrencyBaseRateCommonBasic();
                sourceCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getInvoiceCurrencyBaseRate()));
                exchangeRateType.setSourceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic);
            }

            if (invoiceDTO.getBaseCurrencyDTO() != null
                    && !StringUtils.isEmpty(invoiceDTO.getBaseCurrencyDTO().getCurrencyCode())) {
                TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                        new TargetCurrencyCodeCommonBasic();
                targetCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
                targetCurrencyCodeCommonBasic.setValue(invoiceDTO.getBaseCurrencyDTO().getCurrencyCode());
                exchangeRateType.setTargetCurrencyCode(targetCurrencyCodeCommonBasic);
            }

            if (invoiceDTO.getBaseCurrencyBaseRate() != null) {
                TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                        new TargetCurrencyBaseRateCommonBasic();
                targetCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getBaseCurrencyBaseRate()));
                exchangeRateType.setTargetCurrencyBaseRate(targetCurrencyBaseRateCommonBasic);

                MathematicOperatorCodeCommonBasic operator =
                        new MathematicOperatorCodeCommonBasic();
                operator.setValue(EHFConstants.OP_MULTIPLY.getValue());
                exchangeRateType.setMathematicOperatorCode(operator);
            }
            invoice.setTaxExchangeRate(exchangeRateType);
        }
    }

    /**
     * Method used to map invoice line item details from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2InvoiceLineItems(InvoiceDTO invoiceDTO, Invoice invoice) {

        List<InvoiceLineItemDTO> invoiceLineItemDTOs = invoiceDTO.getInvoiceLineItems();

        if (invoiceLineItemDTOs != null) {

            ItemType item = null;
            InvoiceLineType invoiceLine = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            TaxTotalType taxTotalCommonAggregate = null;
            OrderLineReferenceCommonAggregate orderLineReferenceCommonAggregate = null;
            ItemIdentificationType sellersItemIdentification = null;
            TaxCategoryType taxCategoryType = null;
            PriceTypeCommonAggregate price = null;
            MonetaryTotalType monetaryTotalType = null;
            NameCommonBasic nameCommonBasic = null;
            IDCommonBasic idCommonBasic = null;
            InvoicedQuantityCommonBasic invoicedQuantityCommonBasic = null;
            PayableAmountCommonBasic payableAmountCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionTaxAmount = null;
            LineExtensionAmountCommonBasic lineExtensionTotalAmount = null;
            TaxExclusiveAmountCommonBasic taxExclusiveAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            LineIDCommonBasic lineIDCommonBasic = null;
            ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
            AmountCommonBasic amountCommonBasic = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            PayableRoundingAmountCommonBasic payableRoundingAmountCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            List<AllowanceChargeDTO> allowanceCharges = null;
            int i = 0;
            for (InvoiceLineItemDTO invoiceLineItemDTO : invoiceLineItemDTOs) {

                if (invoiceLineItemDTO.getTotalExcTax() == null) {
                    continue;
                }
                invoiceLine = new InvoiceLineType();

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(invoiceLineItemDTO.getId());
                    invoiceLine.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getNote())) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(invoiceLineItemDTO.getNote());
                    noteCommonBasic.setLanguageID(invoiceDTO.getLanguage());
                    invoiceLine.getNotes().add(noteCommonBasic);
                }

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceLineItemDTO.getProductNo());
                idCommonBasic.setSchemeID(EHFConstants.GTIN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                sellersItemIdentification = new ItemIdentificationType();
                sellersItemIdentification.setID(idCommonBasic);
                item = new ItemType();
                item.setSellersItemIdentification(sellersItemIdentification);

                nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(invoiceLineItemDTO.getProductName());
                item.setName(nameCommonBasic);

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getOriginCountry())) {
                    CountryType countryType = new CountryType();
                    NameCommonBasic name = new NameCommonBasic();
                    name.setValue(invoiceLineItemDTO.getOriginCountry());
                    countryType.setName(name);
                    item.setOriginCountry(countryType);
                }

                if(invoiceLineItemDTO.getManufacturerPartyDTO() != null) {

                    ManufacturerPartyDTO manufacturerPartyDTO = invoiceLineItemDTO.getManufacturerPartyDTO();
                    PartyTypeCommonAggregate partyType = new PartyTypeCommonAggregate();
                    PartyNameCommonAggregate partyName = new PartyNameCommonAggregate();
                    NameCommonBasic name = new NameCommonBasic();
                    if(!StringUtils.isEmpty(manufacturerPartyDTO.getPartyName())) {
                        name.setValue(manufacturerPartyDTO.getPartyName());
                        partyName.setName(name);
                        partyType.getPartyNames().add(partyName);
                    }
                    if(!StringUtils.isEmpty(manufacturerPartyDTO.getCompanyId())) {
                        PartyLegalEntityCommonAggregate partyLegalEntity = new PartyLegalEntityCommonAggregate();
                        CompanyIDCommonBasic companyId = new CompanyIDCommonBasic();
                        companyId.setSchemeID(EHFConstants.COUNTRY_CODE_NORWAY.getValue() + ":" + EHFConstants.SCHEME_ID.getValue());
                        companyId.setValue(manufacturerPartyDTO.getCompanyId());
                        partyLegalEntity.setCompanyID(companyId);
                        partyType.getPartyLegalEntities().add(partyLegalEntity);
                        item.getManufacturerParties().add(partyType);
                    }
                }

                if (!StringUtils.isEmpty(invoiceLineItemDTO.getAccountingCode())) {
                    accountingCostCommonBasic = new AccountingCostCommonBasic();
                    accountingCostCommonBasic.setValue(invoiceLineItemDTO.getAccountingCode());
                    invoiceLine.setAccountingCost(accountingCostCommonBasic);
                }

                if (invoiceLineItemDTO.getPeriodStartDate() != null) {

                    PeriodType periodType = new PeriodType();
                    StartDateCommonBasic startDate = new StartDateCommonBasic();
                    startDate.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceLineItemDTO.getPeriodStartDate()));
                    periodType.setStartDate(startDate);

                if (invoiceLineItemDTO.getPeriodEndDate() != null) {
                    EndDateCommonBasic endDate = new EndDateCommonBasic();
                    endDate.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceLineItemDTO.getPeriodEndDate()));
                    periodType.setEndDate(endDate);
                }
                    invoiceLine.getInvoicePeriods().add(periodType);
                }

                lineIDCommonBasic = new LineIDCommonBasic();
                lineIDCommonBasic.setValue(String.valueOf(++i));
                orderLineReferenceCommonAggregate = new OrderLineReferenceCommonAggregate();
                orderLineReferenceCommonAggregate.setLineID(lineIDCommonBasic);
                invoiceLine.getOrderLineReferences().add(orderLineReferenceCommonAggregate);

                invoicedQuantityCommonBasic = new InvoicedQuantityCommonBasic();
                invoicedQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                invoicedQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                invoiceLine.setInvoicedQuantity(invoicedQuantityCommonBasic);

                if (invoiceLineItemDTO.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    if (invoiceDTO.getIsInvoiceCompanyTaxFree()) {
                        idCommonBasic.setValue("Z");
                    } else {
                        idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(invoiceLineItemDTO.getTaxPercent()));
                    }
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID.getValue());
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
                    item.getClassifiedTaxCategories().add(taxCategoryType);
                }

                if (invoiceLineItemDTO.getUnitPrice() != null) {

                    priceAmountCommonBasic = new PriceAmountCommonBasic();
                    priceAmountCommonBasic.setCurrencyID(currencyCode);
                    priceAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getUnitPrice()));
                    price = new PriceTypeCommonAggregate();
                    price.setPriceAmount(priceAmountCommonBasic);

                    baseQuantityCommonBasic = new BaseQuantityCommonBasic();
                    baseQuantityCommonBasic.setValue(new BigDecimal(1));
                    baseQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                    baseQuantityCommonBasic.setUnitCode(invoiceLineItemDTO.getUnitCode());
                    price.setBaseQuantity(baseQuantityCommonBasic);
                    invoiceLine.setPrice(price);
                }

                if (invoiceLineItemDTO.getQuantity() != null) {
                    invoicedQuantityCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getQuantity()));
                    invoiceLine.setInvoicedQuantity(invoicedQuantityCommonBasic);
                }

                if (invoiceLineItemDTO.getTotalExcTax() != null) {
                    lineExtensionTaxAmount = new LineExtensionAmountCommonBasic();
                    lineExtensionTaxAmount.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTotalExcTax()));
                    lineExtensionTaxAmount.setCurrencyID(currencyCode);
                    invoiceLine.setLineExtensionAmount(lineExtensionTaxAmount);
                }

                if (invoiceLineItemDTO.getTaxAmount() != null) {
                    taxAmountCommonBasic = new TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceLineItemDTO.getTaxAmount()));
                    taxAmountCommonBasic.setCurrencyID(currencyCode);
                    taxTotalCommonAggregate = new TaxTotalType();
                    taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                    invoiceLine.getTaxTotals().add(taxTotalCommonAggregate);
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
                                    new AllowanceChargeReasonCommonBasic();
                            allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                            allowanceChargeType.getAllowanceChargeReasons().add(
                                    allowanceChargeReasonCommonBasic);
                        }

                        if (allowanceChargeDTO.getAmount() != null) {
                            amountCommonBasic = new AmountCommonBasic();
                            amountCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getAmount()));
                            amountCommonBasic.setCurrencyID(currencyCode);
                            allowanceChargeType.setAmount(amountCommonBasic);
                        }

                        if (allowanceChargeDTO.getTaxPercent() != null) {

                            taxCategoryType = new TaxCategoryType();

                            idCommonBasic = new IDCommonBasic();
                            idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(allowanceChargeDTO.getTaxPercent()));
                            idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID.getValue());
                            idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                            taxCategoryType.setID(idCommonBasic);

                            percentCommonBasic = new PercentCommonBasic();
                            percentCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getTaxPercent()));
                            taxCategoryType.setPercent(percentCommonBasic);

                            if (!StringUtils.isEmpty(allowanceChargeDTO.getTaxType())) {

                                idCommonBasic = new IDCommonBasic();
                                idCommonBasic.setValue(allowanceChargeDTO.getTaxType());
                                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                                taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                                taxSchemeCommonAggregate.setID(idCommonBasic);
                            }

                            taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                            allowanceChargeType.getTaxCategories().add(taxCategoryType);
                        }

                        invoiceLine.getAllowanceCharges().add(allowanceChargeType);
                    }
                }

                mapEHFV2DeliveryAddress(invoiceDTO, invoiceLine);
                invoiceLine.setItem(item);
                invoice.getInvoiceLines().add(invoiceLine);
            }

            monetaryTotalType = invoice.getLegalMonetaryTotal();
            if (monetaryTotalType == null) {
                monetaryTotalType = new MonetaryTotalType();
                invoice.setLegalMonetaryTotal(monetaryTotalType);
            }

            BigDecimal taxExclusiveAmount = null;
            if (invoiceDTO.getTotalExcTax() != null) {

                lineExtensionTotalAmount = new LineExtensionAmountCommonBasic();
                lineExtensionTotalAmount.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getTotalExcTax()));
                lineExtensionTotalAmount.setCurrencyID(currencyCode);
                monetaryTotalType.setLineExtensionAmount(lineExtensionTotalAmount);

                taxExclusiveAmount = ConversionUtils.asBigDecimal(invoiceDTO.getTotalExcTax());

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
                        invoiceDTO.getTaxAmount() != null ? taxExclusiveAmount.add(ConversionUtils.asBigDecimal(invoiceDTO.getTaxAmount()))
                                : taxExclusiveAmount;

                payableRoundingAmountCommonBasic = new PayableRoundingAmountCommonBasic();
               if (invoiceDTO.getIsRoundPayableAmount()) {
                   if (invoiceDTO.getPayableRoundingAmount() != null) {
                       payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getPayableRoundingAmount()));
                   } else {
                       payableRoundingAmountCommonBasic.setValue(ConversionUtils.getRoundingAmount(taxInclusiveAmount));
                   }
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

            invoice.setLegalMonetaryTotal(monetaryTotalType);
        }
    }

    /**
     * Method used to map tax summary details from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2TaxSummaries(InvoiceDTO invoiceDTO, Invoice invoice) {

        List<TaxSummaryDTO> taxSummaries = invoiceDTO.getTaxSummaries();

        if (taxSummaries != null) {

            TaxCategoryType taxCategoryType = null;
            IDCommonBasic idCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxSubtotalCommonAggregate taxSubtotalCommonAggregate = null;
            TaxableAmountCommonBasic taxableAmountCommonBasic = null;
            TaxExemptionReasonCodeCommonBasic taxExemptionReasonCodeCommonBasic = null;
            TaxTotalType taxTotalCommonAggregate = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            TransactionCurrencyTaxAmountCommonBasic transactionCurrency = null;

            String currencyCode = null;
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            CurrencyDTO baseCurrencyDTO = invoiceDTO.getBaseCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            taxTotalCommonAggregate = new TaxTotalType();
            for (TaxSummaryDTO taxSummary : taxSummaries) {

                taxSubtotalCommonAggregate = new TaxSubtotalCommonAggregate();
                if (taxSummary.getTotalExcTax() != null) {
                    taxableAmountCommonBasic = new TaxableAmountCommonBasic();
                    taxableAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTotalExcTax()));
                    taxableAmountCommonBasic.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTaxableAmount(taxableAmountCommonBasic);
                }

                if (taxSummary.getTaxAmount() != null) {
                    taxAmountCommonBasic = new TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxAmount()));
                    taxAmountCommonBasic.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                }

                if (taxSummary.getTransactionCurrencyTaxAmount() != null) {
                    transactionCurrency = new TransactionCurrencyTaxAmountCommonBasic();
                    transactionCurrency.setValue(ConversionUtils.asBigDecimal(taxSummary.getTransactionCurrencyTaxAmount()));
                    transactionCurrency.setCurrencyID(baseCurrencyDTO.getCurrencyCode());
                    taxSubtotalCommonAggregate.setTransactionCurrencyTaxAmount(transactionCurrency);
                }

                if (taxSummary.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(taxSummary.getTaxPercent()));
                    if (invoiceDTO.getIsInvoiceCompanyTaxFree()) {
                         idCommonBasic.setValue("Z");
                    } else {
                         idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(taxSummary.getTaxPercent()));
                    }
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(taxSummary.getTaxTypeIntName())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(taxSummary.getTaxTypeIntName());   
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    if (taxSummary.getTaxPercent() == 0) {

                       if(taxSummary.getTaxExcemptionReasion() == null) {
                            throw new RuntimeException("TaxExcemptionReasion can't be null when tax percent is 0 inside \"tax summary\" ");
                        }

                        if (taxSummary.getTaxExcemptionReasionCode() != null) {
                            taxExemptionReasonCodeCommonBasic = new TaxExemptionReasonCodeCommonBasic();
                            taxExemptionReasonCodeCommonBasic.setValue(taxSummary.getTaxExcemptionReasionCode());
                            taxCategoryType.setTaxExemptionReasonCode(taxExemptionReasonCodeCommonBasic);
                        }

                        TaxExemptionReasonCommonBasic taxExemptionReasonCommonBasic = new TaxExemptionReasonCommonBasic();
                        taxExemptionReasonCommonBasic.setValue(taxSummary.getTaxExcemptionReasion());
                        taxCategoryType.getTaxExemptionReasons().add((taxExemptionReasonCommonBasic));
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    taxSubtotalCommonAggregate.setTaxCategory(taxCategoryType);
                }

                if (taxSubtotalCommonAggregate != null) {
                    taxTotalCommonAggregate.getTaxSubtotals().add(taxSubtotalCommonAggregate);
                }
            }

            if (invoiceDTO.getTaxAmount() != null) {
                taxAmountCommonBasic = new TaxAmountCommonBasic();
                taxAmountCommonBasic.setCurrencyID(currencyCode);
                taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getTaxAmount()));
                taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
            }
            invoice.getTaxTotals().add(taxTotalCommonAggregate);
        }
    }

    /**
     * Method used to map tax payment terms from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2PaymentTerms(InvoiceDTO invoiceDTO, Invoice invoice) {

        PaymentTermsType paymentTermsCommonAggregate = null;
        NoteCommonBasic noteCommonBasic = null;

        List<InvoiceSettingDTO> invoiceSettingDTOs = invoiceDTO.getInvoiceSettingDTOs();
        if (invoiceSettingDTOs != null && invoiceSettingDTOs.size() > 0) {
        for (InvoiceSettingDTO invoiceSettingDTO : invoiceSettingDTOs) {

            List<String> paymentTermsNotes = invoiceSettingDTO.getPaymentTermsNotes();
            if (paymentTermsNotes != null && !paymentTermsNotes.isEmpty()) {

                for (String paymentTermsNote : paymentTermsNotes) {

                    paymentTermsCommonAggregate = new PaymentTermsType();
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(paymentTermsNote);
                    paymentTermsCommonAggregate.getNotes().add(noteCommonBasic);
                    invoice.getPaymentTerms().add(paymentTermsCommonAggregate);
                }
            }
        }
        }
    }

    private static void mapEHFV2TaxRepresentativeParty(InvoiceDTO invoiceDTO, Invoice invoice) {

        PartyTypeCommonAggregate partyType = new PartyTypeCommonAggregate();
        String country = "";

        if (invoiceDTO.getTaxRepresentation() != null) {
            TaxRepresentativeDTO taxRepresentation = invoiceDTO.getTaxRepresentation();
            AddressDTO addressDTO = taxRepresentation.getPostalAddress();
            String orgNo = taxRepresentation.getOrganisationNo();
            String taxTypeIntName = taxRepresentation.getTaxTypeIntName();

            if (addressDTO != null && !StringUtils.isEmpty(addressDTO.getCountryCode())) {
                country = addressDTO.getCountryCode().toUpperCase();
            }

            if(!StringUtils.isEmpty(taxRepresentation.getName())) {
                PartyNameCommonAggregate partyNameCommonAggregate = new PartyNameCommonAggregate();
                NameCommonBasic name = new NameCommonBasic();
                name.setValue(taxRepresentation.getName());
                partyNameCommonAggregate.setName(name);
                partyType.getPartyNames().add(partyNameCommonAggregate);
            }

            if (taxRepresentation.getPostalAddress() != null) {

               addressDTO = taxRepresentation.getPostalAddress();
               AddressType addressType = new AddressType();
               if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                   StreetNameCommonBasic streetName = new StreetNameCommonBasic();
                   streetName.setValue(addressDTO.getStreetName());
                   addressType.setStreetName(streetName);
               }
               if (!StringUtils.isEmpty(addressDTO.getAdditionalStreetName())) {
                   AdditionalStreetNameCommonBasic additionalStreetName = new AdditionalStreetNameCommonBasic();
                   additionalStreetName.setValue(addressDTO.getAdditionalStreetName());
                   addressType.setAdditionalStreetName(additionalStreetName);
               }
               if (!StringUtils.isEmpty(addressDTO.getCityName())) {
                   CityNameCommonBasic cityName = new CityNameCommonBasic();
                   cityName.setValue(addressDTO.getCityName());
                   addressType.setCityName(cityName);
               }
               if (!StringUtils.isEmpty(addressDTO.getPostalZone())) {
                   PostalZoneCommonBasic postalZone = new PostalZoneCommonBasic();
                   postalZone.setValue(addressDTO.getPostalZone());
                   addressType.setPostalZone(postalZone);
               }
               if (!StringUtils.isEmpty(addressDTO.getCountrySubentityCode())) {
                   CountrySubentityCodeCommonBasic countryCode = new CountrySubentityCodeCommonBasic();
                   countryCode.setValue(addressDTO.getCountrySubentityCode());
                   addressType.setCountrySubentityCode(countryCode);
               }
               if (!StringUtils.isEmpty(addressDTO.getCountryCode())) {
                   CountryType countryCode = new CountryType();
                   NameCommonBasic countryName = new NameCommonBasic();
                   countryName.setValue(addressDTO.getCountryCode());
                   countryCode.setName(countryName);
                   addressType.setCountry(countryCode);
               }
               partyType.setPostalAddress(addressType);
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                        new  PartyTaxSchemeCommonAggregate();
                CompanyIDCommonBasic companyIDCommonBasic =
                        new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(removeCountryCodeAndMVAFromEndPointId(orgNo) + EHFConstants.MVA.getValue());
                companyIDCommonBasic.setSchemeID(EHFConstants.COUNTRY_CODE_NORWAY.getValue() + ":" + taxTypeIntName);
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);

                TaxSchemeCommonAggregate taxSchemeCommonAggregate =
                        new TaxSchemeCommonAggregate();
                IDCommonBasic idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);

                partyType.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }
          invoice.setTaxRepresentativeParty(partyType);
        }
    }

    /**
     * Method used to map allowance charges from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV2AllowanceCharges(InvoiceDTO invoiceDTO, Invoice invoice) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeType allowanceChargeType = null;

        List<AllowanceChargeDTO> allowanceCharges = invoiceDTO.getAllowanceCharges();
        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            TaxCategoryType taxCategoryType = null;
            IDCommonBasic idCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

            double allowanceTotalAmount = 0d;
            double chargeTotalAmount = 0d;

            String currencyCode = "";
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
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
                    allowanceChargeType.getAllowanceChargeReasons().add(
                            allowanceChargeReasonCommonBasic);
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

                if (allowanceChargeDTO.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(allowanceChargeDTO.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(allowanceChargeDTO.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(allowanceChargeDTO.getTaxType())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(allowanceChargeDTO.getTaxType());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    allowanceChargeType.getTaxCategories().add(taxCategoryType);
                }
                invoice.getAllowanceCharges().add(allowanceChargeType);
            }

            MonetaryTotalType legalMonetaryTotal = new MonetaryTotalType();

            if (allowanceTotalAmount > 0) {
                AllowanceTotalAmountCommonBasic allowanceTotalAmountCB =
                        new AllowanceTotalAmountCommonBasic();
                allowanceTotalAmountCB.setValue(ConversionUtils.asBigDecimal(allowanceTotalAmount));
                allowanceTotalAmountCB.setCurrencyID(currencyCode);
                legalMonetaryTotal.setAllowanceTotalAmount(allowanceTotalAmountCB);
            }

            if (chargeTotalAmount > 0) {
                ChargeTotalAmountCommonBasic chargeTotalAmountCB =
                        new ChargeTotalAmountCommonBasic();
                chargeTotalAmountCB.setValue(ConversionUtils.asBigDecimal(chargeTotalAmount));
                chargeTotalAmountCB.setCurrencyID(currencyCode);
                legalMonetaryTotal.setChargeTotalAmount(chargeTotalAmountCB);
            }
            invoice.setLegalMonetaryTotal(legalMonetaryTotal);
        }
    }

    /**
     * Unmarshalling XML file to invoice object.
     */
    public static InvoiceDTO getEHFV2Invoice(File file) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Invoice.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        Invoice invoice = (Invoice) unmarshaller.unmarshal(file);

        return convertToEHFV2Invoice(invoice);
    }

    /**
     * Method used to convert EHF Invoice XML file to EHF Invoice object
     */
    public static InvoiceDTO getEHFV2Invoice(String filePath) throws JAXBException {

        File file = new File(filePath);
        return getEHFV2Invoice(file);
    }

    /**
     * Method used to convert EHF invoice to Invoice wrapper.
     */
    public static InvoiceDTO convertToEHFV2Invoice(Invoice invoice) {

        InvoiceDTO invoiceDTO = null;
        if (invoice != null) {

            invoiceDTO = new InvoiceDTO();

            // Set UBL version id
            UBLVersionIDCommonBasic ublVersionIDCommonBasic = invoice.getUBLVersionID();
            if (ublVersionIDCommonBasic != null) {
                invoiceDTO.setUblExtensions(ublVersionIDCommonBasic.getValue());
            }

            // Set customization id
            CustomizationIDCommonBasic customizationIDCommonBasic = invoice.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                invoiceDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            // Set profile id
            ProfileIDCommonBasic profileIDCommonBasic = invoice.getProfileID();
            if (profileIDCommonBasic != null) {
                invoiceDTO.setProfileID(profileIDCommonBasic.getValue());
            }

            // Set invoice no
            IDCommonBasic idCommonBasic = invoice.getID();
            if (idCommonBasic != null) {
                invoiceDTO.setInvoiceNo(idCommonBasic.getValue());
            }

            // Set issue date
            IssueDateCommonBasic issueDateCommonBasic = invoice.getIssueDate();
            if (issueDateCommonBasic != null) {
                invoiceDTO.setIssueDate(ConversionUtils.asDate(issueDateCommonBasic.getValue()));
            }
            
            final DueDateCommonBasic dueDate = invoice.getDueDate();
            if (dueDate != null) {
                invoiceDTO.setPaymentDueDate(ConversionUtils.asDate(dueDate.getValue()));
            }

            List<PeriodType> periods = invoice.getInvoicePeriods();
            if (periods != null && periods.size() > 0) {

                PeriodType type = periods.get(0);
                if (type != null) {

                    if (type.getStartDate() != null && type.getStartDate().getValue() != null) {
                        invoiceDTO.setPeriodStartDate(ConversionUtils.asDate(type.getStartDate().getValue()));
                    }

                    if (type.getEndDate() != null && type.getEndDate().getValue() != null) {
                        invoiceDTO.setPeriodEndDate(ConversionUtils.asDate(type.getEndDate().getValue()));
                    }
                }
            }

            // Set purchase no
            OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                    invoice.getOrderReference();
            if (orderReferenceCommonAggregate != null) {
                idCommonBasic = orderReferenceCommonAggregate.getID();
                if (idCommonBasic != null) {
                    invoiceDTO.setPurchaseOrderNo(idCommonBasic.getValue());
                }
            }
            
            // Set Buyer Reference
            BuyerReferenceCommonBasic buyerRef = invoice.getBuyerReference();
            if (buyerRef != null) {
                invoiceDTO.setBuyerReference(buyerRef.getValue());
            }

            //set contract
            if (invoice.getContractDocumentReferences() != null && invoice.getContractDocumentReferences().size() > 0) {

                ContractDTO contractDTO = new ContractDTO();
                DocumentReferenceType documentReferenceType = invoice.getContractDocumentReferences().get(0);
                idCommonBasic = documentReferenceType.getID();
                if (idCommonBasic != null) {
                    contractDTO.setContractId(idCommonBasic.getValue());
                }

                DocumentTypeCodeCommonBasic documentTypeCode = documentReferenceType.getDocumentTypeCode();
                if (documentTypeCode != null) {
                    contractDTO.setContractType(Integer.parseInt(documentTypeCode.getValue()));
                }

                DocumentTypeCommonBasic documentType = documentReferenceType.getDocumentType();
                if (documentType != null) {
                    contractDTO.setDocumentType(documentType.getValue());
                }
                invoiceDTO.setContractDTO(contractDTO);
            }

            List<DocumentReferenceType> additionalDocumentReferences =
                    invoice.getAdditionalDocumentReferences();
            if (additionalDocumentReferences != null) {

                AttachmentType attachmentType = null;
                DocumentTypeCommonBasic documentTypeCommonBasic = null;
                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocumentBinaryObject = null;

                List<FileDTO> files = new ArrayList<FileDTO>();
                FileDTO fileDTO = null;
                String fileExt = "";

                for (DocumentReferenceType documentReferenceType : additionalDocumentReferences) {

                    documentTypeCommonBasic = documentReferenceType.getDocumentType();
                    fileDTO = new FileDTO();

                    if (documentTypeCommonBasic != null) {
                        fileDTO.setDocType(documentTypeCommonBasic.getValue());
                    }
                    
                    if (documentReferenceType.getDocumentTypeCode() != null) {
                        fileDTO.setDocumentTypeCode(documentReferenceType.getDocumentTypeCode().getValue());
                    }

                    attachmentType = documentReferenceType.getAttachment();
                    if (attachmentType != null) {
                        embeddedDocumentBinaryObject =
                                attachmentType.getEmbeddedDocumentBinaryObject();
                        if (embeddedDocumentBinaryObject != null) {

                            fileDTO.setFileContent(embeddedDocumentBinaryObject.getValue());

                            if (embeddedDocumentBinaryObject.getMimeCode() != null) {
                                fileExt = embeddedDocumentBinaryObject.getMimeCode();
                                if (!StringUtils.isEmpty(fileExt) && fileExt.indexOf("/") != -1) {
                                    fileExt = fileExt.substring(fileExt.lastIndexOf("/") + 1);
                                } else {
                                    fileExt = EHFConstants.PDF.getValue();
                                }
                            }
                        }

                        idCommonBasic = documentReferenceType.getID();
                        if (idCommonBasic != null) {
                            fileDTO.setFileName(idCommonBasic.getValue()
                                    + EHFConstants.DOT.getValue() + fileExt);
                        }
                    }
                    files.add(fileDTO);
                }
                invoiceDTO.setFiles(files);
            }

            List<NoteCommonBasic> notes = invoice.getNotes();
            if (notes != null && !notes.isEmpty()) {
                for (NoteCommonBasic noteCommonBasic : notes) {
                    invoiceDTO.getNotes().add(noteCommonBasic.getValue());
                    invoiceDTO.setLanguage(noteCommonBasic.getLanguageID());
                }
            }
            
            mapEHFV2PayeeParty(invoice, invoiceDTO);
            mapEHFV2Supplier(invoice, invoiceDTO);
            mapEHFV2SupplierBankAccount(invoice, invoiceDTO);
            mapEHFV2Customer(invoice, invoiceDTO);
            mapEHFV2CustomerBankAccount(invoice, invoiceDTO);
            mapEHFV2Currency(invoice, invoiceDTO);
            mapEHFV2ExchangeRate(invoice, invoiceDTO);
            mapEHFV2DeliveryAddress(invoice, invoiceDTO);
            mapEHFV2InvoiceLineItems(invoice, invoiceDTO);
            mapEHFV2TaxSummaries(invoice, invoiceDTO);
            mapEHFV2PaymentTerms(invoice, invoiceDTO);
            mapEHFV2AllowanceCharges(invoice, invoiceDTO);
            mapEHFV2TaxRepresentativeParty(invoice, invoiceDTO);
        }

        return invoiceDTO;
    }

    /**
     * Method used to map payee information from EHF invoice to Invoice wrapper.
     */
    private static void mapEHFV2PayeeParty(Invoice invoice, InvoiceDTO invoiceDTO) {

        PartyTypeCommonAggregate partyType = invoice.getPayeeParty();

        if (partyType != null) {

            PayeeParty payeeParty = new PayeeParty();
            List<PartyIdentificationCommonAggregate> partyIdentifications =
                    partyType.getPartyIdentifications();
            if (partyIdentifications != null) {

                for (PartyIdentificationCommonAggregate partyIdentification : partyIdentifications) {
                    if (partyIdentification.getID() != null) {
                        payeeParty.setId(partyIdentification.getID().getValue());
                    }
                }
            }

            List<PartyNameCommonAggregate> partyNames = partyType.getPartyNames();
            if (partyNames != null) {
                for (PartyNameCommonAggregate partyName : partyNames) {
                    NameCommonBasic nameCommonBasic = partyName.getName();
                    if (nameCommonBasic != null) {
                        payeeParty.setName(nameCommonBasic.getValue());
                    }
                }
            }

            List<PartyLegalEntityCommonAggregate> partyLegalEntities =
                    partyType.getPartyLegalEntities();
            if (partyLegalEntities != null) {
                for (PartyLegalEntityCommonAggregate partyLegalEntity : partyLegalEntities) {
                    CompanyIDCommonBasic companyIDCommonBasic = partyLegalEntity.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        payeeParty.setOrganizationNo(companyIDCommonBasic.getValue());
                    }
                }
            }
 
            AddressType addressType = partyType.getPostalAddress();
            AddressDTO addressDTO = new AddressDTO();
            if (addressType != null && addressType.getCountry() != null) {
                 addressDTO.setCountryCode(addressType.getCountry().getIdentificationCode().getValue());
                 payeeParty.setAddressDTO(addressDTO);
            }
            invoiceDTO.setPayeeParty(payeeParty);
        }
    }

    /**
     * Method used to map supplier information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2Supplier(Invoice invoice, InvoiceDTO invoiceDTO) {

        SupplierPartyType supplierPartyType = invoice.getAccountingSupplierParty();

        if (supplierPartyType != null) {

            SupplierDTO supplierDTO = new SupplierDTO();
            AddressDTO addressDTO = null;

            PartyTypeCommonAggregate party = supplierPartyType.getParty();
            ContactType sellerContact = supplierPartyType.getSellerContact();

            //set contact name
            if (sellerContact != null) {
                ContactPersonDTO contactPerson = new ContactPersonDTO();
                if (sellerContact.getID() != null) {
                    IDCommonBasic idCommonBasic = sellerContact.getID();
                    contactPerson.setId(idCommonBasic.getValue());
                }
                if (sellerContact.getName() != null) {
                    NameCommonBasic nameCommonBasic = sellerContact.getName();
                    contactPerson.setName(nameCommonBasic.getValue());
                }
                if (sellerContact.getTelephone() != null) {
                    TelephoneCommonBasic telephoneCommonBasic = sellerContact.getTelephone();
                    contactPerson.setTelephone(telephoneCommonBasic.getValue());
                }
                if (sellerContact.getTelefax() != null) {
                    TelefaxCommonBasic teleFaxCommonBasic = sellerContact.getTelefax();
                    contactPerson.setTeleFax(teleFaxCommonBasic.getValue());
                }
                if (sellerContact.getElectronicMail() != null) {
                    ElectronicMailCommonBasic email = sellerContact.getElectronicMail();
                    contactPerson.setEmail(email.getValue());
                }
                supplierDTO.setContactPersonDTO(contactPerson);
            }

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

                    if (partyLegalEntityCommonAggregate.getRegistrationName() != null) {
                        supplierDTO.setLegalName(partyLegalEntityCommonAggregate.getRegistrationName().getValue());
                    }
                    if (partyLegalEntityCommonAggregate.getRegistrationAddress() != null) {
                        AddressDTO legalAddressDTO = new AddressDTO();
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName() != null) {
                            legalAddressDTO.setCityName(partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName().getValue());
                        }
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry() != null) {
                            if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry().getIdentificationCode() != null) {
                                legalAddressDTO.setCountryCode(partyLegalEntityCommonAggregate.getRegistrationAddress()
                                        .getCountry().getIdentificationCode().getValue());
                            }
                        }
                        supplierDTO.setLegalAddressDTO(legalAddressDTO);
                    }
                }

                AddressType postalAddress = party.getPostalAddress();

                if (postalAddress != null) {

                    addressDTO = new AddressDTO();

                    StreetNameCommonBasic streetNameCommonBasic = postalAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    PostalZoneCommonBasic postalZoneCommonBasic = postalAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
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

            try {
                
                List<PartyTaxSchemeCommonAggregate> taxSchems = party.getPartyTaxSchemes();
                if (taxSchems != null && taxSchems.size() > 0) {
                    for (PartyTaxSchemeCommonAggregate taxSchm : taxSchems) {
                        if (taxSchm.getCompanyID() != null && taxSchm.getCompanyID().getValue() != null) {
                            if (taxSchm.getCompanyID().getValue().equals(EHFConstants.LEGAL_ENTITY_SCHEME_NAME.getValue())) {
                                invoiceDTO.setRegisterInBusinessEnterprises(Boolean.TRUE);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();                
            }
            invoiceDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map supplier bank account information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2SupplierBankAccount(Invoice invoice, InvoiceDTO invoiceDTO) {

        List<PaymentMeansCommonAggregate> paymentMeans = invoice.getPaymentMeans();

        if (paymentMeans != null) {

            BankAccountDTO bankAccountDTO =  new BankAccountDTO();
            AddressDTO bankAddressDTO = null;

            FinancialAccountType financialAccountType = null;
            BranchType branchType = null;
            AddressType bankAddress = null;
            CountryType countryType = null;
            IDCommonBasic idCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            StreetNameCommonBasic streetNameCommonBasic = null;
            PostalZoneCommonBasic postalZoneCommonBasic = null;
            PostboxCommonBasic postboxCommonBasic = null;
            CityNameCommonBasic cityNameCommonBasic = null;
            BuildingNumberCommonBasic buildingNumberCommonBasic = null;
            IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
            FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate = null;
            List<PaymentIDCommonBasic> paymentIDs = null;

            for (PaymentMeansCommonAggregate paymentMeansCommonAggregate : paymentMeans) {

                paymentIDs = paymentMeansCommonAggregate.getPaymentIDs();
                if (paymentIDs != null && !paymentIDs.isEmpty()) {
                    invoiceDTO.setPaymentId(paymentIDs.get(0).getValue());
                }

                financialAccountType = paymentMeansCommonAggregate.getPayeeFinancialAccount();
                if (financialAccountType != null) {

                    idCommonBasic = financialAccountType.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                        
                        String accountNo = idCommonBasic.getValue().replaceAll("[^A-Za-z0-9()\\[\\]]", "");
                        if (idCommonBasic.getSchemeID() != null && EHFConstants.IBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setiBanNo(idCommonBasic.getValue());
                        } else if (!accountNo.matches("^[0-9]+")) {
                            bankAccountDTO.setiBanNo(idCommonBasic.getValue());
                        } else {
                            bankAccountDTO.setBankAccountNumber(idCommonBasic.getValue());
                        }
                    }

                    nameCommonBasic = financialAccountType.getName();
                    if (nameCommonBasic != null) {
                        bankAccountDTO.setBankAccountName(nameCommonBasic.getValue());
                    }

                    branchType = financialAccountType.getFinancialInstitutionBranch();
                    if (branchType != null) {

                        idCommonBasic = branchType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            bankAccountDTO.setBankAccountId(idCommonBasic.getValue());
                        }

                        financialInstitutionCommonAggregate = branchType.getFinancialInstitution();
                        if (financialInstitutionCommonAggregate != null) {

                            idCommonBasic = financialInstitutionCommonAggregate.getID();
                            if (idCommonBasic != null) {
                                bankAccountDTO.setBic(idCommonBasic.getValue());
                            }

                            nameCommonBasic = financialInstitutionCommonAggregate.getName();
                            if (nameCommonBasic != null) {
                                bankAccountDTO.setBankName(nameCommonBasic.getValue());
                            }

                            bankAddress = financialInstitutionCommonAggregate.getAddress();
                            if (bankAddress != null) {

                                bankAddressDTO = new AddressDTO();

                                buildingNumberCommonBasic = bankAddress.getBuildingNumber();
                                if (buildingNumberCommonBasic != null) {
                                    bankAddressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                                }

                                streetNameCommonBasic = bankAddress.getStreetName();
                                if (streetNameCommonBasic != null) {
                                    bankAddressDTO.setStreetName(streetNameCommonBasic.getValue());
                                }

                                postalZoneCommonBasic = bankAddress.getPostalZone();
                                if (postalZoneCommonBasic != null) {
                                    bankAddressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                                }

                                postboxCommonBasic = bankAddress.getPostbox();
                                if (postboxCommonBasic != null) {
                                    bankAddressDTO.setPostalBox(postboxCommonBasic.getValue());
                                }

                                cityNameCommonBasic = bankAddress.getCityName();
                                if (cityNameCommonBasic != null) {
                                    bankAddressDTO.setCityName(cityNameCommonBasic.getValue());
                                }

                                countryType = bankAddress.getCountry();
                                if (countryType != null) {
                                    identificationCodeCommonBasic =
                                            countryType.getIdentificationCode();
                                    if (identificationCodeCommonBasic != null) {
                                        bankAddressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                                    }
                                }
                                bankAccountDTO.setBankAddressDTO(bankAddressDTO);
                            }
                        }
                    }
                }
            }

            SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();
            if (supplierDTO == null) {
                supplierDTO = new SupplierDTO();
            }
            supplierDTO.setBankAccountDTO(bankAccountDTO);
            invoiceDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map customer information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2Customer(Invoice invoice, InvoiceDTO invoiceDTO) {

        CustomerPartyType customerPartyType = invoice.getAccountingCustomerParty();

        if (customerPartyType != null) {

            CustomerDTO customerDTO = new CustomerDTO();
            AddressDTO addressDTO = null;

            PartyTypeCommonAggregate party = customerPartyType.getParty();
            //set contact name
            ContactType buyerContact = customerPartyType.getBuyerContact();
            if (buyerContact != null) {
                ContactPersonDTO contactPerson = new ContactPersonDTO();
                if (buyerContact.getID() != null) {
                    IDCommonBasic idCommonBasic = buyerContact.getID();
                    contactPerson.setId(idCommonBasic.getValue());
                }
                if (buyerContact.getName() != null) {
                    NameCommonBasic nameCommonBasic = buyerContact.getName();
                    contactPerson.setName(nameCommonBasic.getValue());
                }
                if (buyerContact.getTelephone() != null) {
                    TelephoneCommonBasic telephoneCommonBasic = buyerContact.getTelephone();
                    contactPerson.setTelephone(telephoneCommonBasic.getValue());
                }
                if (buyerContact.getTelefax() != null) {
                    TelefaxCommonBasic teleFaxCommonBasic = buyerContact.getTelefax();
                    contactPerson.setTeleFax(teleFaxCommonBasic.getValue());
                }
                if (buyerContact.getElectronicMail() != null) {
                    ElectronicMailCommonBasic email = buyerContact.getElectronicMail();
                    contactPerson.setEmail(email.getValue());
                }
                customerDTO.setContactPersonDTO(contactPerson);
            }

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
                    if (partyLegalEntityCommonAggregate.getRegistrationName() != null) {
                        customerDTO.setLegalName(partyLegalEntityCommonAggregate.getRegistrationName().getValue());
                    }
                    if (partyLegalEntityCommonAggregate.getRegistrationAddress() != null) {
                        AddressDTO legalAddressDTO = new AddressDTO();
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName() != null) {
                            legalAddressDTO.setCityName(partyLegalEntityCommonAggregate.getRegistrationAddress().getCityName().getValue());
                        }
                        if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry() != null) {
                            if (partyLegalEntityCommonAggregate.getRegistrationAddress().getCountry().getIdentificationCode() != null) {
                                legalAddressDTO.setCountryCode(partyLegalEntityCommonAggregate.getRegistrationAddress()
                                        .getCountry().getIdentificationCode().getValue());
                            }
                        }
                        customerDTO.setLegalAddressDTO(legalAddressDTO);
                    }
                }

                AddressType postalAddress = party.getPostalAddress();

                if (postalAddress != null) {

                    addressDTO = new AddressDTO();

                    StreetNameCommonBasic streetNameCommonBasic = postalAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    PostalZoneCommonBasic postalZoneCommonBasic = postalAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
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
            invoiceDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map customer bank account information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2CustomerBankAccount(Invoice invoice, InvoiceDTO invoiceDTO) {

        List<PaymentMeansCommonAggregate> paymentMeans = invoice.getPaymentMeans();

        if (paymentMeans != null) {

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;

            FinancialAccountType financialAccountType = null;
            BranchType branchType = null;
            CountryType countryType = null;
            AddressType bankAddress = null;
            IDCommonBasic idCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            PostboxCommonBasic postboxCommonBasic = null;
            CityNameCommonBasic cityNameCommonBasic = null;
            StreetNameCommonBasic streetNameCommonBasic = null;
            PostalZoneCommonBasic postalZoneCommonBasic = null;
            BuildingNumberCommonBasic buildingNumberCommonBasic = null;
            PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
            FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate = null;

            for (PaymentMeansCommonAggregate paymentMeansCommonAggregate : paymentMeans) {

                paymentDueDateCommonBasic = paymentMeansCommonAggregate.getPaymentDueDate();
                if (paymentDueDateCommonBasic != null
                        && paymentDueDateCommonBasic.getValue() != null) {
                    invoiceDTO.setPaymentDueDate(ConversionUtils.asDate(paymentDueDateCommonBasic.getValue()));
                }

                financialAccountType = paymentMeansCommonAggregate.getPayerFinancialAccount();
                if (financialAccountType != null) {

                    bankAccountDTO = new BankAccountDTO();

                    idCommonBasic = financialAccountType.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null
                            && idCommonBasic.getSchemeID() != null) {

                        if (EHFConstants.IBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setiBanNo(idCommonBasic.getValue());
                        } else if (EHFConstants.BBAN.getValue().equals(idCommonBasic.getSchemeID())) {
                            bankAccountDTO.setBankAccountNumber(idCommonBasic.getValue());
                        }
                    }

                    branchType = financialAccountType.getFinancialInstitutionBranch();
                    if (branchType != null) {

                        idCommonBasic = branchType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            bankAccountDTO.setBankAccountId(idCommonBasic.getValue());
                        }

                        financialInstitutionCommonAggregate = branchType.getFinancialInstitution();
                        if (financialInstitutionCommonAggregate != null) {

                            idCommonBasic = financialInstitutionCommonAggregate.getID();
                            if (idCommonBasic != null) {
                                bankAccountDTO.setBic(idCommonBasic.getValue());
                            }

                            nameCommonBasic = financialInstitutionCommonAggregate.getName();
                            if (nameCommonBasic != null) {
                                bankAccountDTO.setBankName(nameCommonBasic.getValue());
                            }

                            bankAddress = financialInstitutionCommonAggregate.getAddress();
                            if (bankAddress != null) {

                                bankAddressDTO = new AddressDTO();

                                buildingNumberCommonBasic = bankAddress.getBuildingNumber();
                                if (buildingNumberCommonBasic != null) {
                                    bankAddressDTO.setBuildingNumber(buildingNumberCommonBasic.getValue());
                                }

                                streetNameCommonBasic = bankAddress.getStreetName();
                                if (streetNameCommonBasic != null) {
                                    bankAddressDTO.setStreetName(streetNameCommonBasic.getValue());
                                }

                                postalZoneCommonBasic = bankAddress.getPostalZone();
                                if (postalZoneCommonBasic != null) {
                                    bankAddressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                                }

                                postboxCommonBasic = bankAddress.getPostbox();
                                if (postboxCommonBasic != null) {
                                    bankAddressDTO.setPostalBox(postboxCommonBasic.getValue());
                                }

                                cityNameCommonBasic = bankAddress.getCityName();
                                if (cityNameCommonBasic != null) {
                                    bankAddressDTO.setCityName(cityNameCommonBasic.getValue());
                                }

                                countryType = bankAddress.getCountry();
                                if (countryType != null) {
                                    identificationCodeCommonBasic =
                                            countryType.getIdentificationCode();
                                    if (identificationCodeCommonBasic != null) {
                                        bankAddressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();
            if (customerDTO == null) {
                customerDTO = new CustomerDTO();
            }
            customerDTO.setBankAccountDTO(bankAccountDTO);
            invoiceDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map delivery information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2DeliveryAddress(Invoice invoice, InvoiceDTO invoiceDTO) {

        List<DeliveryType> deliveries = invoice.getDeliveries();

        DeliveryDTO deliveryDTO = null;
        AddressDTO addressDTO = null;

        ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
        StreetNameCommonBasic streetNameCommonBasic = null;
        AdditionalStreetNameCommonBasic additionalStreetNameCommonBasic = null;
        PostalZoneCommonBasic postalZoneCommonBasic = null;
        CityNameCommonBasic cityNameCommonBasic = null;
        IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
        IDCommonBasic idCommonBasic = null;
        CountryType countryType = null;
        LocationType locationType = null;
        AddressType deliveryAddress = null;

        for (DeliveryType deliveryCommonAggregate : deliveries) {

            deliveryDTO = new DeliveryDTO();
            actualDeliveryDateCommonBasic = deliveryCommonAggregate.getActualDeliveryDate();
            if (actualDeliveryDateCommonBasic != null) {
                deliveryDTO.setDeliveryDate(ConversionUtils.asDate(actualDeliveryDateCommonBasic.getValue()));
            }

            locationType = deliveryCommonAggregate.getDeliveryLocation();
            if (locationType != null) {

                deliveryAddress = locationType.getAddress();
                addressDTO = new AddressDTO();
                if (deliveryAddress != null) {

                    streetNameCommonBasic = deliveryAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    additionalStreetNameCommonBasic = deliveryAddress.getAdditionalStreetName();
                    if (additionalStreetNameCommonBasic != null) {
                        addressDTO.setAdditionalStreetName(additionalStreetNameCommonBasic.getValue());
                    }

                    postalZoneCommonBasic = deliveryAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    cityNameCommonBasic = deliveryAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    countryType = deliveryAddress.getCountry();
                    if (countryType != null) {
                        identificationCodeCommonBasic = countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                    deliveryDTO.setDeliveryAddressDTO(addressDTO);
                }

                idCommonBasic = locationType.getID();
                if (idCommonBasic != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }
            }

            invoiceDTO.setDeliveryDTO(deliveryDTO);
            break;
        }
    }

    /**
     * Method used to map delivery information from EHF Invoice to InvoiceLineItemDTO wrapper.
     */
    private static void mapEHFV2DeliveryAddress(Invoice invoice,
            InvoiceLineItemDTO invoiceLineItemDTO) {

        List<DeliveryType> deliveries = invoice.getDeliveries();

        DeliveryDTO deliveryDTO = null;
        AddressDTO addressDTO = null;

        ActualDeliveryDateCommonBasic actualDeliveryDateCommonBasic = null;
        StreetNameCommonBasic streetNameCommonBasic = null;
        PostalZoneCommonBasic postalZoneCommonBasic = null;
        CityNameCommonBasic cityNameCommonBasic = null;
        IdentificationCodeCommonBasic identificationCodeCommonBasic = null;
        IDCommonBasic idCommonBasic = null;
        CountryType countryType = null;
        LocationType locationType = null;
        AddressType deliveryAddress = null;

        for (DeliveryType deliveryCommonAggregate : deliveries) {

            deliveryDTO = new DeliveryDTO();
            actualDeliveryDateCommonBasic = deliveryCommonAggregate.getActualDeliveryDate();
            if (actualDeliveryDateCommonBasic != null) {
                deliveryDTO.setDeliveryDate(ConversionUtils.asDate(actualDeliveryDateCommonBasic.getValue()));
            }

            locationType = deliveryCommonAggregate.getDeliveryLocation();
            if (locationType != null) {

                deliveryAddress = locationType.getAddress();
                addressDTO = new AddressDTO();
                if (deliveryAddress != null) {

                    streetNameCommonBasic = deliveryAddress.getStreetName();
                    if (streetNameCommonBasic != null) {
                        addressDTO.setStreetName(streetNameCommonBasic.getValue());
                    }

                    postalZoneCommonBasic = deliveryAddress.getPostalZone();
                    if (postalZoneCommonBasic != null) {
                        addressDTO.setPostalZone(postalZoneCommonBasic.getValue());
                    }

                    cityNameCommonBasic = deliveryAddress.getCityName();
                    if (cityNameCommonBasic != null) {
                        addressDTO.setCityName(cityNameCommonBasic.getValue());
                    }

                    countryType = deliveryAddress.getCountry();
                    if (countryType != null) {
                        identificationCodeCommonBasic = countryType.getIdentificationCode();
                        if (identificationCodeCommonBasic != null) {
                            addressDTO.setCountryCode(identificationCodeCommonBasic.getValue());
                        }
                    }
                    deliveryDTO.setDeliveryAddressDTO(addressDTO);
                }

                idCommonBasic = locationType.getID();
                if (idCommonBasic != null) {
                    addressDTO.setGln(idCommonBasic.getValue());
                }
            }

            invoiceLineItemDTO.setDeliveryDTO(deliveryDTO);
            break;
        }
    }

    /**
     * Method used to map customer currency information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2Currency(Invoice invoice, InvoiceDTO invoiceDTO) {

        DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                invoice.getDocumentCurrencyCode();
        CurrencyDTO currencyDTO = null;

        TaxCurrencyCodeCommonBasic taxCurrencyCode = invoice.getTaxCurrencyCode();
        CurrencyDTO baseCurrencyDTO = null;

        if (documentCurrencyCodeCommonBasic != null) {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getValue());
            invoiceDTO.setCurrencyDTO(currencyDTO);
        }

        if (taxCurrencyCode != null) {
            baseCurrencyDTO = new CurrencyDTO();
            baseCurrencyDTO.setCurrencyCode(taxCurrencyCode.getValue());
            baseCurrencyDTO.setCurrencyName(taxCurrencyCode.getValue());
            invoiceDTO.setBaseCurrencyDTO(baseCurrencyDTO);
        }
    }

    /**
     * Method used to map invoice exchange rate from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2ExchangeRate(Invoice invoice, InvoiceDTO invoiceDTO) {

        // Set exchange rate
        ExchangeRateType exchangeRateType = invoice.getTaxExchangeRate();
        if (exchangeRateType != null) {
            CalculationRateCommonBasic calculationRateCommonBasic =
                    exchangeRateType.getCalculationRate();
            if (calculationRateCommonBasic != null && calculationRateCommonBasic.getValue() != null) {
                invoiceDTO.setExchangeRate(calculationRateCommonBasic.getValue().doubleValue());
            }

            SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                    exchangeRateType.getSourceCurrencyBaseRate();
            if (sourceCurrencyBaseRateCommonBasic != null
                    && sourceCurrencyBaseRateCommonBasic.getValue() != null) {
                invoiceDTO.setInvoiceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }

            TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                    exchangeRateType.getTargetCurrencyCode();
            if (targetCurrencyCodeCommonBasic != null
                    && targetCurrencyCodeCommonBasic.getValue() != null) {
                CurrencyDTO baseCurrencyDTO = new CurrencyDTO();
                baseCurrencyDTO.setCurrencyCode(targetCurrencyCodeCommonBasic.getValue());
                invoiceDTO.setBaseCurrencyDTO(baseCurrencyDTO);
            }

            TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                    exchangeRateType.getTargetCurrencyBaseRate();
            if (targetCurrencyBaseRateCommonBasic != null
                    && targetCurrencyBaseRateCommonBasic.getValue() != null) {
                invoiceDTO.setBaseCurrencyBaseRate(targetCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }
        }
    }

    /**
     * Method used to map invoice line item details from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2InvoiceLineItems(Invoice invoice, InvoiceDTO invoiceDTO) {

        List<InvoiceLineType> invoiceLineItems = invoice.getInvoiceLines();
        if (invoiceLineItems != null) {

            InvoiceLineItemDTO invoiceLineItemDTO = null;

            ItemType itemCommonAggregate = null;
            ItemIdentificationType itemIdentificationType = null;
            PriceTypeCommonAggregate price = null;
            MonetaryTotalType legalMonetaryTotal = null;
            IDCommonBasic idCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            InvoicedQuantityCommonBasic invoicedQuantityCommonBasic = null;
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
            PayableRoundingAmountCommonBasic payableRoundingAmountCB = null;
            PayableAmountCommonBasic payableAmountB = null;

            List<TaxCategoryType> classifiedTaxCategories = null;
            List<TaxTotalType> taxTotals = null;
            List<AllowanceChargeType> allowanceCharges = null;
            List<TaxCategoryType> taxCategories = null;

            for (InvoiceLineType invoiceLine : invoiceLineItems) {

                invoiceLineItemDTO = new InvoiceLineItemDTO();

                idCommonBasic = invoiceLine.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setId(idCommonBasic.getValue());
                }

                if (invoiceLine.getNotes().size() > 0) {
                    noteCommonBasic = invoiceLine.getNotes().get(0);
                    if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setNote(noteCommonBasic.getValue());
                    }
                }

                itemCommonAggregate = invoiceLine.getItem();
                if (itemCommonAggregate != null) {

                    if (itemCommonAggregate.getOriginCountry() != null) {
                        CountryType countryType = itemCommonAggregate.getOriginCountry();
                        NameCommonBasic name = countryType.getName();
                        if (name != null) {
                            invoiceLineItemDTO.setOriginCountry(name.getValue());
                        }
                    }

                    if(itemCommonAggregate.getOriginCountry() != null && itemCommonAggregate.getOriginCountry().getName() != null) {
                        invoiceLineItemDTO.setOriginCountry(itemCommonAggregate.getOriginCountry().getName().getValue());
                    }

                    if(itemCommonAggregate.getManufacturerParties() != null && itemCommonAggregate.getManufacturerParties().size() > 0) {

                        ManufacturerPartyDTO manufacturerPartyDTO = new ManufacturerPartyDTO();
                        PartyTypeCommonAggregate partyType = itemCommonAggregate.getManufacturerParties().get(0);
                        PartyNameCommonAggregate partyName = partyType.getPartyNames().get(0);
                        manufacturerPartyDTO.setPartyName(partyName.getName().getValue());

                        if(partyType.getPartyLegalEntities() != null && partyType.getPartyLegalEntities().size() > 0) {
                            PartyLegalEntityCommonAggregate partyLegalEntity = partyType.getPartyLegalEntities().get(0);
                            CompanyIDCommonBasic companyId = partyLegalEntity.getCompanyID();
                            manufacturerPartyDTO.setCompanyId(companyId.getValue());
                        }
                        invoiceLineItemDTO.setManufacturerPartyDTO(manufacturerPartyDTO);
                    }

                    itemIdentificationType = itemCommonAggregate.getSellersItemIdentification();
                    if (itemIdentificationType != null) {
                        idCommonBasic = itemIdentificationType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setProductNo(idCommonBasic.getValue());
                        }
                    }
                }

                if(invoiceLine.getInvoicePeriods() != null && invoiceLine.getInvoicePeriods().size() > 0) {
                    PeriodType periodType = invoiceLine.getInvoicePeriods().get(0);
                    if (periodType.getStartDate() != null) {
                        invoiceLineItemDTO.setPeriodStartDate(ConversionUtils.asDate(periodType.getStartDate().getValue()));
                    } 

                    if (periodType.getEndDate() != null) {
                        invoiceLineItemDTO.setPeriodEndDate(ConversionUtils.asDate(periodType.getEndDate().getValue()));
                    }
                }

                nameCommonBasic = itemCommonAggregate.getName();
                if (nameCommonBasic != null) {
                    invoiceLineItemDTO.setProductName(nameCommonBasic.getValue());
                }

                accountingCostCommonBasic = invoiceLine.getAccountingCost();
                if (accountingCostCommonBasic != null
                        && accountingCostCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setAccountingCode(accountingCostCommonBasic.getValue());
                }

                invoicedQuantityCommonBasic = invoiceLine.getInvoicedQuantity();
                if (invoicedQuantityCommonBasic != null) {
                    invoiceLineItemDTO.setUnitCode(invoicedQuantityCommonBasic.getUnitCode());
                }

                classifiedTaxCategories = itemCommonAggregate.getClassifiedTaxCategories();
                for (TaxCategoryType taxCategoryType : classifiedTaxCategories) {
                    
                    if (taxCategoryType.getID() != null) {
                        invoiceLineItemDTO.setTaxCode(taxCategoryType.getID().getValue());
                    }

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

                List<DescriptionCommonBasic> descriptions = itemCommonAggregate.getDescriptions();
                if (descriptions != null) {
                    for (DescriptionCommonBasic description : descriptions) {
                        if (description != null) {
                            invoiceLineItemDTO.setDescription(description.getValue());
                        }
                    }
                }

                price = invoiceLine.getPrice();
                if (price != null) {
                    priceAmountCommonBasic = price.getPriceAmount();
                    if (priceAmountCommonBasic != null && priceAmountCommonBasic.getValue() != null) {
                        invoiceLineItemDTO.setUnitPrice(priceAmountCommonBasic.getValue().doubleValue());
                    }
                }

                invoicedQuantityCommonBasic = invoiceLine.getInvoicedQuantity();
                if (invoicedQuantityCommonBasic != null
                        && invoicedQuantityCommonBasic.getValue() != null) {
                    invoiceLineItemDTO.setQuantity(invoicedQuantityCommonBasic.getValue().doubleValue());
                }

                lineExtensionAmount = invoiceLine.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    invoiceLineItemDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                    invoiceLineItemDTO.setTotalAmount(lineExtensionAmount.getValue().doubleValue());
                }

                taxTotals = invoiceLine.getTaxTotals();
                if (taxTotals != null) {

                    for (TaxTotalType taxTotalCommonAggregate : taxTotals) {
                        taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
                        if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                            invoiceLineItemDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                            invoiceLineItemDTO.setTotalAmount(invoiceLineItemDTO.getTotalExcTax() + invoiceLineItemDTO.getTaxAmount());
                        }
                    }
                }

                allowanceCharges = invoiceLine.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    for (AllowanceChargeType allowanceChargeType : allowanceCharges) {

                        chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();

                        amountCommonBasic = allowanceChargeType.getAmount();
                        if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                            allowanceChargeDTO = new AllowanceChargeDTO();
                            allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                            if (allowanceChargeType.getAllowanceChargeReasons().size() > 0) {
                                allowanceChargeReasonCommonBasic =
                                        allowanceChargeType.getAllowanceChargeReasons().get(0);
                                allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeReasonCommonBasic.getValue());
                            }
                            allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                            taxCategories = allowanceChargeType.getTaxCategories();
                            if (taxCategories != null && !taxCategories.isEmpty()) {

                                taxCategory = taxCategories.get(0);
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

                mapEHFV2DeliveryAddress(invoice, invoiceLineItemDTO);
                invoiceDTO.getInvoiceLineItems().add(invoiceLineItemDTO);
            }

            legalMonetaryTotal = invoice.getLegalMonetaryTotal();
            if (legalMonetaryTotal != null) {

                taxExclusiveAmount = legalMonetaryTotal.getTaxExclusiveAmount();
                if (taxExclusiveAmount != null && taxExclusiveAmount.getValue() != null) {
                    invoiceDTO.setTotalExcTax(taxExclusiveAmount.getValue().doubleValue());
                }

                taxInclusiveAmountCommonBasic = legalMonetaryTotal.getTaxInclusiveAmount();
                if (taxInclusiveAmountCommonBasic != null
                        && taxInclusiveAmountCommonBasic.getValue() != null) {
                    invoiceDTO.setTotalAmount(taxInclusiveAmountCommonBasic.getValue().doubleValue());
                }

                payableRoundingAmountCB = legalMonetaryTotal.getPayableRoundingAmount();
                if (payableRoundingAmountCB != null && payableRoundingAmountCB.getValue() != null) {
                    invoiceDTO.setPayableRoundingAmount(payableRoundingAmountCB.getValue().doubleValue());
                } else {
                    invoiceDTO.setPayableRoundingAmount(0.0);
                }

                payableAmountB = legalMonetaryTotal.getPayableAmount();
                if (payableAmountB != null && payableAmountB.getValue() != null) {
                    invoiceDTO.setPayableAmount(payableAmountB.getValue().doubleValue());
                }
            }
        }
    }

    /**
     * Method used to map tax summary details from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2TaxSummaries(Invoice invoice, InvoiceDTO invoiceDTO) {

        List<TaxTotalType> taxTotals = invoice.getTaxTotals();
        List<TaxSummaryDTO> taxSummaries = new ArrayList<TaxSummaryDTO>();
        List<TaxSubtotalCommonAggregate> taxSubtotals = null;

        TaxSummaryDTO taxSummary = null;

        IDCommonBasic idCommonBasic = null;
        TaxableAmountCommonBasic taxableAmountCommonBasic = null;
        PercentCommonBasic percentCommonBasic = null;
        TaxAmountCommonBasic taxAmountCommonBasic = null;
        TaxCategoryType taxCategoryType = null;
        TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
        TransactionCurrencyTaxAmountCommonBasic transactionCurrency = null;

        for (TaxTotalType taxTotalCommonAggregate : taxTotals) {

            taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
            if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                invoiceDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
            }

            taxSubtotals = taxTotalCommonAggregate.getTaxSubtotals();
            for (TaxSubtotalCommonAggregate taxSubtotalCommonAggregate : taxSubtotals) {

                taxSummary = new TaxSummaryDTO();

                taxableAmountCommonBasic = taxSubtotalCommonAggregate.getTaxableAmount();
                if (taxableAmountCommonBasic != null && taxableAmountCommonBasic.getValue() != null) {
                    taxSummary.setTotalExcTax(taxableAmountCommonBasic.getValue().doubleValue());
                }

                taxAmountCommonBasic = taxSubtotalCommonAggregate.getTaxAmount();
                if (taxableAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                    taxSummary.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                }

                transactionCurrency = taxSubtotalCommonAggregate.getTransactionCurrencyTaxAmount();
                if (transactionCurrency != null && transactionCurrency.getValue() != null) {
                    taxSummary.setTransactionCurrencyTaxAmount(transactionCurrency.getValue().doubleValue());
                }

                taxCategoryType = taxSubtotalCommonAggregate.getTaxCategory();
                if (taxCategoryType != null) {

                    if (taxCategoryType.getID() != null) {
                        taxSummary.setTaxCode(taxCategoryType.getID().getValue());
                    }

                    percentCommonBasic = taxCategoryType.getPercent();
                    if (percentCommonBasic != null && percentCommonBasic.getValue() != null) {
                        taxSummary.setTaxPercent(percentCommonBasic.getValue().doubleValue());
                         if(percentCommonBasic.getValue().doubleValue() == 0 ) {
                             if (taxCategoryType.getTaxExemptionReasons().size() > 0) {
                                 taxSummary.setTaxExcemptionReasion(taxCategoryType.getTaxExemptionReasons().get(0) != null 
                                    ? taxCategoryType.getTaxExemptionReasons().get(0).getValue() : null);
                             }
                                taxSummary.setTaxExcemptionReasionCode(taxCategoryType.getTaxExemptionReasonCode() != null
                                    ? taxCategoryType.getTaxExemptionReasonCode().getValue() : null);
                        }
                    }

                    taxSchemeCommonAggregate = taxCategoryType.getTaxScheme();
                    if (taxSchemeCommonAggregate != null) {
                        idCommonBasic = taxSchemeCommonAggregate.getID();
                        if (idCommonBasic != null) {
                            taxSummary.setTaxTypeIntName(idCommonBasic.getValue());
                        }
                    }
                }
                taxSummaries.add(taxSummary);
            }
        }
        invoiceDTO.setTaxSummaries(taxSummaries);
    }

    private static void mapEHFV2PaymentTerms(Invoice invoice, InvoiceDTO invoiceDTO) {

        InvoiceSettingDTO invoiceSettingDTO = null;
        List<InvoiceSettingDTO> invoiceSettingDTOs = null;
        List<NoteCommonBasic> notes = null;

        List<PaymentTermsType> paymentTerms = invoice.getPaymentTerms();
        if (paymentTerms != null && !paymentTerms.isEmpty()) {
            invoiceSettingDTOs = new ArrayList<InvoiceSettingDTO>(paymentTerms.size());

            for (PaymentTermsType paymentTermsCommonAggregate : paymentTerms) {
                invoiceSettingDTO = new InvoiceSettingDTO();
                notes = paymentTermsCommonAggregate.getNotes();
                if (notes != null && !notes.isEmpty()) {
                    for (NoteCommonBasic noteCommonBasic : notes) {
                        if (noteCommonBasic.getValue() != null) {
                            invoiceSettingDTO.getPaymentTermsNotes().add(noteCommonBasic.getValue());
                        }
                    }
                }
                invoiceSettingDTOs.add(invoiceSettingDTO);
            }
            invoiceDTO.getInvoiceSettingDTOs().addAll(invoiceSettingDTOs);
        }
    }

    /**
     * Method used to map allowance charges from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV2AllowanceCharges(Invoice invoice, InvoiceDTO invoiceDTO) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeDTO allowanceChargeDTO = null;
        List<TaxCategoryType> taxCategories = null;
        TaxCategoryType taxCategoryType = null;
        PercentCommonBasic percentCommonBasic = null;
        TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        List<AllowanceChargeType> allowanceCharges = invoice.getAllowanceCharges();

        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            for (AllowanceChargeType allowanceChargeType : allowanceCharges) {

                chargeIndicatorCommonBasic = allowanceChargeType.getChargeIndicator();
                if (allowanceChargeType.getAllowanceChargeReasons().size() > 0) {
                    allowanceChargeReasonCommonBasic =
                            allowanceChargeType.getAllowanceChargeReasons().get(0);
                }
                amountCommonBasic = allowanceChargeType.getAmount();
                if (amountCommonBasic != null && amountCommonBasic.getValue() != null) {

                    allowanceChargeDTO = new AllowanceChargeDTO();
                    if (chargeIndicatorCommonBasic != null) {
                        allowanceChargeDTO.setChargeIndicator(chargeIndicatorCommonBasic.isValue());
                    }
                    if (allowanceChargeReasonCommonBasic != null) {
                        allowanceChargeDTO.setAllowanceChargeReason(allowanceChargeReasonCommonBasic.getValue());
                    }

                    allowanceChargeDTO.setAmount(amountCommonBasic.getValue().doubleValue());

                    taxCategories = allowanceChargeType.getTaxCategories();
                    if (taxCategories != null && !taxCategories.isEmpty()) {

                        taxCategoryType = taxCategories.get(0);
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
                    invoiceDTO.getAllowanceCharges().add(allowanceChargeDTO);
                }
            }
        }
    }

    private static void mapEHFV2TaxRepresentativeParty(Invoice invoice, InvoiceDTO invoiceDTO) {

        PartyTypeCommonAggregate partyType = invoice.getTaxRepresentativeParty();
        TaxRepresentativeDTO taxRepresentativeDTO = null;

        if (partyType != null) {

            taxRepresentativeDTO = new TaxRepresentativeDTO();
            if(partyType.getPartyNames() != null && partyType.getPartyNames().size() > 0) {
                PartyNameCommonAggregate partyNameCommonAggregate =  partyType.getPartyNames().get(0);
                taxRepresentativeDTO.setName(partyNameCommonAggregate.getName().getValue());
            }
            if(partyType.getPartyTaxSchemes() != null && partyType.getPartyTaxSchemes().size() > 0) {
                PartyTaxSchemeCommonAggregate partyTaxScheme = partyType.getPartyTaxSchemes().get(0);

                if (partyTaxScheme != null && (partyTaxScheme.getCompanyID()!= null)) {
                    taxRepresentativeDTO.setOrganisationNo(partyTaxScheme.getCompanyID().getValue());
                }

                if (partyTaxScheme != null && partyTaxScheme.getTaxScheme() != null) {
                    taxRepresentativeDTO.setTaxTypeIntName(partyTaxScheme.getTaxScheme().getID().getValue());
                }
            }
            if (partyType.getPostalAddress() != null) {
                AddressType postalAddress = partyType.getPostalAddress();
                AddressDTO addressDTO = new AddressDTO();
                if(postalAddress.getStreetName() != null) {
                    addressDTO.setStreetName(postalAddress.getStreetName().getValue());
                }
                if(postalAddress.getAdditionalStreetName() != null) {
                    addressDTO.setAdditionalStreetName(postalAddress.getAdditionalStreetName().getValue());
                }
                if(postalAddress.getCityName() != null) {
                    addressDTO.setCityName(postalAddress.getCityName().getValue());
                }
                if(postalAddress.getPostalZone() != null) {
                    addressDTO.setPostalZone(postalAddress.getPostalZone().getValue());
                }
                if(postalAddress.getCountrySubentityCode() != null) {
                    addressDTO.setCountrySubentityCode(postalAddress.getCountrySubentityCode().getValue());
                }
                if(postalAddress.getCountry() != null && postalAddress.getCountry().getName() != null) {
                    addressDTO.setCountryCode(postalAddress.getCountry().getName().getValue());
                }
                taxRepresentativeDTO.setPostalAddress(addressDTO);
            }
        }
        invoiceDTO.setTaxRepresentation(taxRepresentativeDTO);
    }
}
