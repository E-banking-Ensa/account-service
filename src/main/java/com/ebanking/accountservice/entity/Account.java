package com.ebanking.accountservice.entity;

import com.ebanking.accountservice.utils.RibGenerator;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double balance = 0.0;
    private String currency = "MAD";
    
    // Référence vers l'utilisateur dans user-service (centralisé)
    @Column(name = "user_id", nullable = false)
    private java.util.UUID userId;
    
    // Relation locale avec Client pour les requêtes JPA (optionnel, peut être supprimé après migration complète)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    
    @OneToMany
    private List<Account> beneficiere;

//    @OneToMany
//    private List<Transaction> transactions;
    // ajouter par abdelilah
      @ManyToMany(mappedBy = "accounts")
      private List<Transaction> transactions;

    private String rib;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus  accountStatus = AccountStatus.PENDING;;


    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public java.util.UUID getUserId() {
        return userId;
    }

    public void setUserId(java.util.UUID userId) {
        this.userId = userId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Account> getBeneficiere() {
        return beneficiere;
    }

    public void setBeneficiere(List<Account> beneficiere) {
        this.beneficiere = beneficiere;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountStatus getaccountStatus() {
        return  accountStatus;
    }

    public void setaccountStatus(AccountStatus  accountStatus) {
        this.accountStatus =  accountStatus;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }


    // PrePersist pour générer le RIB automatiquement
    @PrePersist
    public void generateRib() {
        if (this.rib == null || this.rib.isEmpty()) {
            this.rib = RibGenerator.generate(); // Utilise une classe utilitaire pour créer le RIB
        }
    }
}