package com.ebanking.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class infoCompteDTO {

    private String prenomClient;
    private String nomClient;
    private String ripCompte;
    private Double balance ;
    private String adresse;


}
