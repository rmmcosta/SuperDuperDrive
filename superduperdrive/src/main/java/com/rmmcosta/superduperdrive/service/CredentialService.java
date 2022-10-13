package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.CredentialMapper;
import com.rmmcosta.superduperdrive.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

@Service
public class CredentialService {
    //Todo this key should be stored in a file for example not in the code itself
    private String key;
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;


    @PostConstruct
    public void initializeEncryptionService() {
        key = encryptionService.generateSymmetricKey();
    }

    public List<Credential> getCredentials() {
        return credentialMapper.getCredentials();
    }

    public Credential getCredentialById(Integer credentialId) {
        Credential tempCredential = credentialMapper.getCredentialById(credentialId);
        if (tempCredential != null)
            tempCredential.setPassword(encryptionService.decrypt(tempCredential.getPassword(), key));
        return tempCredential;
    }

    public int insertCredential(Credential credential) {
        credential.setPassword(encryptionService.encrypt(credential.getPassword(), key));
        System.out.println("Credential before insert: " + credential);
        return credentialMapper.insertCredential(credential);
    }

    public boolean deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public boolean updateCredential(Credential credential) {
        credential.setPassword(encryptionService.encrypt(credential.getPassword(), key));
        return credentialMapper.updateCredential(credential);
    }
}
