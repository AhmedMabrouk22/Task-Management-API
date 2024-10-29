package org.example.taskmanagementapi.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
@Lazy
public class OtpService {

    @Value("${otp.key}")
    private String OTP_KEY;

    public String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public LocalDateTime generateExpirationTime() {
        return LocalDateTime.now().plusMinutes(30);
    }

    public String encrypt(String otp) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encryptedOTP = cipher.doFinal(otp.getBytes());
            return Base64.getEncoder().encodeToString(encryptedOTP);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String decrypt(String encryptedOTP) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decodedOTP = Base64.getDecoder().decode(encryptedOTP);
            byte[] decryptedOTP = cipher.doFinal(decodedOTP);
            return new String(decryptedOTP);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public SecretKey getKey() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(this.OTP_KEY.getBytes());
            return new SecretKeySpec(keyBytes, 0, 16, "AES");
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
