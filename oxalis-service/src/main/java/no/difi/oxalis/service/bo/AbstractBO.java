/*
 * @(#)AbstractBO.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package no.difi.oxalis.service.bo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Abstract class to be extended by all business objects.
 * 
 * @author senthilkumarn
 */
public abstract class AbstractBO {

    private static final String JNDI_CONTEXT = "java:comp/env";

    protected Connection con;

    protected AbstractBO() {
    }

    public AbstractBO(String dsName) {
        initialize(dsName);
    }

    public void initialize(String dsName) {
        try {
            con = getConnection(dsName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BOException(e);
        }
    }

    private Connection getConnection(String jndiName) throws NamingException, SQLException {

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup(JNDI_CONTEXT);
        DataSource ds = (DataSource) envCtx.lookup(jndiName);
        return ds.getConnection();
    }

    protected String getSQLPlaceHolders(int size) {

        StringBuffer sb = new StringBuffer();
        for (int i = size; i > 0; i--) {
            sb.append("?,");
        }
        String qMark = sb.toString();
        return qMark.substring(0, qMark.length() - 1);
    }

    protected String constructSQLPlaceHolders(String sql, int size) {
        return sql.replace("?", getSQLPlaceHolders(size));
    }

    protected void release(PreparedStatement ps, ResultSet rs) {
        release(rs);
        release(ps);
    }

    protected void release(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception ignore) {
            // Ignore
        }
    }

    protected void release(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ignore) {
            // Ignore
        }
    }

    public void cleanup() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }
}
