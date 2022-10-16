package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.Credential;
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
        String authUsername = "user1";
        int initCount = credentialService.getCredentials(authUsername).size();
        int insertedCredentialId = credentialService.insertCredential(credential, authUsername);
        assertEquals(initCount + 1, credentialService.getCredentials(authUsername).size());
        Credential returnedCredential = credentialService.getCredentialById(insertedCredentialId, authUsername);
        assertNotNull(returnedCredential);
        assertEquals("p@ssw0rd", returnedCredential.getPassword());
        Credential newSameUrlUsernameCredential = new Credential();
        newSameUrlUsernameCredential.setUrl("http://localhost");
        newSameUrlUsernameCredential.setUsername("aramos");
        assertThrowsExactly(RuntimeException.class, () -> {
            credentialService.insertCredential(newSameUrlUsernameCredential, authUsername);
        }, "Credential already exists with that Url and Username!");
        Credential insertedCredentialFromList = credentialService.getCredentials(authUsername).stream().filter(c -> c.getCredentialId() == insertedCredentialId).toList().get(0);
        assertEquals(insertedCredentialId, insertedCredentialFromList.getCredentialId());
        assertTrue(credentialService.deleteCredential(insertedCredentialId, authUsername));
        assertEquals(initCount, credentialService.getCredentials(authUsername).size());
        assertNull(credentialService.getCredentialById(insertedCredentialId, authUsername));
    }
}
