package com.ebanking.accountservice.controller;

import com.ebanking.accountservice.dto.request.AccountCreateRequest;
import com.ebanking.accountservice.dto.request.AccountUpdateStatusRequest;
import com.ebanking.accountservice.dto.response.AccountBalanceResponse;
import com.ebanking.accountservice.dto.response.AccountDTO;
import com.ebanking.accountservice.dto.response.AccountUpdateStatusResponse;
import com.ebanking.accountservice.dto.response.TransactionDTO;
import com.ebanking.accountservice.service.AccountService;
import com.ebanking.accountservice.utils.ErrorResponseUtil;
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

    @PostMapping("/createAccount")
    public ResponseEntity<?> createAccount(@RequestBody AccountCreateRequest request) {

        try {
            AccountDTO account = accountService.createAccount(request);

            logger.info("Account created successfully for userId={}", request.getUserId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Account created successfully");
            response.put("account", account);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {

            logger.error("Error creating account for userId={}: {}", request.getUserId(), e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseUtil.createErrorResponse("Account creation failed", e.getMessage()));

        } catch (Exception e) {

            logger.error("Unexpected error while creating account", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse("Internal server error", "Unexpected error occurred"));
        }
    }



    @PutMapping("/{accountId}/status")
    public ResponseEntity<?> updateAccountStatus(
            @PathVariable int accountId,
            @RequestBody AccountUpdateStatusRequest request) {
        try {
            AccountUpdateStatusResponse response = accountService.updateAccountStatus(accountId, request);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Account status updated successfully");
            result.put("account", response);

            return ResponseEntity.ok(result);


        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponseUtil.createErrorResponse("Account update failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse("Unexpected error", e.getMessage()));
        }
    }



    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getAccountBalance(@PathVariable int accountId) {
        try {
            AccountBalanceResponse response =
                    accountService.getAccountBalance(accountId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Account balance retrieved successfully");
            result.put("account", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to retrieve balance",
                            e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }



    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getAccountsByClient(@PathVariable UUID clientId) {
        try {
            List<AccountDTO> accounts =
                    accountService.getAccountsByClientId(clientId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Client accounts retrieved successfully");
            result.put("accounts", accounts);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to retrieve accounts",
                            e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }



    // transactions d'un compte
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable int accountId) {
        try {
            // Récupère la liste des transactions
            List<TransactionDTO> transactions = accountService.getTransactionsByAccountId(accountId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Transactions retrieved successfully");
            result.put("transactions", transactions);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to retrieve transactions",
                            e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }



    @GetMapping("/{accountId}/releve")
    public ResponseEntity<?> downloadReleve(@PathVariable int accountId) {
        try {
            byte[] pdf = accountService.generateReleveByAccountId(accountId);

            // Vérifie si le PDF a bien été généré
            if (pdf == null || pdf.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponseUtil.createErrorResponse(
                                "Releve not found",
                                "No transactions found for account ID " + accountId
                        ));
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=releve.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to generate releve",
                            e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }



    @GetMapping("/nbrTotal")
    public ResponseEntity<?> getTotalAccounts() {
        try {
            long total = accountService.getTotalAccounts();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Total number of accounts retrieved successfully");
            result.put("totalAccounts", total);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to retrieve total accounts",
                            e.getMessage()
                    ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }



    @GetMapping("/client/{clientId}/nbrAccount")
    public ResponseEntity<?> getNumberOfAccountsByClient(@PathVariable UUID clientId) {
        try {
            long count = accountService.getNumberOfAccountsByClientId(clientId);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Number of client accounts retrieved successfully");
            result.put("nbrAccount", count);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Failed to retrieve client accounts",
                            e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponseUtil.createErrorResponse(
                            "Unexpected error",
                            e.getMessage()
                    ));
        }
    }

}







