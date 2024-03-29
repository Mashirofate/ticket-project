package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.entity.Authority;
import com.tickets.service.AuthorityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户权限接口")
@RestController
@RequestMapping("/author")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "创建一个权限")
    @PostMapping("/")
    public ResponseResult add(@RequestBody Authority authority) {
        authorityService.save(authority);
        return new ResponseResult();
    }

  
}
