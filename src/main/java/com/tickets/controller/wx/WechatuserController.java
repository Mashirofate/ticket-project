package com.tickets.controller.wx;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.WeCharUserinfoAddDto;
import com.tickets.service.WeCharUserinfoService;
import com.tickets.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "微信用户权限接口")
@RestController
@RequestMapping("/wechat/user")
public class WechatuserController {

    @Autowired
    private WeCharUserinfoService weCharUserinfoService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Authentication(required = false)
    @PostMapping("/login")
    public ResponseResult login(@RequestBody WeCharUserinfoAddDto weCharUserinfoAddDto) {
        String wId = weCharUserinfoService.login(weCharUserinfoAddDto);
        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtUtil.geneJsonWebToken(wId));
        System.out.printf("ceshi");

        return ResponseResult.SUCCESS(map);

    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo() {
        String id = (String) httpServletRequest.getAttribute("id");
        Map<String, Object> byId = weCharUserinfoService.getById(id);
        return ResponseResult.SUCCESS(byId);

    }


}
