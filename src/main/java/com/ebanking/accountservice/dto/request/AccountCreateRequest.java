package com.ebanking.accountservice.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AccountCreateRequest {
    private UUID userId;
    private String accountType;
    private Double initialBalance;  // Solde initial optionnel

}
