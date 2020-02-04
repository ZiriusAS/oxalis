package ehandel.no.dto;

public class BillingReferenceDTO extends BaseDTO {

    private String invoiceDocumentReference;

    private String billingReferenceLine;

    public String getInvoiceDocumentReference() {
        return invoiceDocumentReference;
    }

    public void setInvoiceDocumentReference(String invoiceDocumentReference) {
        this.invoiceDocumentReference = invoiceDocumentReference;
    }

    public String getBillingReferenceLine() {
        return billingReferenceLine;
    }

    public void setBillingReferenceLine(String billingReferenceLine) {
        this.billingReferenceLine = billingReferenceLine;
    }
}
