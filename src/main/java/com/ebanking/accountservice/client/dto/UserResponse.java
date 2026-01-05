package com.ebanking.accountservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse utilisateur venant du user-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private String phoneNumber;
    private String adresse;
    private String firstName;
    private String lastName;
    private String role;
    private String status;
    private String kycStatus;
    private LocalDateTime createdAt;
}
