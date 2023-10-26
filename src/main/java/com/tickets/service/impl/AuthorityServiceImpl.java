package com.tickets.service.impl;

import com.tickets.entity.Authority;
import com.tickets.mapper.AuthorityMapper;
import com.tickets.service.AuthorityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;



@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Resource
    private AuthorityMapper authorityMapper;

    @Override
    public boolean save(Authority authority) {
        return authorityMapper.insert(authority) == 1;
    }
}
