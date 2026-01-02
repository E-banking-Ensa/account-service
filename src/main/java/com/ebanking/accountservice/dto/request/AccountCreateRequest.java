package com.ebanking.accountservice.dto.request;

public class AccountCreateRequest {
    private int userId;
    private String accountType;

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
