package com.ebanking.accountservice.repository;

import com.ebanking.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    // Recherche tous les comptes d'un utilisateur par son ID
    List<Account> findByUserId(int userId);
}
