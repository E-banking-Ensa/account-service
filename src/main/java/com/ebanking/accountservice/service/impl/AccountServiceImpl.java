package com.ebanking.accountservice.service.impl;

import com.ebanking.accountservice.client.UserServiceClient;
import com.ebanking.accountservice.client.dto.CreateUserRequest;
import com.ebanking.accountservice.client.dto.UserResponse;
import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.response.*;
import com.ebanking.accountservice.entity.*;
import com.ebanking.accountservice.mapper.AccountMapper;
import com.ebanking.accountservice.repository.*;
import com.ebanking.accountservice.service.AccountService;
import com.ebanking.accountservice.utils.RelevePdfUtil;
import org.springframework.http.ResponseEntity;
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
    private final UserServiceClient userServiceClient; // Client Feign vers user-service centralisé
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
            UserServiceClient userServiceClient,
            AccountMapper accountMapper,

            VirementRepository virementRepository,
            DepotRepository depotRepository,
            RetraitRepository retraitRepository,
            MobileRechargeRepository mobileRechargeRepository,
            CryptoWalletRepository cryptoWalletRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userServiceClient = userServiceClient;
        this.accountMapper = accountMapper;

        // transaction
        this.transactionRepository = transactionRepository;
        this.virementRepository = virementRepository;
        this.depotRepository = depotRepository;
        this.retraitRepository = retraitRepository;
        this.mobileRechargeRepository = mobileRechargeRepository;
        this.cryptoWalletRepository = cryptoWalletRepository;

    }

    @Override
    public AccountDTO createAccount(AccountCreateRequest request) {

        // 1️⃣ Vérifier/créer l'utilisateur via user-service centralisé
        UUID userId = request.getUserId();
        UserResponse userResponse = getOrCreateUserFromUserService(userId);

        // Mapping DTO to Entity
        Account account = accountMapper.toEntity(request);
        account.setUserId(userResponse.getId());

        // Définir le solde initial si fourni, sinon 0.0
        if (request.getInitialBalance() != null && request.getInitialBalance() >= 0) {
            account.setBalance(request.getInitialBalance());
        }

        // Sauvegarde (RIB généré via @PrePersist)
        Account savedAccount = accountRepository.save(account);

        // Mapping Entity to DTO
        return accountMapper.toDTO(savedAccount);
    }

    /**
     * Récupère ou crée un utilisateur via le user-service centralisé
     */
    private UserResponse getOrCreateUserFromUserService(UUID userId) {
        try {
            // D'abord essayer de récupérer l'utilisateur existant
            ResponseEntity<UserResponse> response = userServiceClient.getUser(userId);
            if (response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            // L'utilisateur n'existe pas, on va le créer
            System.out.println("User not found in user-service, creating new user: " + userId);
        }

        // Créer un nouvel utilisateur via user-service
        CreateUserRequest createRequest = CreateUserRequest.builder()
                .userId(userId)
                .username("user_" + userId.toString().substring(0, 8))
                .email("user_" + userId.toString().substring(0, 8) + "@ebanking.com")
                .firstName("User")
                .lastName(userId.toString().substring(0, 8))
                .role("Client")
                .build();

        ResponseEntity<UserResponse> createResponse = userServiceClient.getOrCreateUser(createRequest);
        if (createResponse.getBody() == null) {
            throw new RuntimeException("Failed to create user in user-service");
        }
        return createResponse.getBody();
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
                account.getCurrency());
    }

    @Override
    public AccountDTO getAccountByRib(String rib) {
        Account account = accountRepository.findByRib(rib);
        if (account == null) {
            throw new RuntimeException("Account not found with RIB: " + rib);
        }
        return accountMapper.toDTO(account);
    }

    @Override
    public List<AccountDTO> getAccountsByClientId(UUID clientId) {
        // Vérifier que l'utilisateur existe via user-service centralisé
        try {
            ResponseEntity<UserResponse> response = userServiceClient.getUser(clientId);
            if (response.getBody() == null) {
                throw new RuntimeException("User not found in user-service: " + clientId);
            }
        } catch (Exception e) {
            throw new RuntimeException("User not found in user-service: " + clientId, e);
        }

        // Récupérer les comptes depuis le repository par userId
        List<Account> accounts = accountRepository.findByUserId(clientId);
        return accountMapper.toDTOList(accounts);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
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
                            VirementDTO dto = new VirementDTO("VIREMENT", -v.getMontant(), v.getMotife(), v.getDate(),
                                    v.getDestination().getRib(),
                                    v.getType_virement().name());
                            // dto.setMotife(v.getMotife());
                            result.add(dto);
                        } else if (v.getDestination() != null && v.getDestination().getId() == accountId) {
                            VirementDTO dto = new VirementDTO("VIREMENT", +v.getMontant(), v.getMotife(), v.getDate(),
                                    v.getDestination().getRib(),
                                    v.getType_virement().name());
                            // dto.setMotife(v.getMotife());
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
                    result.add(new TransactionDTO("WITHDRAW", -r.getMontant(), r.getDate()));
                }

                case RECHARGE -> {
                    MobileRecharge m = mobileRechargeRepository.findById(t.getId()).orElse(null);
                    if (m != null) {
                        TransactionDTO dto = new MobileRechargeDTO("RECHARGE", -m.getMontant(), m.getPhoneNumber(),
                                m.getDate());
                        // dto.setPhoneNumber(m.getPhoneNumber());
                        result.add(dto);
                    }
                }

                case CRYPTO_BUY -> {
                    result.add(new TransactionDTO("CRYPTO_BUY", -t.getMontant(), t.getDate()));
                }
                case CRYPTO_SELL -> {
                    result.add(new TransactionDTO("CRYPTO_SELL", t.getMontant(), t.getDate()));
                }
            }
        }

        return result;
    }

    // gneration du releve compte
    public byte[] getTransactiondtoByAccountId(int accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Récupérer les infos utilisateur via le service centralisé
        UserResponse user = userServiceClient.getUser(account.getUserId()).getBody();
        String firstName = user != null ? user.getFirstName() : "N/A";
        String lastName = user != null ? user.getLastName() : "N/A";
        String adresse = user != null ? user.getAdresse() : "N/A";

        infoCompteDTO info = new infoCompteDTO(firstName, lastName, account.getRib(), account.getBalance(), adresse);

        List<ReleveCompteDTO> result = new ArrayList<>();

        // ===== Dates "manuelles" pour le test =====
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 31);

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
                            // result.add(new TransactionDTO("VIREMENT", -v.getMontant(), v.getDate()));
                            // VirementDTO dto = new VirementDTO ("VIREMENT", -v.getMontant(),
                            // v.getMotife(), v.getDate(), v.getDestination().getRib(),
                            // v.getType_virement().name());
                            // dto.setMotife(v.getMotife());
                            result.add(new ReleveCompteDTO("VIREMENT", -v.getMontant(), v.getDate(), v.getMotife(),
                                    v.getType_virement().name()));
                            // result.add(dto);
                        } else if (v.getDestination() != null && v.getDestination().getId() == accountId) {
                            // result.add(new TransactionDTO("VIREMENT", +v.getMontant(), v.getDate()));
                            // VirementDTO dto = new VirementDTO ("VIREMENT", +v.getMontant(),
                            // v.getMotife(), v.getDate(), v.getDestination().getRib(),
                            // v.getType_virement().name());
                            result.add(new ReleveCompteDTO("VIREMENT", +v.getMontant(), v.getDate(), v.getMotife(),
                                    v.getType_virement().name()));
                            // dto.setMotife(v.getMotife());
                            // result.add(dto);
                        }
                    }
                }

                case DEPOSIT -> {
                    Depot d = (Depot) t;
                    // result.add(new TransactionDTO("DEPOSIT", +d.getMontant(), d.getDate()));
                    result.add(new ReleveCompteDTO("DEPOSIT", +d.getMontant(), d.getDate(), null, null));
                }

                case WITHDRAW -> {
                    Retrait r = (Retrait) t;
                    // result.add(new TransactionDTO("WITHDRAW", -r.getMontant(), r.getDate()));
                    result.add(new ReleveCompteDTO("WITHDRAW", -r.getMontant(), r.getDate(), null, null));
                }

                case RECHARGE -> {
                    MobileRecharge m = mobileRechargeRepository.findById(t.getId()).orElse(null);
                    if (m != null) {
                        // result.add(new TransactionDTO("RECHARGE", -m.getMontant(), m.getDate()));
                        result.add(new ReleveCompteDTO("RECHARGE", -m.getMontant(), m.getDate(), null, null));

                    }
                }
            }
        }

        // ===== APPEL DU PDF =====
        RelevePdfUtil pdfUtil = new RelevePdfUtil();
        byte[] pdf = pdfUtil.generateRelevePdf(info, result);

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

    @Override
    public void updateBalance(String rib, Double amount) {
        Account account = accountRepository.findByRib(rib);
        if (account == null) {
            throw new RuntimeException("Account not found with RIB: " + rib);
        }

        double newBalance = account.getBalance() + amount;
        if (newBalance < 0) {
            throw new RuntimeException("Insufficient balance for account: " + rib);
        }

        account.setBalance(newBalance);
        account.setUpdatedDate(LocalDateTime.now());
        accountRepository.save(account);

        // Enregistrer la transaction
        if (amount < 0) {
            // Débit -> Retrait (CRYPTO_BUY si via updateBalance public, ou juste WITHDRAW ?)
            // On suppose ici que cet appel vient du service crypto pour l'instant, ou on met un type générique.
            // Le plan disait d'utiliser CRYPTO_BUY si < 0.
            Retrait retrait = new Retrait();
            retrait.setMontant(Math.abs(amount));
            retrait.setDate(LocalDateTime.now());
            retrait.setType(TypeTransaction.CRYPTO_BUY); // Ou WITHDRAW si on veut être générique
            retrait.setAccount(account);
            retrait.setAccounts(List.of(account));
            retraitRepository.save(retrait);
        } else if (amount > 0) {
            // Crédit -> Dépôt
            Depot depot = new Depot();
            depot.setMontant(amount);
            depot.setDate(LocalDateTime.now());
            depot.setType(TypeTransaction.CRYPTO_SELL); // Ou DEPOSIT
            depot.setAccount(account);
            depot.setAccounts(List.of(account));
            depotRepository.save(depot);
        }
    }
}
