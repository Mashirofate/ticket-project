package com.tickets.controller.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TicketingMayoDto;
import com.tickets.service.EntryrecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name = "数据传输接口")
@RestController
@RequestMapping("/Entryrecords")
public class EntryrecordController {


    @Autowired
    private EntryrecordService entryrecordService;


    @Authentication(required = true)
    @Operation(summary = "下载入场记录")
    @PostMapping("/Entryrecorddownloads")
    public ResponseResult getbindingEntryrecorddownloads(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);


        String aId= jsobject.getString("aid");
        String ips= jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);
      //  System.out.println("成功+++++++++codelist:"+ ips+"++++++++下载身份证:"+aId);
        List<Map<String,Object>> codelist = entryrecordService.getByEntryrecorddownload(aId,ips);

        if(codelist!=null){
            List<String> listeid=new ArrayList<>();
            if(codelist.size()>0){
                for (Map<String, Object> simi : codelist) {
                    listeid.add( simi.get("eId").toString());
                }
                String ipss= ips+",";
                entryrecordService.getByEntryrecorddownloadlisteid(aId,listeid,ipss);
            }
            //  System.out.println("成功+++++++++codelist:"+ codelist.size()+"++++++++下载身份证:"+listeid.size());
        }

        return ResponseResult.SUCCESS(codelist);
    }


    @Authentication(required = true)
    @Operation(summary = "查询活动的票务信息传输给本地")
    @PostMapping("/ticketingDownloads")
    public ResponseResult getticketingDownloads(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        String aId= jsobject.getString("aid");
        List<Map<String,Object>> codelist = entryrecordService.getByticketingDownloads(aId);

        return ResponseResult.SUCCESS(codelist);
    }

    @Authentication(required = true)
    @Operation(summary = "接受玛奥传输过来的信息")
    @PostMapping("/EntryrecordMahault")
    public ResponseResult postEntryrecordMahault(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
       //  System.out.print("jsobject:"+jsobject);

        String verify= jsobject.getString("verify");
        String aId= jsobject.getString("aId");
        String permissions= entryrecordService.getPermissions(aId);
        String ppp=null;
        if(verify.equals(permissions)){
            JSONArray listiden=  jsobject.getJSONArray("FaceObject");
            if(listiden!=null){
                List list=new ArrayList();
                for(int i=0;i<listiden.size();i++) {
                    if( listiden.getJSONObject(i).get("tQrcard")!=null){
                        String aName= listiden.getJSONObject(i).get("aName").toString();
                        String tQrcard= listiden.getJSONObject(i).get("tQrcard").toString();
                        String tIdentitycard=listiden.getJSONObject(i).get("tIdentitycard").toString();
                        String time=listiden.getJSONObject(i).get("time").toString();
                        TicketingMayoDto ticketingMayoDto=new TicketingMayoDto();
                        UUID uuid = UUID.randomUUID();
                        ticketingMayoDto.setTId(uuid.toString());
                        ticketingMayoDto.setAId(aId);
                        ticketingMayoDto.setAName(aName);
                        ticketingMayoDto.setTQrcard(tQrcard);
                        ticketingMayoDto.setTIdentitycard(tIdentitycard);
                        ticketingMayoDto.setTNote("0");
                        ticketingMayoDto.setTime(time);
                        list.add(ticketingMayoDto);
                       // System.out.print("aName:"+aName+"+++tQrcard:"+tQrcard+"+++tIdentitycard:"+tIdentitycard+"+++time:"+time);
                    }
                }
                entryrecordService.addMayo(list);
                ppp= String.valueOf(listiden.size());
            }

        }else{
            ppp="没有权限不能添加该数据";
        }


        return ResponseResult.SUCCESS(ppp);
    }

}
