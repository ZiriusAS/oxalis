/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.difi.oxalis.service.util;

/**
 *
 * @author Dinesh
 */
public enum Property {
    
    DATASOURCE_NAME("ds.name"),
    
    TEST_ENVIRONMENT("test.environment"),
    
    OVERRIDE_HEADER("override.header"),
    
    OXALIS_SERVER_URL("oxalis.server.url"),
    
    OXALIS_CERTIFICATE_PATH("oxalis.certificate.path"),

    SAVE_IN_BOTH_OBJECT_STORAGE_DB_ENABLE("save.both.objectstorage.db.enable"),

    OBJECT_STORAGE_ENABLE("object.storage.enable"),
    
    SEND_GRID__API_KEY("send.grid.api.key"),
    
    OUTBOUND_MESSAGE_STORE_PATH("oxalis.outbound.message.store"), 
    
    FOLLOWUP_MESSAGE_STORE_PATH("oxalis.followup.message.store");
    
    protected String propertyName;

    Property(String name) {
        this.propertyName = name;
    }
    
    @Override
    public String toString() {
        return propertyName;
    }
}