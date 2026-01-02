package com.ebanking.accountservice.repository;

import com.ebanking.accountservice.entity.Retrait;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetraitRepository extends JpaRepository<Retrait, Integer> {}