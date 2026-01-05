package com.ebanking.accountservice.dto.request;

import lombok.Data;

@Data
public class BalanceUpdateRequest {
    private String rib;
    private Double amount;
}
