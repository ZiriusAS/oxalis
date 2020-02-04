/*
 * @(#)TestData.java
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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ehandel.no.AllowanceChargeReasons;
import ehandel.no.OrderResponseCode;
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
import ehandel.no.dto.InvoiceSettingDTO;
import ehandel.no.dto.ManufacturerPartyDTO;
import ehandel.no.dto.OrderDTO;
import ehandel.no.dto.OrderResponseDTO;
import ehandel.no.dto.PayeeParty;
import ehandel.no.dto.ReminderDTO;
import ehandel.no.dto.ReminderLineDTO;
import ehandel.no.dto.SubstitutedLineItemDTO;
import ehandel.no.dto.SupplierDTO;
import ehandel.no.dto.TaxRepresentativeDTO;
import ehandel.no.dto.TaxSummaryDTO;

/**
 * The Class TestData.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public final class TestData {

    public static final String INVOICE_FILE_NAME = "invoice.xml";
    public static final String INVOICE_EHF_V2_FILE_NAME = "invoice-v2.xml";
    public static final String CREDITNOTE_FILE_NAME = "creditnote.xml";
    public static final String CREDITNOTE_EHF_V2_FILE_NAME = "creditnote-v2.xml";
    public static final String REMINDER_FILE_NAME = "reminder.xml";
    public static final String ORDER_FILE_NAME = "order_zerp.xml";
    public static final String ORDER_RESPONSE_FILE_NAME = "order_response_zerp.xml";
    public static final byte INVOICE = 1;
    public static final byte CREDITNOTE = 2;
    public static final byte REMINDER = 3;
    public static final byte ORDER = 4;

    public TestData() {
    }

    public static InvoiceDTO getEHFInvoiceInfo(byte invoiceType) throws ParseException, IOException {

        InvoiceDTO invoiceDTO = new InvoiceDTO();

        Date date = new Date();

        invoiceDTO.setIsRoundPayableAmount(true);
        invoiceDTO.setInvoiceNo("TOSL108");
        invoiceDTO.setPurchaseOrderNo("Prosjekt 13");
        invoiceDTO.setExchangeRate(8.92d);
        invoiceDTO.setIssueDate(date);
        invoiceDTO.setPaymentDueDate(date);
        invoiceDTO.setBaseCurrencyBaseRate(1d);
        invoiceDTO.setInvoiceCurrencyBaseRate(1d);
        invoiceDTO.setSupplierDTO(getSupplierInfo());
        invoiceDTO.setCustomerDTO(getCustomerInfo());
        invoiceDTO.setDeliveryDTO(getDeliveryInfo());
        invoiceDTO.getInvoiceSettingDTOs().add(getPaymentTerms());
        invoiceDTO.setPayeeParty(getPayeeParty());
        invoiceDTO.setCurrencyDTO(getCurrencyInfo());
        invoiceDTO.setBaseCurrencyDTO(getBaseCurrencyInfo());
        invoiceDTO.setFiles(getFiles());
        invoiceDTO.getNotes().add("Demonstration of shipment via EHF");
        invoiceDTO.getNotes().add("Another sample note");
        invoiceDTO.setPaymentId("10706416");

        if (INVOICE == invoiceType) {
            invoiceDTO.setTotalExcTax(792d);
            invoiceDTO.setTotalAmount(872d);
            invoiceDTO.setTaxAmount(79.2d);
        } else {
            invoiceDTO.setTaxAmount(120d);
            invoiceDTO.setTotalExcTax(800d);
            invoiceDTO.setTotalAmount(920d);
        }

        invoiceDTO.getInvoiceLineItems().add(getInvoiceLineItem(invoiceType));
        invoiceDTO.getTaxSummaries().add(getTaxSummary(invoiceType));
        invoiceDTO.getAllowanceCharges().add(getAllowanceAmount());
        invoiceDTO.getAllowanceCharges().add(getChargeAmount());
        invoiceDTO.getBillingReferenceDTOs().add(getBillingReference());
        invoiceDTO.setContractDTO(getContractInfo());
        invoiceDTO.setTaxRepresentation(getTaxRepresentative());

        return invoiceDTO;
    }

    public static ContractDTO getContractInfo() {

        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setContractId("Contract321");
        contractDTO.setContractType(2);
        contractDTO.setDocumentType("Framework agreement");
        return contractDTO;
    }

    public static BillingReferenceDTO getBillingReference() {

        BillingReferenceDTO billingRef = new BillingReferenceDTO();
        billingRef.setInvoiceDocumentReference("TOSL108");
        billingRef.setBillingReferenceLine("1");
        return billingRef;
    }

    public static TaxRepresentativeDTO getTaxRepresentative() {

        TaxRepresentativeDTO taxRep = new TaxRepresentativeDTO();
        taxRep.setName("Tax handling company AS");
        AddressDTO postalAddress = getSupplierAddressInfo();
        taxRep.setPostalAddress(postalAddress);
        return taxRep;
    }

    public static ContactPersonDTO getContactPersonInfo() {

        ContactPersonDTO contactPerson = new ContactPersonDTO();
        contactPerson.setId("Our ref.");
        contactPerson.setName("Antonio Salemacher");
        contactPerson.setTelephone("46211230");
        contactPerson.setTeleFax("46211231");
        contactPerson.setEmail("antonio@salescompany.no");
        return contactPerson;
    }

    public static ManufacturerPartyDTO getManufacturerParty() {

        ManufacturerPartyDTO manufacturerParty = new ManufacturerPartyDTO();
        manufacturerParty.setPartyName("Company name ASA");
        manufacturerParty.setCompanyId("904312347");
        return manufacturerParty;
    }

    public static OrderDTO getEHFOrderInfo(byte invoiceType) throws ParseException, IOException {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUblVersionId(ORDER_FILE_NAME);
        orderDTO.setOrderNo("TOSL108");

        orderDTO.setIsRoundPayableAmount(true);
        orderDTO.setSupplierDTO(getSupplierInfo());
        orderDTO.setCustomerDTO(getCustomerInfo());
        orderDTO.setOrderIssueDate(new Date());
        orderDTO.setOrderIssueTime(new Date());
        orderDTO.setNote("Sample EHF Order Implementation");
        orderDTO.setDocumentCurrencyCode("NOK");
        orderDTO.setAccountingCost("Zerp Project");
        orderDTO.setValidityEndDate(new Date());
        orderDTO.setQuotationDocumentReferenceId("Quotation Document Reference");
        orderDTO.setOrderDocumentReferenceId("Order document reference");
        orderDTO.setOriginatorDocumentReferenceId("Originator document reference");
        orderDTO.setFiles(getFiles());
        orderDTO.setCurrencyDTO(getCurrencyInfo());

        orderDTO.getNotes().add("Demonstration of shipment via EHF");
        orderDTO.getNotes().add("Another sample note");
        orderDTO.setPaymentId("10706416");

        orderDTO.setTaxAmount(120d);
        orderDTO.setTotalExcTax(800d);
        orderDTO.setTotalAmount(920d);

        orderDTO.getOrderLineItems().add(getInvoiceLineItem(invoiceType));
        orderDTO.getTaxSummaries().add(getTaxSummary(invoiceType));
        orderDTO.getAllowanceCharges().add(getAllowanceAmount());
        orderDTO.getAllowanceCharges().add(getChargeAmount());

        return orderDTO;
    }

    public static OrderResponseDTO getEHFOrderResponseInfo() throws ParseException, IOException {

        OrderResponseDTO orderDTO = new OrderResponseDTO();
        orderDTO.setOrderResponseNo(OrderResponseCode.ORDER_ACCEPTED.getValue());

        orderDTO.setNote("Change in Item");
        orderDTO.setSupplierDTO(getSupplierInfo());
        orderDTO.setCustomerDTO(getCustomerInfo());
        orderDTO.setOrderResponseIssueDate(new Date());
        orderDTO.setCurrencyDTO(getCurrencyInfo());
        orderDTO.setOrderLineReference("Reference");
        orderDTO.getOrderLineItems().add(getOrderResponseLineItem());
        orderDTO.getOrderLineItems().add(getOrderResponseLineItemWithoutSubStitution());

        return orderDTO;
    }

    public static SupplierDTO getSupplierInfo() {

        SupplierDTO supplierDTO = new SupplierDTO();

        supplierDTO.setId("Supp123");
        supplierDTO.setEndpointId("1234567890123");
        supplierDTO.setName("Leverandar");
        supplierDTO.setLegalName("Leverandar");
        supplierDTO.setOrganizationNo("999999999");
        supplierDTO.setContactId("O Hansen");
        supplierDTO.setTelePhone("4793433770");
        supplierDTO.setTeleFax("4793433660");
        supplierDTO.setEmail("bbx@bbx.com");
        supplierDTO.setGln("1231412341324");
        supplierDTO.setAddressDTO(getSupplierAddressInfo());
        supplierDTO.setBankAccountDTO(getBankAccountInfo());
        supplierDTO.setContactPersonDTO(getContactPersonInfo());

        return supplierDTO;
    }

    public static AddressDTO getSupplierAddressInfo() {

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setBuildingNumber("1");
        addressDTO.setStreetName("Oslogate");
        addressDTO.setPostalBox("Postboks 123");
        addressDTO.setPostalZone("0612");
        addressDTO.setCityName("Oslo");
        addressDTO.setCountryCode("NO");
        addressDTO.setCountrySubentityCode("Norway");

        return addressDTO;
    }

    public static CustomerDTO getCustomerInfo() {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId("Cus123");
        customerDTO.setEndpointId("1234567987654");
        customerDTO.setName("Kjaper");
        customerDTO.setLegalName("Kjaper");
        customerDTO.setOrganizationNo("888888888");
        customerDTO.setContactId("3150bdn");
        customerDTO.setTelePhone("4793433770");
        customerDTO.setTeleFax("4793433660");
        customerDTO.setEmail("bbx@bbx.com");
        customerDTO.setAddressDTO(getCustomerAddressInfo());
        customerDTO.setContactPersonDTO(getContactPersonInfo());

        return customerDTO;
    }

    public static AddressDTO getCustomerAddressInfo() {

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setBuildingNumber("1");
        addressDTO.setStreetName("Testveien");
        addressDTO.setPostalBox("Postboks 11");
        addressDTO.setPostalZone("2012");
        addressDTO.setCityName("Frogner");
        addressDTO.setCountryCode("NO");
        addressDTO.setCountrySubentityCode("Norway");

        return addressDTO;
    }

    public static AddressDTO getBankAddressInfo() {

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setBuildingNumber("1");
        addressDTO.setStreetName("Testveien");
        addressDTO.setPostalBox("Postboks 11");
        addressDTO.setPostalZone("2012");
        addressDTO.setCityName("Frogner");
        addressDTO.setCountryCode("NO");
        addressDTO.setCountrySubentityCode("Norway");

        return addressDTO;
    }

    public static BankAccountDTO getBankAccountInfo() {

        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        bankAccountDTO.setBankAccountNumber("60011111");
        bankAccountDTO.setBankAccountName("CA");
        bankAccountDTO.setBankName("Norway Bank");
        bankAccountDTO.setiBanNo("9386011117947");
        bankAccountDTO.setBic("DNBANOKK");
        bankAccountDTO.setCurrencyDTO(getCurrencyInfo());
        bankAccountDTO.setBankAddressDTO(getBankAddressInfo());

        return bankAccountDTO;
    }

    public static CurrencyDTO getCurrencyInfo() {

        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCurrencyCode("NOK");
        currencyDTO.setCurrencyName("Norwegian Krone");

        return currencyDTO;
    }

    public static CurrencyDTO getBaseCurrencyInfo() {

        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCurrencyCode("INR");
        currencyDTO.setCurrencyName("Indian Rupee");

        return currencyDTO;
    }

    public static DeliveryDTO getDeliveryInfo() throws ParseException {

        Date date = new Date();

        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setDeliveryDate(date);
        deliveryDTO.setDeliveryAddressDTO(getCustomerAddressInfo());

        return deliveryDTO;
    }

    public static InvoiceSettingDTO getPaymentTerms() {

        InvoiceSettingDTO invoiceSettingDTO = new InvoiceSettingDTO();
        invoiceSettingDTO.getPaymentTermsNotes().add("Penalty percentage 10% from due date");

        return invoiceSettingDTO;
    }

    public static PayeeParty getPayeeParty() {

        PayeeParty payeeParty = new PayeeParty();
        payeeParty.setId("098740918237");
        payeeParty.setName("Ebeneser Scrooge AS");
        payeeParty.setOrganizationNo("967611265");

        return payeeParty;
    }

    public static InvoiceLineItemDTO getInvoiceLineItem(byte invoiceType) throws IOException {

        InvoiceLineItemDTO invoiceLineItemDTO = new InvoiceLineItemDTO();

        invoiceLineItemDTO.setId("34");
        invoiceLineItemDTO.setProductNo("24683432");
        invoiceLineItemDTO.setProductName("Testprodukt-2");
        invoiceLineItemDTO.setAccountingCode("BookingCode001");
        invoiceLineItemDTO.setNote("Description of Test Product");
        invoiceLineItemDTO.setOriginCountry("DE");
        invoiceLineItemDTO.setManufacturerPartyDTO(getManufacturerParty());
        invoiceLineItemDTO.setPeriodStartDate(new Date());
        invoiceLineItemDTO.setPeriodEndDate(new Date());
        invoiceLineItemDTO.getBillingReferenceDTOs().add(getBillingReference());

        AllowanceChargeDTO allowanceChargeDTO = null;
        if (INVOICE == invoiceType) {

            allowanceChargeDTO = new AllowanceChargeDTO();
            allowanceChargeDTO.setChargeIndicator(false);
            allowanceChargeDTO.setAllowanceChargeReason(AllowanceChargeReasons.PROMOTION_DISCOUNT.getValue());
            allowanceChargeDTO.setAmount(108d);
            allowanceChargeDTO.setTaxPercent(2d);
            allowanceChargeDTO.setTaxType("VAT");
            invoiceLineItemDTO.getAllowanceCharges().add(allowanceChargeDTO);
            invoiceLineItemDTO.setUnitPrice(450d);
            invoiceLineItemDTO.setQuantity(2d);
            invoiceLineItemDTO.setTaxPercent(10d);
            invoiceLineItemDTO.setTotalExcTax(792d);
            invoiceLineItemDTO.setTaxAmount(79.2d);
        } else {
            invoiceLineItemDTO.setUnitPrice(100d);
            invoiceLineItemDTO.setQuantity(8d);
            invoiceLineItemDTO.setTaxPercent(15d);
            invoiceLineItemDTO.setTotalExcTax(800d);
            invoiceLineItemDTO.setTaxAmount(120d);
        }
        invoiceLineItemDTO.setUnitCode("KGM");

        invoiceLineItemDTO.setTaxType("VAT");
        invoiceLineItemDTO.setTaxTypeIntName("VAT");

        return invoiceLineItemDTO;
    }

    public static TaxSummaryDTO getTaxSummary(byte invoiceType) {

        TaxSummaryDTO taxSummaryDTO = new TaxSummaryDTO();

        taxSummaryDTO.setTaxType("MVA");
        taxSummaryDTO.setTaxTypeIntName("VAT");
        if (INVOICE == invoiceType) {
            taxSummaryDTO.setTaxPercent(10d);
            taxSummaryDTO.setTaxAmount(79.2d);
            taxSummaryDTO.setTotalExcTax(792d);
            taxSummaryDTO.setTransactionCurrencyTaxAmount(79.2d);
        } else {
            taxSummaryDTO.setTaxPercent(15d);
            taxSummaryDTO.setTaxAmount(120d);
            taxSummaryDTO.setTotalExcTax(800d);
            taxSummaryDTO.setTransactionCurrencyTaxAmount(120d);
        }

        return taxSummaryDTO;
    }

    public static AllowanceChargeDTO getAllowanceAmount() {

        AllowanceChargeDTO allowanceChargeDTO = new AllowanceChargeDTO();
        allowanceChargeDTO.setChargeIndicator(false);
        allowanceChargeDTO.setAllowanceChargeReason(AllowanceChargeReasons.PROMOTION_DISCOUNT.getValue());
        allowanceChargeDTO.setAmount(12d);
        allowanceChargeDTO.setTaxPercent(5d);
        allowanceChargeDTO.setTaxType("VAT");

        return allowanceChargeDTO;
    }

    public static AllowanceChargeDTO getChargeAmount() {

        AllowanceChargeDTO allowanceChargeDTO = new AllowanceChargeDTO();
        allowanceChargeDTO.setChargeIndicator(true);
        allowanceChargeDTO.setAllowanceChargeReason(AllowanceChargeReasons.PACKING_COST.getValue());
        allowanceChargeDTO.setAmount(12d);
        allowanceChargeDTO.setTaxPercent(5d);
        allowanceChargeDTO.setTaxType("VAT");

        return allowanceChargeDTO;
    }

    public static List<FileDTO> getFiles() throws IOException {

        List<FileDTO> files = new ArrayList<FileDTO>();
        FileDTO fileDTO = new FileDTO();
        fileDTO.setDocType("Invoice");
        fileDTO.setFileContent(ConversionUtils.getFileAsByteArray(ClassLoader.class.getResourceAsStream("/ehandel/no/util/invoice-report.pdf")));
        files.add(fileDTO);

        return files;
    }

    public static ReminderDTO getReminderInfo() throws ParseException, IOException {

        ReminderDTO reminderDTO = new ReminderDTO();

        Date date = new Date();

        reminderDTO.setReminderId("123456");
        reminderDTO.setIssueDate(date);
        reminderDTO.setReminderTypeCode("999");
        reminderDTO.setReminderSequenceNo(3d);
        reminderDTO.getNotes().add(
                "Henvendelse angaende purring rettes til Utstyr salg AS, tlf 22 05 11 34, faks "
                + " 22 05 11 35, eller epost post@utstyrsalg.no");

        reminderDTO.setTaxType("VAT");
        reminderDTO.setExchangeRate(8.92d);
        reminderDTO.setBaseCurrencyBaseRate(1d);
        reminderDTO.setInvoiceCurrencyBaseRate(1d);
        reminderDTO.setSupplierDTO(getSupplierInfo());
        reminderDTO.setCustomerDTO(getCustomerInfo());
        reminderDTO.setDeliveryDTO(getDeliveryInfo());
        reminderDTO.setCurrencyDTO(getCurrencyInfo());
        reminderDTO.setBaseCurrencyDTO(getBaseCurrencyInfo());
        reminderDTO.setFiles(getFiles());

        reminderDTO.setPaymentId("10706416");
        reminderDTO.setTaxAmount(120d);
        reminderDTO.setTotalExcTax(200d);
        reminderDTO.setTotalAmount(920d);

        reminderDTO.getReminderLines().add(getReminderLine());
        reminderDTO.getTaxSummaries().add(getTaxSummary(TestData.REMINDER));

        return reminderDTO;
    }

    public static ReminderLineDTO getReminderLine() throws IOException {

        ReminderLineDTO reminderLineDTO = new ReminderLineDTO();

        reminderLineDTO.setId("34");
        reminderLineDTO.setCreditLineAmount(0d);
        reminderLineDTO.setDebitLineAmount(200d);
        reminderLineDTO.setTaxType("VAT");
        reminderLineDTO.setTaxTypeIntName("VAT");
        reminderLineDTO.setInvoiceDocumentReference(getFiles().get(0));
        if (reminderLineDTO.getCreditLineAmount() > 0) {
            reminderLineDTO.setCreditNoteDoucmentReference(getFiles().get(0));
        }

        return reminderLineDTO;
    }

    public static InvoiceLineItemDTO getOrderResponseLineItem() throws IOException {

        InvoiceLineItemDTO invoiceLineItemDTO = new InvoiceLineItemDTO();

        invoiceLineItemDTO.setLineStatusCode("30");
        invoiceLineItemDTO.setId("35");
        invoiceLineItemDTO.setNote("Sample Order response");
        invoiceLineItemDTO.setQuantity(30d);
        invoiceLineItemDTO.setUnitPrice(200d);
        invoiceLineItemDTO.setUnitCode("KGM");
        invoiceLineItemDTO.setDescription("Change in quantity");
        invoiceLineItemDTO.setSellersIdentification("SItemNo011");
        invoiceLineItemDTO.setStandardIdentification("07330869106661");
        invoiceLineItemDTO.setSubstitutedLineItem(getSubstituedLineItem());

        return invoiceLineItemDTO;
    }

    public static InvoiceLineItemDTO getOrderResponseLineItemWithoutSubStitution() throws IOException {

        InvoiceLineItemDTO invoiceLineItemDTO = new InvoiceLineItemDTO();

        invoiceLineItemDTO.setLineStatusCode("30");
        invoiceLineItemDTO.setId("35");
        invoiceLineItemDTO.setNote("Sample Order response");
        invoiceLineItemDTO.setQuantity(30d);
        invoiceLineItemDTO.setUnitPrice(200d);
        invoiceLineItemDTO.setUnitCode("KGM");
        invoiceLineItemDTO.setDescription("Change in quantity");
        invoiceLineItemDTO.setSellersIdentification("SItemNo011");
        invoiceLineItemDTO.setStandardIdentification("07330869106661");

        return invoiceLineItemDTO;
    }

    public static SubstitutedLineItemDTO getSubstituedLineItem() {

        SubstitutedLineItemDTO substitutedLineItemDTO = new SubstitutedLineItemDTO();
        substitutedLineItemDTO.setId("34");
        substitutedLineItemDTO.setItemName("Testprodukt-2");
        substitutedLineItemDTO.setSellersItemId("SItemNo011");
        substitutedLineItemDTO.setStandardItemId("07330869106661");
        substitutedLineItemDTO.setItemClassificationCode("234");
        substitutedLineItemDTO.setClassifiedTaxCategoryId("67");
        substitutedLineItemDTO.setClassifiedTaxCategoryPercent(25d);
        substitutedLineItemDTO.setClassifiedTaxCategorySchemeId("45");
        substitutedLineItemDTO.setItemPropertyName("name");
        substitutedLineItemDTO.setItemPropertyValue("value");

        return substitutedLineItemDTO;
    }
}
