package eu.peppol.outbound.api;

import java.io.Serializable;

/**
 * 
 * @author vasanthis
 */
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 112233445566778801L;

    private String userName;
    private String password;
    private String roleName;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
