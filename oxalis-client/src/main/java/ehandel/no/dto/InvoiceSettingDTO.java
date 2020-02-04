/*
 * @(#)InvoiceSettingDTO.java
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
import java.util.List;

/**
 * The Class InvoiceDTO.
 * 
 * @author amuthar
 * @since ehf; Mar 14, 2012
 */
public class InvoiceSettingDTO extends BaseDTO {

    private List<String> paymentTermsNotes;

    public List<String> getPaymentTermsNotes() {
        if (paymentTermsNotes == null) {
            paymentTermsNotes = new ArrayList<String>();
        }
        return paymentTermsNotes;
    }

    public void setPaymentTermsNotes(List<String> paymentTermsNotes) {
        this.paymentTermsNotes = paymentTermsNotes;
    }
}
