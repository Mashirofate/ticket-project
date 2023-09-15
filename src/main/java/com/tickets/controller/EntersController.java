package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;
import com.tickets.dto.ResponseResult;
import com.tickets.service.EntersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "入场记录接口")
@RestController
@RequestMapping("/es")
public class EntersController {
    @Autowired
    private EntersService entersService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "入场记录条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(EntersSearchDto entersSearchDto) throws ParseException {
        Page page= entersService.getByKeys(entersSearchDto);
        if (page != null & page.getRecords()!= null) {
            List list = page.getRecords();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String a1=null;
                if(map.containsKey("fImage")){
                    byte[]  by= (byte[])map.get("fImage");
                    String a = new String((byte[]) map.get("fImage"));
                    a1 = "data:image/png;base64," + a;
                }
                map.put("fImage", a1);
                list.set(i, map);
            }
            page.setRecords(list);

        }
        return ResponseResult.SUCCESS(page);
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "删除入场记录")
    @DeleteMapping("/{eid}")
    public ResponseResult delById(@PathVariable String eid){
        entersService.delById(eid);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除入场记录")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            entersService.delById( activies[i]);
        }
        return ResponseResult.SUCCESS();
    }


}
