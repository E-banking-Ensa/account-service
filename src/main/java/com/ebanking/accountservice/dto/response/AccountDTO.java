package com.ebanking.accountservice.dto.response;

import com.ebanking.accountservice.entity.AccountType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonPropertyOrder({"accountId", "userId", "rib", "accountType","accountStatus", "balance","currency", "createdDate"})
public class AccountDTO {

    private int accountId;
    private AccountType accountType;
    private Double balance;
    private String currency;
    private String rib;
    private String  accountStatus;
    private LocalDateTime createdDate;
    private UUID userId;

}

