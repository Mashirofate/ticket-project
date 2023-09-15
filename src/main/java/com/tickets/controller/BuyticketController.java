package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.EntersService;
import com.tickets.service.TicketingStaffService;
import com.tickets.service.VenueActiviesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(tags = "打印票务")
@RestController
@RequestMapping("/bk")
public class BuyticketController {

    @Autowired
    private VenueActiviesService venueActiviesService;
    @Autowired
    private EntersService entersService;
    @Autowired
    private TicketingStaffService ticketingStaffService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(BuyticketDto buyticketDto) throws ParseException {
        return ResponseResult.SUCCESS(ticketingStaffService.getByBuys(buyticketDto));
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "查询活动名称")
    @GetMapping("/ay")
    public ResponseResult  queryActivity(){
        return ResponseResult.SUCCESS(venueActiviesService.selectByNames());
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
