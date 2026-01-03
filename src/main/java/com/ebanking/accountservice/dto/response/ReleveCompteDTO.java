package com.ebanking.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReleveCompteDTO {

    private String typeTransaction;
    private Double montant;
    private LocalDateTime date;
    private String motife;       // Pour Virement
    private String typeVirement;


}
