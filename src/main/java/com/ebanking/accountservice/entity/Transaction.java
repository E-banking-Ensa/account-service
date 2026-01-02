package com.ebanking.accountservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public  class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private Double Montant;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private TypeTransaction type;



    //ajouter par abdelilah
    @ManyToMany
    @JoinTable(
            name = "accounts_transactions",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> accounts;








}