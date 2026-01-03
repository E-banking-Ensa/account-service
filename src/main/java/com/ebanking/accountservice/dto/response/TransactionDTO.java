package com.ebanking.accountservice.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDTO {

    private String typeTransaction;
    private Double montant;
    private LocalDateTime date;
//    private String motife;       // Pour Virement
//    private String phoneNumber;   // Pour MobileRecharge





}
