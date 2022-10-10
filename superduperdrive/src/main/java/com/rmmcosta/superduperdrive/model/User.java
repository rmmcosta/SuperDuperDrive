package com.rmmcosta.superduperdrive.model;

import lombok.Data;

@Data
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String salt;
    private String fName;
    private String lName;
}
