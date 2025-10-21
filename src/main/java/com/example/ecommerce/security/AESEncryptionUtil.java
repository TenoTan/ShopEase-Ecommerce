package com.example.ecommerce.security;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility class for AES encryption and decryption of sensitive data
 * Uses AES-GCM mode for authenticated encryption with associated data (AEAD)
 */
public class AESEncryptionUtil {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;

    private SecretKey secretKey;

    public AESEncryptionUtil() {
        try {
            // Generate a secure random key for AES encryption
            this.secretKey = generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to initialize AES encryption", e);
        }
    }

    /**
     * Generates a secure AES key
     */
    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_SIZE, SecureRandom.getInstanceStrong());
        return keyGenerator.generateKey();
    }

    /**
     * Sets a specific secret key (useful for testing or when using a stored key)
     */
    public void setSecretKey(byte[] key) {
        this.secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * Gets the current secret key as bytes
     */
    public byte[] getSecretKeyBytes() {
        return secretKey.getEncoded();
    }

    /**
     * Encrypts the given plaintext
     * @param plaintext The text to encrypt
     * @return Base64 encoded encrypted string
     */
    public String encrypt(String plaintext) {
        try {
            // Generate a random IV (Initialization Vector)
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            // Initialize cipher for encryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            // Encrypt the plaintext
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Combine IV and ciphertext
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + ciphertext.length);
            byteBuffer.put(iv);
            byteBuffer.put(ciphertext);
            byte[] cipherMessage = byteBuffer.array();

            // Encode as Base64 string
            return Base64.getEncoder().encodeToString(cipherMessage);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /**
     * Decrypts the given ciphertext
     * @param ciphertext Base64 encoded encrypted string
     * @return The decrypted plaintext
     */
    public String decrypt(String ciphertext) {
        try {
            // Decode the Base64 string
            byte[] cipherMessage = Base64.getDecoder().decode(ciphertext);

            // Extract IV and ciphertext
            ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            byte[] ciphertextBytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(ciphertextBytes);

            // Initialize cipher for decryption
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // Decrypt the ciphertext
            byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);
            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
