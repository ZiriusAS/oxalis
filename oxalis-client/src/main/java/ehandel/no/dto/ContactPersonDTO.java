package ehandel.no.dto;

public class ContactPersonDTO extends BaseDTO {

    private String id;
    private String name;
    private String telephone;
    private String teleFax;
    private String email;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getTeleFax() {
        return teleFax;
    }
    public void setTeleFax(String teleFax) {
        this.teleFax = teleFax;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
