/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ehandel.no.dto;

/**
 *
 * @author dineshkumarrc
 */
public class AccesspointDetails {
    
    private String url;
    private String busDoxProtocol;
    private String commonName;

    public String getBusDoxProtocol() {
        return busDoxProtocol;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getUrl() {
        return url;
    }
    
    public void setBusDoxProtocol(String busDoxProtocol) {
        this.busDoxProtocol = busDoxProtocol;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
