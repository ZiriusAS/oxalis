/*
 * @(#)CreditNoteUtils.java
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

import ehandel.no.ehf.creditnote.PaymentIDCommonBasic;
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
import ehandel.no.dto.BillingReferenceDTO;
import ehandel.no.dto.ContactPersonDTO;
import ehandel.no.dto.ContractDTO;
import ehandel.no.dto.CurrencyDTO;
import ehandel.no.dto.CustomerDTO;
import ehandel.no.dto.DeliveryDTO;
import ehandel.no.dto.FileDTO;
import ehandel.no.dto.InvoiceDTO;
import ehandel.no.dto.InvoiceLineItemDTO;
import ehandel.no.dto.ManufacturerPartyDTO;
import ehandel.no.dto.SupplierDTO;
import ehandel.no.dto.TaxRepresentativeDTO;
import ehandel.no.dto.TaxSummaryDTO;
import ehandel.no.ehf.creditnote.AccountingCostCommonBasic;
import ehandel.no.ehf.creditnote.ActualDeliveryDateCommonBasic;
import ehandel.no.ehf.creditnote.AdditionalStreetNameCommonBasic;
import ehandel.no.ehf.creditnote.AddressType;
import ehandel.no.ehf.creditnote.AllowanceChargeReasonCommonBasic;
import ehandel.no.ehf.creditnote.AllowanceChargeType;
import ehandel.no.ehf.creditnote.AllowanceTotalAmountCommonBasic;
import ehandel.no.ehf.creditnote.AmountCommonBasic;
import ehandel.no.ehf.creditnote.AttachmentType;
import ehandel.no.ehf.creditnote.BaseQuantityCommonBasic;
import ehandel.no.ehf.creditnote.BillingReferenceCommonAggregate;
import ehandel.no.ehf.creditnote.BillingReferenceLineCommonAggregate;
import ehandel.no.ehf.creditnote.BranchType;
import ehandel.no.ehf.creditnote.BuildingNumberCommonBasic;
import ehandel.no.ehf.creditnote.CalculationRateCommonBasic;
import ehandel.no.ehf.creditnote.ChargeIndicatorCommonBasic;
import ehandel.no.ehf.creditnote.ChargeTotalAmountCommonBasic;
import ehandel.no.ehf.creditnote.CityNameCommonBasic;
import ehandel.no.ehf.creditnote.CompanyIDCommonBasic;
import ehandel.no.ehf.creditnote.ContactType;
import ehandel.no.ehf.creditnote.CountrySubentityCodeCommonBasic;
import ehandel.no.ehf.creditnote.CountryType;
import ehandel.no.ehf.creditnote.CreditNote;
import ehandel.no.ehf.creditnote.CreditNoteLineType;
import ehandel.no.ehf.creditnote.CreditNoteTypeCodeCommonBasic;
import ehandel.no.ehf.creditnote.CreditedQuantityCommonBasic;
import ehandel.no.ehf.creditnote.CustomerPartyType;
import ehandel.no.ehf.creditnote.CustomizationIDCommonBasic;
import ehandel.no.ehf.creditnote.DeliveryType;
import ehandel.no.ehf.creditnote.DescriptionCommonBasic;
import ehandel.no.ehf.creditnote.DocumentCurrencyCodeCommonBasic;
import ehandel.no.ehf.creditnote.DocumentDescriptionCommonBasic;
import ehandel.no.ehf.creditnote.DocumentReferenceType;
import ehandel.no.ehf.creditnote.DocumentTypeCodeCommonBasic;
import ehandel.no.ehf.creditnote.DocumentTypeCommonBasic;
import ehandel.no.ehf.creditnote.ElectronicMailCommonBasic;
import ehandel.no.ehf.creditnote.EmbeddedDocumentBinaryObjectCommonBasic;
import ehandel.no.ehf.creditnote.EndDateCommonBasic;
import ehandel.no.ehf.creditnote.EndpointIDCommonBasic;
import ehandel.no.ehf.creditnote.ExchangeRateType;
import ehandel.no.ehf.creditnote.FinancialAccountType;
import ehandel.no.ehf.creditnote.FinancialInstitutionCommonAggregate;
import ehandel.no.ehf.creditnote.IDCommonBasic;
import ehandel.no.ehf.creditnote.IdentificationCodeCommonBasic;
import ehandel.no.ehf.creditnote.IssueDateCommonBasic;
import ehandel.no.ehf.creditnote.ItemIdentificationType;
import ehandel.no.ehf.creditnote.ItemType;
import ehandel.no.ehf.creditnote.LineExtensionAmountCommonBasic;
import ehandel.no.ehf.creditnote.LocationType;
import ehandel.no.ehf.creditnote.MathematicOperatorCodeCommonBasic;
import ehandel.no.ehf.creditnote.MonetaryTotalType;
import ehandel.no.ehf.creditnote.NameCommonBasic;
import ehandel.no.ehf.creditnote.NoteCommonBasic;
import ehandel.no.ehf.creditnote.OrderReferenceCommonAggregate;
import ehandel.no.ehf.creditnote.PartyIdentificationCommonAggregate;
import ehandel.no.ehf.creditnote.PartyLegalEntityCommonAggregate;
import ehandel.no.ehf.creditnote.PartyNameCommonAggregate;
import ehandel.no.ehf.creditnote.PartyTaxSchemeCommonAggregate;
import ehandel.no.ehf.creditnote.PartyTypeCommonAggregate;
import ehandel.no.ehf.creditnote.PayableAmountCommonBasic;
import ehandel.no.ehf.creditnote.PayableRoundingAmountCommonBasic;
import ehandel.no.ehf.creditnote.PaymentDueDateCommonBasic;
import ehandel.no.ehf.creditnote.PaymentMeansCodeCommonBasic;
import ehandel.no.ehf.creditnote.PaymentMeansCommonAggregate;
import ehandel.no.ehf.creditnote.PercentCommonBasic;
import ehandel.no.ehf.creditnote.PeriodType;
import ehandel.no.ehf.creditnote.PostalZoneCommonBasic;
import ehandel.no.ehf.creditnote.PostboxCommonBasic;
import ehandel.no.ehf.creditnote.PriceAmountCommonBasic;
import ehandel.no.ehf.creditnote.PriceTypeCommonAggregate;
import ehandel.no.ehf.creditnote.ProfileIDCommonBasic;
import ehandel.no.ehf.creditnote.RegistrationNameCommonBasic;
import ehandel.no.ehf.creditnote.SourceCurrencyBaseRateCommonBasic;
import ehandel.no.ehf.creditnote.SourceCurrencyCodeCommonBasic;
import ehandel.no.ehf.creditnote.StartDateCommonBasic;
import ehandel.no.ehf.creditnote.StreetNameCommonBasic;
import ehandel.no.ehf.creditnote.SupplierPartyType;
import ehandel.no.ehf.creditnote.TargetCurrencyBaseRateCommonBasic;
import ehandel.no.ehf.creditnote.TargetCurrencyCodeCommonBasic;
import ehandel.no.ehf.creditnote.TaxAmountCommonBasic;
import ehandel.no.ehf.creditnote.TaxCategoryType;
import ehandel.no.ehf.creditnote.TaxCurrencyCodeCommonBasic;
import ehandel.no.ehf.creditnote.TaxExclusiveAmountCommonBasic;
import ehandel.no.ehf.creditnote.TaxExemptionReasonCodeCommonBasic;
import ehandel.no.ehf.creditnote.TaxExemptionReasonCommonBasic;
import ehandel.no.ehf.creditnote.TaxInclusiveAmountCommonBasic;
import ehandel.no.ehf.creditnote.TaxSchemeCommonAggregate;
import ehandel.no.ehf.creditnote.TaxSubtotalCommonAggregate;
import ehandel.no.ehf.creditnote.TaxTotalType;
import ehandel.no.ehf.creditnote.TaxableAmountCommonBasic;
import ehandel.no.ehf.creditnote.TelefaxCommonBasic;
import ehandel.no.ehf.creditnote.TelephoneCommonBasic;
import ehandel.no.ehf.creditnote.TransactionCurrencyTaxAmountCommonBasic;
import ehandel.no.ehf.creditnote.UBLVersionIDCommonBasic;
import ehandel.no.ehf.creditnote.WebsiteURICommonBasic;
import ehandel.no.ehf.creditnote.BuyerReferenceCommonBasic;
import ehandel.no.ehf.creditnote.LineIDCommonBasic;
import ehandel.no.ehf.creditnote.OrderLineReferenceCommonAggregate;
import ehandel.no.ehf.creditnote.PaymentTermsType;
import ehandel.no.ehf.creditnote.ProjectReferenceCommonAggregate;
import java.util.stream.Collectors;
import javax.xml.bind.PropertyException;

/**
 * The Class CreditNoteUtils.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public final class UblCreditNoteUtils {

    private static String taxTypeIntName = "";
    
    private static JAXBContext context;
    
    public static JAXBContext getJAXBContext() throws Exception {
        if (context == null) {
            context = JAXBContext.newInstance(CreditNote.class);
        }
        return context;
    }

    public UblCreditNoteUtils() {
    }

    /**
     * Method to get the marshaller for CreditNote.
     */
    private static Marshaller getEHFV3Marshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.ehf.creditnote.CreditNote.class);

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
     * Marshalling creditnote object to XML file.
     */
    public static void generateEHFV3InvoiceXML(InvoiceDTO invoiceDTO, String filePath)
            throws Throwable {

        CreditNote creditNote = convertToEHFV3CreditNote(invoiceDTO);

        Marshaller marshaller = getEHFV3Marshaller();

        marshaller.marshal(creditNote, new File(filePath));
    }

    /**
     * Marshalling creditnote object to byte array.
     */
    public static byte[] generateEHFV3InvoiceXML(InvoiceDTO invoiceDTO) throws JAXBException {

        CreditNote creditNote = convertToEHFV3CreditNote(invoiceDTO);

        Marshaller marshaller = getEHFV3Marshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(creditNote, out);

        return out.toByteArray();
    }

    /**
     * Method used to convert CreditNote wrapper to EHF creditNote.
     */
    private static CreditNote convertToEHFV3CreditNote(InvoiceDTO invoiceDTO) {

        CreditNote creditNote = null;
        if (invoiceDTO != null) {

            creditNote = new CreditNote();

            UBLVersionIDCommonBasic ublVersionIDCommonBasic = new UBLVersionIDCommonBasic();
            ublVersionIDCommonBasic.setValue(EHFConstants.UBL_VERSION_ID_TWO_DOT_ONE.getValue());
            creditNote.setUBLVersionID(ublVersionIDCommonBasic);

            // Initialize customization id and profile id
            CustomizationIDCommonBasic customizationIDCommonBasic =
                    new CustomizationIDCommonBasic();
             ProfileIDCommonBasic profileIDCommonBasic = new ProfileIDCommonBasic();
            // Set customization id and profile id
            if(invoiceDTO.getIsCreditNoteAgaintInvoice()) {
                customizationIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_CUSTOMIZATION_ID.getValue());
                profileIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue());
            } else {
                customizationIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_CUSTOMIZATION_ID.getValue());
                profileIDCommonBasic.setValue(EHFConstants.EHF_THREE_DOT_ZERO_PROFILE_ID.getValue());
            }
            creditNote.setCustomizationID(customizationIDCommonBasic);
            creditNote.setProfileID(profileIDCommonBasic);

            CreditNoteTypeCodeCommonBasic creditNoteType = new CreditNoteTypeCodeCommonBasic();
            creditNoteType.setValue(EHFConstants.CREDIT_NOTE_TYPE.getValue());
            creditNote.setCreditNoteTypeCode(creditNoteType);

            // Set creditNote no
            IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(invoiceDTO.getInvoiceNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getInvoiceNo());
                creditNote.setID(idCommonBasic);
            }

            // Set issue date, tax point date and creditNote period
            if (invoiceDTO.getIssueDate() != null) {
                IssueDateCommonBasic issueDateCommonBasic = new IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getIssueDate()));
                creditNote.setIssueDate(issueDateCommonBasic);
            }

            if (invoiceDTO.getPeriodStartDate() != null) {

                PeriodType periodType = new PeriodType();
                StartDateCommonBasic startDateCommonBasic = new StartDateCommonBasic();
                startDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPeriodStartDate()));
                periodType.setStartDate(startDateCommonBasic);

                if (invoiceDTO.getPeriodEndDate() != null) {
                    EndDateCommonBasic endDateCommonBasic = new EndDateCommonBasic();
                    endDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPeriodEndDate()));
                    periodType.setEndDate(endDateCommonBasic);
                }
            }                

            // Set order reference id
            if (!StringUtils.isEmpty(invoiceDTO.getPurchaseOrderNo())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(invoiceDTO.getPurchaseOrderNo());
                OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                        new OrderReferenceCommonAggregate();
                orderReferenceCommonAggregate.setID(idCommonBasic);
                creditNote.setOrderReference(orderReferenceCommonAggregate);
            }
            
            // Buyer Reference
            if (!StringUtils.isEmpty(invoiceDTO.getBuyerReference())) {
                
                BuyerReferenceCommonBasic buyerRef = new BuyerReferenceCommonBasic();
                buyerRef.setValue(invoiceDTO.getBuyerReference());
                creditNote.setBuyerReference(buyerRef);
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
                
                creditNote.getContractDocumentReferences().add(documentReferenceType);
            }

            if(invoiceDTO.getBillingReferenceDTOs() != null && invoiceDTO.getBillingReferenceDTOs().size() > 0) {

                BillingReferenceCommonAggregate billingRef = null;
                for (BillingReferenceDTO billingReferenceDTO : invoiceDTO.getBillingReferenceDTOs()) {

                    billingRef = new BillingReferenceCommonAggregate();
                    DocumentReferenceType documentRef = new DocumentReferenceType();
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(billingReferenceDTO.getInvoiceDocumentReference());
                    documentRef.setID(idCommonBasic);
                    billingRef.setInvoiceDocumentReference(documentRef);
                    creditNote.getBillingReferences().add(billingRef);
                }
            }

            List<FileDTO> files = invoiceDTO.getFiles();
            if (files != null && !files.isEmpty()) {

                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCommonBasic = null;
                DocumentReferenceType documentReferenceType = null;
                DocumentTypeCommonBasic documentTypeCommonBasic = null;
                AttachmentType attachmentType = null;

                for (FileDTO fileDTO : files) {

                    embeddedDocBinObjCommonBasic = new EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(fileDTO.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(fileDTO.getMimeType());
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode("application/pdf");
                    }
                    embeddedDocBinObjCommonBasic.setFilename(invoiceDTO.getInvoiceNo() + "_" + files.indexOf(fileDTO));
                    embeddedDocBinObjCommonBasic.setValue(fileDTO.getFileContent());

                    documentReferenceType = new DocumentReferenceType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(EHFConstants.DOCUMENT.getValue() + 1);
                    documentReferenceType.setID(idCommonBasic);

                    if (!StringUtils.isEmpty(fileDTO.getFileName())) {

                        DocumentDescriptionCommonBasic description = new DocumentDescriptionCommonBasic();
                        description.setValue(fileDTO.getFileName());
                        documentReferenceType.getDocumentDescriptions().add(description);
                    }                    

                    attachmentType = new AttachmentType();
                    attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocBinObjCommonBasic);
                    documentReferenceType.setAttachment(attachmentType);
                    
                    if (fileDTO.getDocumentTypeCode() != null && !fileDTO.getDocumentTypeCode().equals("130")) {                        
                        DocumentTypeCodeCommonBasic documentTypeCode = new DocumentTypeCodeCommonBasic();
                        documentTypeCode.setValue(fileDTO.getDocumentTypeCode());
                        documentReferenceType.setDocumentTypeCode(documentTypeCode);
                    }

                    creditNote.getAdditionalDocumentReferences().add(documentReferenceType);
                }
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
                    creditNote.getNotes().add(noteCommonBasic);
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

            mapEHFV3Supplier(invoiceDTO, creditNote);
            mapEHFV3Customer(invoiceDTO, creditNote);
            mapEHFV3CustomerBankAccount(invoiceDTO, creditNote);
            mapEHFV3Currency(invoiceDTO, creditNote);
            mapEHFV3DeliveryAddress(invoiceDTO, creditNote);
            mapEHFV3ExchangeRate(invoiceDTO, creditNote);
            mapEHFV3AllowanceCharges(invoiceDTO, creditNote);
            mapEHFV3InvoiceLineItems(invoiceDTO, creditNote);
            mapEHFV3TaxSummaries(invoiceDTO, creditNote);
           // mapEHFV3TaxRepresentativeParty(invoiceDTO, creditNote);
        }
        
        return creditNote;
    }

    /**
     * Method used to map supplier information from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3Supplier(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        SupplierDTO supplierDTO = invoiceDTO.getSupplierDTO();
        
        if (supplierDTO != null) {

            SupplierPartyType supplierPartyType = new SupplierPartyType();
            PartyTypeCommonAggregate party = new PartyTypeCommonAggregate();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();

            IDCommonBasic idCommonBasic = null;

            String country = "";

            boolean isPartyLegalEntity = false;

            //set contact name
            ContactPersonDTO contactPersonDTO = supplierDTO.getContactPersonDTO();
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
                supplierPartyType.setSellerContact(contactType);
            }
            
            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {
                
                EndpointIDCommonBasic endpointId = new EndpointIDCommonBasic();
                endpointId.setValue(supplierDTO.getOrganizationNo());
                endpointId.setSchemeID(supplierDTO.getEaID());
                party.setEndpointID(endpointId);
            }

            if (!StringUtils.isEmpty(supplierDTO.getId())) {

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(supplierDTO.getId());

                PartyIdentificationCommonAggregate partyIdentificationCommonAggregate = new PartyIdentificationCommonAggregate();
                partyIdentificationCommonAggregate.setID(idCommonBasic);
                party.getPartyIdentifications().add(partyIdentificationCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getName())) {
                NameCommonBasic nameCommonBasic = new NameCommonBasic();
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

            AddressDTO addressDTO = supplierDTO.getAddressDTO();

            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                AddressType postalAddress = new AddressType();

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
                    country = addressDTO.getCountryCode().toUpperCase();
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(country);
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    postalAddress.setCountry(countryType);
                    isAddressAvailable = true;
                }

                if (isAddressAvailable) {
                    party.setPostalAddress(postalAddress);
                }
            }

            PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new PartyTaxSchemeCommonAggregate();
            CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(country + removeCountryCodeAndMVAFromEndPointId(supplierDTO.getOrganizationNo()) + EHFConstants.MVA.getValue()));
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {
                
                List<TaxSummaryDTO> taxSummaries = invoiceDTO.getTaxSummaries();
                List<String> taxCodes = new ArrayList<>();
                if (taxSummaries != null) {
                    taxCodes = taxSummaries.stream().map(TaxSummaryDTO::getTaxCode).collect(Collectors.toList());
                }

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                idCommonBasic = new IDCommonBasic();

                if (taxCodes.contains("O") || taxCodes.contains("o")) {
                    idCommonBasic.setValue(EHFConstants.ORG.getValue());
                } else {
                    idCommonBasic.setValue(EHFConstants.VAT.getValue());
                }

                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable  /*&& !invoiceDTO.getIsInvoiceCompanyTaxFree()*/) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
                
                if (invoiceDTO.getRegisterInBusinessEnterprises()) {  // For Foretaksregisteret

                    PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate_1 = new PartyTaxSchemeCommonAggregate();

                    CompanyIDCommonBasic companyIDCommonBasic_1 = new CompanyIDCommonBasic();
                    companyIDCommonBasic_1.setValue("Foretaksregisteret");
                    partyTaxSchemeCommonAggregate_1.setCompanyID(companyIDCommonBasic_1);

                    TaxSchemeCommonAggregate taxSchemeCommonAggregate_1 = new TaxSchemeCommonAggregate();

                    IDCommonBasic idCommonBasicTax_1 = new IDCommonBasic();
                    idCommonBasicTax_1.setValue("TAX");
                    taxSchemeCommonAggregate_1.setID(idCommonBasicTax_1);
                    partyTaxSchemeCommonAggregate_1.setTaxScheme(taxSchemeCommonAggregate_1);
                    party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate_1);
                }
            }

            if (!StringUtils.isEmpty(supplierDTO.getLegalName())) {

                RegistrationNameCommonBasic registrationNameCB = new RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(supplierDTO.getLegalName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            } else {
                throw new RuntimeException("Supplier party legal name required");
            }

            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(supplierDTO.getOrganizationNo()));                
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }
            
            if (isPartyLegalEntity) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            supplierPartyType.setParty(party);
            creditNote.setAccountingSupplierParty(supplierPartyType);
        }
    }

    /**
     * Method used to map customer information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV3Customer(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            CustomerPartyType customerPartyType = new CustomerPartyType();
            PartyTypeCommonAggregate party = new PartyTypeCommonAggregate();
            PartyTypeCommonAggregate payeePartyType = new PartyTypeCommonAggregate();
            AddressType postalAddress = new AddressType();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();

            boolean isPartyLegalEntity = false;

            //set contact name
            ContactPersonDTO contactPersonDTO = customerDTO.getContactPersonDTO();
            if (contactPersonDTO != null) {

                ContactType contactType = new ContactType();
                NameCommonBasic nameCommonBasic = new NameCommonBasic();
                IDCommonBasic idCommonBasic = new IDCommonBasic();
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
            
            if (!StringUtils.isEmpty(customerDTO.getEndpointId())) {
                EndpointIDCommonBasic endpointIDCB = new EndpointIDCommonBasic();
                endpointIDCB.setSchemeID(customerDTO.getEaID());
                endpointIDCB.setValue(removeCountryCodeAndMVAFromEndPointId(customerDTO.getEndpointId()));
                party.setEndpointID(endpointIDCB);
            }

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

            AddressDTO addressDTO = customerDTO.getAddressDTO();

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
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());

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
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    registrationAddress.setCountry(countryType);
                    isRegistrationAddress = true;
                }
            }

            if (!StringUtils.isEmpty(customerDTO.getLegalName())) {

                RegistrationNameCommonBasic registrationNameCB = new RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(invoiceDTO.getLanguage());
                registrationNameCB.setValue(customerDTO.getLegalName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            } else {
                throw new RuntimeException("Customer party legal name required");
            }

            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {
                CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(ConversionUtils.stripNonAlphaNumeric(customerDTO.getOrganizationNo()));                
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyLegalEntity = true;
            }

            if (isRegistrationAddress) {
                partyLegalEntityCommonAggregate.setRegistrationAddress(registrationAddress);
            }
            if (isPartyLegalEntity || isRegistrationAddress) {
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
                payeePartyType.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            customerPartyType.setParty(party);
            creditNote.setPayeeParty(payeePartyType);
            creditNote.setAccountingCustomerParty(customerPartyType);
            
            if (customerDTO.getPaymentTerms() != null) {

                PaymentTermsType pmtTerm = new PaymentTermsType();
                NoteCommonBasic note = new NoteCommonBasic();
                note.setValue(customerDTO.getPaymentTerms());
                pmtTerm.getNotes().add(note);
                creditNote.getPaymentTerms().add(pmtTerm);
            }
        }
    }

    /**
     * Method used to map customer bank account information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV3CustomerBankAccount(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        CustomerDTO customerDTO = invoiceDTO.getCustomerDTO();

        if (customerDTO != null) {

            BranchType branchType = null;
            FinancialAccountType financialAccountType = null;
            FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate = null;
            IDCommonBasic idCommonBasic = null;
            PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            PaymentMeansCodeCommonBasic paymentMeansCodeCommonBasic = null;

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;
            AddressType bankAddress = null;

            boolean isFinancialInsAvailable = false;
            boolean isFinancialAddressAvailable = false;
            
            for (PaymentMeansCommonAggregate paymentMeansCommonAggregate : creditNote.getPaymentMeans()) {
            
                isFinancialInsAvailable = false;
                isFinancialAddressAvailable = false;

                if (paymentMeansCommonAggregate == null) {
                    paymentMeansCommonAggregate = new PaymentMeansCommonAggregate();
                    creditNote.getPaymentMeans().add(paymentMeansCommonAggregate);
                }

                /*if (invoiceDTO.getPaymentDueDate() != null) {
                    paymentDueDateCommonBasic = new PaymentDueDateCommonBasic();
                    paymentDueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(invoiceDTO.getPaymentDueDate()));
                    paymentMeansCommonAggregate.setPaymentDueDate(paymentDueDateCommonBasic);
                }*/

                paymentMeansCodeCommonBasic = new PaymentMeansCodeCommonBasic();
                paymentMeansCodeCommonBasic.setValue(EHFConstants.PAYMENT_MEANS_CODE.getValue());
                paymentMeansCommonAggregate.setPaymentMeansCode(paymentMeansCodeCommonBasic);

                bankAccountDTO = customerDTO.getBankAccountDTO();
                if (bankAccountDTO != null) {

                    financialAccountType = new FinancialAccountType();

                    idCommonBasic = new IDCommonBasic();

                    if (bankAccountDTO.getiBanNo() != null && !bankAccountDTO.getiBanNo().isEmpty()) {
                        idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                        idCommonBasic.setValue(bankAccountDTO.getiBanNo());
                    } else {
                        idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                        idCommonBasic.setValue(bankAccountDTO.getBankAccountNumber());
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
     * Method used to map currency information from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3Currency(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
        CurrencyDTO baseCurrencyDTO = invoiceDTO.getBaseCurrencyDTO();

        if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
            DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                    new DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
            documentCurrencyCodeCommonBasic.setValue(currencyDTO.getCurrencyCode());
            documentCurrencyCodeCommonBasic.setName(currencyDTO.getCurrencyName());
            creditNote.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);

           if ((baseCurrencyDTO != null) &&
           !(currencyDTO.getCurrencyCode().equalsIgnoreCase(baseCurrencyDTO.getCurrencyCode()))) {
            if (baseCurrencyDTO != null && !StringUtils.isEmpty(baseCurrencyDTO.getCurrencyCode())) {
                TaxCurrencyCodeCommonBasic taxCurrencyCodeCommonBasic =
                        new TaxCurrencyCodeCommonBasic();
                taxCurrencyCodeCommonBasic.setListID(EHFConstants.DOCUMENT_CURRENCY_CODE_ID.getValue());
                taxCurrencyCodeCommonBasic.setValue(baseCurrencyDTO.getCurrencyCode());
                taxCurrencyCodeCommonBasic.setName(baseCurrencyDTO.getCurrencyName());
                creditNote.setTaxCurrencyCode(taxCurrencyCodeCommonBasic);
            }
        }
        }
    }

    /**
     * Method used to map creditNote exchange rate from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3ExchangeRate(InvoiceDTO invoiceDTO, CreditNote creditNote) {

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
            creditNote.setTaxExchangeRate(exchangeRateType);
        }
    }

    /**
     * Method used to map creditNote line item details from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3InvoiceLineItems(InvoiceDTO invoiceDTO, CreditNote creditNote) {
        List<InvoiceLineItemDTO> creditNoteLineItemDTOs = invoiceDTO.getInvoiceLineItems();

        if (creditNoteLineItemDTOs != null) {

            ItemType item = null;
            CreditNoteLineType creditNoteLine = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            TaxTotalType taxTotalCommonAggregate = null;
            ItemIdentificationType sellersItemIdentification = null;
            TaxCategoryType taxCategoryType = null;
            PriceTypeCommonAggregate price = null;
            MonetaryTotalType monetaryTotalType = null;
            NameCommonBasic nameCommonBasic = null;
            IDCommonBasic idCommonBasic = null;
            CreditedQuantityCommonBasic creditedQuantityCommonBasic = null;
            PayableAmountCommonBasic payableAmountCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionTaxAmount = null;
            LineExtensionAmountCommonBasic lineExtensionTotalAmount = null;
            TaxExclusiveAmountCommonBasic taxExclusiveAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            BaseQuantityCommonBasic baseQuantityCommonBasic = null;
            PayableRoundingAmountCommonBasic payableRoundingAmountCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;
            ItemIdentificationType buyersItemIdentification = null;
            OrderLineReferenceCommonAggregate orderLineReferenceCommonAggregate = null;
            LineIDCommonBasic lineIDCommonBasic = null;

            String currencyCode = null;
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            int i=0;
            for (InvoiceLineItemDTO creditNoteLineItemDTO : creditNoteLineItemDTOs) {

                if (creditNoteLineItemDTO.getTotalExcTax() == null) {
                    continue;
                }
                creditNoteLine = new CreditNoteLineType();
                
                lineIDCommonBasic = new LineIDCommonBasic();
                if(creditNoteLineItemDTO.getInvoiceLineReference() != null 
                        && !creditNoteLineItemDTO.getInvoiceLineReference().isEmpty()) {

                    lineIDCommonBasic.setValue(creditNoteLineItemDTO.getInvoiceLineReference());
                } else {

                    lineIDCommonBasic.setValue(String.valueOf(++i));
                }
                
                orderLineReferenceCommonAggregate = new OrderLineReferenceCommonAggregate();
                orderLineReferenceCommonAggregate.setLineID(lineIDCommonBasic);
                creditNoteLine.getOrderLineReferences().add(orderLineReferenceCommonAggregate);

                if (!StringUtils.isEmpty(creditNoteLineItemDTO.getId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(creditNoteLineItemDTO.getId());
                    creditNoteLine.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(creditNoteLineItemDTO.getNote())) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(creditNoteLineItemDTO.getNote());
                    noteCommonBasic.setLanguageID(invoiceDTO.getLanguage());
                    creditNoteLine.getNotes().add(noteCommonBasic);
                }

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(creditNoteLineItemDTO.getProductNo());
                idCommonBasic.setSchemeID(EHFConstants.GTIN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                sellersItemIdentification = new ItemIdentificationType();
                sellersItemIdentification.setID(idCommonBasic);
                item = new ItemType();
                item.setSellersItemIdentification(sellersItemIdentification);
                
                if(!StringUtils.isEmpty(creditNoteLineItemDTO.getBuyersItemId())) {

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(creditNoteLineItemDTO.getBuyersItemId());
                    buyersItemIdentification = new ItemIdentificationType();
                    buyersItemIdentification.setID(idCommonBasic);
                    item.setBuyersItemIdentification(buyersItemIdentification);
                }

                nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(creditNoteLineItemDTO.getProductName());
                item.setName(nameCommonBasic);

                if (!StringUtils.isEmpty(creditNoteLineItemDTO.getAccountingCode())) {
                    accountingCostCommonBasic = new AccountingCostCommonBasic();
                    accountingCostCommonBasic.setValue(creditNoteLineItemDTO.getAccountingCode());
                    creditNoteLine.setAccountingCost(accountingCostCommonBasic);
                }

                creditedQuantityCommonBasic = new CreditedQuantityCommonBasic();
                creditedQuantityCommonBasic.setUnitCode(creditNoteLineItemDTO.getUnitCode());
                creditedQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                creditNoteLine.setCreditedQuantity(creditedQuantityCommonBasic);

                if(creditNoteLineItemDTO.getBillingReferenceDTOs() != null && creditNoteLineItemDTO.getBillingReferenceDTOs().size() > 0) {

                    BillingReferenceCommonAggregate billingRef = null;
                    BillingReferenceLineCommonAggregate billingRefLine = null;
                    for (BillingReferenceDTO billingReferenceDTO : creditNoteLineItemDTO.getBillingReferenceDTOs()) {

                        billingRef = new BillingReferenceCommonAggregate();
                        DocumentReferenceType documentRef = new DocumentReferenceType();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(billingReferenceDTO.getInvoiceDocumentReference());
                        documentRef.setID(idCommonBasic);
                        billingRef.setInvoiceDocumentReference(documentRef);

                        billingRefLine = new BillingReferenceLineCommonAggregate();
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(billingReferenceDTO.getBillingReferenceLine());
                        billingRefLine.setID(idCommonBasic);

                        billingRef.getBillingReferenceLines().add(billingRefLine);
                        creditNoteLine.getBillingReferences().add(billingRef);
                    }
                }

                if (!StringUtils.isEmpty(creditNoteLineItemDTO.getOriginCountry())) {
                    CountryType countryType = new CountryType();
                    NameCommonBasic name = new NameCommonBasic();
                    name.setValue(creditNoteLineItemDTO.getOriginCountry());
                    countryType.setName(name);
                    item.setOriginCountry(countryType);
                }

                if (creditNoteLineItemDTO.getPeriodStartDate() != null) {

                    PeriodType periodType = new PeriodType();
                    StartDateCommonBasic startDate = new StartDateCommonBasic();
                    startDate.setValue(ConversionUtils.asXMLGregorianCalendar(creditNoteLineItemDTO.getPeriodStartDate()));
                    periodType.setStartDate(startDate);

                if (creditNoteLineItemDTO.getPeriodEndDate() != null) {
                    EndDateCommonBasic endDate = new EndDateCommonBasic();
                    endDate.setValue(ConversionUtils.asXMLGregorianCalendar(creditNoteLineItemDTO.getPeriodEndDate()));
                    periodType.setEndDate(endDate);
                }
                    creditNoteLine.getInvoicePeriods().add(periodType);
                }

                if (creditNoteLineItemDTO.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    //idCommonBasic.setValue(ConversionUtils.getEHFV3TaxCategoryCode(creditNoteLineItemDTO.getTaxPercent(), creditNoteLineItemDTO.getTaxCode()));
                    idCommonBasic.setValue(creditNoteLineItemDTO.getTaxCode());
                    taxCategoryType.setID(idCommonBasic);

                    if (!creditNoteLineItemDTO.getTaxCode().equalsIgnoreCase("O")) {

                        percentCommonBasic = new PercentCommonBasic();
                        percentCommonBasic.setValue(ConversionUtils.asBigDecimal(creditNoteLineItemDTO.getTaxPercent()));
                        taxCategoryType.setPercent(percentCommonBasic);
                    }

                    if (!StringUtils.isEmpty(creditNoteLineItemDTO.getTaxTypeIntName())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(creditNoteLineItemDTO.getTaxTypeIntName());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    item.getClassifiedTaxCategories().add(taxCategoryType);
                }

                if (creditNoteLineItemDTO.getUnitPrice() != null) {

                    priceAmountCommonBasic = new PriceAmountCommonBasic();
                    priceAmountCommonBasic.setCurrencyID(currencyCode);
                    priceAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(creditNoteLineItemDTO.getUnitPrice())));
                    price = new PriceTypeCommonAggregate();
                    price.setPriceAmount(priceAmountCommonBasic);

                    baseQuantityCommonBasic = new BaseQuantityCommonBasic();
                    baseQuantityCommonBasic.setValue(new BigDecimal(1));
                    baseQuantityCommonBasic.setUnitCodeListID(EHFConstants.UNIT_CODE_LIST_ID.getValue());
                    baseQuantityCommonBasic.setUnitCode(creditNoteLineItemDTO.getUnitCode());
                    price.setBaseQuantity(baseQuantityCommonBasic);
                    creditNoteLine.setPrice(price);
                }

                if (creditNoteLineItemDTO.getQuantity() != null) {
                    creditedQuantityCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(creditNoteLineItemDTO.getQuantity())));
                    creditNoteLine.setCreditedQuantity(creditedQuantityCommonBasic);
                }

                if (creditNoteLineItemDTO.getTotalExcTax() != null) {
                    lineExtensionTaxAmount = new LineExtensionAmountCommonBasic();
                    lineExtensionTaxAmount.setValue(ConversionUtils.asBigDecimal(Math.abs(creditNoteLineItemDTO.getTotalExcTax())));
                    lineExtensionTaxAmount.setCurrencyID(currencyCode);
                    creditNoteLine.setLineExtensionAmount(lineExtensionTaxAmount);
                }

                if (creditNoteLineItemDTO.getTaxAmount() != null) {
                    taxAmountCommonBasic = new TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(creditNoteLineItemDTO.getTaxAmount())));
                    taxAmountCommonBasic.setCurrencyID(currencyCode);
                    taxTotalCommonAggregate = new TaxTotalType();
                    taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                    creditNoteLine.getTaxTotals().add(taxTotalCommonAggregate);
                }

                List<AllowanceChargeDTO> allowanceCharges =  creditNoteLineItemDTO.getAllowanceCharges();
                if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

                    AllowanceChargeType allowanceChargeType = null;
                    for (AllowanceChargeDTO allowanceChargeDTO : allowanceCharges) {

                        allowanceChargeType = new AllowanceChargeType();

                        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic =
                                new  ChargeIndicatorCommonBasic();
                        chargeIndicatorCommonBasic.setValue(allowanceChargeDTO.isChargeIndicator());
                        allowanceChargeType.setChargeIndicator(chargeIndicatorCommonBasic);

                        if (allowanceChargeDTO.getAllowanceChargeReason() != null) {
                            AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic =
                                    new  AllowanceChargeReasonCommonBasic();
                            allowanceChargeReasonCommonBasic.setValue(allowanceChargeDTO.getAllowanceChargeReason());
                            allowanceChargeType.getAllowanceChargeReasons().add(allowanceChargeReasonCommonBasic);
                        }

                        if (allowanceChargeDTO.getAmount() != null) {
                            AmountCommonBasic amountCommonBasic = new  AmountCommonBasic();
                            amountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(allowanceChargeDTO.getAmount())));
                            amountCommonBasic.setCurrencyID(currencyCode);
                            allowanceChargeType.setAmount(amountCommonBasic);
                        }

                        if (allowanceChargeDTO.getTaxPercent() != null) {

                            taxCategoryType = new TaxCategoryType();

                            idCommonBasic = new IDCommonBasic();
                            //idCommonBasic.setValue(ConversionUtils.getEHFV3TaxCategoryCode(allowanceChargeDTO.getTaxPercent(), allowanceChargeDTO.getTaxCode()));
                            idCommonBasic.setValue(allowanceChargeDTO.getTaxCode());
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
                                taxSchemeCommonAggregate =
                                        new TaxSchemeCommonAggregate();
                                taxSchemeCommonAggregate.setID(idCommonBasic);
                            }

                            taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                            allowanceChargeType.getTaxCategories().add(taxCategoryType);
                        }

                        creditNoteLine.getAllowanceCharges().add(allowanceChargeType);
                    }
                }

                mapEHFV3DeliveryAddress(invoiceDTO, creditNoteLine);
                creditNoteLine.setItem(item);
                creditNote.getCreditNoteLines().add(creditNoteLine);
            }

            monetaryTotalType = creditNote.getLegalMonetaryTotal();
            if (monetaryTotalType == null) {
                monetaryTotalType = new MonetaryTotalType();
                creditNote.setLegalMonetaryTotal(monetaryTotalType);
            }

            BigDecimal taxExclusiveAmount = null;
            if (invoiceDTO.getTotalExcTax() != null) {

                lineExtensionTotalAmount = new LineExtensionAmountCommonBasic();
                lineExtensionTotalAmount.setValue(ConversionUtils.asBigDecimal(Math.abs(invoiceDTO.getTotalExcTax())));
                lineExtensionTotalAmount.setCurrencyID(currencyCode);
                monetaryTotalType.setLineExtensionAmount(lineExtensionTotalAmount);

                taxExclusiveAmount = ConversionUtils.asBigDecimal(Math.abs(invoiceDTO.getTotalExcTax()));

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
                        invoiceDTO.getTaxAmount() != null ? taxExclusiveAmount.add(ConversionUtils.asBigDecimal(Math.abs(invoiceDTO.getTaxAmount())))
                                : taxExclusiveAmount;

                payableRoundingAmountCommonBasic = new PayableRoundingAmountCommonBasic();
                if (invoiceDTO.getIsRoundPayableAmount()) {
                    if (invoiceDTO.getPayableRoundingAmount() != null) {
                        payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(invoiceDTO.getPayableRoundingAmount()).negate());
                    } else {
                        payableRoundingAmountCommonBasic.setValue(BigDecimal.ZERO);
                    }
                } else {
                    payableRoundingAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(0.0d));
                }
                payableRoundingAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setPayableRoundingAmount(payableRoundingAmountCommonBasic);

                BigDecimal payableAmount =
                        taxInclusiveAmount.add(payableRoundingAmountCommonBasic.getValue());

                taxInclusiveAmountCommonBasic = new TaxInclusiveAmountCommonBasic();
                taxInclusiveAmountCommonBasic.setValue(taxInclusiveAmount);
                taxInclusiveAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountCommonBasic);

                payableAmountCommonBasic = new PayableAmountCommonBasic();
                payableAmountCommonBasic.setValue(payableAmount);
                payableAmountCommonBasic.setCurrencyID(currencyCode);
                monetaryTotalType.setPayableAmount(payableAmountCommonBasic);
            }

            creditNote.setLegalMonetaryTotal(monetaryTotalType);
        }
    }

    /**
     * Method used to map tax summary details from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3TaxSummaries(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        List<TaxSummaryDTO> taxSummaries = invoiceDTO.getTaxSummaries();

        if (taxSummaries != null) {

            TaxCategoryType taxCategoryType = null;
            IDCommonBasic idCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxSubtotalCommonAggregate taxSubtotalCommonAggregate = null;
            TaxableAmountCommonBasic taxableAmountCommonBasic = null;
            TaxExemptionReasonCodeCommonBasic taxExemptionReasonCodeCommonBasic = null;
            TaxExemptionReasonCommonBasic taxExemptionReasonCommonBasic = null;
            TaxTotalType taxTotalCommonAggregate = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            TransactionCurrencyTaxAmountCommonBasic transactionCurrency = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = invoiceDTO.getCurrencyDTO();
            if (currencyDTO != null) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            taxTotalCommonAggregate = new TaxTotalType();

            for (TaxSummaryDTO taxSummary : taxSummaries) {

                taxSubtotalCommonAggregate = new TaxSubtotalCommonAggregate();
                if (taxSummary.getTotalExcTax() != null) {
                    taxableAmountCommonBasic = new TaxableAmountCommonBasic();
                    taxableAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(taxSummary.getTotalExcTax())));
                    taxableAmountCommonBasic.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTaxableAmount(taxableAmountCommonBasic);
                }

                if (taxSummary.getTaxAmount() != null) {
                    taxAmountCommonBasic = new TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(taxSummary.getTaxAmount())));
                    taxAmountCommonBasic.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                }

                if (taxSummary.getTransactionCurrencyTaxAmount() != null) {
                    transactionCurrency = new TransactionCurrencyTaxAmountCommonBasic();
                    transactionCurrency.setValue(ConversionUtils.asBigDecimal(Math.abs(taxSummary.getTransactionCurrencyTaxAmount())));
                    transactionCurrency.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTransactionCurrencyTaxAmount(transactionCurrency);
                } else if (taxSummary.getTaxAmount() != null) {
                    transactionCurrency = new TransactionCurrencyTaxAmountCommonBasic();
                    transactionCurrency.setValue(ConversionUtils.asBigDecimal(Math.abs(taxSummary.getTaxAmount())));
                    transactionCurrency.setCurrencyID(currencyCode);
                    taxSubtotalCommonAggregate.setTransactionCurrencyTaxAmount(transactionCurrency);
                }

                if (taxSummary.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    //idCommonBasic.setValue(ConversionUtils.getEHFV3TaxCategoryCode(taxSummary.getTaxPercent(), taxSummary.getTaxCode()));    should be handled by consumer                 
                    idCommonBasic.setValue(taxSummary.getTaxCode());                     
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    //if (!StringUtils.isEmpty(taxSummary.getTaxTypeIntName())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(EHFConstants.VAT.getValue());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                        taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    //}

                    if (taxSummary.getTaxPercent() == 0 && !taxSummary.getTaxCode().equals("Z")) {

                        taxExemptionReasonCodeCommonBasic = new TaxExemptionReasonCodeCommonBasic();
                        taxExemptionReasonCodeCommonBasic.setValue(EHFConstants.TAX_EXEMPTION_REASON_CODE_V3.getValue());
                        taxCategoryType.setTaxExemptionReasonCode(taxExemptionReasonCodeCommonBasic);

                        taxExemptionReasonCommonBasic = new TaxExemptionReasonCommonBasic();
                        taxExemptionReasonCommonBasic.setValue(EHFConstants.TAX_EXEMPTION_REASON.getValue());
                        taxCategoryType.getTaxExemptionReasons().add(taxExemptionReasonCommonBasic);
                    }

                    taxSubtotalCommonAggregate.setTaxCategory(taxCategoryType);
                }

                if (taxSubtotalCommonAggregate != null) {
                    taxTotalCommonAggregate.getTaxSubtotals().add(taxSubtotalCommonAggregate);
                }
            }

            if (invoiceDTO.getTaxAmount() != null) {
                taxAmountCommonBasic = new TaxAmountCommonBasic();
                taxAmountCommonBasic.setCurrencyID(currencyCode);
                taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(invoiceDTO.getTaxAmount())));
                taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
            }
            creditNote.getTaxTotals().add(taxTotalCommonAggregate);
        }
    }

    /**
     * Method used to map delivery information from Invoice wrapper to EHF Invoice.
     */
    private static void mapEHFV3DeliveryAddress(InvoiceDTO invoiceDTO, CreditNote creditNote) {

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
            creditNote.getDeliveries().add(deliveryCommonAggregate);
        }
    }

    /**
     * Method used to map delivery information from Invoice wrapper to EHF Invoice Line.
     */
    private static void mapEHFV3DeliveryAddress(InvoiceDTO invoiceDTO, CreditNoteLineType invoiceLine) {

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
     * Method used to map allowance charges from Invoice wrapper to EHF CreditNote.
     */
    private static void mapEHFV3AllowanceCharges(InvoiceDTO invoiceDTO, CreditNote creditNote) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;

        List<AllowanceChargeDTO> allowanceCharges = invoiceDTO.getAllowanceCharges();
        if (allowanceCharges != null && !allowanceCharges.isEmpty()) {

            TaxCategoryType taxCategoryType = null;
            IDCommonBasic idCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            AllowanceChargeType allowanceChargeType = null;

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
                    amountCommonBasic.setValue(ConversionUtils.asBigDecimal(Math.abs(allowanceChargeDTO.getAmount())));
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
                    //idCommonBasic.setValue(ConversionUtils.getEHFV3TaxCategoryCode(allowanceChargeDTO.getTaxPercent(), allowanceChargeDTO.getTaxCode()));
                    idCommonBasic.setValue(allowanceChargeDTO.getTaxCode());
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
                creditNote.getAllowanceCharges().add(allowanceChargeType);
            }

            MonetaryTotalType legalMonetaryTotal = new MonetaryTotalType();

            if (allowanceTotalAmount > 0) {
                AllowanceTotalAmountCommonBasic allowanceTotalAmountCB =
                        new AllowanceTotalAmountCommonBasic();
                allowanceTotalAmountCB.setValue(ConversionUtils.asBigDecimal(Math.abs(allowanceTotalAmount)));
                allowanceTotalAmountCB.setCurrencyID(currencyCode);
                legalMonetaryTotal.setAllowanceTotalAmount(allowanceTotalAmountCB);
            }

            if (chargeTotalAmount > 0) {
                ChargeTotalAmountCommonBasic chargeTotalAmountCB =
                        new ChargeTotalAmountCommonBasic();
                chargeTotalAmountCB.setValue(ConversionUtils.asBigDecimal(Math.abs(chargeTotalAmount)));
                chargeTotalAmountCB.setCurrencyID(currencyCode);
                legalMonetaryTotal.setChargeTotalAmount(chargeTotalAmountCB);
            }
            creditNote.setLegalMonetaryTotal(legalMonetaryTotal);
        }
    }

    /**
     * Unmarshalling XML file to creditnote object.
     */
    public static InvoiceDTO getEHFV3Invoice(File file) throws Throwable {

        final JAXBContext context = JAXBContext.newInstance(ehandel.no.ehf.creditnote.CreditNote.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        ehandel.no.ehf.creditnote.CreditNote creditNote = (ehandel.no.ehf.creditnote.CreditNote) unmarshaller.unmarshal(file);

        return convertToEHFV3CreditNote(creditNote);
    }

    /**
     * Method used to convert EHF CreditNote XML file to EHF CreditNote object
     */
    public static InvoiceDTO getEHFV3Invoice(String filePath) throws Throwable {

        File file = new File(filePath);
        return UblCreditNoteUtils.getEHFV3Invoice(file);
    }

    /**
     * Method used to convert EHF CreditNote XML file stream to EHF CreditNote object
     */
    public static InvoiceDTO getEHFV3Invoice(InputStream is) throws Throwable {

        final Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
        CreditNote creditNote = (CreditNote) unmarshaller.unmarshal(is);
        
        return convertToEHFV3CreditNote(creditNote);
    }

    /**
     * Method used to convert EHF CreditNote to Credit note wrapper.
     */
    private static InvoiceDTO convertToEHFV3CreditNote(CreditNote creditNote) {

        InvoiceDTO invoiceDTO = null;
        if (creditNote != null) {

            invoiceDTO = new InvoiceDTO();

            // Set customization id
            CustomizationIDCommonBasic customizationIDCommonBasic = creditNote.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                invoiceDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            // Set profile id
            ProfileIDCommonBasic profileIDCommonBasic = creditNote.getProfileID();
            if (profileIDCommonBasic != null) {
                invoiceDTO.setProfileID(profileIDCommonBasic.getValue());
            }

            // Set creditNote no
            IDCommonBasic idCommonBasic = creditNote.getID();
            if (idCommonBasic != null) {
                invoiceDTO.setInvoiceNo(idCommonBasic.getValue());
            }

            // Set issue date
            IssueDateCommonBasic issueDateCommonBasic = creditNote.getIssueDate();
            if (issueDateCommonBasic != null) {
                invoiceDTO.setIssueDate(ConversionUtils.asDate(issueDateCommonBasic.getValue()));
            }

            // Set purchase no
            OrderReferenceCommonAggregate orderReferenceCommonAggregate =
                    creditNote.getOrderReference();
            if (orderReferenceCommonAggregate != null) {
                idCommonBasic = orderReferenceCommonAggregate.getID();
                if (idCommonBasic != null) {
                    invoiceDTO.setPurchaseOrderNo(idCommonBasic.getValue());
                }
            }
            
            //set billing reference
            if(creditNote.getBillingReferences() != null && creditNote.getBillingReferences().size() > 0) {
                BillingReferenceDTO billingReferenceDTO = null;
                List<BillingReferenceDTO> billingReferenceDTOs = new ArrayList<BillingReferenceDTO>();
                for (BillingReferenceCommonAggregate billingRef : creditNote.getBillingReferences()) {
                    billingReferenceDTO = new BillingReferenceDTO();
                    if (billingRef.getInvoiceDocumentReference() != null) {
                        billingReferenceDTO.setInvoiceDocumentReference(billingRef.getInvoiceDocumentReference().getID().getValue());
                    } else if (billingRef.getCreditNoteDocumentReference() != null) {
                        billingReferenceDTO.setInvoiceDocumentReference(billingRef.getCreditNoteDocumentReference().getID().getValue());
                    }

                    billingReferenceDTOs.add(billingReferenceDTO);
                }
                invoiceDTO.setBillingReferenceDTOs(billingReferenceDTOs);
            }
            
            //set contract
            if (creditNote.getContractDocumentReferences() != null && creditNote.getContractDocumentReferences().size() > 0) {

                ContractDTO contractDTO = new ContractDTO();
                DocumentReferenceType documentReferenceType = creditNote.getContractDocumentReferences().get(0);
                idCommonBasic = documentReferenceType.getID();
                if (idCommonBasic != null) {
                    contractDTO.setContractId(idCommonBasic.getValue());
                }

                invoiceDTO.setContractDTO(contractDTO);
            }

            List<DocumentReferenceType> additionalDocumentReferences =
                    creditNote.getAdditionalDocumentReferences();
            if (additionalDocumentReferences != null) {

                AttachmentType attachmentType = null;
                EmbeddedDocumentBinaryObjectCommonBasic embeddedDocumentBinaryObject = null;

                List<FileDTO> files = new ArrayList<FileDTO>();
                FileDTO fileDTO = null;
                String fileExt = "";

                for (DocumentReferenceType documentReferenceType : additionalDocumentReferences) {

                    fileDTO = new FileDTO();
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
            
            //set contract
            if (creditNote.getContractDocumentReferences() != null && creditNote.getContractDocumentReferences().size() > 0) {

                ContractDTO contractDTO = new ContractDTO();
                DocumentReferenceType documentReferenceType = creditNote.getContractDocumentReferences().get(0);
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
            }
            
            List<NoteCommonBasic> notes = creditNote.getNotes();
            if (notes != null && !notes.isEmpty()) {
                for (NoteCommonBasic noteCommonBasic : notes) {
                    invoiceDTO.getNotes().add(noteCommonBasic.getValue());
                    invoiceDTO.setLanguage(noteCommonBasic.getLanguageID());
                }
            }


            mapEHFV3Supplier(creditNote, invoiceDTO);

            
            mapEHFV3Customer(creditNote, invoiceDTO);            
            mapEHFV3CustomerBankAccount(creditNote, invoiceDTO);            
            mapEHFV3Currency(creditNote, invoiceDTO);            
            mapEHFV3DeliveryAddress(creditNote, invoiceDTO);            
            mapEHFV3ExchangeRate(creditNote, invoiceDTO);            
            mapEHFV3InvoiceLineItems(creditNote, invoiceDTO);            
            mapEHFV3TaxSummaries(creditNote, invoiceDTO);            
            mapEHFV3AllowanceCharges(creditNote, invoiceDTO);            
           // mapEHFV3TaxRepresentativeParty(creditNote, invoiceDTO);            
        }

        return invoiceDTO;
    }

    /**
     * Method used to map supplier information from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3Supplier(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        SupplierPartyType supplierPartyType = creditNote.getAccountingSupplierParty();

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
            invoiceDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map customer information from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3Customer(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        CustomerPartyType customerPartyType = creditNote.getAccountingCustomerParty();

        if (customerPartyType != null) {

            CustomerDTO customerDTO = new CustomerDTO();
            AddressDTO addressDTO = null;

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

            PartyTypeCommonAggregate party = customerPartyType.getParty();
            if (party != null) {

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
    private static void mapEHFV3CustomerBankAccount(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        List<PaymentMeansCommonAggregate> paymentMeans = creditNote.getPaymentMeans();

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

                List<PaymentIDCommonBasic> paymentIDs;
                paymentIDs = paymentMeansCommonAggregate.getPaymentIDs();
                if (paymentIDs != null && !paymentIDs.isEmpty()) {
                    invoiceDTO.setPaymentId(paymentIDs.get(0).getValue());
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
     * Method used to map customer currency information from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3Currency(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                creditNote.getDocumentCurrencyCode();
        CurrencyDTO currencyDTO = null;

        TaxCurrencyCodeCommonBasic taxCurrencyCodeCommonBasic =
                creditNote.getTaxCurrencyCode();
        CurrencyDTO baseCurrencyDTO = null;

        if (documentCurrencyCodeCommonBasic != null) {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getName());
            invoiceDTO.setCurrencyDTO(currencyDTO);
        }

        if (taxCurrencyCodeCommonBasic != null) {
            baseCurrencyDTO = new CurrencyDTO();
            baseCurrencyDTO.setCurrencyCode(taxCurrencyCodeCommonBasic.getValue());
            baseCurrencyDTO.setCurrencyName(taxCurrencyCodeCommonBasic.getValue());
            invoiceDTO.setBaseCurrencyDTO(baseCurrencyDTO);
        }
    }

    /**
     * Method used to map creditNote exchange rate from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3ExchangeRate(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        // Set exchange rate
        ExchangeRateType exchangeRateType = creditNote.getTaxExchangeRate();
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
     * Method used to map creditNote line item details from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3InvoiceLineItems(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        List<CreditNoteLineType> creditNoteLineItems = creditNote.getCreditNoteLines();
        if (creditNoteLineItems != null) {

            InvoiceLineItemDTO creditNoteLineItemDTO = null;

            ItemType itemCommonAggregate = null;
            ItemIdentificationType itemIdentificationType = null;
            PriceTypeCommonAggregate price = null;
            MonetaryTotalType legalMonetaryTotal = null;
            IDCommonBasic idCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            CreditedQuantityCommonBasic creditedQuantityCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            PriceAmountCommonBasic priceAmountCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionAmount = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;
            ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
            AmountCommonBasic amountCommonBasic = null;
            AllowanceChargeDTO allowanceChargeDTO = null;
            AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
            List<TaxCategoryType> taxCategories = null;
            TaxCategoryType taxCategory = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

            for (CreditNoteLineType creditNoteLine : creditNoteLineItems) {

                creditNoteLineItemDTO = new InvoiceLineItemDTO();

                if(creditNoteLine.getOrderLineReferences() != null && 
                        !creditNoteLine.getOrderLineReferences().isEmpty()) {

                    LineIDCommonBasic lineIDCommonBasic = creditNoteLine.getOrderLineReferences().get(0).getLineID();

                    if(lineIDCommonBasic != null) {

                       creditNoteLineItemDTO.setInvoiceLineReference(lineIDCommonBasic.getValue()); 
                    }
                }
                
                BillingReferenceDTO billingReferenceDTO = null;
                List<BillingReferenceDTO> billingReferenceDTOs = new ArrayList<BillingReferenceDTO>();
                if (creditNoteLine.getBillingReferences() != null && creditNoteLine.getBillingReferences().size() > 0) {
                    for (BillingReferenceCommonAggregate billingRef : creditNoteLine.getBillingReferences()) {
                        billingReferenceDTO = new BillingReferenceDTO();
                        if (billingRef.getCreditNoteDocumentReference() != null) {
                            billingReferenceDTO.setInvoiceDocumentReference(billingRef.getCreditNoteDocumentReference().getID().getValue());
                        }
                        if (billingRef.getBillingReferenceLines() != null && billingRef.getBillingReferenceLines().size() > 0) {
                                billingReferenceDTO.setBillingReferenceLine(billingRef.getBillingReferenceLines().get(0).getID().getValue());
                        }
                        billingReferenceDTOs.add(billingReferenceDTO);
                    }
                    creditNoteLineItemDTO.setBillingReferenceDTOs(billingReferenceDTOs);
                }

                idCommonBasic = creditNoteLine.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    creditNoteLineItemDTO.setId(idCommonBasic.getValue());
                }

                itemCommonAggregate = creditNoteLine.getItem();
                if (itemCommonAggregate != null) {
                    itemIdentificationType = itemCommonAggregate.getSellersItemIdentification();
                    if (itemIdentificationType != null) {
                        idCommonBasic = itemIdentificationType.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            creditNoteLineItemDTO.setProductNo(idCommonBasic.getValue());
                        }
                    }
                }

                if(itemCommonAggregate.getOriginCountry() != null && itemCommonAggregate.getOriginCountry().getName() != null) {
                    creditNoteLineItemDTO.setOriginCountry(itemCommonAggregate.getOriginCountry().getName().getValue());
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
                    creditNoteLineItemDTO.setManufacturerPartyDTO(manufacturerPartyDTO);
                }

                if(creditNoteLine.getInvoicePeriods() != null && creditNoteLine.getInvoicePeriods().size() > 0) {
                    PeriodType periodType = creditNoteLine.getInvoicePeriods().get(0);
                    creditNoteLineItemDTO.setPeriodStartDate(ConversionUtils.asDate(periodType.getStartDate().getValue()));
                    creditNoteLineItemDTO.setPeriodEndDate(ConversionUtils.asDate(periodType.getEndDate().getValue()));
                }

                List<DescriptionCommonBasic> descriptions = itemCommonAggregate.getDescriptions();
                if (descriptions != null) {
                    for (DescriptionCommonBasic description : descriptions) {
                        if (description != null) {
                            creditNoteLineItemDTO.setDescription(description.getValue());
                        }
                    }
                }

                if (creditNoteLine.getNotes().size() > 0) {
                    noteCommonBasic = creditNoteLine.getNotes().get(0);
                }

                if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                    creditNoteLineItemDTO.setNote(noteCommonBasic.getValue());
                }

                nameCommonBasic = itemCommonAggregate.getName();
                if (nameCommonBasic != null) {
                    creditNoteLineItemDTO.setProductName(nameCommonBasic.getValue());
                }

                accountingCostCommonBasic = creditNoteLine.getAccountingCost();
                if (accountingCostCommonBasic != null
                        && accountingCostCommonBasic.getValue() != null) {
                    creditNoteLineItemDTO.setAccountingCode(accountingCostCommonBasic.getValue());
                }

                creditedQuantityCommonBasic = creditNoteLine.getCreditedQuantity();
                if (creditedQuantityCommonBasic != null) {
                    creditNoteLineItemDTO.setUnitCode(creditedQuantityCommonBasic.getUnitCode());
                }

                List<TaxCategoryType> classifiedTaxCategories =
                        itemCommonAggregate.getClassifiedTaxCategories();
                for (TaxCategoryType taxCategoryType : classifiedTaxCategories) {

                    percentCommonBasic = taxCategoryType.getPercent();
                    if (percentCommonBasic != null && percentCommonBasic.getValue() != null) {
                        creditNoteLineItemDTO.setTaxPercent(percentCommonBasic.getValue().doubleValue());
                    }

                    taxSchemeCommonAggregate =
                            taxCategoryType.getTaxScheme();
                    if (taxSchemeCommonAggregate != null) {
                        idCommonBasic = taxSchemeCommonAggregate.getID();
                        if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                            creditNoteLineItemDTO.setTaxTypeIntName(idCommonBasic.getValue());
                        }
                    }
                }

                price = creditNoteLine.getPrice();
                if (price != null) {
                    priceAmountCommonBasic = price.getPriceAmount();
                    if (priceAmountCommonBasic != null && priceAmountCommonBasic.getValue() != null) {
                        creditNoteLineItemDTO.setUnitPrice(priceAmountCommonBasic.getValue().doubleValue());
                    }
                }

                creditedQuantityCommonBasic = creditNoteLine.getCreditedQuantity();
                if (creditedQuantityCommonBasic != null
                        && creditedQuantityCommonBasic.getValue() != null) {
                    creditNoteLineItemDTO.setQuantity(creditedQuantityCommonBasic.getValue().doubleValue());
                }

                lineExtensionAmount = creditNoteLine.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    creditNoteLineItemDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                }

                List<TaxTotalType> taxTotals = creditNoteLine.getTaxTotals();
                if (taxTotals != null) {

                    for (TaxTotalType taxTotalCommonAggregate : taxTotals) {
                        taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
                        if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                            creditNoteLineItemDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
                            creditNoteLineItemDTO.setTotalAmount(creditNoteLineItemDTO.getTotalExcTax() + creditNoteLineItemDTO.getTaxAmount());
                        }
                    }
                }

                List<AllowanceChargeType> allowanceCharges = creditNoteLine.getAllowanceCharges();
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
                            creditNoteLineItemDTO.getAllowanceCharges().add(allowanceChargeDTO);
                        }
                    }
                }
                
                itemIdentificationType = itemCommonAggregate.getBuyersItemIdentification();
                if (itemIdentificationType != null) {
                    idCommonBasic = itemIdentificationType.getID();
                    if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                        creditNoteLineItemDTO.setBuyersItemId(idCommonBasic.getValue());
                    }
                }

                mapEHFV3DeliveryAddress(creditNote, creditNoteLineItemDTO);
                invoiceDTO.getInvoiceLineItems().add(creditNoteLineItemDTO);
            }

            legalMonetaryTotal = creditNote.getLegalMonetaryTotal();
            if (legalMonetaryTotal != null) {

                lineExtensionAmount = legalMonetaryTotal.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    invoiceDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                }

                taxInclusiveAmountCommonBasic = legalMonetaryTotal.getTaxInclusiveAmount();
                if (taxInclusiveAmountCommonBasic != null
                        && taxInclusiveAmountCommonBasic.getValue() != null) {
                    invoiceDTO.setTotalAmount(taxInclusiveAmountCommonBasic.getValue().doubleValue() * -1);
                }
            }
        }
    }

    /**
     * Method used to map tax summary details from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3TaxSummaries(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        List<TaxTotalType> taxTotals = creditNote.getTaxTotals();
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

                    percentCommonBasic = taxCategoryType.getPercent();
                    if (percentCommonBasic != null && percentCommonBasic.getValue() != null) {
                        taxSummary.setTaxPercent(percentCommonBasic.getValue().doubleValue());
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
     * Method used to map allowance charges from EHF CreditNote to Invoice wrapper.
     */
    private static void mapEHFV3AllowanceCharges(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        AllowanceChargeReasonCommonBasic allowanceChargeReasonCommonBasic = null;
        ChargeIndicatorCommonBasic chargeIndicatorCommonBasic = null;
        AmountCommonBasic amountCommonBasic = null;
        AllowanceChargeDTO allowanceChargeDTO = null;
        List<TaxCategoryType> taxCategories = null;
        TaxCategoryType taxCategoryType = null;
        PercentCommonBasic percentCommonBasic = null;
        TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        List<AllowanceChargeType> allowanceCharges = creditNote.getAllowanceCharges();

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

    /**
     * Method used to map delivery information from EHF Invoice to Invoice wrapper.
     */
    private static void mapEHFV3DeliveryAddress(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        List<DeliveryType> deliveries = creditNote.getDeliveries();

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
    private static void mapEHFV3DeliveryAddress(CreditNote creditNote,
            InvoiceLineItemDTO invoiceLineItemDTO) {

        List<DeliveryType> deliveries = creditNote.getDeliveries();

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

    private static void mapEHFV3TaxRepresentativeParty(InvoiceDTO invoiceDTO, CreditNote creditNote) {

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

                   IdentificationCodeCommonBasic code = new IdentificationCodeCommonBasic();
                   code.setValue(addressDTO.getCountryCode());
                   countryCode.setIdentificationCode(code);
                   addressType.setCountry(countryCode);
               }
               partyType.setPostalAddress(addressType);
            }

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                        new  PartyTaxSchemeCommonAggregate();
                CompanyIDCommonBasic companyIDCommonBasic =
                        new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(country + removeCountryCodeAndMVAFromEndPointId(orgNo) + EHFConstants.MVA.getValue());
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
          creditNote.setTaxRepresentativeParty(partyType);
        }
    }

    private static void mapEHFV3TaxRepresentativeParty(CreditNote creditNote, InvoiceDTO invoiceDTO) {

        PartyTypeCommonAggregate partyType = creditNote.getTaxRepresentativeParty();
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
