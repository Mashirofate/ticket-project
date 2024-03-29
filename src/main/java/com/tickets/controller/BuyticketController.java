package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.BuyticketDto;
import com.tickets.dto.ResponseResult;
import com.tickets.service.EntersService;
import com.tickets.service.TicketingStaffService;
import com.tickets.service.VenueActiviesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Tag(name = "打印票务")
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
    @Operation(summary = "条件搜索", description  = "")
    @GetMapping("/search")
    public ResponseResult search(BuyticketDto buyticketDto) throws ParseException {
        return ResponseResult.SUCCESS(ticketingStaffService.getByBuys(buyticketDto));
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "查询活动名称")
    @GetMapping("/ay")
    public ResponseResult  queryActivity(){
        return ResponseResult.SUCCESS(venueActiviesService.selectByNames());
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
