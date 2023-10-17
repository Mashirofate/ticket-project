package com.tickets.controller;


import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.service.TicketFormsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "报表接口")
@RestController
@RequestMapping("/tf")
public class TicketFormsController {

    @Autowired
   private TicketFormsService ticketFormsService;


    @Authentication(required = false)
    @ApiOperation(value = "查询人数情况", notes = "根据aId活动ID查询")
    @GetMapping("/{aId}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult getNumber(@PathVariable String aId) {


        HashMap<String, Object> map=new HashMap<>();


        // 获取整个展会的每一天的人数
        List<Map<String, Object>> headcountLists= ticketFormsService.getNumber(aId);
//        List<Map<String, Object>> headcountList=new ArrayList<>();

//        for (int i = 0; i <headcountLists.size() ; i++) {
//            Map<String, Object> b=new HashMap<>();
//            Map<String, Object> a= headcountLists.get(i);
//
//            b.put("name",a.get("type"));
//            b.put("value",a.get("value"));
//            headcountList.add(b);
//        }


        //获取整个活动区间内的每个入口的人数
        List<Map<String, Object>> ExitcountList= ticketFormsService.getEncount(aId);



        // 获取整个展会的每一时刻的人数
        // 统计总人数
        List<Map<String, Object>> list= ticketFormsService.getEnTotality(aId);

        /*for (int i = 0; i <list.size() ; i++) {
            Map<String, Object> b=new HashMap<>();
            Map<String, Object> a= list.get(i);
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // string ---》Date
            Date date= null;
            try {
                date = sdf.parse(a.get("time").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("MM-dd HH:mm:ss");
            String time = format0.format(  date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("时间",time);
            b.put("人数",a.get("value"));
            rows.add(b);
        }
*/
        map.put("rows",list);
        map.put("headcountList",headcountLists);
        map.put("ExitcountList", ExitcountList);
        return ResponseResult.SUCCESS(map);

    }

    @Authentication(required = false)
    @ApiOperation(value = "查询人数入场人流数情况", notes = "根据aId查询")
    @GetMapping("/s/{aId}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult getSum(@PathVariable String aId) throws ParseException {

        /*HashMap<String, Object> map=new HashMap<>();


        // 工作证入口统计人数
        List<Map<String, Object>> columns=ticketFormsService.getcolumns(aId);

        List<Map<String, Object>> columns1=new ArrayList<>();
        // 票务入口统计人数
        List<Map<String, Object>> rows= ticketFormsService.getSum(aId);
        List<Map<String, Object>> rows1=new ArrayList<>();
        for (int i = 0; i <rows.size() ; i++) {
            Map<String, Object> b=new HashMap<>();
            Map<String, Object> a= rows.get(i);
             // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // string ---》Date
            Date date= sdf.parse(a.get("time").toString());

            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
            String time = format0.format(  date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("date",time);
            b.put("type",a.get("Names"));
            b.put("value",a.get("amount"));
            rows1.add(b);
        }
        for (int i = 0; i <columns.size() ; i++) {
            Map<String, Object> b=new HashMap<>();
            Map<String, Object> a= columns.get(i);
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // string ---》Date
            Date date= sdf.parse(a.get("time").toString());

            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
            String time = format0.format(  date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("date",time);
            b.put("type","工作入口");
            b.put("value",a.get("amount"));
            columns1.add(b);
        }

        rows1.addAll(columns1);
        map.put("rows",rows1);
        return ResponseResult.SUCCESS(map);
*/



            List<Map<String, Object>> rows=new ArrayList<>();
            // 票务入口统计人数
            List<Map<String, Object>> ExportList= ticketFormsService.getEnSum(aId);

        for (Map<String, Object> stringObjectMap : ExportList) {
            Map<String, Object> b = new HashMap<>();
            Map<String, Object> a = stringObjectMap;
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // string ---》Date
            Date date = sdf.parse(a.get("time").toString());

            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
            String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("type", a.get("Names") + "号");
            b.put("date", time);
            b.put("value", a.get("value"));
            rows.add(b);
        }



            return ResponseResult.SUCCESS(rows);





    }


    @Authentication(required = false)
    @ApiOperation(value = "查询人数入场人流数情况", notes = "根据aId查询")
    @GetMapping("/Totality/{aId}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult getTotality(@PathVariable String aId) throws ParseException {

        HashMap<String, Object> map=new HashMap<>();
        // 统计总人数
        List<Map<String, Object>> list= ticketFormsService.getTotality(aId);
        List<Map<String, Object>> rows=new ArrayList<>();
        for (Map<String, Object> stringObjectMap : list) {
            Map<String, Object> b = new HashMap<>();
            Map<String, Object> a = stringObjectMap;
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // string ---》Date
            Date date = sdf.parse(a.get("time").toString());

            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
            String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("时间", time);
            b.put("人数", a.get("value"));
            rows.add(b);
        }

        map.put("rows",rows);
        return ResponseResult.SUCCESS(map);
    }

     /*
        展会
        出口人流报表

     */



    @Authentication(required = false)
    @ApiOperation(value = "查询出场人员总数", notes = "根据aId查询")
    @GetMapping("/getHeadcount/{aId}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult getHeadcount (@PathVariable String aId) throws ParseException {

        HashMap<String, Object> map=new HashMap<>();


        // 获取整个展会的每一天的人数
        List<Map<String, Object>> headcountLists= ticketFormsService.getheadcount(aId);


        //获取整个活动区间内的每个出口的人数
    //     List<Map<String, Object>> ExitcountList= ticketFormsService.getExitcount(aId);


        // 获取整个展会的每一时刻的人数
        // 统计总人数
        List<Map<String, Object>> list= ticketFormsService.getExportTotality(aId);

       /* List<Map<String, Object>> rows=new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            Map<String, Object> b=new HashMap<>();
            Map<String, Object> a= list.get(i);
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // string ---》Date
            Date date= sdf.parse(a.get("time").toString());
            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("MM-dd HH:mm:ss");
            String time = format0.format(  date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("时间",time);
            b.put("人数",a.get("value"));
            rows.add(b);
        }*/

        map.put("rows",list);
        map.put("headcountList",headcountLists);

        return ResponseResult.SUCCESS(map);
    }

    @Authentication(required = false)
    @ApiOperation(value = "查询人数出场人流数情况", notes = "根据aId查询")
    @GetMapping("/HeadExport/{aId}")  // 不同的情求  DeleteMapping 需要更改要不会产生跨域问题
    public ResponseResult getExportSum(@PathVariable String aId) throws ParseException {
        List<Map<String, Object>> rows=new ArrayList<>();
        // 票务入口统计人数
        List<Map<String, Object>> ExportList= ticketFormsService.getExportSum(aId);

        for (Map<String, Object> stringObjectMap : ExportList) {
            Map<String, Object> b = new HashMap<>();
            Map<String, Object> a = stringObjectMap;
            // "yyyy-MM-dd HH:mm:ss"
            // 使用这个方法转化 Date和string
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // string ---》Date
            Date date = sdf.parse(a.get("time").toString());

            //按固定的格式显示时间
            SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
            String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
            b.put("date", time);
            b.put("type", a.get("Names") + "号");
            b.put("value", a.get("value"));
            rows.add(b);
        }
        return ResponseResult.SUCCESS(rows);

    }


}
