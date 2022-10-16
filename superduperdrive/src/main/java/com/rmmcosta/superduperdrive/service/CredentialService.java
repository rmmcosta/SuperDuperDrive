package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.CredentialMapper;
import com.rmmcosta.superduperdrive.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    public List<Credential> getCredentials(String ownerUsername) {
        return credentialMapper.getCredentials(ownerUsername);
    }

    public Credential getCredentialById(Integer credentialId, String ownerUsername) {
        Credential tempCredential = credentialMapper.getCredentialById(credentialId, ownerUsername);
        if (tempCredential != null)
            tempCredential.setPassword(encryptionService.decrypt(tempCredential.getPassword(), key));
        return tempCredential;
    }

    public int insertCredential(Credential credential, String ownerUsername) {
        checkExistingCredential(credential, ownerUsername);
        credential.setPassword(encryptionService.encrypt(credential.getPassword(), key));
        System.out.println("Credential before insert: " + credential);
        return credentialMapper.insertCredential(credential, ownerUsername);
    }

    public boolean deleteCredential(Integer credentialId, String ownerUsername) {
        return credentialMapper.deleteCredential(credentialId, ownerUsername);
    }

    public boolean updateCredential(Credential credential, String ownerUsername) {
        checkExistingCredential(credential, ownerUsername);
        credential.setPassword(encryptionService.encrypt(credential.getPassword(), key));
        return credentialMapper.updateCredential(credential, ownerUsername);
    }

    private void checkExistingCredential(Credential credential, String ownerUsername) {
        Credential existingCredential = credentialMapper.getCredentialByUrlAndUsername(credential.getUrl(), credential.getUsername(), ownerUsername);
        if (existingCredential != null && (credential.getCredentialId() == null || existingCredential.getCredentialId() != credential.getCredentialId())) {
            throw new RuntimeException("Credential already exists with that Url and Username!");
        }
    }
}
