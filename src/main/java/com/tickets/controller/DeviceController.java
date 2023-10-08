package com.tickets.controller;

import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.entity.Aisle;
import com.tickets.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

@Api(tags = "设备ip接口")
@RestController
@RequestMapping("/de")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "设备ip条件搜索", notes = "")
    @GetMapping("/search")
    public ResponseResult search(DeviceSearchDto deviceSearchDto) throws IOException {

        Page page = deviceService.getByKeys(deviceSearchDto);
        /*if (page != null & page.getRecords()!= null) {
            List list = page.getRecords();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                // boolean status = InetAddress.getByName((String) map.get("dIp")).isReachable(100);     // 当返回值是true时，说明host是可用的，false则不可。
                // if (status){

              //   isHostConnectable((String) map.get("dIp"),80);
              //  if (isConnect((String) map.get("dIp"))) {
                if (pingIp((String) map.get("dIp"))) {
                    map.put("dEnable", "在线");
                } else {
                    map.put("dEnable", "离线");
                }
                list.set(i, map);
            }
            page.setRecords(list);

        }*/
        return ResponseResult.SUCCESS(page);
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "创建设备ip", notes = "")
    @PostMapping("/add")
    public ResponseResult addDevice(@RequestBody DeviceAddDto deviceAddDto) {
        deviceService.save(deviceAddDto);
        return ResponseResult.SUCCESS();
    }

//    手动装配
//    @Authentication(isLogin = true, isRequiredUserInfo = true)
//    @ApiOperation(value = "更新设备ip")
//    @PostMapping("/update")
//    public ResponseResult updatDevice(@RequestBody Map map) {
//        DeviceAddDto deviceAddDto = new DeviceAddDto();
//        deviceAddDto.setDId((String) map.get("dId"));
//        deviceAddDto.setDName((String) map.get("dName"));
//        deviceAddDto.setDIp((String) map.get("dIp"));
//        deviceService.update(deviceAddDto);
//        return ResponseResult.SUCCESS();
//    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "更新设备ip")
    @PostMapping("/update")
    public ResponseResult updatDevice(@RequestBody DeviceAddDto deviceAddDto) {
        deviceService.update(deviceAddDto);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "删除设备ip")
    @DeleteMapping("/{did}")
    public ResponseResult delById(@PathVariable String did) {
        deviceService.delById(did);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除设备ip")
    @DeleteMapping("/s/{ids}")
    public ResponseResult delByIds(@PathVariable String ids) {
        String[] activies = ids.split(",");
        for (int i = 0; i < activies.length; i++) {
            deviceService.delById(activies[i]);
        }
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "同步在线设备的数据ip")
    @PostMapping("/synchrodata")
    public ResponseResult synchrodata(@RequestBody  List <Map<String, Object>> list ) {
        int j=0;
        for (Map<String, Object> stringObjectMap : list) {
            Map map = (Map) stringObjectMap;
            String dEnable = (String) map.get("dEnable");
            if (dEnable.equals("在线")) {
                String dip = (String) map.get("dIp");
                // 同步数据再线设备的数据
                transmitData(dip);
            }

        }
        return ResponseResult.SUCCESS();
    }



    public static final boolean transmitData(String dip) {

        return true;
    }



    public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static final boolean isNodeReachable(String hostname) {
        try {
            return 0==Runtime.getRuntime().exec("ping "+hostname).waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean pingIp(String ip) throws UnknownHostException, IOException{
        //能ping通放回true 反之 false 超时时间 3000毫秒
        return InetAddress.getByName(ip).isReachable(3000);
    }

    /**
     * 根据ip判断当前ip是否能够ping通
     *  
     *
     * @param ip
     * @return
     */
    public static boolean isConnect(String ip) {
        boolean bool = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping " + ip);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                // 优化速度
                if (line.indexOf("请求超时") >= 0) {
                    // System.out.println(ip + "网络断开，时间 " + new Date());
                    return false;
                }
            }
            is.close();
            isr.close();
            br.close();

            if (null != sb && !sb.toString().equals("")) {
                // 网络畅通
                //System.out.println(ip + "网络正常 ，时间" + new Date());
                // 网络不畅通
                //System.out.println(ip + "网络断开，时间 " + new Date());
                bool = sb.toString().indexOf("TTL") > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "区域通道条件搜索", notes = "")
    @GetMapping("/searchai")
    public ResponseResult searchai(AisleSearchDto aisleSearchDto) throws IOException {

        Page page = deviceService.getaiKeys(aisleSearchDto);
        /*if (page != null & page.getRecords()!= null) {
            List list = page.getRecords();
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                // boolean status = InetAddress.getByName((String) map.get("dIp")).isReachable(100);     // 当返回值是true时，说明host是可用的，false则不可。
                // if (status){

              //   isHostConnectable((String) map.get("dIp"),80);
              //  if (isConnect((String) map.get("dIp"))) {
                if (pingIp((String) map.get("dIp"))) {
                    map.put("dEnable", "在线");
                } else {
                    map.put("dEnable", "离线");
                }
                list.set(i, map);
            }
            page.setRecords(list);

        }*/
        return ResponseResult.SUCCESS(page);
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "删除区域通道")
    @DeleteMapping("/saii/{aid}")
    public ResponseResult deaiById(@PathVariable String aid) {
        deviceService.deaiById(aid);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "批量删除区域通道")
    @DeleteMapping("/sai/{ids}")
    public ResponseResult delaiByIds(@PathVariable String ids) {
        String[] activies = ids.split(",");
        for (String activy : activies) {
            deviceService.deaiById(activy);
        }
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "更新区域通道")
    @PostMapping("/updateai")
    public ResponseResult updatai(@RequestBody Aisle aisle) {
        deviceService.updateai(aisle);
        return ResponseResult.SUCCESS();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @ApiOperation(value = "创建区域通道", notes = "")
    @PostMapping("/addai")
    public ResponseResult addai(@RequestBody  Aisle aisle) {
        deviceService.saveai(aisle);
        return ResponseResult.SUCCESS();
    }
}
