/*
 * @(#)AllowanceChargeReasons.java
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
 * The Class AllowanceChargeReasons.
 * 
 * @author amuthar
 * @since ehf; Mar 07, 2012
 */
public enum AllowanceChargeReasons {

    PACKING_COST("Packing cost"), 
    PROMOTION_DISCOUNT("Promotion discount"), 
    DAMAGE("Damage"), 
    CONTRACT("Contract");

    private String value;

    private AllowanceChargeReasons(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
