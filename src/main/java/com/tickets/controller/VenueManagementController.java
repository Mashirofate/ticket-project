package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.VenueManagementAddDto;
import com.tickets.dto.VenueMgDto;
import com.tickets.service.VenueManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "场馆管理")
@RestController
@RequestMapping("/vm")
public class VenueManagementController { //场馆管理

    @Autowired
    private VenueManagementService venueManagementService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "创建场馆", description  = "")
    @PostMapping("/add")
    public ResponseResult addVenue(@RequestBody VenueManagementAddDto venueManagementAddDto) {
        venueManagementService.save(venueManagementAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "更新场馆", description  = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody VenueManagementAddDto venueManagementAddDto) {
        venueManagementService.updates(venueManagementAddDto);
        return ResponseResult.SUCCESS();
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "场馆条件搜索", description  = "")
    @GetMapping("/search")
    public ResponseResult search(VenueMgDto venueMgDto) {
        return ResponseResult.SUCCESS(venueManagementService.getByKeys(venueMgDto));
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "获取简单的数据列表", description  = "vmId, vmName")
    @GetMapping("/simple")
    public ResponseResult search() {
        return ResponseResult.SUCCESS(venueManagementService.getSimpleList());
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "更新场馆状态信息", description  = "0不启用，1启用")
    @PutMapping("/{vmId}/{enable}")
    public ResponseResult updateEnable(@PathVariable String vmId, @PathVariable Character enable) {
        venueManagementService.update(vmId, enable);
        return ResponseResult.SUCCESS();
    }
    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "删除场馆")
    @DeleteMapping("/{id}")
    public ResponseResult delById(@PathVariable String id){
        venueManagementService.delById(id);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary  = "批量删除场馆")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            venueManagementService.delById(activies[i]);
        }
        return ResponseResult.SUCCESS();
    }


}
