/*
 * @(#)OrderDTO.java
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
 * The Class OrderDTO.
 *
 * @author vasanthis
 * @since Oct 18, 2013
 */
public class OrderDTO extends BaseDTO {

    private String ublVersionId;
    private String customizationId;
    private String profileId;
    private String orderNo;
    private String orderResponseNo;
    private Date orderIssueDate;
    private Date orderIssueTime;
    private String note;
    private String documentCurrencyCode;
    private String accountingCost;
    private Date validityEndDate;
    private String quotationDocumentReferenceId;
    private String orderDocumentReferenceId;
    private String originatorDocumentReferenceId;
    private String contractId;
    private String contractType;
    private Double totalExcTax;
    private Double taxAmount;
    private CustomerDTO customerDTO;
    private CurrencyDTO currencyDTO;
    private SupplierDTO supplierDTO;
    private List<FileDTO> files;
    private List<AllowanceChargeDTO> allowanceCharges;
    private List<InvoiceLineItemDTO> orderLineItems;
    private List<TaxSummaryDTO> taxSummaries;
    private List<String> notes;
    private String paymentId;
    private Double totalAmount;
    private String language;
    private Boolean isRoundPayableAmount = false;

    public String getAccountingCost() {
        return accountingCost;
    }

    public void setAccountingCost(String accountingCost) {
        this.accountingCost = accountingCost;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }

    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }

    public String getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(String customizationId) {
        this.customizationId = customizationId;
    }

    public String getDocumentCurrencyCode() {
        return documentCurrencyCode;
    }

    public void setDocumentCurrencyCode(String documentCurrencyCode) {
        this.documentCurrencyCode = documentCurrencyCode;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrderDocumentReferenceId() {
        return orderDocumentReferenceId;
    }

    public void setOrderDocumentReferenceId(String orderDocumentReferenceId) {
        this.orderDocumentReferenceId = orderDocumentReferenceId;
    }

    public Date getOrderIssueDate() {
        return orderIssueDate;
    }

    public void setOrderIssueDate(Date orderIssueDate) {
        this.orderIssueDate = orderIssueDate;
    }

    public Date getOrderIssueTime() {
        return orderIssueTime;
    }

    public void setOrderIssueTime(Date orderIssueTime) {
        this.orderIssueTime = orderIssueTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOriginatorDocumentReferenceId() {
        return originatorDocumentReferenceId;
    }

    public void setOriginatorDocumentReferenceId(String originatorDocumentReferenceId) {
        this.originatorDocumentReferenceId = originatorDocumentReferenceId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getQuotationDocumentReferenceId() {
        return quotationDocumentReferenceId;
    }

    public void setQuotationDocumentReferenceId(String quotationDocumentReferenceId) {
        this.quotationDocumentReferenceId = quotationDocumentReferenceId;
    }

    public String getUblVersionId() {
        return ublVersionId;
    }

    public void setUblVersionId(String ublVersionId) {
        this.ublVersionId = ublVersionId;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public CurrencyDTO getCurrencyDTO() {
        return currencyDTO;
    }

    public void setCurrencyDTO(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    public SupplierDTO getSupplierDTO() {
        return supplierDTO;
    }

    public void setSupplierDTO(SupplierDTO supplierDTO) {
        this.supplierDTO = supplierDTO;
    }

    public List<AllowanceChargeDTO> getAllowanceCharges() {

        if (allowanceCharges == null) {
            allowanceCharges = new ArrayList<AllowanceChargeDTO>();
        }
        return allowanceCharges;
    }

    public List<InvoiceLineItemDTO> getOrderLineItems() {

        if (orderLineItems == null) {
            orderLineItems = new ArrayList<InvoiceLineItemDTO>();
        }
        return orderLineItems;
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

    public List<String> getNotes() {
        if (notes == null) {
            notes = new ArrayList<String>();
        }
        return notes;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<TaxSummaryDTO> getTaxSummaries() {
         if (taxSummaries == null) {
            taxSummaries = new ArrayList<TaxSummaryDTO>();
        }
        return taxSummaries;
    }

    public String getOrderResponseNo() {
        return orderResponseNo;
    }

    public void setOrderResponseNo(String orderResponseNo) {
        this.orderResponseNo = orderResponseNo;
    }

        public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getIsRoundPayableAmount() {
        return isRoundPayableAmount;
    }

    public void setIsRoundPayableAmount(Boolean isRoundPayableAmount) {
        this.isRoundPayableAmount = isRoundPayableAmount;
    }
}
