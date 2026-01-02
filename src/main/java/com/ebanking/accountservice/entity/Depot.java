package com.ebanking.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Depot extends Transaction{
    @ManyToOne
    private Account account;
}
