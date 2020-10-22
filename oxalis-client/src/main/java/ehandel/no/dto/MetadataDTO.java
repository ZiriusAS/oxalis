/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehandel.no.dto;

/**
 *
 * @author zerp labs
 */
public class MetadataDTO {
    
    private String version;
    private String customerIdentifier;
    private String divisionIdentifier;
    private String userIdentifier;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getDivisionIdentifier() {
        return divisionIdentifier;
    }

    public void setDivisionIdentifier(String divisionIdentifier) {
        this.divisionIdentifier = divisionIdentifier;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }
    
}
