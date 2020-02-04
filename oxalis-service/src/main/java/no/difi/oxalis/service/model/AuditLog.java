/*
 * @(#)AuditLog.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package no.difi.oxalis.service.model;

import java.util.Date;

/**
 * The Class AuditLog.
 * 
 * @author Dinesh
 */
public class AuditLog {

    // Auto generated
    private Long auditLogId;
    // Send, Receive
    private String eventId;
    // Invoice, Credit note, Reminder
    private String eventType;
    // Sender participant id
    private String senderId;
    // Recipient id
    private String receiverId;
    // Transacted user id
    private String userId;
    // License id of the user
    private String licenseId;
    // Date of event
    private Date eventDate;
    // Any description
    private String description;
    // Status of event
    private Integer status;
    // Any comment / error log
    private String comments;
    // Invoice file content
    private byte[] fileContent;
    // File Path
    private String filePath;

    public AuditLog() {
    }

    public AuditLog(String eventId, String eventType, String senderId, String receiverId,
            String userId, String licenseId, Date eventDate, String description, Integer status,
            String comments, byte[] fileContent, String filePath) {
        super();
        this.eventId = eventId;
        this.eventType = eventType;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.userId = userId;
        this.licenseId = licenseId;
        this.eventDate = eventDate;
        this.description = description;
        this.status = status;
        this.comments = comments;
        this.fileContent = fileContent;
        this.filePath = filePath;
    }

    public Long getAuditLogId() {
        return auditLogId;
    }

    public void setAuditLogId(Long auditLogId) {
        this.auditLogId = auditLogId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
    
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}