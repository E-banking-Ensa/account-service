package com.ebanking.accountservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountUpdateStatusResponse {

    private int accountId;
    private String  accountStatus;
    private LocalDateTime updatedAt;


}
