package com.ebanking.accountservice.dto.response;


import java.time.LocalDateTime;

public class TransactionDTO {
    private String typeTransaction;
    private Double montant;
    private LocalDateTime date;
//    private String motife;       // Pour Virement
//    private String phoneNumber;   // Pour MobileRecharge

    public TransactionDTO() {}

    public TransactionDTO(String typeTransaction, Double montant, LocalDateTime date) {
        this.typeTransaction = typeTransaction;
        this.montant = montant;
        this.date = date;
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



}
