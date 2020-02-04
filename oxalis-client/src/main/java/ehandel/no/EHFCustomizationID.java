/*
 * @(#)EHFConstants.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved.
 *
 * Use is subject to license terms. This software is protected by
 * copyright law and international treaties. Unauthorized reproduction or
 * distribution of this program, or any portion of it, may result in severe
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no;

/**
 * The Class EHFConstants.
 *
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public enum EHFCustomizationID {

    // PEPPOL Catalogues (PEPPOL BIS profile 1a)
    PEPPOL_CATALOGUE("urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2::Catalogue##urn:www.cenbii.eu:transaction:biicoretrdm019:ver1.0:#urn:www.peppol.eu:bis:peppol1a:ver1.0::2.0"),

    // Basic Order according to PEPPOL BIS 3a
    ORDER("urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:www.cenbii.eu:transaction:biicoretrdm001:ver1.0:#urn:www.peppol.eu:bis:peppol3a:ver1.0::2.0"),

    // EHF Invoice 2.0
    EHF_INVOICE_TWO_DOT_ZERO("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol4a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0::2.1"),

    // EHF CreditNote 2.0
    EHF_CREDITNOTE_TWO_DOT_ZERO("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.cenbii.eu:profile:biixx:ver2.0:extended:urn:www.difi.no:ehf:kreditnota:ver2.0::2.1"),

    // Message Level Response
    MLR("urn:www.cenbii.eu:transaction:biitrns071:ver2.0:extended:urn:www.peppol.eu:bis:peppol36a:ver1.0"),

    //EHF_INVOICE_CREDITNOTE 2.0(INVOICE)
    EHF_INVOICE_CREDITNOTE_TWO_DOT_ZERO_FORINVOICE("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0::2.1"),

    //EHF_INVOICE_CREDITNOTE 2.0(CREDITNOTE)
    EHF_INVOICE_CREDITNOTE_TWO_DOT_ZERO_FORCREDITNOTE("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:kreditnota:ver2.0::2.1"),

    //EHFORDER
    EHF_ORDER("urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:www.cenbii.eu:transaction:biitrns001:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver2.0:extended:urn:www.difi.no:ehf:ordre:ver1.0::2.1"),

    //EHF ORDER RESPONSE
    EHF_ORDER_RESPONSE("urn:oasis:names:specification:ubl:schema:xsd:OrderResponse-2::OrderResponse##urn:www.cenbii.eu:transaction:biitrns076:ver2.0:extended:urn:www.peppol.eu::bis:peppol28a:ver1.0:extended:urn:www.difi.no:ehf:ordrebekreftelse:ve"),
    
     // EHF 3.0 Invoice
    EHF_THREE_DOT_ZERO_INVOICE("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"),
    
    // EHF 3.0 Credit Note
    EHF_THREE_DOT_ZERO_CREDIT_NOTE("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1");

    private String value;

    private EHFCustomizationID(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
