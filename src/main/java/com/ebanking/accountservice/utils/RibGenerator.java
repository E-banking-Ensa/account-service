package com.ebanking.accountservice.utils;

import java.util.Random;

public class RibGenerator {

    private static final String BANK_CODE = "230";   // code banque
    private static final String AGENCY_CODE = "780"; // code agence

    public static String generate() {
        Random random = new Random();
        String accountNumber = String.format("%012d", random.nextLong() & Long.MAX_VALUE);
        String key = String.format("%02d", random.nextInt(97));
        return BANK_CODE + AGENCY_CODE + accountNumber + key;
    }
}
