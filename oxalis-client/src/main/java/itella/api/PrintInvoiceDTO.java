/*
 * @(#)PrintInvoiceDTO.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package itella.api;

/**
 * The Class PrintInvoiceDTO.
 * 
 * @author vasanthis
 */
public class PrintInvoiceDTO {

    private String userId;
    
    private String licenseId;
    private PostType postType;
    private FormCode formCode;
    private byte[] fileData;
    private PrintType printType;
    private String orgNo;
    private boolean isReminder;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public FormCode getFormCode() {
        return formCode;
    }

    public void setFormCode(FormCode formCode) {
        this.formCode = formCode;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    
    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public boolean isReminder() {
        return isReminder;
    }

    public void setReminder(boolean isReminder) {
        this.isReminder = isReminder;
    }
}
