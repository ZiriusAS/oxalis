/*
 * @(#)OrganizationDTO.java
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
 * The Class OrganizationDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class OrganizationDTO extends BaseDTO {

    private String id;
    private String name;
    private String endpointId;
    private String organizationNo;
    private String contactId;
    private String telePhone;
    private String teleFax;
    private String email;
    private String gln;
    private AddressDTO addressDTO;
    private BankAccountDTO bankAccountDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getOrganizationNo() {
        return organizationNo;
    }

    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeleFax() {
        return teleFax;
    }

    public void setTeleFax(String teleFax) {
        this.teleFax = teleFax;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
    }

    public BankAccountDTO getBankAccountDTO() {
        return bankAccountDTO;
    }

    public void setBankAccountDTO(BankAccountDTO bankAccountDTO) {
        this.bankAccountDTO = bankAccountDTO;
    }
}
