/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author aktharhussainis
 */
public class BaseController {
    
    public Object getObjectFromStream(byte[] data) throws IOException, ClassNotFoundException {
        
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        
        return is.readObject();
    }
    
    public byte[] getByteArray(Object responseInfo) throws IOException {
        
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(responseInfo).getBytes("UTF-8");
    }
    
    /*public void respond(Object responseInfo, HttpServletResponse response) throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        outputStream.write(mapper.writeValueAsString(responseInfo).getBytes("UTF-8"));
        
        IOUtils.closeQuietly(outputStream);
    }*/
    
    public byte[] respond(Object responseInfo) throws IOException {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(responseInfo);
        IOUtils.closeQuietly(oos);
        
        return bos.toByteArray();
    }
    
    public Response writeErrorResponse(Exception e) {
        
        return Response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .entity("Error Code : " + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " - " + ExceptionUtils.getRootCauseMessage(e)).build();
    }
}
