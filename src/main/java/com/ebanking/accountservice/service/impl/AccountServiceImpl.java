package com.ebanking.accountservice.service.impl;

import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.response.*;
import com.ebanking.accountservice.entity.*;
import com.ebanking.accountservice.mapper.AccountMapper;
import com.ebanking.accountservice.repository.*;
import com.ebanking.accountservice.service.AccountService;
import com.ebanking.accountservice.utils.RelevePdfUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    // transaction
    private final TransactionRepository transactionRepository;
    private final VirementRepository virementRepository;
    private final DepotRepository depotRepository;
    private final RetraitRepository retraitRepository;
    private final MobileRechargeRepository mobileRechargeRepository;
    private final CryptoWalletRepository cryptoWalletRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            UserRepository userRepository,
            AccountMapper accountMapper,

            VirementRepository virementRepository,
            DepotRepository depotRepository,
            RetraitRepository retraitRepository,
            MobileRechargeRepository mobileRechargeRepository,
            CryptoWalletRepository cryptoWalletRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountMapper = accountMapper;

        //transaction
        this.transactionRepository = transactionRepository;
        this.virementRepository = virementRepository;
        this.depotRepository = depotRepository;
        this.retraitRepository = retraitRepository;
        this.mobileRechargeRepository = mobileRechargeRepository;
        this.cryptoWalletRepository = cryptoWalletRepository;

    }

    @Override
    public AccountDTO createAccount(AccountCreateRequest request) {

        // 1️Vérifier que l'utilisateur existe
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mapping DTO to Entity
        Account account = accountMapper.toEntity(request);
        account.setUser(user);

        //  Sauvegarde (RIB généré via @PrePersist)
        Account savedAccount = accountRepository.save(account);

        //  Mapping Entity to DTO
        return accountMapper.toDTO(savedAccount);
    }




    @Override
    public AccountUpdateStatusResponse updateAccountStatus(int accountId, AccountUpdateStatusRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        AccountStatus newStatus = AccountStatus.valueOf(request.getAccountStatus());

        account.setaccountStatus(newStatus);
        account.setUpdatedDate(LocalDateTime.now());

        accountRepository.save(account);

        AccountUpdateStatusResponse response = new AccountUpdateStatusResponse();
        response.setAccountId(account.getId());
        response.setAccountStatus(account.getaccountStatus().name());
        response.setUpdatedAt(account.getUpdatedDate());

        return response;
    }



    @Override
    public AccountBalanceResponse getAccountBalance(int accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return new AccountBalanceResponse(
                account.getId(),
                account.getBalance(),
                account.getCurrency()
        );
    }




    @Override
    public List<AccountDTO> getAccountsByClientId(UUID clientId) {
        // Vérifier que l'utilisateur existe
//        userRepository.findById(clientId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 2⃣ Récupérer les comptes
//        List<Account> accounts = accountRepository.findByUserId(clientId);

        // Vérifier que le client existe
        Client client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Récupérer les comptes depuis le client
        List<Account> accounts = client.getAccounts();

        return accountMapper.toDTOList(accounts);

    }



    // transaction
    public List<TransactionDTO> getTransactionsByAccountId(int accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<TransactionDTO> result = new ArrayList<>();

        // Parcourir les transactions liées au compte
        for (Transaction t : account.getTransactions()) {
            TypeTransaction type = t.getType();

            switch (type) {
                case VIREMENT -> {
                    Virement v = virementRepository.findById(t.getId()).orElse(null);
                    if (v != null) {
                        if (v.getSource() != null && v.getSource().getId() == accountId) {
                            VirementDTO dto = new VirementDTO ("VIREMENT", -v.getMontant(), v.getMotife(), v.getDate(), v.getDestination().getRib(),
                                    v.getType_virement().name());
                             //dto.setMotife(v.getMotife());
                            result.add(dto);
                        } else if (v.getDestination() != null && v.getDestination().getId() == accountId) {
                            VirementDTO dto = new VirementDTO ("VIREMENT", +v.getMontant(), v.getMotife(), v.getDate(), v.getDestination().getRib(),
                                    v.getType_virement().name());
                            //dto.setMotife(v.getMotife());
                            result.add(dto);
                        }
                    }
                }

                case DEPOSIT -> {
                    Depot d = (Depot) t;
                    result.add(new TransactionDTO("DEPOSIT", +d.getMontant(), d.getDate()));
                }

                case WITHDRAW -> {
                    Retrait r = (Retrait) t;
                    result.add(new TransactionDTO("WITHDRAW", -r.getMontant(),  r.getDate()));
                }

                case RECHARGE -> {
                    MobileRecharge m = mobileRechargeRepository.findById(t.getId()).orElse(null);
                    if (m != null) {
                        TransactionDTO dto = new MobileRechargeDTO("RECHARGE", -m.getMontant(),m.getPhoneNumber(), m.getDate());
                         //dto.setPhoneNumber(m.getPhoneNumber());
                        result.add(dto);
                    }
                }

                case CRYPTO_BUY, CRYPTO_SELL -> {
                    CryptoWallet c = cryptoWalletRepository.findById((long) t.getId()).orElse(null);
                    if (c != null) {
                        if (c.getType() == TypeTransaction.CRYPTO_BUY) {
                            result.add(new TransactionDTO("CRYPTO_BUY", -c.getMontant(), c.getDate()));
                        } else if (c.getType() == TypeTransaction.CRYPTO_SELL) {
                            result.add(new TransactionDTO("CRYPTO_SELL", +c.getMontant(), c.getDate()));
                        }
                    }
                }
            }
        }

        return result;
    }






    // gneration du  releve compte
    public byte[] getTransactiondtoByAccountId(int accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        infoCompteDTO info = new infoCompteDTO(account.getUser().getFirstName(),account.getUser().getLastName(),account.getRib(),account.getBalance(),account.getUser().getAdresse());

        List<ReleveCompteDTO> result = new ArrayList<>();

        // ===== Dates "manuelles" pour le test =====
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate   = LocalDate.of(2026, 1, 31);


        // Filtrer les transactions entre les deux dates (sans tenir compte de l'heure)
        List<Transaction> filteredTransactions = account.getTransactions().stream()
                .filter(t -> {
                    LocalDate transactionDate = t.getDate().toLocalDate(); // on ignore l'heure
                    return !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate);
                })
                .collect(Collectors.toList());

        // Parcourir les transactions liées au compte
        for (Transaction t : account.getTransactions()) {

            TypeTransaction type = t.getType();

            switch (type) {

                case VIREMENT -> {
                    Virement v = virementRepository.findById(t.getId()).orElse(null);
                    if (v != null) {
                        if (v.getSource() != null && v.getSource().getId() == accountId) {
                            //result.add(new TransactionDTO("VIREMENT", -v.getMontant(), v.getDate()));
//                            VirementDTO dto = new VirementDTO ("VIREMENT", -v.getMontant(), v.getMotife(), v.getDate(), v.getDestination().getRib(),
//                                    v.getType_virement().name());
                            //dto.setMotife(v.getMotife());
                            result.add(new ReleveCompteDTO("VIREMENT", -v.getMontant(), v.getDate(),v.getMotife(),v.getType_virement().name()));
//                            result.add(dto);
                        } else if (v.getDestination() != null && v.getDestination().getId() == accountId) {
                            //result.add(new TransactionDTO("VIREMENT", +v.getMontant(), v.getDate()));
//                            VirementDTO dto = new VirementDTO ("VIREMENT", +v.getMontant(), v.getMotife(), v.getDate(), v.getDestination().getRib(),
//                                    v.getType_virement().name());
                            result.add(new ReleveCompteDTO("VIREMENT", +v.getMontant(), v.getDate(),v.getMotife(),v.getType_virement().name()));
                            //dto.setMotife(v.getMotife());
//                            result.add(dto);
                        }
                    }
                }

                case DEPOSIT -> {
                    Depot d = (Depot) t;
                    //result.add(new TransactionDTO("DEPOSIT", +d.getMontant(), d.getDate()));
                    result.add(new ReleveCompteDTO("DEPOSIT", +d.getMontant(), d.getDate(),null,null));
                }

                case WITHDRAW -> {
                    Retrait r = (Retrait) t;
                    //result.add(new TransactionDTO("WITHDRAW", -r.getMontant(), r.getDate()));
                    result.add(new ReleveCompteDTO("WITHDRAW", -r.getMontant(), r.getDate(),null,null));
                }

                case RECHARGE -> {
                    MobileRecharge m = mobileRechargeRepository.findById(t.getId()).orElse(null);
                    if (m != null) {
                        //result.add(new TransactionDTO("RECHARGE", -m.getMontant(), m.getDate()));
                        result.add(new ReleveCompteDTO("RECHARGE", -m.getMontant(), m.getDate(),null, null));

                    }
                }

                case CRYPTO_BUY, CRYPTO_SELL -> {
                    CryptoWallet c = cryptoWalletRepository.findById((long) t.getId()).orElse(null);
                    if (c != null) {
                        if (c.getType() == TypeTransaction.CRYPTO_BUY) {
                            //result.add(new TransactionDTO("CRYPTO_BUY", -c.getMontant(), c.getDate()));
                            result.add(new ReleveCompteDTO("CRYPTO_BUY", -c.getMontant(), c.getDate(),null, null));
                        } else {
                            //result.add(new TransactionDTO("CRYPTO_SELL", +c.getMontant(), c.getDate()));
                            result.add(new ReleveCompteDTO("CRYPTO_SELL", +c.getMontant(), c.getDate(),null, null));
                        }
                    }
                }
            }
        }

        // ===== APPEL DU PDF =====
        RelevePdfUtil pdfUtil = new RelevePdfUtil();
        byte[] pdf = pdfUtil.generateRelevePdf(info,result);

        return pdf;
    }



    @Override
    public long getTotalAccounts() {
        return accountRepository.count(); // Méthode JPA pour compter les lignes
    }


    @Override
    public long getNumberOfAccountsByClientId(UUID clientId) {
        // On suppose que User a un champ UUID id
        return accountRepository.findByUserId(clientId).size();
    }
}
