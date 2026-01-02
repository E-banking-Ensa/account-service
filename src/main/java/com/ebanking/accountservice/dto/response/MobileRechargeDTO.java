package com.ebanking.accountservice.dto.response;

import java.time.LocalDateTime;

public class MobileRechargeDTO  extends  TransactionDTO {

    private String phoneNumber;

    public MobileRechargeDTO(String typeTransaction, Double montant, String phoneNumber, LocalDateTime date) {
        super(typeTransaction, montant, date); // Appel du constructeur de la classe parente
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
