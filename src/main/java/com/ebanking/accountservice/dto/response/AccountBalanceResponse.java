package com.ebanking.accountservice.dto.response;


public class AccountBalanceResponse {

    private int accountId;
    private double balance;
    private String currency;

    public AccountBalanceResponse() {}

    public AccountBalanceResponse(int accountId, double balance, String currency) {
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
