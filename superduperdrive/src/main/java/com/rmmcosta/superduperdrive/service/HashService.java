package com.rmmcosta.superduperdrive.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
@Slf4j
public class HashService {
    private static final Logger logger = LoggerFactory.getLogger(HashService.class);
    public String getHashedValue(String originalValue, String salt) {
        byte[] hashedValue = null;
        PBEKeySpec pbeKeySpec = new PBEKeySpec(originalValue.toCharArray(), salt.getBytes(), 5000, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hashedValue = factory.generateSecret(pbeKeySpec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }

        return Base64.getEncoder().encodeToString(hashedValue);
    }
}
