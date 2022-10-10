package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.UserMapper;
import com.rmmcosta.superduperdrive.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HashService hashService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String inputtedUsername = authentication.getName();
        String inputtedPassword = authentication.getCredentials().toString();
        User user = userMapper.getUser(inputtedUsername);
        //hashed saved password matches hashed inputted password?
        if (user != null) {
            String hashedInputtedPassword = hashService.getHashedValue(inputtedPassword, user.getSalt());
            if (hashedInputtedPassword.equals(user.getPassword())) {
                System.out.println("password match!");
                return new UsernamePasswordAuthenticationToken(inputtedUsername, inputtedPassword, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
