package com.ebanking.accountservice.repository;

import com.ebanking.accountservice.entity.Virement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirementRepository extends JpaRepository<Virement, Integer> {}