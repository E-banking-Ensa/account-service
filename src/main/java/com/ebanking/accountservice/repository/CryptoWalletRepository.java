package com.ebanking.accountservice.repository;


import com.ebanking.accountservice.entity.Account;
import com.ebanking.accountservice.entity.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {

    // Vérifier si un wallet principal existe pour un client
    Optional<CryptoWallet> findByClientId(Long clientId);

    // Vérifier si un wallet avec une adresse donnée existe déjà
    Optional<CryptoWallet> findByWalletAddress(String walletAddress);

    // Rechercher tous les wallets liés à un compte
    List<CryptoWallet> findByAccountsContaining(Account account);
}
