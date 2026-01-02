package com.ebanking.accountservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crypto_wallets")
public class CryptoWallet extends  Transaction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Column(unique = true, nullable = false)
    private String walletAddress;

    private Double balanceBTC;
    private Double balanceETH;

    @OneToOne(mappedBy = "cryptoWallet")
    private Client client;


    public CryptoWallet() {}

    public CryptoWallet(String walletAddress) {
        this.walletAddress = walletAddress;
        this.balanceBTC = 0.0;
        this.balanceETH = 0.0;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Double getBalanceBTC() {
        return balanceBTC;
    }

    public void setBalanceBTC(Double balanceBTC) {
        this.balanceBTC = balanceBTC;
    }

    public Double getBalanceETH() {
        return balanceETH;
    }

    public void setBalanceETH(Double balanceETH) {
        this.balanceETH = balanceETH;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}