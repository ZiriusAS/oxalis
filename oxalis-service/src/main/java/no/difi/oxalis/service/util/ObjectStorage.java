/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

import com.zirius.objectstorage.ObjectService;
import com.zirius.objectstorage.ObjectStorageFile;
import com.zirius.objectstorage.ObjectStorageFiles;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.EncoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dinesh
 */
public class ObjectStorage {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ObjectStorage.class);
  
    public static List<String> uploadFiles(HashMap<String, byte[]> fileListMap) throws IOException, 
            EncoderException, Exception {
        
        List<String> uploadedFile = new ArrayList<String>();
        
        try {
        
            ObjectStorageFiles fs = new ObjectStorageFiles();

            for(Map.Entry<String, byte[]> entry : fileListMap.entrySet()) {

                ObjectStorageFile f = new ObjectStorageFile();
                f.setFilename(entry.getKey());
                f.setFileContent(entry.getValue());
                fs.getObjectStorageFiles().add(f);
            }

            LOGGER.info("Files to be uploaded to object storage: " + fs.getObjectStorageFiles().size());
            
            uploadedFile = ObjectService.uploadFile(fs);
            
            LOGGER.info("Upload to Object Storage Success: " + uploadedFile.size());

        } catch (IOException ex) {
            LOGGER.error("Object Stoirage - IO Exception Ocurred: " + ex);
            throw ex;
        } catch (EncoderException ex) {
            LOGGER.error("Object Stoirage - Encoder Exception Ocurred: " + ex);
            throw ex;
        }
        
        return uploadedFile;
    }

    /**
     * To get file content from object storage
     */
    public static byte[] getFile(String fileName) {

        try {

            return ObjectService.getFile(fileName);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to get file from object storage", e);
        }
    }
}