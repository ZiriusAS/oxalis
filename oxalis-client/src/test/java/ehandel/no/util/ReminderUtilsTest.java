/*
 * @(#)ReminderUtilsTest.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.util;

import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import ehandel.no.dto.ReminderDTO;
import ehandel.no.util.validation.SchemaValidator;

/**
 * The Class ReminderUtilsTest.
 * 
 * @author amuthar
 * @since ehf; Jul 11, 2012
 */
public class ReminderUtilsTest {

    public ReminderUtilsTest() {
    }

    /**
     * Test get reminder xml file.
     */
    @Test
    public void getXML() {

        try {

            File file = new File(TestData.REMINDER_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            ReminderDTO reminderDTO = TestData.getReminderInfo();
            boolean result = ReminderUtils.isValidLineExtensionAmount(reminderDTO);

            Assert.assertTrue(result);

            ReminderUtils.generateReminderXML(reminderDTO, TestData.REMINDER_FILE_NAME);

            Assert.assertTrue(file.exists());
            Assert.assertTrue(new SchemaValidator().validateReminder(file.getAbsolutePath()));

            byte[] xmlByteArray = ReminderUtils.generateReminderXML(reminderDTO);
            Assert.assertNotNull(xmlByteArray);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get EHF reminder info.
     */
    @Test
    public void getReminder() {

        try {
            InputStream is = getClass().getResourceAsStream("reminder-ehf.xml");

            ReminderDTO reminderDTO = ReminderUtils.getReminder(is);

            File newFile = new File("reminder-out.xml");
            if (newFile.exists()) {
                newFile.delete();
            }

            ReminderUtils.generateReminderXML(reminderDTO, newFile.getAbsolutePath());

            Assert.assertNotNull(reminderDTO);
            Assert.assertTrue(new SchemaValidator().validateReminder(newFile.getAbsolutePath()));

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
