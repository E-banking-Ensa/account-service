package com.ebanking.accountservice.dto.response;

import java.time.LocalDateTime;

public class ReleveCompteDTO {
    private String typeTransaction;
    private Double montant;
    private LocalDateTime date;
    private String motife;       // Pour Virement
    private String typeVirement;




    public ReleveCompteDTO() {}

    public ReleveCompteDTO(String typeTransaction, Double montant, LocalDateTime date, String motife, String typeVirement) {
        this.typeTransaction = typeTransaction;
        this.montant = montant;
        this.date = date;
        this.motife = motife;
        this.typeVirement = typeVirement;
    }


    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMotife() {
        return motife;
    }

    public void setMotife(String motife) {
        this.motife = motife;
    }

    public String getTypeVirement() {
        return typeVirement;
    }

    public void setTypeVirement(String typeVirement) {
        this.typeVirement = typeVirement;
    }
}
