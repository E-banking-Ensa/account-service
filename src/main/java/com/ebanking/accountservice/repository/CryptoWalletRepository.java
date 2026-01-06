package com.ebanking.accountservice.repository;

import com.ebanking.accountservice.entity.Account;
import com.ebanking.accountservice.entity.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {

    // Vérifier si un wallet principal existe pour un client
    Optional<CryptoWallet> findByClientId(UUID clientId);

    // Vérifier si un wallet avec une adresse donnée existe déjà
    Optional<CryptoWallet> findByWalletAddress(String walletAddress);

}
