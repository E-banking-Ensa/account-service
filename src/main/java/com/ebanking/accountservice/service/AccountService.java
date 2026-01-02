package com.ebanking.accountservice.service;


import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.response.AccountBalanceResponse;
import com.ebanking.accountservice.dto.response.AccountDTO;
import com.ebanking.accountservice.dto.response.AccountUpdateStatusResponse;
import com.ebanking.accountservice.dto.response.TransactionDTO;

import java.util.List;

public interface AccountService {

     // Créer un compte pour un client donné.
    AccountDTO createAccount(AccountCreateRequest request);

    // Modifier le statut du compte
    AccountUpdateStatusResponse updateAccountStatus(int accountId, AccountUpdateStatusRequest request);

     // Consulter le solde d'un compte
    AccountBalanceResponse getAccountBalance(int accountId);

    // Lister les comptes d’un client  donné.
    List<AccountDTO> getAccountsByClientId(int clientId);



    //transaction
    public List<TransactionDTO> getTransactionsByAccountId(int accountId) ;



    // genration du releve
    public byte[] getTransactiondtoByAccountId(int accountId);

}
