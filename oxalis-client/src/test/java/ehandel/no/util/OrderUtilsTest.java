/*
 * @(#)OrderUtilsTest.java
 *
 * Copyright (c) 2013, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.util;

import ehandel.no.dto.OrderDTO;
import ehandel.no.dto.OrderResponseDTO;
import ehandel.no.util.validation.SchemaValidator;

import java.io.File;
import java.io.InputStream;
import junit.framework.Assert;
import org.junit.Test;

/**
 * The Class OrderUtilsTest.
 * 
 * @author vasanthis
 * @since Oct 18, 2013
 */
public class OrderUtilsTest {

    public OrderUtilsTest() {
    }

    /**
     * Test get order xml file.
     */
    @Test
    public void getOrderXML() {

        try {

            File file = new File(TestData.ORDER_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            OrderDTO orderDTO = TestData.getEHFOrderInfo(TestData.ORDER);

            OrderUtils.generateEHFOrderXML(orderDTO, TestData.ORDER_FILE_NAME);
            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateOrder(file.getAbsolutePath()));

            byte[] xmlByteArray = OrderUtils.generateEHFOrderXML(orderDTO);
            Assert.assertNotNull(xmlByteArray);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    /**
     * Test get ehf order.
     */
    @Test
    public void getEHFOrder() {

        try {
            InputStream is = getClass().getResourceAsStream("order-ehf.xml");

            OrderDTO orderDTO = OrderUtils.getEHFOrder(is);
            Assert.assertNotNull(orderDTO);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void getOrderReponseXML() {

        try {

            File file = new File(TestData.ORDER_RESPONSE_FILE_NAME);
            if (file.exists()) {
                file.delete();
            }

            OrderResponseDTO orderResponseDTO = TestData.getEHFOrderResponseInfo();

            OrderUtils.generateEHFOrderResponseXML(orderResponseDTO, TestData.ORDER_RESPONSE_FILE_NAME);
            Assert.assertTrue(file.exists());

            Assert.assertTrue(new SchemaValidator().validateOrderResponse(file.getAbsolutePath()));

            byte[] xmlByteArray = OrderUtils.generateEHFOrderResponseXML(orderResponseDTO);
            Assert.assertNotNull(xmlByteArray);

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void getEHFOrderResponse() {

        try {

            InputStream is = getClass().getResourceAsStream("order-response-ehf.xml");

            OrderResponseDTO orderResponseDTO = OrderUtils.getEHFOrderResponse(is);

            File newFile = new File("order-response-out.xml");
            if (newFile.exists()) {
                newFile.delete();
            }

            OrderUtils.generateEHFOrderResponseXML(orderResponseDTO, newFile.getAbsolutePath());

            Assert.assertNotNull(orderResponseDTO);

            Assert.assertTrue(new SchemaValidator().validateOrderResponse(newFile.getAbsolutePath()));

        } catch (Throwable ex) {
            Assert.fail(ex.getMessage());
        }
    }
}
