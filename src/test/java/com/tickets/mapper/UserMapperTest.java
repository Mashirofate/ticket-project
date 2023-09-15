package com.tickets.mapper;

import com.tickets.dto.UserSeachDto;
import com.tickets.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    void insert() {
        User user = new User();
        user.setUId("123");
        user.setUUser("刘佳伟");
        user.setUPassword("123456");
        user.setUStartusing(1);
//        user.setUAuthorityId("1");

        System.out.println(userMapper.insert(user));
    }
    @Test
    void testDelete() {
        System.out.println(userMapper.delete("124"));
    }

    @Test
    void selectByKeys() {
        UserSeachDto userSeachDto = new UserSeachDto();
//        userSeachDto.setUUser("刘");
        userSeachDto.setUStartusing(0);
        System.out.println(userMapper.selectByKeys(userSeachDto));
    }

    @Test
    void selectCountByKeys() {
        UserSeachDto userSeachDto = new UserSeachDto();
        System.out.println(userMapper.selectCountByKeys(userSeachDto));
    }
}