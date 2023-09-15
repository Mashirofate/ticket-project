package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.EntersService;
import com.tickets.service.EntranceManagementService;
import com.tickets.service.TradeshowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Api(tags = "入场记录接口")
@RestController
@RequestMapping("/tc")
public class TradeshowController {

    @Autowired
    private TradeshowService tradeshowService;

    @Authentication(required = true)
    @ApiOperation(value = "传入数据接口", notes = "")
    @PostMapping("/add")
    public ResponseResult addVenue( String tQrcard,String tType,String aId  ) {
        TradeshowPartDto tradeshowPartDto =new TradeshowPartDto();
        tradeshowPartDto.setAId(aId);
        tradeshowPartDto.setTQrcard(tQrcard);
        tradeshowPartDto.setTType(tType);
        tradeshowService.save(tradeshowPartDto);
        return ResponseResult.SUCCESS();
    }



}
