
package eu.peppol.outbound.api;

/**
 * The Enum UserRole.
 */
public enum UserRole {
    
    AccessPointAdmin("AccessPointAdmin"),

    AccessPointUser("AccessPointUser");

    private String value;

    private UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidRole(String roleName) {

        for (UserRole userRole : UserRole.values()) {
            if (userRole.getValue().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
