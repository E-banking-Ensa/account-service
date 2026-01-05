package com.ebanking.accountservice.controller;

import com.ebanking.accountservice.client.UserServiceClient;
import com.ebanking.accountservice.client.dto.CreateUserRequest;
import com.ebanking.accountservice.client.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur utilisateur qui délègue les opérations au user-service centralisé.
 * Ce contrôleur agit comme un proxy pour les opérations utilisateur.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceClient userServiceClient;

    @PostMapping("/clients")
    public ResponseEntity<Map<String, Object>> createClient(@RequestBody CreateClientRequest request) {
        try {
            // Créer l'utilisateur via user-service centralisé
            CreateUserRequest userRequest = CreateUserRequest.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .phoneNumber(request.getPhoneNumber())
                    .adresse(request.getAddress())
                    .age(request.getAge())
                    .role("Client")
                    .build();

            ResponseEntity<UserResponse> response = userServiceClient.syncUser(userRequest);
            UserResponse createdUser = response.getBody();

            if (createdUser == null) {
                throw new RuntimeException("Failed to create user in user-service");
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "Client created successfully via user-service");
            result.put("userId", createdUser.getId());
            result.put("username", createdUser.getUsername());
            result.put("email", createdUser.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(result);

        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Client creation failed");
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/clients")
    public ResponseEntity<Map<String, Object>> getAllClients() {
        try {
            // Récupérer tous les clients via user-service centralisé
            List<UserResponse> clients = userServiceClient.getAllClients();

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Clients retrieved successfully from user-service");
            response.put("count", clients.size());
            response.put("clients", clients);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Failed to retrieve clients");
            error.put("message", e.getMessage());
            error.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

class CreateClientRequest {
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String identificationNumber;
    private int age;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
