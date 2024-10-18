package org.example.taskmanagementapi.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class RefreshTokenGenerator {
    private static final int TOKEN_LENGTH = 32;
    public static String generateRandomToken() {
        // SecureRandom for generating secure random numbers
        SecureRandom secureRandom = new SecureRandom();

        // Byte array to hold the random bytes
        byte[] tokenBytes = new byte[TOKEN_LENGTH];

        // Generate the random bytes
        secureRandom.nextBytes(tokenBytes);

        // Encode the bytes into a Base64 string (URL-safe)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
