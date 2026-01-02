package com.ebanking.accountservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class VirementDTO extends TransactionDTO {

    //private String motife;
    @JsonProperty("details")
    private VirementDetailsDTO details;


    public VirementDTO(String typeTransaction, Double montant, String motife, LocalDateTime date, String destinationRib,
                       String typeVirement) {
        super(typeTransaction, montant, date); // Appel du constructeur de la classe parente
        //this.motife = motife;
        this.details = new VirementDetailsDTO(motife, destinationRib, typeVirement);
    }


//    public String getMotife() {
//        return motife;
//    }
//
//    public void setMotife(String motife) {
//        this.motife = motife;
//    }


    public VirementDetailsDTO getDetails() {
        return details;
    }

    public void setDetails(VirementDetailsDTO details) {
        this.details = details;
    }
}
