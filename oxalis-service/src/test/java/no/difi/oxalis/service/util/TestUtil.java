/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 *
 * @author aktharhussainis
 */
public class TestUtil {
    
    public static byte[] streamObject(Object obj) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        return baos.toByteArray();
    } 
}
