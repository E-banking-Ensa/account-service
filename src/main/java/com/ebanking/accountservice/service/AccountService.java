package com.ebanking.accountservice.service;


import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.response.AccountBalanceResponse;
import com.ebanking.accountservice.dto.response.AccountDTO;
import com.ebanking.accountservice.dto.response.AccountUpdateStatusResponse;
import com.ebanking.accountservice.dto.response.TransactionDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {

     // Créer un compte pour un client donné.
    AccountDTO createAccount(AccountCreateRequest request);

    // Modifier le statut du compte
    AccountUpdateStatusResponse updateAccountStatus(int accountId, AccountUpdateStatusRequest request);

     // Consulter le solde d'un compte
    AccountBalanceResponse getAccountBalance(int accountId);
    
    // Rechercher un compte par RIB
    AccountDTO getAccountByRib(String rib);

    // Lister les comptes d'un client  donné.
    List<AccountDTO> getAccountsByClientId(UUID clientId);
    
    // Lister tous les comptes
    List<AccountDTO> getAllAccounts();



    //transaction
    public List<TransactionDTO> getTransactionsByAccountId(int accountId) ;



    // genration du releve
    public byte[] getTransactiondtoByAccountId(int accountId);

    // pour récupérer le nombre total de comptes
    public long getTotalAccounts();


    public long getNumberOfAccountsByClientId(UUID clientId);
    
    // Mettre à jour le solde (positif pour crédit, négatif pour débit)
    void updateBalance(String rib, Double amount);
}
