/*
 * @(#)ReminderUtils.java
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
import ehandel.no.dto.BankAccountDTO;
import ehandel.no.dto.CurrencyDTO;
import ehandel.no.dto.CustomerDTO;
import ehandel.no.dto.FileDTO;
import ehandel.no.dto.PayeeParty;
import ehandel.no.dto.ReminderDTO;
import ehandel.no.dto.ReminderLineDTO;
import ehandel.no.dto.SupplierDTO;
import ehandel.no.dto.TaxSummaryDTO;
import ehandel.no.reminder.AccountingCostCommonBasic;
import ehandel.no.reminder.AddressType;
import ehandel.no.reminder.AttachmentType;
import ehandel.no.reminder.BillingReferenceCommonAggregate;
import ehandel.no.reminder.BinaryObjectMimeCodeContentType;
import ehandel.no.reminder.BranchType;
import ehandel.no.reminder.BuildingNumberCommonBasic;
import ehandel.no.reminder.CalculationRateCommonBasic;
import ehandel.no.reminder.CityNameCommonBasic;
import ehandel.no.reminder.CompanyIDCommonBasic;
import ehandel.no.reminder.ContactType;
import ehandel.no.reminder.CountrySubentityCodeCommonBasic;
import ehandel.no.reminder.CountryType;
import ehandel.no.reminder.CreditLineAmountCommonBasic;
import ehandel.no.reminder.CurrencyCodeContentType;
import ehandel.no.reminder.CustomerPartyType;
import ehandel.no.reminder.CustomizationIDCommonBasic;
import ehandel.no.reminder.DebitLineAmountCommonBasic;
import ehandel.no.reminder.DocumentCurrencyCodeCommonBasic;
import ehandel.no.reminder.DocumentReferenceType;
import ehandel.no.reminder.DocumentTypeCommonBasic;
import ehandel.no.reminder.ElectronicMailCommonBasic;
import ehandel.no.reminder.EmbeddedDocumentBinaryObjectCommonBasic;
import ehandel.no.reminder.EndpointIDCommonBasic;
import ehandel.no.reminder.ExchangeRateType;
import ehandel.no.reminder.FinancialAccountType;
import ehandel.no.reminder.FinancialInstitutionCommonAggregate;
import ehandel.no.reminder.IDCommonBasic;
import ehandel.no.reminder.IdentificationCodeCommonBasic;
import ehandel.no.reminder.IssueDateCommonBasic;
import ehandel.no.reminder.ItemCommonAggregate;
import ehandel.no.reminder.ItemIdentificationType;
import ehandel.no.reminder.LineExtensionAmountCommonBasic;
import ehandel.no.reminder.MathematicOperatorCodeCommonBasic;
import ehandel.no.reminder.MonetaryTotalType;
import ehandel.no.reminder.NameCommonBasic;
import ehandel.no.reminder.NoteCommonBasic;
import ehandel.no.reminder.PartyIdentificationCommonAggregate;
import ehandel.no.reminder.PartyLegalEntityCommonAggregate;
import ehandel.no.reminder.PartyNameCommonAggregate;
import ehandel.no.reminder.PartyTaxSchemeCommonAggregate;
import ehandel.no.reminder.PartyType;
import ehandel.no.reminder.PayableAmountCommonBasic;
import ehandel.no.reminder.PayableRoundingAmountCommonBasic;
import ehandel.no.reminder.PaymentDueDateCommonBasic;
import ehandel.no.reminder.PaymentIDCommonBasic;
import ehandel.no.reminder.PaymentMeansCodeCommonBasic;
import ehandel.no.reminder.PaymentMeansCommonAggregate;
import ehandel.no.reminder.PercentCommonBasic;
import ehandel.no.reminder.PostalZoneCommonBasic;
import ehandel.no.reminder.PostboxCommonBasic;
import ehandel.no.reminder.ProfileIDCommonBasic;
import ehandel.no.reminder.RegistrationNameCommonBasic;
import ehandel.no.reminder.Reminder;
import ehandel.no.reminder.ReminderLineCommonAggregate;
import ehandel.no.reminder.ReminderSequenceNumericCommonBasic;
import ehandel.no.reminder.ReminderTypeCodeCommonBasic;
import ehandel.no.reminder.SourceCurrencyBaseRateCommonBasic;
import ehandel.no.reminder.SourceCurrencyCodeCommonBasic;
import ehandel.no.reminder.StreetNameCommonBasic;
import ehandel.no.reminder.SupplierPartyType;
import ehandel.no.reminder.TargetCurrencyBaseRateCommonBasic;
import ehandel.no.reminder.TargetCurrencyCodeCommonBasic;
import ehandel.no.reminder.TaxAmountCommonBasic;
import ehandel.no.reminder.TaxCategoryType;
import ehandel.no.reminder.TaxExclusiveAmountCommonBasic;
import ehandel.no.reminder.TaxExemptionReasonCodeCommonBasic;
import ehandel.no.reminder.TaxExemptionReasonCommonBasic;
import ehandel.no.reminder.TaxInclusiveAmountCommonBasic;
import ehandel.no.reminder.TaxSchemeCommonAggregate;
import ehandel.no.reminder.TaxSubtotalCommonAggregate;
import ehandel.no.reminder.TaxTotalCommonAggregate;
import ehandel.no.reminder.TaxableAmountCommonBasic;
import ehandel.no.reminder.TelefaxCommonBasic;
import ehandel.no.reminder.TelephoneCommonBasic;
import ehandel.no.reminder.UBLVersionIDCommonBasic;
import ehandel.no.reminder.WebsiteURICommonBasic;
import javax.xml.bind.PropertyException;

/**
 * The Class ReminderUtils.
 * 
 * @author amuthar
 * @since ehf; Jul 11, 2012
 */
public class ReminderUtils {

    private static String taxTypeIntName = "";

    public ReminderUtils() {
    }

    /**
     * Marshalling reminder object to XML file.
     */
    public static void generateReminderXML(ReminderDTO reminderDTO, String filePath)
            throws JAXBException {

        Reminder reminder = convertToReminder(reminderDTO);

        Marshaller marshaller = getMarshaller();

        marshaller.marshal(reminder, new File(filePath));
    }

    /**
     * Marshalling reminder object to byte array.
     */
    public static byte[] generateReminderXML(ReminderDTO reminderDTO) throws JAXBException {

        Reminder reminder = convertToReminder(reminderDTO);

        Marshaller marshaller = getMarshaller();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        marshaller.marshal(reminder, out);

        return out.toByteArray();
    }

    /**
     * Method to get the marshaller for Reminder.
     */
    private static Marshaller getMarshaller() throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Reminder.class);

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
     * Method used to convert Reminder wrapper to Reminder.
     */
    private static Reminder convertToReminder(ReminderDTO reminderDTO) {

        Reminder reminder = null;
        if (reminderDTO != null) {

            reminder = new Reminder();

            // Set UBL version ID
            UBLVersionIDCommonBasic ublVersionIDCommonBasic = new UBLVersionIDCommonBasic();
            ublVersionIDCommonBasic.setValue(EHFConstants.UBL_VERSION_ID.getValue());
            reminder.setUBLVersionID(ublVersionIDCommonBasic);

            // Set customization id
            CustomizationIDCommonBasic customizationIDCommonBasic =
                    new CustomizationIDCommonBasic();
            customizationIDCommonBasic.setValue(EHFConstants.REMINDER_CUSTOMIZATION_ID.getValue());
            reminder.setCustomizationID(customizationIDCommonBasic);

            // Set profile id
            ProfileIDCommonBasic profileIDCommonBasic = new ProfileIDCommonBasic();
            profileIDCommonBasic.setValue(EHFConstants.REMINDER_PROFILE_ID.getValue());
            reminder.setProfileID(profileIDCommonBasic);

            // Set reminder id
            IDCommonBasic idCommonBasic = null;
            if (!StringUtils.isEmpty(reminderDTO.getReminderId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(reminderDTO.getReminderId());
                reminder.setID(idCommonBasic);
            }

            // Set issue date, tax point date and reminder period
            if (reminderDTO.getIssueDate() != null) {
                IssueDateCommonBasic issueDateCommonBasic = new IssueDateCommonBasic();
                issueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(reminderDTO.getIssueDate()));
                reminder.setIssueDate(issueDateCommonBasic);
            }

            // Set invoice type
            if (!StringUtils.isEmpty(reminderDTO.getReminderTypeCode())) {
                ReminderTypeCodeCommonBasic reminderTypeCodeCommonBasic =
                        new ReminderTypeCodeCommonBasic();
                reminderTypeCodeCommonBasic.setValue(reminderDTO.getReminderTypeCode());
                reminder.setReminderTypeCode(reminderTypeCodeCommonBasic);
            }

            if (reminderDTO.getReminderSequenceNo() != null) {
                ReminderSequenceNumericCommonBasic reminderSeqNumericCB =
                        new ReminderSequenceNumericCommonBasic();
                reminderSeqNumericCB.setValue(ConversionUtils.asBigDecimal(reminderDTO.getReminderSequenceNo()));
                reminder.setReminderSequenceNumeric(reminderSeqNumericCB);
            }

            String language =
                    (StringUtils.isEmpty(reminderDTO.getLanguage()) ? EHFConstants.DEFAULT_LANGUAGE.getValue()
                            : reminderDTO.getLanguage());
            reminderDTO.setLanguage(language);

            if (reminderDTO.getNotes() != null && !reminderDTO.getNotes().isEmpty()) {
                NoteCommonBasic noteCommonBasic = null;
                for (String note : reminderDTO.getNotes()) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(note);
                    noteCommonBasic.setLanguageID(language);
                    reminder.getNotes().add(noteCommonBasic);
                }
            }

            List<ReminderLineDTO> reminderLineDTOs = reminderDTO.getReminderLines();
            if (reminderLineDTOs != null && !reminderLineDTOs.isEmpty()) {
                for (ReminderLineDTO reminderLineDTO : reminderLineDTOs) {
                    if (reminderLineDTO.getTotalExcTax() != null) {
                        taxTypeIntName =
                                reminderLineDTO.getTaxTypeIntName() != null ? reminderLineDTO.getTaxTypeIntName().toUpperCase()
                                        : "";
                        break;
                    }
                }
            }

            mapPayeeParty(reminderDTO, reminder);
            mapCurrency(reminderDTO, reminder);
            mapSupplier(reminderDTO, reminder);
            mapSupplierBankAccount(reminderDTO, reminder);
            mapCustomer(reminderDTO, reminder);
            mapCustomerBankAccount(reminderDTO, reminder);
            mapExchangeRate(reminderDTO, reminder);
            mapReminderLine(reminderDTO, reminder);
            mapTaxSummaries(reminderDTO, reminder);
        }

        return reminder;
    }

    /**
     * Method used to map supplier information from Reminder wrapper to Reminder.
     */
    private static void mapPayeeParty(ReminderDTO reminderDTO, Reminder reminder) {

        PayeeParty payeeParty = reminderDTO.getPayeeParty();

        if (payeeParty != null) {

            PartyType party = new PartyType();

            IDCommonBasic idCommonBasic = null;

            String organizationNo = "";
            String country = "";

            if (!StringUtils.isEmpty(payeeParty.getId())) {

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(payeeParty.getId());
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
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.LEGAL_ENTITY_SCHEME_SGENCY_ID.getValue());
                PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                        new PartyLegalEntityCommonAggregate();
                partyLegalEntityCommonAggregate.setCompanyID(companyIDCommonBasic);
                party.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            ContactType contactType = new ContactType();
            boolean isContactAvailable = false;
            if (!StringUtils.isEmpty(payeeParty.getContactId())) {
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(payeeParty.getContactId());
                contactType.setID(idCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(payeeParty.getTelePhone())) {
                TelephoneCommonBasic telephoneCommonBasic = new TelephoneCommonBasic();
                telephoneCommonBasic.setValue(payeeParty.getTelePhone());
                contactType.setTelephone(telephoneCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(payeeParty.getTeleFax())) {
                TelefaxCommonBasic telefaxCommonBasic = new TelefaxCommonBasic();
                telefaxCommonBasic.setValue(payeeParty.getTeleFax());
                contactType.setTelefax(telefaxCommonBasic);
                isContactAvailable = true;
            }

            if (!StringUtils.isEmpty(payeeParty.getEmail())) {
                ElectronicMailCommonBasic electronicMailCommonBasic =
                        new ElectronicMailCommonBasic();
                electronicMailCommonBasic.setValue(payeeParty.getEmail());
                contactType.setElectronicMail(electronicMailCommonBasic);
                isContactAvailable = true;
            }
            if (isContactAvailable) {
                party.setContact(contactType);
            }
            AddressDTO addressDTO = payeeParty.getAddressDTO();
            if (addressDTO != null) {

                AddressType postalAddress = new AddressType();
                boolean isAddressAvailable = false;

                if (!StringUtils.isEmpty(addressDTO.getGln())) {

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(addressDTO.getGln());
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    postalAddress.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    postalAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    postalAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalBox())) {
                    PostboxCommonBasic postboxCommonBasic = new PostboxCommonBasic();
                    postboxCommonBasic.setValue(addressDTO.getPostalBox());
                    postalAddress.setPostbox(postboxCommonBasic);
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
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
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

            if (!StringUtils.isEmpty(taxTypeIntName)) {

                PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                        new PartyTaxSchemeCommonAggregate();
                CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();
                companyIDCommonBasic.setValue(country + organizationNo + taxTypeIntName);
                companyIDCommonBasic.setSchemeID(taxTypeIntName);
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(taxTypeIntName);
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);

                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            reminder.setPayeeParty(party);
        }
    }

    /**
     * Method used to map currency information from Reminder wrapper to Reminder.
     */
    private static void mapCurrency(ReminderDTO reminderDTO, Reminder reminder) {

        CurrencyDTO currencyDTO = reminderDTO.getCurrencyDTO();

        if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {

            DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                    new DocumentCurrencyCodeCommonBasic();
            documentCurrencyCodeCommonBasic.setValue(currencyDTO.getCurrencyCode());
            documentCurrencyCodeCommonBasic.setName(currencyDTO.getCurrencyName());
            reminder.setDocumentCurrencyCode(documentCurrencyCodeCommonBasic);
        }
    }

    /**
     * Method used to map supplier information from Reminder wrapper to Reminder.
     */
    private static void mapSupplier(ReminderDTO reminderDTO, Reminder reminder) {

        SupplierDTO supplierDTO = reminderDTO.getSupplierDTO();

        if (supplierDTO != null) {

            SupplierPartyType supplierPartyType = new SupplierPartyType();
            PartyType party = new PartyType();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();
            AddressType registrationAddress = null;

            IDCommonBasic idCommonBasic = null;
            EndpointIDCommonBasic endpointIDCB = null;

            String country = "";

            boolean isPartyLegalEntity = false;
            boolean isRegistrationAddress = false;

            if (!StringUtils.isEmpty(supplierDTO.getEndpointId())) {
                endpointIDCB = new EndpointIDCommonBasic();
                endpointIDCB.setValue(supplierDTO.getEndpointId());
                endpointIDCB.setSchemeID(EHFConstants.GLN.getValue());
                endpointIDCB.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
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
            AddressDTO addressDTO = supplierDTO.getAddressDTO();
            if (addressDTO != null) {

                AddressType postalAddress = new AddressType();
                registrationAddress = new AddressType();
                boolean isAddressAvailable = false;

                if (!StringUtils.isEmpty(addressDTO.getGln())) {

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(addressDTO.getGln());
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    postalAddress.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    postalAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getStreetName())) {
                    StreetNameCommonBasic streetNameCommonBasic = new StreetNameCommonBasic();
                    streetNameCommonBasic.setValue(addressDTO.getStreetName());
                    postalAddress.setStreetName(streetNameCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getPostalBox())) {
                    PostboxCommonBasic postboxCommonBasic = new PostboxCommonBasic();
                    postboxCommonBasic.setValue(addressDTO.getPostalBox());
                    postalAddress.setPostbox(postboxCommonBasic);
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

                    country = addressDTO.getCountryCode().toUpperCase();
                    IdentificationCodeCommonBasic identificationCodeCommonBasic =
                            new IdentificationCodeCommonBasic();
                    identificationCodeCommonBasic.setValue(addressDTO.getCountryCode().toUpperCase());
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    postalAddress.setCountry(countryType);
                    registrationAddress.setCountry(countryType);
                    isAddressAvailable = true;
                    isRegistrationAddress = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getCountrySubentityCode())) {
                    CountrySubentityCodeCommonBasic countrySubentityCodeCommonBasic =
                            new CountrySubentityCodeCommonBasic();
                    countrySubentityCodeCommonBasic.setValue(addressDTO.getCountrySubentityCode());
                    postalAddress.setCountrySubentityCode(countrySubentityCodeCommonBasic);
                    registrationAddress.setCountrySubentityCode(countrySubentityCodeCommonBasic);
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

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(supplierDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + reminderDTO.getTaxType());
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_TAX_SCHEME_AGENCY_ID.getValue());
                partyTaxSchemeCommonAggregate.setCompanyID(companyIDCommonBasic);
                isPartyTaxSchemeAvailable = true;
            }

            if (!StringUtils.isEmpty(reminderDTO.getTaxType())) {

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(reminderDTO.getTaxType());
                idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                taxSchemeCommonAggregate.setID(idCommonBasic);
                partyTaxSchemeCommonAggregate.setTaxScheme(taxSchemeCommonAggregate);
                isPartyTaxSchemeAvailable = true;
            }
            if (isPartyTaxSchemeAvailable) {
                party.getPartyTaxSchemes().add(partyTaxSchemeCommonAggregate);
            }

            if (!StringUtils.isEmpty(supplierDTO.getName())) {

                RegistrationNameCommonBasic registrationNameCB = new RegistrationNameCommonBasic();
                registrationNameCB.setLanguageID(reminderDTO.getLanguage());
                registrationNameCB.setValue(supplierDTO.getName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(supplierDTO.getOrganizationNo())) {

                companyIDCommonBasic = new CompanyIDCommonBasic();
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
            reminder.setAccountingSupplierParty(supplierPartyType);
        }
    }

    /**
     * Method used to map supplier bank account information from Reminder wrapper to Reminder.
     */
    private static void mapSupplierBankAccount(ReminderDTO reminderDTO, Reminder reminder) {

        SupplierDTO supplierDTO = reminderDTO.getSupplierDTO();

        if (supplierDTO != null) {

            String country = "";
            boolean isFinancialAddressAvailable = false;
            String bankAccountNo = null;

            BankAccountDTO bankAccountDTO = supplierDTO.getBankAccountDTO();
            if (bankAccountDTO != null) {

                FinancialAccountType financialAccountType = new FinancialAccountType();

                IDCommonBasic idCommonBasic = new IDCommonBasic();

                if (supplierDTO.getAddressDTO() != null
                        && supplierDTO.getAddressDTO().getCountryCode() != null) {
                    country = supplierDTO.getAddressDTO().getCountryCode().toUpperCase();
                }
                if (!StringUtils.isEmpty(bankAccountDTO.getiBanNo())) {
                    idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                    bankAccountNo = country + bankAccountDTO.getiBanNo();
                } else {
                    idCommonBasic.setSchemeID(EHFConstants.BBAN.getValue());
                    bankAccountNo = bankAccountDTO.getBankAccountNumber();
                }
                idCommonBasic.setValue(bankAccountNo);
                financialAccountType.setID(idCommonBasic);

                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountName())) {
                    NameCommonBasic nameCommonBasic = new NameCommonBasic();
                    nameCommonBasic.setValue(bankAccountDTO.getBankAccountName());
                    financialAccountType.setName(nameCommonBasic);
                }

                FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate =
                        new FinancialInstitutionCommonAggregate();

                BranchType branchType = null;
                if (!StringUtils.isEmpty(bankAccountDTO.getBankAccountId())) {
                    branchType = new BranchType();
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(bankAccountDTO.getBankAccountId());
                    branchType.setID(idCommonBasic);
                } else if (!StringUtils.isEmpty(bankAccountNo)) {
                    branchType = new BranchType();
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(bankAccountNo);
                    branchType.setID(idCommonBasic);
                }

                if (branchType != null) {
                    if (!StringUtils.isEmpty(bankAccountDTO.getBic())) {
                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(bankAccountDTO.getBic());
                        idCommonBasic.setSchemeID(EHFConstants.BIC.getValue());
                        financialInstitutionCommonAggregate.setID(idCommonBasic);
                    }

                    if (!StringUtils.isEmpty(bankAccountDTO.getBankName())) {
                        NameCommonBasic nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
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

                            country = bankAddressDTO.getCountryCode().toUpperCase();
                            IdentificationCodeCommonBasic identificationCodeCommonBasic =
                                    new IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
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
                    branchType.setFinancialInstitution(financialInstitutionCommonAggregate);
                    financialAccountType.setFinancialInstitutionBranch(branchType);
                }

                PaymentMeansCommonAggregate paymentMeansCommonAggregate =
                        new PaymentMeansCommonAggregate();
                paymentMeansCommonAggregate.setPayeeFinancialAccount(financialAccountType);

                if (!StringUtils.isEmpty(reminderDTO.getPaymentId())) {
                    PaymentIDCommonBasic paymentIDCommonBasic = new PaymentIDCommonBasic();
                    paymentIDCommonBasic.setValue(reminderDTO.getPaymentId());
                    paymentMeansCommonAggregate.getPaymentIDs().add(paymentIDCommonBasic);
                }

                reminder.getPaymentMeans().add(paymentMeansCommonAggregate);
            }
        }
    }

    /**
     * Method used to map customer information from Reminder wrapper to Reminder.
     */
    private static void mapCustomer(ReminderDTO reminderDTO, Reminder reminder) {

        CustomerDTO customerDTO = reminderDTO.getCustomerDTO();

        if (customerDTO != null) {

            PartyType party = new PartyType();
            PartyType payeePartyType = new PartyType();
            AddressType postalAddress = new AddressType();
            AddressType registrationAddress = new AddressType();
            CustomerPartyType customerPartyType = new CustomerPartyType();
            PartyLegalEntityCommonAggregate partyLegalEntityCommonAggregate =
                    new PartyLegalEntityCommonAggregate();

            IDCommonBasic idCommonBasic = null;
            EndpointIDCommonBasic endpointIDCB = null;

            boolean isPartyLegalEntity = false;
            boolean isRegistrationAddress = true;

            String country = "";

            if (!StringUtils.isEmpty(customerDTO.getEndpointId())) {
                endpointIDCB = new EndpointIDCommonBasic();
                endpointIDCB.setValue(customerDTO.getEndpointId());
                endpointIDCB.setSchemeID(EHFConstants.GLN.getValue());
                endpointIDCB.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
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

            AddressDTO addressDTO = customerDTO.getAddressDTO();

            if (addressDTO != null) {

                boolean isAddressAvailable = false;

                if (!StringUtils.isEmpty(addressDTO.getGln())) {

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(addressDTO.getGln());
                    idCommonBasic.setSchemeID(EHFConstants.GLN.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.SCHEME_AGENCY_ID.getValue());
                    postalAddress.setID(idCommonBasic);
                    isAddressAvailable = true;
                }

                if (!StringUtils.isEmpty(addressDTO.getBuildingNumber())) {
                    BuildingNumberCommonBasic buildingNumberCommonBasic =
                            new BuildingNumberCommonBasic();
                    buildingNumberCommonBasic.setValue(addressDTO.getBuildingNumber());
                    postalAddress.setBuildingNumber(buildingNumberCommonBasic);
                    isAddressAvailable = true;
                }

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

                if (!StringUtils.isEmpty(addressDTO.getPostalBox())) {
                    PostboxCommonBasic postboxCommonBasic = new PostboxCommonBasic();
                    postboxCommonBasic.setValue(addressDTO.getPostalBox());
                    postalAddress.setPostbox(postboxCommonBasic);
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
                    identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
                    identificationCodeCommonBasic.setListAgencyID(EHFConstants.LIST_AGENCY_ID.getValue());
                    CountryType countryType = new CountryType();
                    countryType.setIdentificationCode(identificationCodeCommonBasic);
                    postalAddress.setCountry(countryType);
                    registrationAddress.setCountry(countryType);
                    isAddressAvailable = true;
                    isRegistrationAddress = true;
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

            PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate =
                    new PartyTaxSchemeCommonAggregate();
            CompanyIDCommonBasic companyIDCommonBasic = new CompanyIDCommonBasic();

            boolean isPartyTaxSchemeAvailable = false;
            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {

                companyIDCommonBasic.setValue(ConversionUtils.removeEmptySpaces(customerDTO.getOrganizationNo()));
                companyIDCommonBasic.setSchemeID(country + ":" + taxTypeIntName);
                companyIDCommonBasic.setSchemeAgencyID(EHFConstants.PARTY_TAX_SCHEME_AGENCY_ID.getValue());
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
                registrationNameCB.setLanguageID(reminderDTO.getLanguage());
                registrationNameCB.setValue(customerDTO.getName());
                partyLegalEntityCommonAggregate.setRegistrationName(registrationNameCB);
                isPartyLegalEntity = true;
            }

            if (!StringUtils.isEmpty(customerDTO.getOrganizationNo())) {
                companyIDCommonBasic = new CompanyIDCommonBasic();
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
                payeePartyType.getPartyLegalEntities().add(partyLegalEntityCommonAggregate);
            }

            customerPartyType.setParty(party);
            reminder.setPayeeParty(payeePartyType);
            reminder.setAccountingCustomerParty(customerPartyType);
        }
    }

    /**
     * Method used to map customer bank account information from Reminder wrapper to Reminder.
     */
    private static void mapCustomerBankAccount(ReminderDTO reminderDTO, Reminder reminder) {

        CustomerDTO customerDTO = reminderDTO.getCustomerDTO();

        if (customerDTO != null) {

            String country = "";
            BranchType branchType = null;
            FinancialAccountType financialAccountType = null;
            FinancialInstitutionCommonAggregate financialInstitutionCommonAggregate = null;
            IDCommonBasic idCommonBasic = null;
            PaymentDueDateCommonBasic paymentDueDateCommonBasic = null;
            PaymentMeansCodeCommonBasic paymentMeansCodeCommonBasic = null;
            NameCommonBasic nameCommonBasic = null;
            PostalZoneCommonBasic postalZoneCommonBasic = null;
            BuildingNumberCommonBasic buildingNumberCommonBasic = null;
            StreetNameCommonBasic streetNameCommonBasic = null;
            PostboxCommonBasic postboxCommonBasic = null;
            CityNameCommonBasic cityNameCommonBasic = null;
            IdentificationCodeCommonBasic identificationCodeCommonBasic = null;

            BankAccountDTO bankAccountDTO = null;
            AddressDTO bankAddressDTO = null;
            AddressType bankAddress = null;

            boolean isFinancialInsAvailable = false;
            boolean isFinancialAddressAvailable = false;

            for (PaymentMeansCommonAggregate paymentMeansCommonAggregate : reminder.getPaymentMeans()) {

                isFinancialInsAvailable = false;
                isFinancialAddressAvailable = false;

                if (paymentMeansCommonAggregate == null) {
                    paymentMeansCommonAggregate = new PaymentMeansCommonAggregate();
                    reminder.getPaymentMeans().add(paymentMeansCommonAggregate);
                }

                if (reminderDTO.getPaymentDueDate() != null) {
                    paymentDueDateCommonBasic = new PaymentDueDateCommonBasic();
                    paymentDueDateCommonBasic.setValue(ConversionUtils.asXMLGregorianCalendar(reminderDTO.getPaymentDueDate()));
                    paymentMeansCommonAggregate.setPaymentDueDate(paymentDueDateCommonBasic);
                }

                paymentMeansCodeCommonBasic = new PaymentMeansCodeCommonBasic();
                paymentMeansCodeCommonBasic.setValue(EHFConstants.PAYMENT_MEANS_CODE.getValue());
                paymentMeansCodeCommonBasic.setListID(EHFConstants.PAYMENT_MEANS_LIST_ID.getValue());
                paymentMeansCodeCommonBasic.setListAgencyID(EHFConstants.PAYMENT_MEANS_LIST_AGENCY_ID.getValue());
                paymentMeansCommonAggregate.setPaymentMeansCode(paymentMeansCodeCommonBasic);

                bankAccountDTO = customerDTO.getBankAccountDTO();
                if (bankAccountDTO != null) {

                    financialAccountType = new FinancialAccountType();
                    idCommonBasic = new IDCommonBasic();

                    if (customerDTO.getAddressDTO() != null
                            && !StringUtils.isEmpty(customerDTO.getAddressDTO().getCountryCode())) {
                        country = customerDTO.getAddressDTO().getCountryCode().toUpperCase();
                    }
                    if (bankAccountDTO.getiBanNo() != null && !bankAccountDTO.getiBanNo().isEmpty()) {
                        idCommonBasic.setSchemeID(EHFConstants.IBAN.getValue());
                        idCommonBasic.setValue(country + bankAccountDTO.getiBanNo());
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
                        nameCommonBasic = new NameCommonBasic();
                        nameCommonBasic.setValue(bankAccountDTO.getBankName());
                        financialInstitutionCommonAggregate.setName(nameCommonBasic);
                        isFinancialInsAvailable = true;
                    }

                    bankAddressDTO = bankAccountDTO.getBankAddressDTO();

                    if (bankAddressDTO != null) {

                        bankAddress = new AddressType();

                        if (!StringUtils.isEmpty(bankAddressDTO.getBuildingNumber())) {
                            buildingNumberCommonBasic = new BuildingNumberCommonBasic();
                            buildingNumberCommonBasic.setValue(bankAddressDTO.getBuildingNumber());
                            bankAddress.setBuildingNumber(buildingNumberCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getStreetName())) {
                            streetNameCommonBasic = new StreetNameCommonBasic();
                            streetNameCommonBasic.setValue(bankAddressDTO.getStreetName());
                            bankAddress.setStreetName(streetNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalBox())) {
                            postboxCommonBasic = new PostboxCommonBasic();
                            postboxCommonBasic.setValue(bankAddressDTO.getPostalBox());
                            bankAddress.setPostbox(postboxCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getPostalZone())) {
                            postalZoneCommonBasic = new PostalZoneCommonBasic();
                            postalZoneCommonBasic.setValue(bankAddressDTO.getPostalZone());
                            bankAddress.setPostalZone(postalZoneCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCityName())) {
                            cityNameCommonBasic = new CityNameCommonBasic();
                            cityNameCommonBasic.setValue(bankAddressDTO.getCityName());
                            bankAddress.setCityName(cityNameCommonBasic);
                            isFinancialAddressAvailable = true;
                        }

                        if (!StringUtils.isEmpty(bankAddressDTO.getCountryCode())) {

                            country = bankAddressDTO.getCountryCode().toUpperCase();
                            identificationCodeCommonBasic = new IdentificationCodeCommonBasic();
                            identificationCodeCommonBasic.setValue(bankAddressDTO.getCountryCode().toUpperCase());
                            identificationCodeCommonBasic.setListID(EHFConstants.ADDRESS_LIST_ID.getValue());
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
     * Method used to map invoice exchange rate from Reminder wrapper to Reminder.
     */
    private static void mapExchangeRate(ReminderDTO reminderDTO, Reminder reminder) {

        // Set exchange rate
        if (reminderDTO.getExchangeRate() != null) {
            CalculationRateCommonBasic calculationRateCommonBasic =
                    new CalculationRateCommonBasic();
            calculationRateCommonBasic.setValue(ConversionUtils.asBigDecimal(reminderDTO.getExchangeRate()));
            ExchangeRateType exchangeRateType = new ExchangeRateType();
            exchangeRateType.setCalculationRate(calculationRateCommonBasic);

            if (reminderDTO.getCurrencyDTO() != null
                    && !StringUtils.isEmpty(reminderDTO.getCurrencyDTO().getCurrencyCode())) {
                SourceCurrencyCodeCommonBasic sourceCurrencyCodeCommonBasic =
                        new SourceCurrencyCodeCommonBasic();
                sourceCurrencyCodeCommonBasic.setValue(reminderDTO.getCurrencyDTO().getCurrencyCode());
                exchangeRateType.setSourceCurrencyCode(sourceCurrencyCodeCommonBasic);
            }

            if (reminderDTO.getInvoiceCurrencyBaseRate() != null) {
                SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                        new SourceCurrencyBaseRateCommonBasic();
                sourceCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(reminderDTO.getInvoiceCurrencyBaseRate()));
                exchangeRateType.setSourceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic);
            }

            if (reminderDTO.getBaseCurrencyDTO() != null
                    && !StringUtils.isEmpty(reminderDTO.getBaseCurrencyDTO().getCurrencyCode())) {
                TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                        new TargetCurrencyCodeCommonBasic();
                targetCurrencyCodeCommonBasic.setValue(reminderDTO.getBaseCurrencyDTO().getCurrencyCode());
                exchangeRateType.setTargetCurrencyCode(targetCurrencyCodeCommonBasic);
            }

            if (reminderDTO.getBaseCurrencyBaseRate() != null) {
                TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                        new TargetCurrencyBaseRateCommonBasic();
                targetCurrencyBaseRateCommonBasic.setValue(ConversionUtils.asBigDecimal(reminderDTO.getBaseCurrencyBaseRate()));
                exchangeRateType.setTargetCurrencyBaseRate(targetCurrencyBaseRateCommonBasic);

                MathematicOperatorCodeCommonBasic operator =
                        new MathematicOperatorCodeCommonBasic();
                operator.setValue(EHFConstants.OP_MULTIPLY.getValue());
                exchangeRateType.setMathematicOperatorCode(operator);
            }
            reminder.setPricingExchangeRate(exchangeRateType);
        }
    }

    /**
     * Method used to map reminder line details from Reminder wrapper to Reminder.
     */
    private static void mapReminderLine(ReminderDTO reminderDTO, Reminder reminder) {

        List<ReminderLineDTO> reminderLineDTOs = reminderDTO.getReminderLines();

        if (reminderLineDTOs != null) {

            NoteCommonBasic noteCommonBasic = null;
            ItemCommonAggregate item = null;
            ReminderLineCommonAggregate reminderLine = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
            ItemIdentificationType sellersItemIdentification = null;
            TaxCategoryType taxCategoryType = null;
            MonetaryTotalType monetaryTotalType = null;
            NameCommonBasic nameCommonBasic = null;
            IDCommonBasic idCommonBasic = null;
            PayableAmountCommonBasic payableAmountCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionTotalAmount = null;
            TaxExclusiveAmountCommonBasic taxExclusiveAmountCommonBasic = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            PayableRoundingAmountCommonBasic payableRoundingAmountCommonBasic = null;
            DebitLineAmountCommonBasic debitLineAmountCB;
            CreditLineAmountCommonBasic creditLineAmountCB;
            EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCommonBasic = null;
            DocumentReferenceType documentReferenceType = null;
            DocumentTypeCommonBasic documentTypeCommonBasic = null;
            BillingReferenceCommonAggregate billingReferenceCommonAggregate = null;
            AttachmentType attachmentType = null;

            FileDTO documentReferenceFile = null;

            String currencyCode = "";
            CurrencyDTO currencyDTO = reminderDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            for (ReminderLineDTO reminderLineDTO : reminderLineDTOs) {

                reminderLine = new ReminderLineCommonAggregate();

                if (!StringUtils.isEmpty(reminderLineDTO.getId())) {
                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(reminderLineDTO.getId());
                    reminderLine.setID(idCommonBasic);
                }

                if (!StringUtils.isEmpty(reminderLineDTO.getNote())) {
                    noteCommonBasic = new NoteCommonBasic();
                    noteCommonBasic.setValue(reminderLineDTO.getNote());
                    noteCommonBasic.setLanguageID(reminderDTO.getLanguage());
                    reminderLine.setNote(noteCommonBasic);
                }

                if (reminderLineDTO.getDebitLineAmount() != null) {
                    debitLineAmountCB = new DebitLineAmountCommonBasic();
                    debitLineAmountCB.setValue(ConversionUtils.asBigDecimal(reminderLineDTO.getDebitLineAmount()));
                    debitLineAmountCB.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                    reminderLine.setDebitLineAmount(debitLineAmountCB);
                }

                if (reminderLineDTO.getCreditLineAmount() != null) {
                    creditLineAmountCB = new CreditLineAmountCommonBasic();
                    creditLineAmountCB.setValue(ConversionUtils.asBigDecimal(reminderLineDTO.getCreditLineAmount()));
                    creditLineAmountCB.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                    reminderLine.setCreditLineAmount(creditLineAmountCB);
                }

                documentReferenceFile = reminderLineDTO.getInvoiceDocumentReference();
                if (documentReferenceFile != null) {

                    embeddedDocBinObjCommonBasic = new EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(documentReferenceFile.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(BinaryObjectMimeCodeContentType.fromValue(documentReferenceFile.getMimeType()));
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode(BinaryObjectMimeCodeContentType.APPLICATION_PDF);
                    }
                    embeddedDocBinObjCommonBasic.setValue(documentReferenceFile.getFileContent());

                    documentReferenceType = new DocumentReferenceType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(EHFConstants.DOCUMENT.getValue() + 1);
                    documentReferenceType.setID(idCommonBasic);

                    documentTypeCommonBasic = new DocumentTypeCommonBasic();
                    documentTypeCommonBasic.setValue(documentReferenceFile.getDocType());
                    documentReferenceType.setDocumentType(documentTypeCommonBasic);

                    attachmentType = new AttachmentType();
                    attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocBinObjCommonBasic);
                    documentReferenceType.setAttachment(attachmentType);

                    billingReferenceCommonAggregate = new BillingReferenceCommonAggregate();
                    billingReferenceCommonAggregate.setInvoiceDocumentReference(documentReferenceType);
                }

                documentReferenceFile = reminderLineDTO.getCreditNoteDoucmentReference();
                if (documentReferenceFile != null) {

                    embeddedDocBinObjCommonBasic = new EmbeddedDocumentBinaryObjectCommonBasic();
                    if (!StringUtils.isEmpty(documentReferenceFile.getMimeType())) {
                        embeddedDocBinObjCommonBasic.setMimeCode(BinaryObjectMimeCodeContentType.fromValue(documentReferenceFile.getMimeType()));
                    } else {
                        embeddedDocBinObjCommonBasic.setMimeCode(BinaryObjectMimeCodeContentType.APPLICATION_PDF);
                    }
                    embeddedDocBinObjCommonBasic.setValue(documentReferenceFile.getFileContent());

                    documentReferenceType = new DocumentReferenceType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(EHFConstants.DOCUMENT.getValue() + 1);
                    documentReferenceType.setID(idCommonBasic);

                    documentTypeCommonBasic = new DocumentTypeCommonBasic();
                    documentTypeCommonBasic.setValue(documentReferenceFile.getDocType());
                    documentReferenceType.setDocumentType(documentTypeCommonBasic);

                    attachmentType = new AttachmentType();
                    attachmentType.setEmbeddedDocumentBinaryObject(embeddedDocBinObjCommonBasic);
                    documentReferenceType.setAttachment(attachmentType);

                    billingReferenceCommonAggregate = new BillingReferenceCommonAggregate();
                    billingReferenceCommonAggregate.setCreditNoteDocumentReference(documentReferenceType);
                }

                if (billingReferenceCommonAggregate != null) {
                    reminderLine.getBillingReferences().add(billingReferenceCommonAggregate);
                }

                idCommonBasic = new IDCommonBasic();
                idCommonBasic.setValue(reminderLineDTO.getProductNo());
                idCommonBasic.setSchemeID(EHFConstants.GTIN.getValue());
                idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                sellersItemIdentification = new ItemIdentificationType();
                sellersItemIdentification.setID(idCommonBasic);
                item = new ItemCommonAggregate();
                item.setSellersItemIdentification(sellersItemIdentification);

                nameCommonBasic = new NameCommonBasic();
                nameCommonBasic.setValue(reminderLineDTO.getProductName());
                item.setName(nameCommonBasic);

                if (!StringUtils.isEmpty(reminderLineDTO.getAccountingCode())) {
                    accountingCostCommonBasic = new AccountingCostCommonBasic();
                    accountingCostCommonBasic.setValue(reminderLineDTO.getAccountingCode());
                    reminderLine.setAccountingCost(accountingCostCommonBasic);
                }

                if (reminderLineDTO.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(reminderLineDTO.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
                    idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                    taxCategoryType.setID(idCommonBasic);

                    percentCommonBasic = new PercentCommonBasic();
                    percentCommonBasic.setValue(ConversionUtils.asBigDecimal(reminderLineDTO.getTaxPercent()));
                    taxCategoryType.setPercent(percentCommonBasic);

                    if (!StringUtils.isEmpty(reminderLineDTO.getTaxTypeIntName())) {

                        idCommonBasic = new IDCommonBasic();
                        idCommonBasic.setValue(reminderLineDTO.getTaxTypeIntName());
                        idCommonBasic.setSchemeID(EHFConstants.TAX_SCHEME_ID.getValue());
                        idCommonBasic.setSchemeAgencyID(EHFConstants.TAX_SCHEME_AGENCY_ID.getValue());
                        taxSchemeCommonAggregate = new TaxSchemeCommonAggregate();
                        taxSchemeCommonAggregate.setID(idCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    item.getClassifiedTaxCategories().add(taxCategoryType);
                }

                reminder.getReminderLines().add(reminderLine);
            }

            monetaryTotalType = reminder.getLegalMonetaryTotal();
            if (monetaryTotalType == null) {
                monetaryTotalType = new MonetaryTotalType();
                reminder.setLegalMonetaryTotal(monetaryTotalType);
            }

            BigDecimal taxExclusiveAmount = null;
            if (reminderDTO.getTotalExcTax() != null) {

                lineExtensionTotalAmount = new LineExtensionAmountCommonBasic();
                lineExtensionTotalAmount.setValue(ConversionUtils.asBigDecimal(reminderDTO.getTotalExcTax()));
                lineExtensionTotalAmount.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setLineExtensionAmount(lineExtensionTotalAmount);

                taxExclusiveAmount = ConversionUtils.asBigDecimal(reminderDTO.getTotalExcTax());

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
                taxExclusiveAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setTaxExclusiveAmount(taxExclusiveAmountCommonBasic);
            }

            if (taxExclusiveAmount != null) {

                BigDecimal taxInclusiveAmount =
                        reminderDTO.getTaxAmount() != null ? taxExclusiveAmount.add(ConversionUtils.asBigDecimal(reminderDTO.getTaxAmount()))
                                : taxExclusiveAmount;

                payableRoundingAmountCommonBasic = new PayableRoundingAmountCommonBasic();
                payableRoundingAmountCommonBasic.setValue(ConversionUtils.getRoundingAmount(taxInclusiveAmount));
                payableRoundingAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setPayableRoundingAmount(payableRoundingAmountCommonBasic);

                BigDecimal payableAmount =
                        taxInclusiveAmount.add(payableRoundingAmountCommonBasic.getValue());

                taxInclusiveAmountCommonBasic = new TaxInclusiveAmountCommonBasic();
                taxInclusiveAmountCommonBasic.setValue(payableAmount);
                taxInclusiveAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setTaxInclusiveAmount(taxInclusiveAmountCommonBasic);

                payableAmountCommonBasic = new PayableAmountCommonBasic();
                payableAmountCommonBasic.setValue(payableAmount);
                payableAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                monetaryTotalType.setPayableAmount(payableAmountCommonBasic);
            }

            reminder.setLegalMonetaryTotal(monetaryTotalType);
        }
    }

    /**
     * Method used to map tax summary details from Reminder wrapper to Reminder.
     */
    private static void mapTaxSummaries(ReminderDTO reminderDTO, Reminder reminder) {

        List<TaxSummaryDTO> taxSummaries = reminderDTO.getTaxSummaries();

        if (taxSummaries != null) {

            TaxCategoryType taxCategoryType = null;
            IDCommonBasic idCommonBasic = null;
            PercentCommonBasic percentCommonBasic = null;
            TaxAmountCommonBasic taxAmountCommonBasic = null;
            TaxSubtotalCommonAggregate taxSubtotalCommonAggregate = null;
            TaxableAmountCommonBasic taxableAmountCommonBasic = null;
            TaxExemptionReasonCodeCommonBasic taxExemptionReasonCodeCommonBasic = null;
            TaxExemptionReasonCommonBasic taxExemptionReasonCommonBasic = null;
            TaxTotalCommonAggregate taxTotalCommonAggregate = null;
            TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

            String currencyCode = null;
            CurrencyDTO currencyDTO = reminderDTO.getCurrencyDTO();
            if (currencyDTO != null && !StringUtils.isEmpty(currencyDTO.getCurrencyCode())) {
                currencyCode = currencyDTO.getCurrencyCode();
            }

            taxTotalCommonAggregate = new TaxTotalCommonAggregate();
            for (TaxSummaryDTO taxSummary : taxSummaries) {

                taxSubtotalCommonAggregate = new TaxSubtotalCommonAggregate();
                if (taxSummary.getTotalExcTax() != null) {
                    taxableAmountCommonBasic = new TaxableAmountCommonBasic();
                    taxableAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTotalExcTax()));
                    taxableAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                    taxSubtotalCommonAggregate.setTaxableAmount(taxableAmountCommonBasic);
                }

                if (taxSummary.getTaxAmount() != null) {
                    taxAmountCommonBasic = new TaxAmountCommonBasic();
                    taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(taxSummary.getTaxAmount()));
                    taxAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                    taxSubtotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
                }

                if (taxSummary.getTaxPercent() != null) {

                    taxCategoryType = new TaxCategoryType();

                    idCommonBasic = new IDCommonBasic();
                    idCommonBasic.setValue(ConversionUtils.getTaxCategoryCode(taxSummary.getTaxPercent()));
                    idCommonBasic.setSchemeID(EHFConstants.TAX_CATEGORY_SCHEME_ID.getValue());
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

                        taxExemptionReasonCodeCommonBasic = new TaxExemptionReasonCodeCommonBasic();
                        taxExemptionReasonCodeCommonBasic.setValue(EHFConstants.TAX_EXEMPTION_REASON_CODE.getValue());
                        taxCategoryType.setTaxExemptionReasonCode(taxExemptionReasonCodeCommonBasic);

                        taxExemptionReasonCommonBasic = new TaxExemptionReasonCommonBasic();
                        taxExemptionReasonCommonBasic.setValue(EHFConstants.TAX_EXEMPTION_REASON.getValue());
                        taxCategoryType.setTaxExemptionReason(taxExemptionReasonCommonBasic);
                    }

                    taxCategoryType.setTaxScheme(taxSchemeCommonAggregate);
                    taxSubtotalCommonAggregate.setTaxCategory(taxCategoryType);
                }

                if (taxSubtotalCommonAggregate != null) {
                    taxTotalCommonAggregate.getTaxSubtotals().add(taxSubtotalCommonAggregate);
                }
            }

            if (reminderDTO.getTaxAmount() != null) {
                taxAmountCommonBasic = new TaxAmountCommonBasic();
                taxAmountCommonBasic.setCurrencyID(CurrencyCodeContentType.fromValue(currencyCode));
                taxAmountCommonBasic.setValue(ConversionUtils.asBigDecimal(reminderDTO.getTaxAmount()));
                taxTotalCommonAggregate.setTaxAmount(taxAmountCommonBasic);
            }
            reminder.getTaxTotals().add(taxTotalCommonAggregate);
        }
    }

    /**
     * Unmarshalling XML file to reminder object.
     */
    public static ReminderDTO getReminder(File file) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Reminder.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        Reminder reminder = (Reminder) unmarshaller.unmarshal(file);

        return convertToReminderDTO(reminder);
    }

    /**
     * Method used to convert EHF Reminder XML file to Reminder object
     */
    public static ReminderDTO getReminder(String filePath) throws JAXBException {

        File file = new File(filePath);
        return getReminder(file);
    }

    /**
     * Method used to convert EHF Reminder XML file stream to EHF Reminder object
     */
    public static ReminderDTO getReminder(InputStream is) throws JAXBException {

        final JAXBContext context = JAXBContext.newInstance(Reminder.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        Reminder reminder = (Reminder) unmarshaller.unmarshal(is);

        return convertToReminderDTO(reminder);
    }

    /**
     * Method used to convert Reminder to ReminderDTO wrapper.
     */
    public static ReminderDTO convertToReminderDTO(Reminder reminder) {

        ReminderDTO reminderDTO = null;
        if (reminder != null) {

            reminderDTO = new ReminderDTO();

            // Set UBL version id
            UBLVersionIDCommonBasic ublVersionIDCommonBasic = reminder.getUBLVersionID();
            if (ublVersionIDCommonBasic != null) {
                reminderDTO.setUblExtensions(ublVersionIDCommonBasic.getValue());
            }

            // Set customization id
            CustomizationIDCommonBasic customizationIDCommonBasic = reminder.getCustomizationID();
            if (customizationIDCommonBasic != null) {
                reminderDTO.setCustomizationId(customizationIDCommonBasic.getValue());
            }

            // Set profile id
            ProfileIDCommonBasic profileIDCommonBasic = reminder.getProfileID();
            if (profileIDCommonBasic != null) {
                reminderDTO.setProfileID(profileIDCommonBasic.getValue());
            }

            // Set invoice no
            IDCommonBasic idCommonBasic = reminder.getID();
            if (idCommonBasic != null) {
                reminderDTO.setReminderId(idCommonBasic.getValue());
            }

            // Set issue date
            IssueDateCommonBasic issueDateCommonBasic = reminder.getIssueDate();
            if (issueDateCommonBasic != null) {
                reminderDTO.setIssueDate(ConversionUtils.asDate(issueDateCommonBasic.getValue()));
            }

            ReminderTypeCodeCommonBasic reminderTypeCodeCommonBasic =
                    reminder.getReminderTypeCode();
            if (reminderTypeCodeCommonBasic != null) {
                reminder.setReminderTypeCode(reminder.getReminderTypeCode());
            }

            ReminderSequenceNumericCommonBasic reminderSeqNumericCB =
                    reminder.getReminderSequenceNumeric();
            if (reminderSeqNumericCB != null) {
                reminderDTO.setReminderSequenceNo(reminderSeqNumericCB.getValue().doubleValue());
            }

            List<NoteCommonBasic> notes = reminder.getNotes();
            if (notes != null && !notes.isEmpty()) {
                for (NoteCommonBasic noteCommonBasic : notes) {
                    reminderDTO.getNotes().add(noteCommonBasic.getValue());
                    reminderDTO.setLanguage(noteCommonBasic.getLanguageID());
                }
            }

            mapPayeeParty(reminder, reminderDTO);
            mapCurrency(reminder, reminderDTO);
            mapSupplier(reminder, reminderDTO);
            mapSupplierBankAccount(reminder, reminderDTO);
            mapCustomer(reminder, reminderDTO);
            mapCustomerBankAccount(reminder, reminderDTO);
            mapExchangeRate(reminder, reminderDTO);
            mapReminderLine(reminder, reminderDTO);
            mapTaxSummaries(reminder, reminderDTO);
        }

        return reminderDTO;
    }

    /**
     * Method used to map payee information from EHF Reminder to Reminder wrapper.
     */
    private static void mapPayeeParty(Reminder reminder, ReminderDTO reminderDTO) {

        PartyType partyType = reminder.getPayeeParty();

        if (partyType != null) {

            PartyType party = new PartyType();
            PayeeParty payeeParty = new PayeeParty();

            IDCommonBasic idCommonBasic = null;

            List<PartyIdentificationCommonAggregate> partyIdentifications =
                    party.getPartyIdentifications();
            if (partyIdentifications != null) {

                for (PartyIdentificationCommonAggregate partyIdentification : partyIdentifications) {
                    if (partyIdentification.getID() != null) {
                        payeeParty.setId(partyIdentification.getID().getValue());
                    }
                }
            }

            List<PartyNameCommonAggregate> partyNames = party.getPartyNames();
            if (partyNames != null) {
                for (PartyNameCommonAggregate partyName : partyNames) {
                    NameCommonBasic nameCommonBasic = partyName.getName();
                    if (nameCommonBasic != null) {
                        payeeParty.setName(nameCommonBasic.getValue());
                    }
                }
            }

            List<PartyLegalEntityCommonAggregate> partyLegalEntities =
                    party.getPartyLegalEntities();
            if (partyLegalEntities != null) {
                for (PartyLegalEntityCommonAggregate partyLegalEntity : partyLegalEntities) {
                    CompanyIDCommonBasic companyIDCommonBasic = partyLegalEntity.getCompanyID();
                    if (companyIDCommonBasic != null) {
                        payeeParty.setOrganizationNo(companyIDCommonBasic.getValue());
                    }
                }
            }

            ContactType contactType = partyType.getContact();
            if (contactType != null) {

                idCommonBasic = contactType.getID();
                if (idCommonBasic != null) {
                    payeeParty.setContactId(idCommonBasic.getValue());
                }

                TelephoneCommonBasic telephoneCommonBasic = contactType.getTelephone();
                if (telephoneCommonBasic != null) {
                    payeeParty.setTelePhone(telephoneCommonBasic.getValue());
                }

                TelefaxCommonBasic telefaxCommonBasic = contactType.getTelefax();
                if (telefaxCommonBasic != null) {
                    payeeParty.setTeleFax(telefaxCommonBasic.getValue());
                }

                ElectronicMailCommonBasic electronicMailCommonBasic =
                        contactType.getElectronicMail();
                if (electronicMailCommonBasic != null) {
                    payeeParty.setEmail(electronicMailCommonBasic.getValue());
                }
            }

            AddressDTO addressDTO = null;
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
        }
    }

    /**
     * Method used to map customer currency information from Reminder to ReminderDTO wrapper.
     */
    private static void mapCurrency(Reminder reminder, ReminderDTO reminderDTO) {

        DocumentCurrencyCodeCommonBasic documentCurrencyCodeCommonBasic =
                reminder.getDocumentCurrencyCode();
        CurrencyDTO currencyDTO = null;

        if (documentCurrencyCodeCommonBasic != null) {
            currencyDTO = new CurrencyDTO();
            currencyDTO.setCurrencyCode(documentCurrencyCodeCommonBasic.getValue());
            currencyDTO.setCurrencyName(documentCurrencyCodeCommonBasic.getValue());
            reminderDTO.setCurrencyDTO(currencyDTO);
        }
    }

    /**
     * Method used to map supplier information from Reminder to ReminderDTO wrapper.
     */
    private static void mapSupplier(Reminder reminder, ReminderDTO reminderDTO) {

        SupplierPartyType supplierPartyType = reminder.getAccountingSupplierParty();

        if (supplierPartyType != null) {

            SupplierDTO supplierDTO = new SupplierDTO();
            AddressDTO addressDTO = null;

            PartyType party = supplierPartyType.getParty();

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

            if (party.getPartyTaxSchemes() != null && !party.getPartyTaxSchemes().isEmpty()) {

                TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;
                IDCommonBasic idCommonBasic = null;
                for (PartyTaxSchemeCommonAggregate partyTaxSchemeCommonAggregate : party.getPartyTaxSchemes()) {
                    taxSchemeCommonAggregate = partyTaxSchemeCommonAggregate.getTaxScheme();
                    if (taxSchemeCommonAggregate != null) {
                        idCommonBasic = taxSchemeCommonAggregate.getID();
                        if (idCommonBasic != null) {
                            reminderDTO.setTaxType(idCommonBasic.getValue());
                        }
                    }
                }
            }

            reminderDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map supplier bank account information from Reminder to ReminderDTO wrapper.
     */
    private static void mapSupplierBankAccount(Reminder reminder, ReminderDTO reminderDTO) {

        List<PaymentMeansCommonAggregate> paymentMeans = reminder.getPaymentMeans();

        if (paymentMeans != null) {

            BankAccountDTO bankAccountDTO = null;
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
                    reminderDTO.setPaymentId(paymentIDs.get(0).getValue());
                }

                financialAccountType = paymentMeansCommonAggregate.getPayeeFinancialAccount();
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

            SupplierDTO supplierDTO = reminderDTO.getSupplierDTO();
            if (supplierDTO == null) {
                supplierDTO = new SupplierDTO();
            }
            supplierDTO.setBankAccountDTO(bankAccountDTO);
            reminderDTO.setSupplierDTO(supplierDTO);
        }
    }

    /**
     * Method used to map customer information from Reminder to ReminderDTO wrapper.
     */
    private static void mapCustomer(Reminder reminder, ReminderDTO reminderDTO) {

        CustomerPartyType customerPartyType = reminder.getAccountingCustomerParty();

        if (customerPartyType != null) {

            CustomerDTO customerDTO = new CustomerDTO();
            AddressDTO addressDTO = null;

            PartyType party = customerPartyType.getParty();
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
            reminderDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map customer bank account information from Reminder to ReminderDTO wrapper.
     */
    private static void mapCustomerBankAccount(Reminder reminder, ReminderDTO reminderDTO) {

        List<PaymentMeansCommonAggregate> paymentMeans = reminder.getPaymentMeans();

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
                    reminderDTO.setPaymentDueDate(ConversionUtils.asDate(paymentDueDateCommonBasic.getValue()));
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

            CustomerDTO customerDTO = reminderDTO.getCustomerDTO();
            if (customerDTO == null) {
                customerDTO = new CustomerDTO();
            }
            customerDTO.setBankAccountDTO(bankAccountDTO);
            reminderDTO.setCustomerDTO(customerDTO);
        }
    }

    /**
     * Method used to map invoice exchange rate from Reminder to ReminderDTO wrapper.
     */
    private static void mapExchangeRate(Reminder reminder, ReminderDTO reminderDTO) {

        // Set exchange rate
        ExchangeRateType exchangeRateType = reminder.getPricingExchangeRate();
        if (exchangeRateType != null) {
            CalculationRateCommonBasic calculationRateCommonBasic =
                    exchangeRateType.getCalculationRate();
            if (calculationRateCommonBasic != null && calculationRateCommonBasic.getValue() != null) {
                reminderDTO.setExchangeRate(calculationRateCommonBasic.getValue().doubleValue());
            }

            SourceCurrencyBaseRateCommonBasic sourceCurrencyBaseRateCommonBasic =
                    exchangeRateType.getSourceCurrencyBaseRate();
            if (sourceCurrencyBaseRateCommonBasic != null
                    && sourceCurrencyBaseRateCommonBasic.getValue() != null) {
                reminderDTO.setInvoiceCurrencyBaseRate(sourceCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }

            TargetCurrencyCodeCommonBasic targetCurrencyCodeCommonBasic =
                    exchangeRateType.getTargetCurrencyCode();
            if (targetCurrencyCodeCommonBasic != null
                    && targetCurrencyCodeCommonBasic.getValue() != null) {
                CurrencyDTO baseCurrencyDTO = new CurrencyDTO();
                baseCurrencyDTO.setCurrencyCode(targetCurrencyCodeCommonBasic.getValue());
                reminderDTO.setBaseCurrencyDTO(baseCurrencyDTO);
            }

            TargetCurrencyBaseRateCommonBasic targetCurrencyBaseRateCommonBasic =
                    exchangeRateType.getTargetCurrencyBaseRate();
            if (targetCurrencyBaseRateCommonBasic != null
                    && targetCurrencyBaseRateCommonBasic.getValue() != null) {
                reminderDTO.setBaseCurrencyBaseRate(targetCurrencyBaseRateCommonBasic.getValue().doubleValue());
            }
        }
    }

    /**
     * Method used to map reminder line item details from Reminder to ReminderDTO wrapper.
     */
    private static void mapReminderLine(Reminder reminder, ReminderDTO reminderDTO) {

        List<ReminderLineCommonAggregate> reminderLineItems = reminder.getReminderLines();
        if (reminderLineItems != null) {

            ReminderLineDTO reminderLineDTO = null;

            MonetaryTotalType legalMonetaryTotal = null;
            IDCommonBasic idCommonBasic = null;
            LineExtensionAmountCommonBasic lineExtensionAmount = null;
            TaxInclusiveAmountCommonBasic taxInclusiveAmountCommonBasic = null;
            AccountingCostCommonBasic accountingCostCommonBasic = null;
            NoteCommonBasic noteCommonBasic = null;
            DebitLineAmountCommonBasic debitLineAmountCB = null;
            CreditLineAmountCommonBasic creditLineAmountCB = null;
            EmbeddedDocumentBinaryObjectCommonBasic embeddedDocBinObjCB = null;
            DocumentReferenceType documentReferenceType = null;
            AttachmentType attachmentType = null;
            List<BillingReferenceCommonAggregate> billingReferences = null;

            FileDTO fileDTO = null;

            for (ReminderLineCommonAggregate reminderLine : reminderLineItems) {

                reminderLineDTO = new ReminderLineDTO();

                idCommonBasic = reminderLine.getID();
                if (idCommonBasic != null && idCommonBasic.getValue() != null) {
                    reminderLineDTO.setId(idCommonBasic.getValue());
                }

                noteCommonBasic = reminderLine.getNote();
                if (noteCommonBasic != null && noteCommonBasic.getValue() != null) {
                    reminderLineDTO.setNote(noteCommonBasic.getValue());
                }

                debitLineAmountCB = reminderLine.getDebitLineAmount();
                if (debitLineAmountCB != null) {
                    reminderLineDTO.setDebitLineAmount(debitLineAmountCB.getValue().doubleValue());
                }

                creditLineAmountCB = reminderLine.getCreditLineAmount();
                if (creditLineAmountCB != null) {
                    reminderLineDTO.setCreditLineAmount(creditLineAmountCB.getValue().doubleValue());
                }

                billingReferences = reminderLine.getBillingReferences();
                if (billingReferences != null && !billingReferences.isEmpty()) {

                    for (BillingReferenceCommonAggregate billingReference : billingReferences) {

                        documentReferenceType = billingReference.getInvoiceDocumentReference();
                        if (documentReferenceType != null) {
                            attachmentType = documentReferenceType.getAttachment();
                            if (attachmentType != null) {
                                embeddedDocBinObjCB =
                                        attachmentType.getEmbeddedDocumentBinaryObject();
                                if (embeddedDocBinObjCB != null) {
                                    fileDTO = new FileDTO();
                                    fileDTO.setFileContent(embeddedDocBinObjCB.getValue());
                                    reminderLineDTO.setInvoiceDocumentReference(fileDTO);
                                }
                            }
                        }
                        documentReferenceType = billingReference.getCreditNoteDocumentReference();
                        if (documentReferenceType != null) {
                            attachmentType = documentReferenceType.getAttachment();
                            if (attachmentType != null) {
                                embeddedDocBinObjCB =
                                        attachmentType.getEmbeddedDocumentBinaryObject();
                                if (embeddedDocBinObjCB != null) {
                                    fileDTO = new FileDTO();
                                    fileDTO.setFileContent(embeddedDocBinObjCB.getValue());
                                    reminderLineDTO.setCreditNoteDoucmentReference(fileDTO);
                                }
                            }
                        }
                    }
                }

                accountingCostCommonBasic = reminderLine.getAccountingCost();
                if (accountingCostCommonBasic != null
                        && accountingCostCommonBasic.getValue() != null) {
                    reminderLineDTO.setAccountingCode(accountingCostCommonBasic.getValue());
                }

                reminderDTO.getReminderLines().add(reminderLineDTO);
            }

            legalMonetaryTotal = reminder.getLegalMonetaryTotal();
            if (legalMonetaryTotal != null) {

                lineExtensionAmount = legalMonetaryTotal.getLineExtensionAmount();
                if (lineExtensionAmount != null && lineExtensionAmount.getValue() != null) {
                    reminderDTO.setTotalExcTax(lineExtensionAmount.getValue().doubleValue());
                }

                taxInclusiveAmountCommonBasic = legalMonetaryTotal.getTaxInclusiveAmount();
                if (taxInclusiveAmountCommonBasic != null
                        && taxInclusiveAmountCommonBasic.getValue() != null) {
                    reminderDTO.setTotalAmount(taxInclusiveAmountCommonBasic.getValue().doubleValue());
                }
            }
        }
    }

    /**
     * Method used to map tax summary details from Reminder to ReminderDTO wrapper.
     */
    private static void mapTaxSummaries(Reminder reminder, ReminderDTO reminderDTO) {

        List<TaxTotalCommonAggregate> taxTotals = reminder.getTaxTotals();
        List<TaxSummaryDTO> taxSummaries = new ArrayList<TaxSummaryDTO>();
        List<TaxSubtotalCommonAggregate> taxSubtotals = null;

        TaxSummaryDTO taxSummary = null;

        IDCommonBasic idCommonBasic = null;
        TaxableAmountCommonBasic taxableAmountCommonBasic = null;
        PercentCommonBasic percentCommonBasic = null;
        TaxAmountCommonBasic taxAmountCommonBasic = null;
        TaxCategoryType taxCategoryType = null;
        TaxSchemeCommonAggregate taxSchemeCommonAggregate = null;

        for (TaxTotalCommonAggregate taxTotalCommonAggregate : taxTotals) {

            taxAmountCommonBasic = taxTotalCommonAggregate.getTaxAmount();
            if (taxAmountCommonBasic != null && taxAmountCommonBasic.getValue() != null) {
                reminderDTO.setTaxAmount(taxAmountCommonBasic.getValue().doubleValue());
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
        reminderDTO.setTaxSummaries(taxSummaries);
    }

    public static boolean isValidLineExtensionAmount(ReminderDTO reminderDTO) {

        double totalExcAmt = 0d;
        for (ReminderLineDTO reminderLineDTO : reminderDTO.getReminderLines()) {

            totalExcAmt +=
                    reminderLineDTO.getDebitLineAmount() - reminderLineDTO.getCreditLineAmount();

        }
        return (totalExcAmt == reminderDTO.getTotalExcTax());
    }
}
