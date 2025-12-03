package com.mycompany.btap.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public final class PasswordUtil {
    private static final SecureRandom RNG = new SecureRandom();
    private static final int SALT_LEN = 16; // bytes
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private PasswordUtil() {}

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LEN];
        RNG.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(char[] password, String saltBase64) throws RuntimeException {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashed = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error hashing password", e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword(char[] password, String saltBase64, String expectedHash) {
        String hash = hashPassword(password, saltBase64);
        return hash.equals(expectedHash);
    }
}
