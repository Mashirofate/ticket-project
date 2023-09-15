package com.tickets.service.impl;

import com.tickets.dto.UserSeachDto;
import com.tickets.entity.User;
import com.tickets.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void save() {
        User user = new User();
        user.setUId("124");
        user.setUUser("刘佳伟");
        user.setUPassword("123456");
        user.setUStartusing(1);
//        user.setUAuthorityId("1");

    }

    @Test
    void getByKeys() {
        System.out.println(userService.getByKeys(new UserSeachDto()));
    }
}