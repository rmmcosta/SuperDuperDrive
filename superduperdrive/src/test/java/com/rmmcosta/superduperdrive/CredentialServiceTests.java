package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.Credential;
import com.rmmcosta.superduperdrive.model.Note;
import com.rmmcosta.superduperdrive.service.CredentialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CredentialServiceTests {
    @Autowired
    private CredentialService credentialService;

    @Test
    public void CredentialLifeCycle() {
        Credential credential = new Credential();
        credential.setUrl("http://localhost");
        credential.setUsername("aramos");
        credential.setPassword("p@ssw0rd");
        int initCount = credentialService.getCredentials().size();
        int insertedCredentialId = credentialService.insertCredential(credential);
        assertEquals(initCount + 1, credentialService.getCredentials().size());
        Credential returnedCredential = credentialService.getCredentialById(insertedCredentialId);
        assertNotNull(returnedCredential);
        assertEquals("p@ssw0rd", returnedCredential.getPassword());
        Credential insertedCredentialFromList = credentialService.getCredentials().stream().filter(c -> c.getCredentialId() == insertedCredentialId).toList().get(0);
        assertEquals(insertedCredentialId, insertedCredentialFromList.getCredentialId());
        assertTrue(credentialService.deleteCredential(insertedCredentialId));
        assertEquals(initCount, credentialService.getCredentials().size());
        assertNull(credentialService.getCredentialById(insertedCredentialId));
    }
}
