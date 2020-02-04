/*
 * @(#)TaxSummaryDTO.java
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

/**
 * The Class TaxSummaryDTO.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class TaxSummaryDTO extends BaseDTO {

    private Double taxPercent;
    private String taxType;
    private String taxTypeIntName;
    private String taxCode;
    private Double totalExcTax;
    private Double taxAmount;
    private Double totalAmount;
    private Double transactionCurrencyTaxAmount;
    private String taxExcemptionReasion;
    private String taxExcemptionReasionCode;

    public Double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(Double taxPercent) {
        this.taxPercent = taxPercent;
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
    
    public String getTaxCode() {
        return taxCode;
    }
    
    public void setTaxCode(String code) {
        this.taxCode = code;
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

    public Double getTransactionCurrencyTaxAmount() {
        return transactionCurrencyTaxAmount;
    }

    public void setTransactionCurrencyTaxAmount(Double transactionCurrencyTaxAmount) {
        this.transactionCurrencyTaxAmount = transactionCurrencyTaxAmount;
    }
    
    public String getTaxExcemptionReasion() {
        return taxExcemptionReasion;
    }

    public void setTaxExcemptionReasion(String taxExcemptionReasion) {
        this.taxExcemptionReasion = taxExcemptionReasion;
    }

    public String getTaxExcemptionReasionCode() {
        return taxExcemptionReasionCode;
    }

    public void setTaxExcemptionReasionCode(String taxExcemptionReasionCode) {
        this.taxExcemptionReasionCode = taxExcemptionReasionCode;
    }
}
