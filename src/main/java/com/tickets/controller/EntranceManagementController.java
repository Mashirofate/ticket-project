package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.EntranceManagementSearchDto;
import com.tickets.dto.ResponseResult;
import com.tickets.service.EntranceManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "入口接口")
@RestController
@RequestMapping("/em")
public class EntranceManagementController { // 入口管理

    @Autowired
    private EntranceManagementService entranceManagementService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "创建入口", description  = "")
    @PostMapping("/add")
    public ResponseResult addVenue(@RequestBody EntranceManagementAddDto entranceManagementAddDto) {
        entranceManagementService.save(entranceManagementAddDto);
        return ResponseResult.SUCCESS();
    }

/*    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(value = "更新入口", description  = "")
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
    @Operation(summary = "更新入口", description  = "")
    @PostMapping("/update")
    public ResponseResult updateVenue(@RequestBody EntranceManagementAddDto entranceManagementAddDto) {
        entranceManagementService.update(entranceManagementAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "用户条件搜索")
    @GetMapping("/search")
    public ResponseResult search(EntranceManagementSearchDto entranceManagementSearchDto) {
        return ResponseResult.SUCCESS(entranceManagementService.getByKeys(entranceManagementSearchDto));
    }
    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "更新场馆状态信息", description  = "0不启用，1启用")
    @PutMapping("/{emId}/{enable}")
    public ResponseResult updateEnable(@PathVariable String emId, @PathVariable Character enable) {
        entranceManagementService.updateEnable(emId, enable);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "删除场馆")
    @DeleteMapping("/{id}")
    public ResponseResult delById(@PathVariable String id){
        entranceManagementService.delById(id);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "批量删除场馆")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (String activy : activies) {
            entranceManagementService.delById(activy);
        }
        return ResponseResult.SUCCESS();
    }

}
