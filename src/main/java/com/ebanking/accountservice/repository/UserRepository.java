package com.ebanking.accountservice.repository;

import com.ebanking.accountservice.entity.Client;
import com.ebanking.accountservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Client, Integer> {
}
