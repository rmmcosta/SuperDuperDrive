package com.rmmcosta.superduperdrive;


import org.junit.jupiter.api.Test;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Cipher3Tests {
    @Test
    public void encryptDecrypt() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // create new key
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        // get base64 encoded version of the key
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // decode the base64 encoded string
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        byte[] cipherText = null;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, originalKey,new IvParameterSpec(new byte[16]));
        cipherText = cipher.doFinal("12345678".getBytes(StandardCharsets.UTF_8));
        String cipherTextString = Base64.getEncoder().encodeToString(cipherText);

        cipher.init(Cipher.DECRYPT_MODE, originalKey, new IvParameterSpec(new byte[16]));
        String plainText = new String(cipher.doFinal(Base64.getDecoder().decode(cipherTextString)));
        assertEquals("12345678", plainText);
    }
}
