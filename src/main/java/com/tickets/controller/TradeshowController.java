package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TradeshowPartDto;
import com.tickets.service.TradeshowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name= "入场记录接口")
@RestController
@RequestMapping("/tc")
public class TradeshowController {

    @Autowired
    private TradeshowService tradeshowService;

    @Authentication(required = true)
    @Operation(summary = "传入数据接口", description  = "")
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
