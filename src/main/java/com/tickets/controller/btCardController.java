package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.ClearCardService;
import com.tickets.service.VenueActiviesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "网络绑定身份证接口，不需要token验证")
@RestController
@RequestMapping("/bt")
public class btCardController {


    @Autowired
    private VenueActiviesService venueActiviesService;

    @Authentication(required = true)
    @ApiOperation(value = "清楚卡条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search() {
        return ResponseResult.SUCCESS(venueActiviesService.selectByNames());
    }


    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "创建清楚卡", notes = "")
    @PostMapping("/add")
    public ResponseResult addDevice(@RequestBody ClearCardAddDto clearCardAddDto) {

        return ResponseResult.SUCCESS();
    }



    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "更新清楚卡")
    @PostMapping("/update")
    public ResponseResult updatDevice(@RequestBody ClearCardAddDto clearCardAddDto) {

        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "删除清楚卡")
    @DeleteMapping("/{fid}")
    public ResponseResult delById(@PathVariable String fid) {

        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除清楚卡")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids) {

        return ResponseResult.SUCCESS();
    }



}
