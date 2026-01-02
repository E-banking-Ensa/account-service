package com.ebanking.accountservice.utils;

import java.util.HashMap;
import java.util.Map;

// Utility class for creating standardized error responses

public class ErrorResponseUtil {


    // Empêche l'instanciation
    private ErrorResponseUtil() {}


    public static Map<String, Object> createErrorResponse(String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return errorResponse;
    }
}
