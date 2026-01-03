//package com.ebanking.accountservice.init;
//
//import com.ebanking.accountservice.entity.*;
//import com.ebanking.accountservice.repository.*;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Configuration
//@Profile("dev")   // S’exécute UNIQUEMENT en DEV
//public class DataInitializer {
//
//    @Bean
//    @Transactional
//    CommandLineRunner initData(
//            AccountRepository accountRepo,
//            VirementRepository virementRepo,
//            DepotRepository depotRepo,
//            RetraitRepository retraitRepo,
//            MobileRechargeRepository mobileRechargeRepo,
//            CryptoWalletRepository cryptoWalletRepo
//    ) {
//        return args -> {
//
//            // -------------------------------
//            // Charger les comptes existants
//            // -------------------------------
//            Account acc1 = accountRepo.findById(1).orElseThrow();
//            Account acc2 = accountRepo.findById(2).orElseThrow();
//            Account acc3 = accountRepo.findById(3).orElseThrow();
//            User client1 = acc1.getUser(); // client lié à acc1
//
//            // -------------------------------
//            // 1️⃣ Virement 1 → 2
//            // -------------------------------
//            Virement v1 = new Virement();
//            v1.setMontant(500.0);
//            v1.setDate(LocalDateTime.now());
//            v1.setType(TypeTransaction.VIREMENT);
//            v1.setSource(acc1);
//            v1.setDestination(acc2);
//            v1.setMotife("Paiement fournisseur");
//            v1.setType_virement(VirementType.CLASSIC);
//            v1.setAccounts(List.of(acc1, acc2));
//            virementRepo.save(v1);
//
//            // -------------------------------
//            // 2️⃣ Virement 3 → 1
//            // -------------------------------
//            Virement v2 = new Virement();
//            v2.setMontant(1000.0);
//            v2.setDate(LocalDateTime.now());
//            v2.setType(TypeTransaction.VIREMENT);
//            v2.setSource(acc3);
//            v2.setDestination(acc1);
//            v2.setMotife("Remboursement prêt");
//            v2.setType_virement(VirementType.CLASSIC);
//            v2.setAccounts(List.of(acc3, acc1));
//            virementRepo.save(v2);
//
//            // -------------------------------
//            // 3️⃣ Dépôt compte 1
//            // -------------------------------
//            Depot depot = new Depot();
//            depot.setMontant(1000.0);
//            depot.setDate(LocalDateTime.now());
//            depot.setType(TypeTransaction.DEPOSIT);
//            depot.setAccount(acc1);
//            depot.setAccounts(List.of(acc1));
//            depotRepo.save(depot);
//
//            // -------------------------------
//            // 4️⃣ Retrait compte 1
//            // -------------------------------
//            Retrait retrait = new Retrait();
//            retrait.setMontant(200.0);
//            retrait.setDate(LocalDateTime.now());
//            retrait.setType(TypeTransaction.WITHDRAW);
//            retrait.setAccount(acc1);
//            retrait.setAccounts(List.of(acc1));
//            retraitRepo.save(retrait);
//
//            // -------------------------------
//            // 5️⃣ MobileRecharge compte 1
//            // -------------------------------
//            MobileRecharge recharge = new MobileRecharge();
//            recharge.setMontant(50.0);
//            recharge.setDate(LocalDateTime.now());
//            recharge.setType(TypeTransaction.RECHARGE);
//            recharge.setAccount(acc1);
//            recharge.setPhoneNumber("0612345678");
//            recharge.setAccounts(List.of(acc1));
//            mobileRechargeRepo.save(recharge);
//
//            // -------------------------------
//            // 6️⃣ CryptoWallet principal pour acc1
//            // -------------------------------
//            CryptoWallet wallet = new CryptoWallet();
//            wallet.setWalletAddress("WALLET_MAIN_001");
//            wallet.setBalanceBTC(1.0);
//            wallet.setBalanceETH(2.0);
//            //wallet.setClient(client1);
//            wallet.setMontant(0.0);
//            wallet.setDate(LocalDateTime.now());
//            wallet.setType(TypeTransaction.CRYPTO_BUY);
//            wallet.setAccounts(List.of(acc1));
//            cryptoWalletRepo.save(wallet);
//
//            // -------------------------------
//            // 7️⃣ Crypto SELL pour acc1
//            // -------------------------------
//            CryptoWallet cryptoSell = new CryptoWallet();
//            cryptoSell.setWalletAddress("WALLET_SELL_001");
//            cryptoSell.setBalanceBTC(0.5);
//            cryptoSell.setBalanceETH(1.2);
//            //cryptoSell.setClient(client1);
//            cryptoSell.setMontant(300.0);
//            cryptoSell.setDate(LocalDateTime.now());
//            cryptoSell.setType(TypeTransaction.CRYPTO_SELL);
//            cryptoSell.setAccounts(List.of(acc1));
//            cryptoWalletRepo.save(cryptoSell);
//
//            // -------------------------------
//            // 8️⃣ Crypto BUY pour acc1
//            // -------------------------------
//            CryptoWallet cryptoBuy = new CryptoWallet();
//            cryptoBuy.setWalletAddress("WALLET_BUY_001");
//            cryptoBuy.setBalanceBTC(0.7);
//            cryptoBuy.setBalanceETH(2.0);
//           // cryptoBuy.setClient(client1);
//            cryptoBuy.setMontant(500.0);
//            cryptoBuy.setDate(LocalDateTime.now());
//            cryptoBuy.setType(TypeTransaction.CRYPTO_BUY);
//            cryptoBuy.setAccounts(List.of(acc1));
//            cryptoWalletRepo.save(cryptoBuy);
//
//            System.out.println("✅ Data initialization complete (virements, dépôt, retrait, recharge, crypto)");
//        };
//    }
//}
