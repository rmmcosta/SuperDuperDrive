package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.CredentialMapper;
import com.rmmcosta.superduperdrive.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private static final String KEY = "ECOiSO2e4tal5e6tale3coIso";
    @Autowired
    private CredentialMapper credentialMapper;
    @Autowired
    private EncryptionService encryptionService;

    public List<Credential> getCredentials() {
        return credentialMapper.getCredentials();
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public int insertCredential(Credential credential) {
        credential.setPassword(encryptionService.encrypt(credential.getPassword(), KEY));
        return credentialMapper.insertCredential(credential);
    }

    public boolean deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public boolean updateCredential(Credential credential) {
        return credentialMapper.updateCredential(credential);
    }
}
