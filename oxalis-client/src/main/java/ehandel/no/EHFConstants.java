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
public enum EHFConstants {

    CHAR_ENCODING("UTF-8"),
    UBL_VERSION_ID("2.0"),
    UBL_VERSION_ID_TWO_DOT_ONE("2.1"),
    INVOICE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver1"),
    //Commented one is mentioned in example file and in implementation guide. Respective other one is used for smp lookup. - Same applies for credit note, reminder and order.
    //INVOICE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biicoretrdm010:ver1.0:#urn:www.peppol.eu:bis:peppol4a:ver1.0#urn:www.difi.no:ehf:faktura:ver2.0"),
    //INVOICE_TWO_DOT_ONE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0"),
    INVOICE_TWO_DOT_ONE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol4a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0"),
    CREDITNOTE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biicoretrdm014:ver1.0:#urn:www.cenbii.eu:profile:biixx:ver1.0#urn:www.difi.no:ehf:kreditnota:ver1"),
    INVOICE_CREDITNOTE_TWO_DOT_ONE_CUSTOMIZATION_ID_FOR_INVOICE("urn:www.cenbii.eu:transaction:biitrns010:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:faktura:ver2.0"),
    INVOICE_CREDITNOTE_TWO_DOT_ONE_CUSTOMIZATION_ID_FOR_CREDITNOTE("urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.peppol.eu:bis:peppol5a:ver2.0:extended:urn:www.difi.no:ehf:kreditnota:ver2.0"),
    CREDITNOTE_TWO_DOT_ONE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns014:ver2.0:extended:urn:www.cenbii.eu:profile:biixx:ver2.0:extended:urn:www.difi.no:ehf:kreditnota:ver2.0"),
    REMINDER_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biicoretrdm017:ver1.0:#urn:www.cenbii.eu:profile:biixy:ver1.0#urn:www.difi.no:ehf:purring:ver1"),
    //REMINDER_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:BiiCoreTrdm017:ver1.0:extentionId"),
    ORDER_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns001:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver2.0:extended:urn:www.difi.no:ehf:ordre:ver1.0"),
    //ORDER_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns001:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver1.0"),
    ORDER_RESPONSE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns076:ver2.0:extended:urn:www.peppol.eu::bis:peppol28a:ver1.0:extended:urn:www.difi.no:ehf:ordrebekreftelse:ver1"),
    //ORDER_RESPONSE_CUSTOMIZATION_ID("urn:www.cenbii.eu:transaction:biitrns076:ver2.0:extended:urn:www.peppol.eu:bis:peppol28a:ver1.0:extended:urn:www.difi.no:ehf:ordrebekreftelse:ver1.0"),
    INVOICE_PROFILE_ID("urn:www.cenbii.eu:profile:bii04:ver1.0"),
    INVOICE_TWO_DOT_ONE_PROFILE_ID("urn:www.cenbii.eu:profile:bii04:ver2.0"),
    CREDIT_NOTE_PROFILE_ID("urn:www.cenbii.eu:profile:biixx:ver1.0"),
    CREDIT_TWO_DOT_ONE_NOTE_PROFILE_ID("urn:www.cenbii.eu:profile:biixx:ver2.0"),
    INVOICE_CREDIT_TWO_DOT_ONE_NOTE_PROFILE_ID("urn:www.cenbii.eu:profile:bii05:ver2.0"),
    REMINDER_PROFILE_ID("urn:www.cenbii.eu:profile:bii08:ver1.0"),
    ORDER_PROFILE_ID("urn:www.cenbii.eu:profile:bii28:ver2.0"),
    INVOICE_TYPE("380"),
    CREDIT_NOTE_TYPE("381"),
    NORWAY("NO"),
    IBAN("IBAN"),
    BBAN("BBAN"),
    BIC("BIC"),
    PDF_CONTENT_TYPE("application/pdf"),
    DOCUMENT("Doc"),
    TAX_EXEMPTION_REASON_CODE("AAM"),
    TAX_EXEMPTION_REASON_CODE_V3("vatex-eu-o"),
    TAX_EXEMPTION_REASON("Tax Free"),
    PAYMENT_MEANS_CODE("31"),
    PAYMENT_MEANS_LIST_ID("UN/ECE 4461 Subset"),
    PAYMENT_MEANS_TWO_DOT_ONE_LIST_ID("UNCL4461"),
    INVOICE_TYPE_CODE_LIST_ID("UNCL1001"),
    PAYMENT_MEANS_LIST_AGENCY_ID("NES"),
    GLN("GLN"),
    GTIN("GTIN"),
    ORDER_EHFV3_GTIN("0160"), // Link to list - https://vefa.difi.no/ehf/g3/codelist/ICD/
    TAX_CATEGORY_SCHEME_ID("UN/ECE 5305"),
    TAX_CATEGORY_TWO_DOT_ONE_SCHEME_ID("UNCL5305"),
    TAX_SCHEME_ID("UN/ECE 5153"),
    TAX_SCHEME_AGENCY_ID("6"),
    PARTY_TAX_SCHEME_AGENCY_ID("82"),
    SCHEME_AGENCY_ID("6"),
    PARTY_IDENTIFICATION_SCHEME_AGENCY_ID("9"),
    SCHEME_ID("ORGNR"),
    LEGAL_ENTITY_SCHEME_NAME("Foretaksregisteret"),
    LEGAL_ENTITY_SCHEME_SGENCY_ID("82"),
    ADDRESS_LIST_ID("ISO3166-1"),
    ADDRESS_LIST_ID_ORDER("ISO3166-1:Alpha2"),
    UNIT_CODE_LIST_ID("UNECERec20"),
    LIST_AGENCY_ID("6"),
    OP_MULTIPLY("multiply"),
    PDF("pdf"),
    DOT("."),
    DEFAULT_LANGUAGE("no"),
    DOCUMENT_CURRENCY_CODE_ID("ISO4217"),
    ORDER_RESPONSE_CODE("UNCL1225"),
    COUNTRY_CODE_NORWAY("NO"),
    ORDER_RESPONSE_ITEM_CLASSIFICATION_CODE("SST"),
    MVA("MVA"),
    ORG("ORG"),
    
    EHF_THREE_DOT_ZERO_ORDER_COMPANY_ID_SCHEME("0192"),
    EHF_THREE_DOT_ZERO_ORDER_LEGAL_ENTITY_SCHEME_NAME("Organisasjonsnummer"),
    
    EHF_THREE_DOT_ZERO_INVOICE("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2::Invoice##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"),
    EHF_THREE_DOT_ZERO_CREDIT_NOTE("urn:oasis:names:specification:ubl:schema:xsd:CreditNote-2::CreditNote##urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0::2.1"),
    EHF_THREE_DOT_ZERO_ORDER("urn:oasis:names:specification:ubl:schema:xsd:Order-2::Order##urn:fdc:peppol.eu:poacc:trns:order:3::2.1"),
    EHF_THREE_DOT_ZERO_ORDER_RESPONSE("urn:oasis:names:specification:ubl:schema:xsd:OrderResponse-2::OrderResponse##urn:fdc:peppol.eu:poacc:trns:order_response:3::2.1"),

    EHF_THREE_DOT_ZERO_ORDER_CUSTOMIZATION_ID("urn:fdc:peppol.eu:poacc:trns:order:3"),
    EHF_THREE_DOT_ZERO_ORDER_RESPONSE_CUSTOMIZATION_ID("urn:fdc:peppol.eu:poacc:trns:order_response:3"),
    
    EHF_THREE_DOT_ZERO_ORDER_PROFILE_ID("urn:fdc:peppol.eu:poacc:bis:ordering:3"),
    EHF_THREE_DOT_ZERO_ORDER_RESPONSE_PROFILE_ID("urn:fdc:peppol.eu:poacc:bis:ordering:3"),

    EHF_THREE_DOT_ZERO_CUSTOMIZATION_ID("urn:cen.eu:en16931:2017#compliant#urn:fdc:peppol.eu:2017:poacc:billing:3.0"),
    EHF_THREE_DOT_ZERO_PROFILE_ID("urn:fdc:peppol.eu:2017:poacc:billing:01:1.0"),
    EHFV3_SCHEME_ID("0192"),
    VAT("VAT");
    
    private String value;

    private EHFConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
