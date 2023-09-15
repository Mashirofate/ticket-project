package com.tickets.controller.wx;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TicketingSaveDto;
import com.tickets.service.TicketingStaffService;
import com.tickets.service.VenueActiviesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "微信用户权限接口")
@RestController
@RequestMapping("/wechat/activity")
public class ActivityController {

    @Autowired
    private VenueActiviesService venueActiviesService;
    @Autowired
    private TicketingStaffService ticketingStaffService;

    // required =  false  @GetMapping("/open")
    @Authentication(required = true)
    @ApiOperation(value = "启用的活动")
    @PostMapping("/open")
    public ResponseResult openActivies(@RequestBody String x) {
        String s =x;
      /*  List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMaps = new HashMap<String, Object>();
        myMaps.put("title","调用二维码小程序码");
        myMaps.put("page","getMiniProgramCode");
        List<Map<String, Object>> list1= new ArrayList<>();
        list1.add(myMaps);

        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("title","云函数1");
        myMap.put("tip","安全、免鉴权运行业务代码1");
        myMap.put("showItem",false);
        myMap.put("item",list1);
        list.add(myMap);
        Map<String, Object> myMap1 = new HashMap<String, Object>();
        myMap1.put("title","数据库");
        myMap1.put("tip","安全、免鉴权运行业务代码1");
        myMap1.put("showItem",false);
        myMap1.put("item",list1);
        list.add(myMap1);
        Map<String, Object> myMap2 = new HashMap<String, Object>();
        myMap2.put("title","云存储1");
        myMap2.put("tip","自带CDN加速文件存储1");
        myMap2.put("showItem",false);
        list.add(myMap2);
        return ResponseResult.SUCCESS(list);*/


        return ResponseResult.SUCCESS(venueActiviesService.getOpenActivies());



    }

    @Authentication(required = false)
    @ApiOperation(value = "根据id获取活动信息")
    @GetMapping("/")
    public ResponseResult getByVaId(@RequestParam String aId) {
        return ResponseResult.SUCCESS(venueActiviesService.getByVaId(aId));
    }

    @Authentication(required = true)
    @ApiOperation(value = "根据id获取活动信息")
    @PostMapping("/binding")
    public ResponseResult getbindingaId(String aId,String cardId,String Rname,String scanCode,String datei) {

        int i=0;
        String tid=null;
        // 查询是否是有效票务
        List<Map<String,Object>> codelist= ticketingStaffService.getByKeys(scanCode,aId);
        for (Map<String, Object> map : codelist) {
            for (String s : map.keySet()) {
                 tid = map.get(s).toString();
            }
        }

        if(tid == null || tid.length() == 0){
            i=3;
        }else{

            boolean boll= ticketingStaffService.update(cardId, Rname, tid,datei);
            if(boll){
                i=1;
            }else {
                i=2;
            }
        }
        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("codes",i);
        list.add(myMap);
       return ResponseResult.SUCCESS(list);


    }


}
