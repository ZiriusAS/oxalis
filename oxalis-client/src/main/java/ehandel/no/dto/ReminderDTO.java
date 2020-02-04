/*
 * @(#)ReminderDTO.java
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
 * The Class ReminderDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 17, 2012
 */
public class ReminderDTO extends InvoiceDTO {

    private String reminderId;
    private String taxType;
    private String reminderTypeCode;
    private Double reminderSequenceNo;
    private List<ReminderLineDTO> reminderLines;

    public String getReminderId() {
        return reminderId;
    }

    public void setReminderId(String reminderId) {
        this.reminderId = reminderId;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getReminderTypeCode() {
        return reminderTypeCode;
    }

    public void setReminderTypeCode(String reminderTypeCode) {
        this.reminderTypeCode = reminderTypeCode;
    }

    public Double getReminderSequenceNo() {
        return reminderSequenceNo;
    }

    public void setReminderSequenceNo(Double reminderSequenceNo) {
        this.reminderSequenceNo = reminderSequenceNo;
    }

    public List<ReminderLineDTO> getReminderLines() {

        if (reminderLines == null) {
            reminderLines = new ArrayList<ReminderLineDTO>();
        }
        return reminderLines;
    }

    public void setReminderLines(List<ReminderLineDTO> reminderLines) {
        this.reminderLines = reminderLines;
    }
}
