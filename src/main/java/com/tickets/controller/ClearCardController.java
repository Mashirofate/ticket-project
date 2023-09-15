package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.ClearCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "清楚卡接口")
@RestController
@RequestMapping("/cc")
public class ClearCardController {

    @Autowired
    private ClearCardService clearCardService;

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "清楚卡条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(ClearCardSearchDto clearCardSearchDto) throws IOException {

        Page page = clearCardService.getByKeys(clearCardSearchDto);
        if (page != null & page.getRecords()!= null) {
            List list = page.getRecords();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                // boolean status = InetAddress.getByName((String) map.get("dIp")).isReachable(100);     // 当返回值是true时，说明host是可用的，false则不可。
                // if (status){

                list.set(i, map);
            }
            page.setRecords(list);

        }

        return ResponseResult.SUCCESS(page);
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "创建清楚卡", notes = "")
    @PostMapping("/add")
    public ResponseResult addDevice(@RequestBody ClearCardAddDto clearCardAddDto) {
        clearCardService.save(clearCardAddDto);
        return ResponseResult.SUCCESS();
    }



    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "更新清楚卡")
    @PostMapping("/update")
    public ResponseResult updatDevice(@RequestBody ClearCardAddDto clearCardAddDto) {
        clearCardService.update(clearCardAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "删除清楚卡")
    @DeleteMapping("/{fid}")
    public ResponseResult delById(@PathVariable String fid) {
        clearCardService.delById(fid);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除清楚卡")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids) {
        String[] activies = ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            clearCardService.delById(activies[i]);
        }
        return ResponseResult.SUCCESS();
    }



}
