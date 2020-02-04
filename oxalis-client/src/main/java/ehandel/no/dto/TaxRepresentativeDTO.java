package ehandel.no.dto;

public class TaxRepresentativeDTO extends BaseDTO {

    private String name;
    private String organisationNo;
    private AddressDTO postalAddress;
    private String taxTypeIntName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisationNo() {
        return organisationNo;
    }

    public void setOrganisationNo(String organisationNo) {
        this.organisationNo = organisationNo;
    }

    public AddressDTO getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(AddressDTO postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getTaxTypeIntName() {
        return taxTypeIntName;
    }

    public void setTaxTypeIntName(String taxTypeIntName) {
        this.taxTypeIntName = taxTypeIntName;
    }
}
