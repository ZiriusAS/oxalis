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
public class HandlingExceptionDTO {
    
    private String version;
    private ExceptionDTO exception;
    private String sbdh;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ExceptionDTO getException() {
        return exception;
    }

    public void setException(ExceptionDTO exception) {
        this.exception = exception;
    }

    public String getSbdh() {
        return sbdh;
    }

    public void setSbdh(String sbdh) {
        this.sbdh = sbdh;
    }
    
}
