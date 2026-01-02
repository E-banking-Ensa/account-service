package com.ebanking.accountservice.repository;



import com.ebanking.accountservice.entity.MobileRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MobileRechargeRepository extends JpaRepository<MobileRecharge, Integer> {

    // Optionnel : méthode pour rechercher par compte et montant si besoin
    List<MobileRecharge> findByAccountIdAndMontant(int accountId, Double montant);

    // Optionnel : rechercher par numéro de téléphone
    List<MobileRecharge> findByPhoneNumber(String phoneNumber);
}
