/*
 * @(#)InvoiceLineItemDTO.java
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
 * The Class InvoiceLineItemDTO.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class InvoiceLineItemDTO extends BaseDTO {

    private String id;
    private String productNo;
    private String productName;
    private String accountingCode;
    private String unitCode;
    private Double unitPrice;
    private Double quantity;
    private Double taxPercent;
    private String taxCode;
    private String taxType;
    private String taxTypeIntName;
    private String gtin;
    private Double totalExcTax;
    private Double taxAmount;
    private Double totalAmount;
    private String note;
    private String description;
    private String responseCode;
    private String sellersIdentification;
    private String standardIdentification;
    private String lineStatusCode;
    private String originCountry;
    private DeliveryDTO deliveryDTO;
    private List<AllowanceChargeDTO> allowanceCharges;
    private SubstitutedLineItemDTO substitutedLineItem;
    private ManufacturerPartyDTO manufacturerPartyDTO;
    private Date periodStartDate;
    private Date periodEndDate;
    private List<BillingReferenceDTO> billingReferenceDTOs;
    private String InvoiceLineReference;
    private String buyersItemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAccountingCode() {
        return accountingCode;
    }

    public void setAccountingCode(String accountingCode) {
        this.accountingCode = accountingCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(Double taxPercent) {
        this.taxPercent = taxPercent;
    }
    
    public String getTaxCode() {
        return taxCode;
    }
    
    public void setTaxCode(String code) {
        this.taxCode = code;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getTaxTypeIntName() {
        return taxTypeIntName;
    }

    public void setTaxTypeIntName(String taxTypeIntName) {
        this.taxTypeIntName = taxTypeIntName;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getSellersIdentification() {
        return sellersIdentification;
    }

    public void setSellersIdentification(String sellersIdentification) {
        this.sellersIdentification = sellersIdentification;
    }

    public String getStandardIdentification() {
        return standardIdentification;
    }

    public void setStandardIdentification(String standardIdentification) {
        this.standardIdentification = standardIdentification;
    }

    public String getLineStatusCode() {
        return lineStatusCode;
    }

    public void setLineStatusCode(String lineStatusCode) {
        this.lineStatusCode = lineStatusCode;
    }

    public DeliveryDTO getDeliveryDTO() {
        return deliveryDTO;
    }

    public void setDeliveryDTO(DeliveryDTO deliveryDTO) {
        this.deliveryDTO = deliveryDTO;
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

    public SubstitutedLineItemDTO getSubstitutedLineItem() {
        return substitutedLineItem;
    }

    public void setSubstitutedLineItem(SubstitutedLineItemDTO substitutedLineItem) {
        this.substitutedLineItem = substitutedLineItem;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public ManufacturerPartyDTO getManufacturerPartyDTO() {
        return manufacturerPartyDTO;
    }

    public void setManufacturerPartyDTO(ManufacturerPartyDTO manufacturerPartyDTO) {
        this.manufacturerPartyDTO = manufacturerPartyDTO;
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

    public List<BillingReferenceDTO> getBillingReferenceDTOs() {

        if (billingReferenceDTOs == null) {
            billingReferenceDTOs = new ArrayList<BillingReferenceDTO>();
        }
        return billingReferenceDTOs;
    }

    public void setBillingReferenceDTOs(List<BillingReferenceDTO> billingReferenceDTOs) {
        this.billingReferenceDTOs = billingReferenceDTOs;
    }

    public String getInvoiceLineReference() {
        return InvoiceLineReference;
    }

    public void setInvoiceLineReference(String InvoiceLineReference) {
        this.InvoiceLineReference = InvoiceLineReference;
    }

    public String getBuyersItemId() {
        return buyersItemId;
    }

    public void setBuyersItemId(String buyersItemId) {
        this.buyersItemId = buyersItemId;
    }
    
}
