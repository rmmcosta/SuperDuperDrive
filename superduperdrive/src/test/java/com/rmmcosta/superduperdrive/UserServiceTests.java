package com.rmmcosta.superduperdrive;

import com.rmmcosta.superduperdrive.model.User;
import com.rmmcosta.superduperdrive.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Test
    public void insertUser() {
        User user = new User();
        user.setUsername("rmmcosta");
        user.setPassword("12345");
        user.setLName("Costa");
        user.setFName("Ricardo");
        assertNull(userService.getUser(user.getUsername()));
        userService.createUser(user);
        assertNotNull(userService.getUser(user.getUsername()));
    }
}
