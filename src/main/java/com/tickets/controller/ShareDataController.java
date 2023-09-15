package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dao.Page;
import com.tickets.dto.AisleAddDto;
import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.VenueManagementAddDto;
import com.tickets.service.*;
import com.tickets.sync.DataHandler;
import com.tickets.sync.SyncConstant;
import com.tickets.utils.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Api(tags = "共享数据接口")
@RestController
@RequestMapping("/share")
public class ShareDataController {

    @Autowired
    private VenueActiviesService venueActiviesService;

    @Autowired
    private VenueManagementService venueManagementService;

    @Autowired
    private EntranceManagementService entranceManagementService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private EntersService entersService;

    @Autowired
    private DataHandler dataHandler;

    private Logger logger = LoggerFactory.getLogger(ShareDataController.class);
    /**
     * 提供当前有效的活动信息，以时间为查询条件，返回的有活动地
     * @return
     */
    @Authentication(isLogin = false,isRequiredUserInfo = false)
    @ApiOperation(value = "获取启用活动列表")
    @RequestMapping(value = "/{clientId}/activie", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseResult activie(@PathVariable("clientId") String clientId) {
        logger.info("{} ### 活动数据同步请求",clientId);
        //logger.info(aa);
        //logger.info(configMapBean.getActivity().toString());
        List<Map<String, Object>> list = venueActiviesService.getOpenActivies();
        logger.info("{} ### 活动数据返回列表 {}",clientId, JsonUtil.formatObjectToJsonString(list));
        return ResponseResult.SUCCESS(list);
    }
    /**
     * 获取场馆数据
     * @return
     */
    @Authentication(isLogin = false,isRequiredUserInfo = false)
    @ApiOperation(value = "获取场馆列表")
    @RequestMapping(value = "/{clientId}/venues", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseResult venues(@PathVariable("clientId") String clientId) {
        logger.info("{} ### 场馆数据同步请求",clientId);
        List<VenueManagementAddDto> list = venueManagementService.selectVenuesByEnable();
        logger.info("{} ### 同步场馆数据返回列表 {}",clientId, JsonUtil.formatObjectToJsonString(list));
        return ResponseResult.SUCCESS(list);
    }

    /**
     * 获取入口数据
     * @return
     */
    @Authentication(isLogin = false,isRequiredUserInfo = false)
    @ApiOperation(value = "获取开放场馆中所有开放的入口列表")
    @RequestMapping(value = "/{clientId}/entrance", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseResult entrance(@PathVariable("clientId") String clientId) {
        logger.info("{} ### 入口数据同步请求",clientId);
        List<EntranceManagementAddDto> list = entranceManagementService.selectEntranceByEnable();
        logger.info("{} ### 同步入口数据返回列表 {}",clientId, JsonUtil.formatObjectToJsonString(list));
        return ResponseResult.SUCCESS(list);
    }

    /**
     * 获取通道数据
     * @return
     */
    @Authentication(isLogin = false,isRequiredUserInfo = false)
    @ApiOperation(value = "获取通道列表", notes = "vmId, vmName")
    @GetMapping("/{clientId}/aisle")
    public ResponseResult aisle(@PathVariable("clientId") String clientId) {
        logger.info("{} ### 通道数据同步请求",clientId);
        List<AisleAddDto> list = deviceService.selectAll();
        logger.info("{} ### 同步数据返回列表 {}",clientId, JsonUtil.formatObjectToJsonString(list));
        return ResponseResult.SUCCESS(list);
    }

    @Authentication(required = false)
    @ApiOperation(value = "查询服务的数据", notes = "根据aId活动ID查询")
    @GetMapping("/{clientId}/enter/{page}/{size}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult enter(@PathVariable("clientId") String aId,@PathVariable("page") int page,@PathVariable("size") int size) {
        logger.info("{} 进行数据同步请求",aId);
        //String a = "2023-02-16 22:16:50";
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String a = ldt.format(dtf);
        Page data = entersService.selectNewData(a, aId, page, size);
        logger.info("{} 同步数据返回列表 {}",aId, JsonUtil.formatObjectToJsonString(data));
        return ResponseResult.SUCCESS(data);
    }

    @Authentication(required = false)
    @ApiOperation(value = "查询服务的数据", notes = "根据aId活动ID查询")
    @PostMapping ("/{clientId}/upload/enter")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult uploadEnter(@PathVariable("clientId") String aId, @RequestBody String json) {
        logger.info("{} 进行数据同步请求",aId);
        String a = "2023-02-16 22:16:50";
        dataHandler.processor(SyncConstant.ENTER,aId, json);
        //Page data = entersService.selectNewData(a, page, size);
        //logger.info("{} 同步数据返回列表 {}",aId, JsonUtil.formatObjectToJsonString(data));
        return ResponseResult.SUCCESS();
    }
}
