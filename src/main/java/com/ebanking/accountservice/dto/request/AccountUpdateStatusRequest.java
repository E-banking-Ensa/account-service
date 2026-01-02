package com.ebanking.accountservice.dto.request;


public class AccountUpdateStatusRequest {


    private String  accountStatus; // ACTIVE | INACTIVE | SUSPENDED | PENDING

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
