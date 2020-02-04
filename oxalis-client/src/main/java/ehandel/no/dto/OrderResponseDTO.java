/*
 * @(#)OrderResponseDTO.java
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
 * @since Oct 21, 2013
 */
public class OrderResponseDTO extends BaseDTO {

    private String ublVersionId;
    private String customizationId;
    private String profileId;
    private String orderResponseNo;
    private Date orderResponseIssueDate;
    private String orderResponseCode;
    private String orderReferenceId;
    private CustomerDTO customerDTO;
    private SupplierDTO supplierDTO;
    private String orderLineReference;
    private CurrencyDTO currencyDTO;
    private List<InvoiceLineItemDTO> orderLineItems;
    private String language;
    private String note;

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

    public String getOrderLineReference() {
        return orderLineReference;
    }

    public void setOrderLineReference(String orderLineReference) {
        this.orderLineReference = orderLineReference;
    }

    public String getOrderReferenceId() {
        return orderReferenceId;
    }

    public void setOrderReferenceId(String orderReferenceId) {
        this.orderReferenceId = orderReferenceId;
    }

    public String getOrderResponseCode() {
        return orderResponseCode;
    }

    public void setOrderResponseCode(String orderResponseCode) {
        this.orderResponseCode = orderResponseCode;
    }

    public Date getOrderResponseIssueDate() {
        return orderResponseIssueDate;
    }

    public void setOrderResponseIssueDate(Date orderResponseIssueDate) {
        this.orderResponseIssueDate = orderResponseIssueDate;
    }

    public String getOrderResponseNo() {
        return orderResponseNo;
    }

    public void setOrderResponseNo(String orderResponseNo) {
        this.orderResponseNo = orderResponseNo;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public SupplierDTO getSupplierDTO() {
        return supplierDTO;
    }

    public void setSupplierDTO(SupplierDTO supplierDTO) {
        this.supplierDTO = supplierDTO;
    }

    public String getUblVersionId() {
        return ublVersionId;
    }

    public void setUblVersionId(String ublVersionId) {
        this.ublVersionId = ublVersionId;
    }

    public CurrencyDTO getCurrencyDTO() {
        return currencyDTO;
    }

    public void setCurrencyDTO(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    public List<InvoiceLineItemDTO> getOrderLineItems() {

        if (orderLineItems == null) {
            orderLineItems = new ArrayList<InvoiceLineItemDTO>();
        }
        return orderLineItems;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
