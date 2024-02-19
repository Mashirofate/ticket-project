package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.VenueActivieAddDto;
import com.tickets.dto.VenueActivieSearchDto;
import com.tickets.service.VenueActiviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "场馆活动接口")
@RestController
@RequestMapping("/va")
public class VenueActiviesController { //活动管理

    @Autowired
    private VenueActiviesService venueActiviesService;

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "创建一个活动")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody VenueActivieAddDto venueActivieAddDto) {
        venueActiviesService.save(venueActivieAddDto);
        return ResponseResult.SUCCESS();
    }

    /*@Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(value = "更新一个用户")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody Map map) {
        VenueActivieAddDto venueActivieAddDto =new VenueActivieAddDto();
        venueActivieAddDto.setAId((String) map.get("aId"));
        venueActivieAddDto.setAName((String) map.get("aName"));
        venueActivieAddDto.setAEnable(Integer.parseInt((map.get("aEnable").toString())));
        venueActivieAddDto.setANote((String) map.get("aNote"));
        venueActivieAddDto.setAEmployeenumber(Integer.parseInt((map.get("aEmployeenumber").toString())));
        venueActivieAddDto.setATicketnumber(Integer.parseInt((map.get("aTicketnumber").toString())));
        venueActivieAddDto.setAImage((String) map.get("aImage"));
        venueActiviesService.update(venueActivieAddDto);
        return ResponseResult.SUCCESS();
    }*/
    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "更新一个活动")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody VenueActivieAddDto venueActivieAddDto) {
        venueActiviesService.update(venueActivieAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "活动条件搜索")
    @GetMapping("/search")
    public ResponseResult search(VenueActivieSearchDto venueActivieSearchDto) {
        return ResponseResult.SUCCESS(venueActiviesService.getByKeys(venueActivieSearchDto));
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "启用的活动")
    @GetMapping("/open")
    public ResponseResult openActivies() {
        List<Map<String, Object>> list= venueActiviesService.getOpenActiviess();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activitiyAmount",venueActiviesService.activitiyAmount());
        map.put("venuesAmount",venueActiviesService.venuesAmount());
        map.put("deviceAmount",venueActiviesService.deviceAmount());
        map.put("ticketingAmount",venueActiviesService.ticketingAmount());
        map.put("list",list);
        return ResponseResult.SUCCESS(map);
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "删除活动", description  = "根据vaId删除")
    @DeleteMapping("/{aId}")
    public ResponseResult delById(@PathVariable String aId) {
        venueActiviesService.remove(aId);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "批量删除活动", description  = "根据vaId删除")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids) {
       /* 1、如果用“.”作为分隔的话,必须是如下写法,String.split("\\."),这样才能正确的分隔开,不能用String.split(".");
        2、如果用“|”作为分隔的话,必须是如下写法,String.split("\\|"),这样才能正确的分隔开,不能用String.split("|");*/
        String[] activies =ids.split(",");
        for (String activy : activies) {
            venueActiviesService.remove(activy);
        }
        return ResponseResult.SUCCESS();
    }



    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "更新活动的状态", description  = "")
    @PutMapping("/{vaId}/{vaEnable}")
    public ResponseResult updateEnable(@PathVariable String vaId, @PathVariable Character vaEnable) {
        venueActiviesService.updateEnable(vaId, vaEnable);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "根据id获取活动信息")
    @GetMapping("/{id}")
    public ResponseResult getByVaId(@PathVariable String id) {
        return ResponseResult.SUCCESS(venueActiviesService.getByVaId(id));
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "获取简单的数据列表", description  = "vmId, vmName")
    @GetMapping("/simple")
    public ResponseResult search() {
        return ResponseResult.SUCCESS(venueActiviesService.getSimpleList());
    }

}