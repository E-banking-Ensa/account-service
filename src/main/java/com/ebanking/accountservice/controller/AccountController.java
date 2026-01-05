package com.ebanking.accountservice.controller;

import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.request.BalanceUpdateRequest;
import com.ebanking.accountservice.dto.response.AccountBalanceResponse;
import com.ebanking.accountservice.dto.response.AccountDTO;
import com.ebanking.accountservice.dto.response.AccountUpdateStatusResponse;
import com.ebanking.accountservice.dto.response.TransactionDTO;
import com.ebanking.accountservice.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) {
        AccountDTO account = accountService.createAccount(request);
        logger.info("Account created successfully for userId={}", request.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Account created successfully");
        response.put("account", account);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable int accountId,
            @RequestBody AccountUpdateStatusRequest request) {
        AccountUpdateStatusResponse response = accountService.updateAccountStatus(accountId, request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Account status updated successfully");
        result.put("account", response);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable int accountId) {
        AccountBalanceResponse response = accountService.getAccountBalance(accountId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Account balance retrieved successfully");
        result.put("account", response);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "All accounts retrieved successfully");
        result.put("count", accounts.size());
        result.put("accounts", accounts);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/rib/{rib}")
    public ResponseEntity<AccountDTO> getAccountByRib(@PathVariable String rib) {
        AccountDTO account = accountService.getAccountByRib(rib);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getAccountsByClient(@PathVariable UUID clientId) {
        List<AccountDTO> accounts = accountService.getAccountsByClientId(clientId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Client accounts retrieved successfully");
        result.put("accounts", accounts);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable int accountId) {
        List<TransactionDTO> transactions = accountService.getTransactionsByAccountId(accountId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Transactions retrieved successfully");
        result.put("transactions", transactions);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accountId}/releve")
    public ResponseEntity<?> downloadReleve(@PathVariable int accountId) {
        byte[] pdf = accountService.getTransactiondtoByAccountId(accountId);

        if (pdf == null || pdf.length == 0) {
            throw new RuntimeException("No transactions found for account ID " + accountId);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=releve.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/nbrTotal")
    public ResponseEntity<?> getTotalAccounts() {
        long total = accountService.getTotalAccounts();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Total number of accounts retrieved successfully");
        result.put("totalAccounts", total);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{clientId}/nbrAccount")
    public ResponseEntity<?> getNumberOfAccountsByClient(@PathVariable UUID clientId) {
        long count = accountService.getNumberOfAccountsByClientId(clientId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Number of client accounts retrieved successfully");
        result.put("nbrAccount", count);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/balance/update")
    public ResponseEntity<?> updateBalance(@RequestBody BalanceUpdateRequest request) {
        accountService.updateBalance(request.getRib(), request.getAmount());
        
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Balance updated successfully");
        result.put("rib", request.getRib());
        result.put("amount", request.getAmount());
        
        return ResponseEntity.ok(result);
    }
}







