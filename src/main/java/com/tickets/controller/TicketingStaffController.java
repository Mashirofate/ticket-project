package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TicketingAddDto;
import com.tickets.dto.TicketingStaffSearchDto;
import com.tickets.service.TicketingStaffService;
import com.tickets.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "活动接口")
@RestController
@RequestMapping("/ts")
public class TicketingStaffController {

    @Autowired
    private TicketingStaffService ticketingStaffService;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "场馆条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(TicketingStaffSearchDto ticketingStaffSearchDto) {
        return ResponseResult.SUCCESS(ticketingStaffService.getByKeys(ticketingStaffSearchDto));
    }
    @Authentication(required = false)
    @ApiOperation(value = "下载批量导入票务信息模板", notes = "")
    @GetMapping("/model/download")
    public void modelDownload(HttpServletResponse response) throws IOException {
        ticketingStaffService.exportExcel(response);
    }
    @Authentication(required = false)
    @ApiOperation(value = "批量添加用户", notes = "需要下载固定的模板")
    @PostMapping("/addBatch")
    public ResponseResult addBatch(@RequestParam("file") MultipartFile file, @RequestParam String aId) {
        List<Map<String, Object>> analysis = ExcelUtils.analysis(file);
        ticketingStaffService.saveBath(analysis, aId);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "添加一个用户")
    @PostMapping("/add")
    public ResponseResult add(@RequestBody TicketingAddDto ticketingAddDto) {
        ticketingStaffService.install(ticketingAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "更新入口", notes = "")
    @PostMapping("/update")
    public ResponseResult update(@RequestBody TicketingAddDto ticketingAddDto) {
        ticketingStaffService.update(ticketingAddDto);
        return ResponseResult.SUCCESS();
    }



    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "删除票务人员", notes = "根据tid删除")
    @DeleteMapping("/{tid}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult delT(@PathVariable String tid) {
        ticketingStaffService.remove(tid);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除票务人员", notes = "根据tid删除")
    @DeleteMapping("/s/{ids}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult delTs(@PathVariable String ids) {
        String[] activies =ids.split(",");
        for (String activy : activies) {
            ticketingStaffService.remove(activy);
        }
        return ResponseResult.SUCCESS();
    }
}
