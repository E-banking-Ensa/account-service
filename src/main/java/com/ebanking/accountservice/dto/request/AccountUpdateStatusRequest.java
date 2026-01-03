package com.ebanking.accountservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountUpdateStatusRequest {

    private String  accountStatus; // ACTIVE | INACTIVE | SUSPENDED | PENDING


}
