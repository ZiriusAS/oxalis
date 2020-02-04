/*
 * @(#)AddressDTO.java
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
 * The Class AddressDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class AddressDTO extends BaseDTO {

    private String addressId;
    private String addressType;
    private String buildingNumber;
    private String streetName;
    private String additionalStreetName;
    private String postalBox;
    private String postalZone;
    private String cityName;
    private String countryCode;
    private String countrySubentityCode;
    private String url;
    private String gln;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAdditionalStreetName() {
        return additionalStreetName;
    }

    public void setAdditionalStreetName(String additionalStreetName) {
        this.additionalStreetName = additionalStreetName;
    }

    public String getPostalBox() {
        return postalBox;
    }

    public void setPostalBox(String postalBox) {
        this.postalBox = postalBox;
    }

    public String getPostalZone() {
        return postalZone;
    }

    public void setPostalZone(String postalZone) {
        this.postalZone = postalZone;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountrySubentityCode() {
        return countrySubentityCode;
    }

    public void setCountrySubentityCode(String countrySubentityCode) {
        this.countrySubentityCode = countrySubentityCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }
}
