package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;
import com.tickets.dto.ResponseResult;
import com.tickets.service.EntersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Tag(name  = "入场记录接口")
@RestController
@RequestMapping("/es")
public class EntersController {
    @Autowired
    private EntersService entersService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "入场记录条件搜索", description  = "")
    @GetMapping("/search")
    public ResponseResult search(EntersSearchDto entersSearchDto) throws ParseException {
        Page page= entersService.getByKeys(entersSearchDto);
        if (page != null & page.getRecords()!= null) {
            List list = page.getRecords();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String a1=null;
                if(map.containsKey("BIND_PHOTO")){
                    byte[]  by= (byte[])map.get("BIND_PHOTO");
                    if(null !=by){
                        String a = new String(by);
                        a1 = "data:image/png;base64," + a;
                    }
                }
                map.put("BIND_PHOTO", a1);
                list.set(i, map);
            }

            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String a1=null;
                if(map.containsKey("BIND_CARD_PHOTO")){
                    byte[]  by= (byte[])map.get("BIND_CARD_PHOTO");
                    if(null !=by){
                        String a = new String(by);
                        a1 = "data:image/png;base64," + a;
                    }
                }
                map.put("BIND_CARD_PHOTO", a1);
                list.set(i, map);
            }

            page.setRecords(list);

        }
        return ResponseResult.SUCCESS(page);
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "删除入场记录")
    @DeleteMapping("/{eid}")
    public ResponseResult delById(@PathVariable String eid){
        entersService.delById(eid);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "批量删除入场记录")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids){
        String[] activies =ids.split(",");
        for (String activy : activies) {
            entersService.delById(activy);
        }
        return ResponseResult.SUCCESS();
    }


}
