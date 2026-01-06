package com.ebanking.accountservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crypto_wallets")
public class CryptoWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String walletAddress;

    private Double balanceBTC;
    private Double balanceETH;

    @OneToOne(mappedBy = "cryptoWallet")
    private Client client;

    public CryptoWallet() {
    }

    public CryptoWallet(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}