package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.EntranceManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "入口接口")
@RestController
@RequestMapping("/em")
public class EntranceManagementController { // 入口管理

    @Autowired
    private EntranceManagementService entranceManagementService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "创建入口", notes = "")
    @PostMapping("/add")
    public ResponseResult addVenue(@RequestBody EntranceManagementAddDto entranceManagementAddDto) {
        entranceManagementService.save(entranceManagementAddDto);
        return ResponseResult.SUCCESS();
    }

/*    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新入口", notes = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody Map map) {
        EntranceManagementAddDto entranceManagementAddDto = new EntranceManagementAddDto();
        entranceManagementAddDto.setEmId((String) map.get("emId"));
        entranceManagementAddDto.setEmName((String) map.get("emName"));
        entranceManagementAddDto.setEmEnable(Integer.parseInt((map.get("emEnable").toString())));
        entranceManagementAddDto.setEmNote((String) map.get("emNote"));
        entranceManagementAddDto.setEmVmId((map.get("emVmId").toString()));
        entranceManagementService.update(entranceManagementAddDto);
        return ResponseResult.SUCCESS();
    }*/

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新入口", notes = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody EntranceManagementAddDto entranceManagementAddDto) {
        entranceManagementService.update(entranceManagementAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "用户条件搜索")
    @GetMapping("/search")
    public ResponseResult search(EntranceManagementSearchDto entranceManagementSearchDto) {
        return ResponseResult.SUCCESS(entranceManagementService.getByKeys(entranceManagementSearchDto));
    }
    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新场馆状态信息", notes = "0不启用，1启用")
    @PutMapping("/{emId}/{enable}")
    public ResponseResult updateEnable(@PathVariable String emId, @PathVariable Character enable) {
        entranceManagementService.updateEnable(emId, enable);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "删除场馆")
    @DeleteMapping("/{id}")
    public ResponseResult delById(@PathVariable String id){
        entranceManagementService.delById(id);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除场馆")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            entranceManagementService.delById( activies[i]);
        }
        return ResponseResult.SUCCESS();
    }

}
