package com.rmmcosta.superduperdrive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {
    private Logger logger = LoggerFactory.getLogger(EncryptionService.class);
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";

    public String encrypt(String value, String key) {
        byte[] cipherText = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getOriginalSecretKey(key), new IvParameterSpec(new byte[16]));
            cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage());
        }
        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String value, String key) {
        byte[] plainText = null;
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getOriginalSecretKey(key), new IvParameterSpec(new byte[16]));
            plainText = cipher.doFinal(Base64.getDecoder().decode(value));
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage());
        }

        return new String(plainText);

    }

    private SecretKey getOriginalSecretKey(String encodedKey) {
        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // rebuild key using SecretKeySpec
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, KEY_ALGORITHM);
    }

    public String generateSymmetricKey() {
        SecretKey secretKey;
        try {
            secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();
            // return base64 encoded version of the key
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        return "";
    }
}
