package com.ebanking.accountservice.dto.response;

public class infoCompteDTO {

    private String prenomClient;
    private String nomClient;
    private String ripCompte;
    private Double balance ;

    public infoCompteDTO(String prenomClient, String nomClient, String ripCompte, Double balance) {
        this.prenomClient = prenomClient;
        this.nomClient = nomClient;
        this.ripCompte = ripCompte;
        this.balance = balance;
    }

    public String getPrenomClient() {
        return prenomClient;
    }

    public void setPrenomClient(String prenomClient) {
        this.prenomClient = prenomClient;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getRipCompte() {
        return ripCompte;
    }

    public void setRipCompte(String ripCompte) {
        this.ripCompte = ripCompte;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
