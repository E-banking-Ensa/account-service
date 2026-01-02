package com.ebanking.accountservice.entity;

public enum AccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    BUSINESS("Business Account"),
    INVESTMENT("Investment Account");

    private final String label;

    AccountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
