/*
 * @(#)DocumentDTO.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package eu.peppol.outbound.api;

import java.io.Serializable;

/**
 * The Class DocumentDTO.
 * 
 * @author vasanthis
 */
public class DocumentDTO implements Serializable {

    private static final long serialVersionUID = 112233445566778801L;

    private String senderId;
    private String receiverId;
    private byte[] fileData;
    private String licenseId;
    private String fileName;
    private boolean EHFDocument = true;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isComplete() {
        return ((senderId != null) && (receiverId != null) && (fileData != null));        
    }

    public boolean isEHFDocument() {
        return EHFDocument;
    }

    public void setEHFDocument(boolean EHFDocument) {
        this.EHFDocument = EHFDocument;
    }
    
}
