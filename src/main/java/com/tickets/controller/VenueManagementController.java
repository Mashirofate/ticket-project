package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.VenueManagementAddDto;
import com.tickets.dto.VenueMgDto;
import com.tickets.service.VenueManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "场馆管理")
@RestController
@RequestMapping("/vm")
public class VenueManagementController { //场馆管理

    @Autowired
    private VenueManagementService venueManagementService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "创建场馆", notes = "")
    @PostMapping("/add")
    public ResponseResult addVenue(@RequestBody VenueManagementAddDto venueManagementAddDto) {
        venueManagementService.save(venueManagementAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新场馆", notes = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody VenueManagementAddDto venueManagementAddDto) {
        venueManagementService.updates(venueManagementAddDto);
        return ResponseResult.SUCCESS();
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "场馆条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(VenueMgDto venueMgDto) {
        return ResponseResult.SUCCESS(venueManagementService.getByKeys(venueMgDto));
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "获取简单的数据列表", notes = "vmId, vmName")
    @GetMapping("/simple")
    public ResponseResult search() {
        return ResponseResult.SUCCESS(venueManagementService.getSimpleList());
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新场馆状态信息", notes = "0不启用，1启用")
    @PutMapping("/{vmId}/{enable}")
    public ResponseResult updateEnable(@PathVariable String vmId, @PathVariable Character enable) {
        venueManagementService.update(vmId, enable);
        return ResponseResult.SUCCESS();
    }
    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "删除场馆")
    @DeleteMapping("/{id}")
    public ResponseResult delById(@PathVariable String id){
        venueManagementService.delById(id);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除场馆")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            venueManagementService.delById(activies[i]);
        }
        return ResponseResult.SUCCESS();
    }


}
