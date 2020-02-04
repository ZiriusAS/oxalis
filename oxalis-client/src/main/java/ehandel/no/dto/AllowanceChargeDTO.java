/*
 * @(#)AllowanceChargeDTO.java
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
 * The Class AllowanceChargeDTO.
 * 
 * @author amuthar
 * @since ehf; Mar 08, 2012
 */
public class AllowanceChargeDTO extends BaseDTO {

    private boolean chargeIndicator;
    private String allowanceChargeReason;
    private Double amount;
    private Double taxPercent;
    private String taxType;
    private String taxCode;

    public boolean isChargeIndicator() {
        return chargeIndicator;
    }

    public void setChargeIndicator(boolean chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    public String getAllowanceChargeReason() {
        return allowanceChargeReason;
    }

    public void setAllowanceChargeReason(String allowanceChargeReason) {
        this.allowanceChargeReason = allowanceChargeReason;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

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
    
    public String getTaxCode() {
        return taxCode;
    }
    
    public void setTaxCode(String code) {
        this.taxCode = code;
    }
}
