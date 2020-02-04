/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itella.api;

/**
 *
 * @author ashwinkumarr
 */
public enum PrintType {

    SIMPLEX("Simplex"),

    DUPLEX("Duplex");

    private String printType;

    private PrintType(String printType) {
        this.printType = printType;
    }

    public String getPrintType() {
        return printType;
    }
}
