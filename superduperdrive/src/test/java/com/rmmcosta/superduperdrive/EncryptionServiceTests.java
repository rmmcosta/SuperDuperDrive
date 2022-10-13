package com.rmmcosta.superduperdrive;


import com.rmmcosta.superduperdrive.service.EncryptionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncryptionServiceTests {
    @Test
    public void encryptDecrypt() {
        EncryptionService encryptionService = new EncryptionService();
        String key = encryptionService.generateSymmetricKey();
        String encryptedPassword = encryptionService.encrypt("12345678", key);
        String decryptedPassword = encryptionService.decrypt(encryptedPassword, key);
        assertEquals("12345678", decryptedPassword);
    }
}
