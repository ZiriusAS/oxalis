/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehandel.no;

/**
 *
 * @author Dinesh
 */
public enum PaymentMeansCode {

    // For full list please refer this link http://docs.peppol.eu/poacc/billing/3.0/codelist/UNCL4461/
    INSTRUMENT_NOT_DEFINED("1"),
    CREDIT_TRANSFER("30"),
    DEBIT_TRANSFER("31");
    
    private String value;

    private PaymentMeansCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
