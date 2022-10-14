package com.rmmcosta.superduperdrive.service;

import com.rmmcosta.superduperdrive.mapper.UserMapper;
import com.rmmcosta.superduperdrive.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HashService hashService;

    public int createUser(User user) throws RuntimeException {
        User existingUser = getUser(user.getUsername());
        if(existingUser!=null)
            throw new RuntimeException("User already exists with that username!");
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        user.setSalt(Base64.getEncoder().encodeToString(salt));
        user.setPassword(hashService.getHashedValue(user.getPassword(), user.getSalt()));
        System.out.println("All prepared to insert user");
        System.out.println(user);
        return userMapper.insertUser(user);
    }

    public User getUser(String username) {
        return userMapper.getUser(username);
    }
}
