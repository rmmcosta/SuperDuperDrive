package com.rmmcosta.superduperdrive.model;

import lombok.Data;

@Data
public class CredentialForm {
    private Integer credentialId;
    private String url;
    private String username;
    private String password;
}
