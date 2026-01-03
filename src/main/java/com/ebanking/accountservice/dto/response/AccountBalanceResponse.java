package com.ebanking.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountBalanceResponse {

    private int accountId;
    private double balance;
    private String currency;

}
