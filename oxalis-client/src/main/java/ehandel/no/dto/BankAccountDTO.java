/*
 * BankAccountDTO.java
 *
 * Copyright (c) 2012, Zirius AS.
 * All rights reserved. 
 * 
 * Use is subject to license terms. This software is protected by 
 * copyright law and international treaties. Unauthorized reproduction or 
 * distribution of this program, or any portion of it, may result in severe 
 * civil and criminal penalties, and will be prosecuted to the maximum extent.
 */
package ehandel.no.dto;

/**
 * The Class BankAccountDTO.
 * 
 * @author amuthar
 * @since ehf; Feb 15, 2012
 */
public class BankAccountDTO extends BaseDTO {

    private String bankAccountId;
    private String bankAccountNumber;
    private String bankAccountName;
    private String bankName;
    private String bic;
    private String iBanNo;
    private CurrencyDTO currencyDTO;
    private AddressDTO bankAddressDTO;

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getiBanNo() {
        return iBanNo;
    }

    public void setiBanNo(String iBanNo) {
        this.iBanNo = iBanNo;
    }

    public CurrencyDTO getCurrencyDTO() {
        return currencyDTO;
    }

    public void setCurrencyDTO(CurrencyDTO currencyDTO) {
        this.currencyDTO = currencyDTO;
    }

    public AddressDTO getBankAddressDTO() {
        return bankAddressDTO;
    }

    public void setBankAddressDTO(AddressDTO bankAddressDTO) {
        this.bankAddressDTO = bankAddressDTO;
    }
}
