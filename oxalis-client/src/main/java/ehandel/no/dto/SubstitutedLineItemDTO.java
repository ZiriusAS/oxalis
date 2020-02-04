/*
 * @(#)SubstitutedLineItemDTO.java
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
 * The Class SubstitutedLineItemDTO.
 * 
 * @author vasanthis
 * @since Nov 17, 2013
 */
public class SubstitutedLineItemDTO extends BaseDTO {

    private String id;
    private String itemName;
    private String sellersItemId;
    private String standardItemId;
    private String productClassification;
    private String vatCategoryCode;
    private String vatRate;
    private String taxScheme;
    private String itemPropertyName;
    private String itemClassificationCode;
    private String itemPropertyValue;
    private String classifiedTaxCategoryId;
    private Double classifiedTaxCategoryPercent;
    private String classifiedTaxCategorySchemeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemClassificationCode() {
        return itemClassificationCode;
    }

    public void setItemClassificationCode(String itemClassificationCode) {
        this.itemClassificationCode = itemClassificationCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPropertyName() {
        return itemPropertyName;
    }

    public void setItemPropertyName(String itemPropertyName) {
        this.itemPropertyName = itemPropertyName;
    }

    public String getItemPropertyValue() {
        return itemPropertyValue;
    }

    public void setItemPropertyValue(String itemPropertyValue) {
        this.itemPropertyValue = itemPropertyValue;
    }

    public String getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(String productClassification) {
        this.productClassification = productClassification;
    }

    public String getSellersItemId() {
        return sellersItemId;
    }

    public void setSellersItemId(String sellersItemId) {
        this.sellersItemId = sellersItemId;
    }

    public String getStandardItemId() {
        return standardItemId;
    }

    public void setStandardItemId(String standardItemId) {
        this.standardItemId = standardItemId;
    }

    public String getTaxScheme() {
        return taxScheme;
    }

    public void setTaxScheme(String taxScheme) {
        this.taxScheme = taxScheme;
    }

    public String getVatCategoryCode() {
        return vatCategoryCode;
    }

    public void setVatCategoryCode(String vatCategoryCode) {
        this.vatCategoryCode = vatCategoryCode;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getClassifiedTaxCategoryId() {
        return classifiedTaxCategoryId;
    }

    public void setClassifiedTaxCategoryId(String classifiedTaxCategoryId) {
        this.classifiedTaxCategoryId = classifiedTaxCategoryId;
    }

    public Double getClassifiedTaxCategoryPercent() {
        return classifiedTaxCategoryPercent;
    }

    public void setClassifiedTaxCategoryPercent(Double classifiedTaxCategoryPercent) {
        this.classifiedTaxCategoryPercent = classifiedTaxCategoryPercent;
    }

    public String getClassifiedTaxCategorySchemeId() {
        return classifiedTaxCategorySchemeId;
    }

    public void setClassifiedTaxCategorySchemeId(String classifiedTaxCategorySchemeId) {
        this.classifiedTaxCategorySchemeId = classifiedTaxCategorySchemeId;
    }
}
