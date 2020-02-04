/*
 * @(#)ReminderLineDTO.java
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
 * The Class ReminderLineDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class ReminderLineDTO extends InvoiceLineItemDTO {

    private Double creditLineAmount;
    private Double debitLineAmount;
    private FileDTO invoiceDocumentReference;
    private FileDTO creditNoteDoucmentReference;

    public Double getCreditLineAmount() {
        return creditLineAmount;
    }

    public void setCreditLineAmount(Double creditLineAmount) {
        this.creditLineAmount = creditLineAmount;
    }

    public Double getDebitLineAmount() {
        return debitLineAmount;
    }

    public void setDebitLineAmount(Double debitLineAmount) {
        this.debitLineAmount = debitLineAmount;
    }

    public FileDTO getInvoiceDocumentReference() {
        return invoiceDocumentReference;
    }

    public void setInvoiceDocumentReference(FileDTO invoiceDocumentReference) {
        this.invoiceDocumentReference = invoiceDocumentReference;
    }

    public FileDTO getCreditNoteDoucmentReference() {
        return creditNoteDoucmentReference;
    }

    public void setCreditNoteDoucmentReference(FileDTO creditNoteDoucmentReference) {
        this.creditNoteDoucmentReference = creditNoteDoucmentReference;
    }
}
