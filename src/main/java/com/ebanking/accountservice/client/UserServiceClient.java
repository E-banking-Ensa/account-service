package com.ebanking.accountservice.client;

import com.ebanking.accountservice.client.dto.CreateUserRequest;
import com.ebanking.accountservice.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Client Feign pour communiquer avec le user-service centralisé.
 * Toutes les opérations liées aux utilisateurs passent par ce client.
 */
@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    /**
     * Récupère un utilisateur par son ID
     */
    @GetMapping("/{userId}")
    ResponseEntity<UserResponse> getUser(@PathVariable UUID userId);

    /**
     * Récupère un client par son ID
     */
    @GetMapping("/client/{clientId}")
    ResponseEntity<UserResponse> getClientById(@PathVariable UUID clientId);

    /**
     * Synchronise/Crée un utilisateur (endpoint interne)
     */
    @PostMapping("/internal/sync")
    ResponseEntity<UserResponse> syncUser(@RequestBody CreateUserRequest request);

    /**
     * Récupère tous les clients
     */
    @GetMapping("/allClients")
    List<UserResponse> getAllClients();

    /**
     * Vérifie si un utilisateur existe
     */
    @GetMapping("/internal/exists/{userId}")
    ResponseEntity<Boolean> userExists(@PathVariable UUID userId);

    /**
     * Récupère ou crée un utilisateur (pour la création de compte)
     */
    @PostMapping("/internal/get-or-create")
    ResponseEntity<UserResponse> getOrCreateUser(@RequestBody CreateUserRequest request);
}
