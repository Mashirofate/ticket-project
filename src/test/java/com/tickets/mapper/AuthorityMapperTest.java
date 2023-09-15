package com.tickets.mapper;

import com.tickets.entity.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthorityMapperTest {

    @Resource
    private AuthorityMapper authorityMapper;

    @Test
    void insert() {
        Authority authority = new Authority();
//        authority.setAuthorityName("管理员");
//        authority.setAuthorityNote("主管权限");
//        authority.setAuthorityGrade("1");
        System.out.println(authorityMapper.insert(authority));
        System.out.println(authority);
    }
}