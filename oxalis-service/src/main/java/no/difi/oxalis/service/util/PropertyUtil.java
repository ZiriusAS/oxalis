/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Dinesh
 */
@Slf4j
public class PropertyUtil {
    
    private static Properties Oxalis_Conf;
    
    public static final String OXALIS_GLOBAL_PROPERTIES = "oxalis.conf";
    
    private static void loadOxalisConf() throws IOException {

        Oxalis_Conf = new Properties();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input =  new FileInputStream(computeOxalisHomeDir());

        // load a properties file
        Oxalis_Conf.load(input);
    }
    
    private static String computeOxalisHomeDir() {
        File oxalisHomeDirectory = new OxalisHomeDirectory().locateDirectory();
        log.info("Oxalis home directory: " + oxalisHomeDirectory);
        final File file = new File(oxalisHomeDirectory, OXALIS_GLOBAL_PROPERTIES);
        return file.getPath();
    }

    public static String getProperty(Property key) throws IOException {
        
        if (Oxalis_Conf == null) {
            loadOxalisConf();
        }
        String property = Oxalis_Conf.getProperty(key.toString());
        if (property != null) {
            property = property.replace("\"", "");
        }
        return property;
    }
}
