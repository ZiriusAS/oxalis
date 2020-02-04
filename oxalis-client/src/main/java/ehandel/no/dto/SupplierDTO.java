/*
 * @(#)SupplierDTO.java
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
 * The Class SupplierDTO.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class SupplierDTO extends OrganizationDTO {

    private ContactPersonDTO contactPersonDTO;
    private AddressDTO legalAddressDTO;
    private String legalName;
    private String eaID;

    public ContactPersonDTO getContactPersonDTO() {
        return contactPersonDTO;
    }

    public void setContactPersonDTO(ContactPersonDTO contactPersonDTO) {
        this.contactPersonDTO = contactPersonDTO;
    }

    public AddressDTO getLegalAddressDTO() {
        return legalAddressDTO;
    }

    public void setLegalAddressDTO(AddressDTO legalAddressDTO) {
        this.legalAddressDTO = legalAddressDTO;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getEaID() {
        return eaID;
    }

    public void setEaID(String eaID) {
        this.eaID = eaID;
    }      
}
