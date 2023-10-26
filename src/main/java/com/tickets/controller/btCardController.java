package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ClearCardAddDto;
import com.tickets.dto.ResponseResult;
import com.tickets.service.VenueActiviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "网络绑定身份证接口，不需要token验证")
@RestController
@RequestMapping("/bt")
public class btCardController {


    @Autowired
    private VenueActiviesService venueActiviesService;

    @Authentication(required = true)
    @Operation(summary = "清楚卡条件搜索", description  = "")
    @GetMapping("/search")
    public ResponseResult search() {
        return ResponseResult.SUCCESS(venueActiviesService.selectByNames());
    }


    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary = "创建清楚卡", description  = "")
    @PostMapping("/add")
    public ResponseResult addDevice(@RequestBody ClearCardAddDto clearCardAddDto) {

        return ResponseResult.SUCCESS();
    }



    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary = "更新清楚卡")
    @PostMapping("/update")
    public ResponseResult updatDevice(@RequestBody ClearCardAddDto clearCardAddDto) {

        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary = "删除清楚卡")
    @DeleteMapping("/{fid}")
    public ResponseResult delById(@PathVariable String fid) {

        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary = "批量删除清楚卡")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids) {

        return ResponseResult.SUCCESS();
    }



}
