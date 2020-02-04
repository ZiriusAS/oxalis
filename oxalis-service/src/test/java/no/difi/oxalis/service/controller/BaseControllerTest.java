/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author aktharhussainis
 */
public class BaseControllerTest {
    
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
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    protected static byte[] streamObject(Object obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        return baos.toByteArray();
    }
}
