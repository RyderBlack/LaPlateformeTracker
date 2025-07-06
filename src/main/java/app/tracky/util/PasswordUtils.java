package app.tracky.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {
    private static final int SALT_LENGTH = 16; // 128 bits
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256; // 256 bits
    
    /**
     * Generate a random salt
     * @return Base64 encoded salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * Hash a password with a salt using SHA-256
     * @param password The password to hash
     * @param salt The salt to use for hashing
     * @return The hashed password as a Base64 string
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify if a password matches a hashed password
     * @param password The password to verify
     * @param salt The salt used for the original hash
     * @param hashedPassword The hashed password to compare against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        if (password == null || salt == null || hashedPassword == null) {
            return false;
        }
        String newHash = hashPassword(password, salt);
        // Use constant-time comparison to prevent timing attacks
        return MessageDigest.isEqual(newHash.getBytes(), hashedPassword.getBytes());
    }
}
