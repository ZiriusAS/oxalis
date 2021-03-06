/*
 * @(#)DeliveryDTO.java
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

import java.util.Date;

/**
 * The Class DeliveryDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class DeliveryDTO extends BaseDTO {

    private Date deliveryDate;
    private AddressDTO deliveryAddressDTO;

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public AddressDTO getDeliveryAddressDTO() {
        return deliveryAddressDTO;
    }

    public void setDeliveryAddressDTO(AddressDTO deliveryAddressDTO) {
        this.deliveryAddressDTO = deliveryAddressDTO;
    }
}
