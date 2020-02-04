/*
 * @(#)InvoiceDTO.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved.
 *
 * Use is subject to license terms. This software is protected by
 * copyright law and international treaties. Unauthorized reproduction or
 * distribution of this program, or any portion of it, may result in severe
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class InvoiceDTO.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class InvoiceDTO extends BaseDTO {

    private String ublExtensions;
    private String customizationId;
    private String profileID;
    private String invoiceNo;
    private String invoiceType;
    private String orderReferenceId;
    private String purchaseOrderNo;
    private String buyerReference;
    private Date issueDate;
    private Date paymentDueDate;
    private Date periodStartDate;
    private Date periodEndDate;
    private Double invoiceCurrencyBaseRate;
    private Double baseCurrencyBaseRate;
    private Double exchangeRate;
    private Double totalExcTax;
    private Double taxAmount;
    private Double totalAmount;
    private Double payableRoundingAmount;
    private Double payableAmount;
    private String paymentId;
    private String language;
    private SupplierDTO supplierDTO;
    private CustomerDTO customerDTO;
    private CurrencyDTO currencyDTO;
    private CurrencyDTO baseCurrencyDTO;
    private DeliveryDTO deliveryDTO;
    private List<InvoiceSettingDTO> invoiceSettingDTOs;
    private ContractDTO contractDTO;
    private PayeeParty payeeParty;
    private TaxRepresentativeDTO taxRepresentation;
    private List<InvoiceLineItemDTO> invoiceLineItems;
    private List<TaxSummaryDTO> taxSummaries;
    private List<AllowanceChargeDTO> allowanceCharges;
    private List<FileDTO> files;
    private List<String> notes;
    private List<BillingReferenceDTO> billingReferenceDTOs;
    private Boolean isRoundPayableAmount = false;
    private Boolean isCreditNoteAgaintInvoice = false;
    private Boolean isInvoiceCompanyTaxFree = false;
    private Boolean registerInBusinessEnterprises=false;

    public String getUblExtensions() {
        return ublExtensions;
    }

    public void setUblExtensions(String ublExtensions) {
        this.ublExtensions = ublExtensions;
    }

    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getOrderReferenceId() {
        return orderReferenceId;
    }

    public void setOrderReferenceId(String orderReferenceId) {
        this.orderReferenceId = orderReferenceId;
    }

    public String getPurchaseOrderNo() {
        return purchaseOrderNo;
    }

    public void setPurchaseOrderNo(String purchaseOrderNo) {
        this.purchaseOrderNo = purchaseOrderNo;
    }
    
    public String getBuyerReference() {
        return buyerReference;
    }

    public void setBuyerReference(String reference) {
        this.buyerReference = reference;
    }
    
    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(Date paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public Double getInvoiceCurrencyBaseRate() {
        return invoiceCurrencyBaseRate;
    }

    public void setInvoiceCurrencyBaseRate(Double invoiceCurrencyBaseRate) {
        this.invoiceCurrencyBaseRate = invoiceCurrencyBaseRate;
    }

    public Double getBaseCurrencyBaseRate() {
        return baseCurrencyBaseRate;
    }

    public void setBaseCurrencyBaseRate(Double baseCurrencyBaseRate) {
        this.baseCurrencyBaseRate = baseCurrencyBaseRate;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getTotalExcTax() {
        return totalExcTax;
    }

    public void setTotalExcTax(Double totalExcTax) {
        this.totalExcTax = totalExcTax;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPayableRoundingAmount() {
        return payableRoundingAmount;
    }

    public void setPayableRoundingAmount(Double payableRoundingAmount) {
        this.payableRoundingAmount = payableRoundingAmount;
    }

    public Double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public SupplierDTO getSupplierDTO() {
        return supplierDTO;
    }

    public void setSupplierDTO(SupplierDTO supplierDTO) {
        this.supplierDTO = supplierDTO;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public CurrencyDTO getCurrencyDTO() {
        return currencyDTO;
    }

    public void setCurrencyDTO(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    public CurrencyDTO getBaseCurrencyDTO() {
        return baseCurrencyDTO;
    }

    public void setBaseCurrencyDTO(CurrencyDTO baseCurrencyDTO) {
        this.baseCurrencyDTO = baseCurrencyDTO;
    }

    public DeliveryDTO getDeliveryDTO() {
        return deliveryDTO;
    }

    public void setDeliveryDTO(DeliveryDTO deliveryDTO) {
        this.deliveryDTO = deliveryDTO;
    }

    public ContractDTO getContractDTO() {
        return contractDTO;
    }

    public void setContractDTO(ContractDTO contractDTO) {
        this.contractDTO = contractDTO;
    }

    public PayeeParty getPayeeParty() {
        return payeeParty;
    }

    public void setPayeeParty(PayeeParty payeeParty) {
        this.payeeParty = payeeParty;
    }

    public List<InvoiceLineItemDTO> getInvoiceLineItems() {

        if (invoiceLineItems == null) {
            invoiceLineItems = new ArrayList<InvoiceLineItemDTO>();
        }
        return invoiceLineItems;
    }

    public void setInvoiceLineItems(List<InvoiceLineItemDTO> invoiceLineItems) {
        this.invoiceLineItems = invoiceLineItems;
    }

    public List<TaxSummaryDTO> getTaxSummaries() {

        if (taxSummaries == null) {
            taxSummaries = new ArrayList<TaxSummaryDTO>();
        }
        return taxSummaries;
    }

    public void setTaxSummaries(List<TaxSummaryDTO> taxSummaries) {
        this.taxSummaries = taxSummaries;
    }

    public List<AllowanceChargeDTO> getAllowanceCharges() {

        if (allowanceCharges == null) {
            allowanceCharges = new ArrayList<AllowanceChargeDTO>();
        }
        return allowanceCharges;
    }

    public void setAllowanceCharges(List<AllowanceChargeDTO> allowanceCharges) {
        this.allowanceCharges = allowanceCharges;
    }

    public List<FileDTO> getFiles() {
        if (files == null) {
            files = new ArrayList<FileDTO>();
        }
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public List<String> getNotes() {

        if (notes == null) {
            notes = new ArrayList<String>();
        }
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public TaxRepresentativeDTO getTaxRepresentation() {
        return taxRepresentation;
    }

    public void setTaxRepresentation(TaxRepresentativeDTO taxRepresentation) {
        this.taxRepresentation = taxRepresentation;
    }

    public List<InvoiceSettingDTO> getInvoiceSettingDTOs() {

        if (invoiceSettingDTOs == null) {
            invoiceSettingDTOs = new ArrayList<InvoiceSettingDTO>();
        }
        return invoiceSettingDTOs;
    }

    public InvoiceSettingDTO getInvoiceSettingDTO() {

        List<InvoiceSettingDTO> invoiceSettingDTOs = getInvoiceSettingDTOs();
        InvoiceSettingDTO invoiceSettingDTO = null;
        if (invoiceSettingDTOs != null && !invoiceSettingDTOs.isEmpty()) {
            invoiceSettingDTO = invoiceSettingDTOs.get(0);
        }
        return invoiceSettingDTO;
    }

    public void setInvoiceSettingDTOs(List<InvoiceSettingDTO> invoiceSettingDTOs) {
        this.invoiceSettingDTOs = invoiceSettingDTOs;
    }

    public List<BillingReferenceDTO> getBillingReferenceDTOs() {

        if (billingReferenceDTOs == null) {
            billingReferenceDTOs = new ArrayList<BillingReferenceDTO>();
        }
        return billingReferenceDTOs;
    }

    public void setBillingReferenceDTOs(List<BillingReferenceDTO> billingReferenceDTOs) {
        this.billingReferenceDTOs = billingReferenceDTOs;
    }

    public Boolean getIsRoundPayableAmount() {
        return isRoundPayableAmount;
    }

    public void setIsRoundPayableAmount(Boolean isRoundPayableAmount) {
        this.isRoundPayableAmount = isRoundPayableAmount;
    }

    public Boolean getIsCreditNoteAgaintInvoice() {
        return isCreditNoteAgaintInvoice;
    }

    public void setIsCreditNoteAgaintInvoice(Boolean isCreditNoteAgaintInvoice) {
        this.isCreditNoteAgaintInvoice = isCreditNoteAgaintInvoice;
    }

    public Boolean getIsInvoiceCompanyTaxFree() {
        return isInvoiceCompanyTaxFree;
    }

    public void setIsInvoiceCompanyTaxFree(Boolean isInvoiceCompanyTaxFree) {
        this.isInvoiceCompanyTaxFree = isInvoiceCompanyTaxFree;
    }
    
    public Boolean getRegisterInBusinessEnterprises() {
        return registerInBusinessEnterprises;
    }
    
    public void setRegisterInBusinessEnterprises(Boolean isRegisterInBusinessEnterprises) {        
        this.registerInBusinessEnterprises = isRegisterInBusinessEnterprises;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }
}
