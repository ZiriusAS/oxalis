/*
 * @(#)BaseServiceTest.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package no.difi.oxalis.service.transmission;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.dbcp2.BasicDataSource;
/**
 * Base class for service test cases.
 * 
 * @author senthilkumarn
 */
public class BaseServiceTest {

    protected static void init() throws Exception {

        try {
            // Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            // Construct DataSource
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName("com.mysql.jdbc.Driver");
            ds.setUrl("jdbc:mysql://ziriusmaster:3306/zapdb");
            ds.setUsername("zapuser");
            ds.setPassword("zappazz");

            ic.bind("java:comp/env/jdbc/zap", ds);
        } catch (NamingException ex) {
        }
    }
}
