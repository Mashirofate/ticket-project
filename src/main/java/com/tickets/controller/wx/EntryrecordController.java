package com.tickets.controller.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.dto.TicketingMayoDto;
import com.tickets.dto.TloeAddDto;
import com.tickets.service.EntryrecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

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
        JSONObject jsobject = JSONObject.parseObject(jsonstr);


        String aId = jsobject.getString("aid");
        String ips = jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);
        //  System.out.println("成功+++++++++codelist:"+ ips+"++++++++下载身份证:"+aId);
        List<Map<String, Object>> codelist = entryrecordService.getByEntryrecorddownload(aId, ips);

        if (codelist != null) {
            List<String> listeid = new ArrayList<>();
            if (codelist.size() > 0) {
                for (Map<String, Object> simi : codelist) {
                    listeid.add(simi.get("eId").toString());
                }
                String ipss = ips + ",";
                entryrecordService.getByEntryrecorddownloadlisteid(aId, listeid, ipss);
            }
            //  System.out.println("成功+++++++++codelist:"+ codelist.size()+"++++++++下载身份证:"+listeid.size());
        }

        return ResponseResult.SUCCESS(codelist);
    }

    @Authentication(required = true)
    @Operation(summary = "下载工作证入场记录")
    @PostMapping("/Entryrecoremployeeddownloads")
    public ResponseResult getbindingEntryrecoremployeeddownloads(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject = JSONObject.parseObject(jsonstr);


        String aId = jsobject.getString("aid");
        String ips = jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);
        //  System.out.println("成功+++++++++codelist:"+ ips+"++++++++下载身份证:"+aId);
        List<Map<String, Object>> codelist1 = entryrecordService.getByemployee(aId, ips);
        List<Map<String, Object>> codelist = new ArrayList<>();

        codelist1.stream().forEach(map -> {
            Map<String, Object> map1 = new HashMap<>();
          /*  byte[] bytes = (byte[]) map.get("FACEPHOTO");
            String base64String = Base64.getEncoder().encodeToString(bytes);*/
            byte[]  by= (byte[])map.get("FACEPHOTO");
            String base64String = new String(by);
            map1.put("FACEPHOTO", base64String);
            map1.put("eId",  map.get("eId"));
            map1.put("aId", map.get("aId"));
            map1.put("tId", map.get("tId"));
            map1.put("eName", map.get("eName"));
            map1.put("ENTERID", map.get("ENTERID"));
            map1.put("IDCODE", map.get("IDCODE"));
            map1.put("stamp", map.get("stamp"));
            codelist.add(map1);
        });

        if (codelist != null) {
            List<String> listeid = new ArrayList<>();
            if (codelist.size() > 0) {
                for (Map<String, Object> simi : codelist) {
                    listeid.add(simi.get("eId").toString());
                }
                String ipss = ips + ",";
                entryrecordService.getemployeeid(aId, listeid, ipss);
            }
            //  System.out.println("成功+++++++++codelist:"+ codelist.size()+"++++++++下载身份证:"+listeid.size());
        }

        return ResponseResult.SUCCESS(codelist);
    }


    @Authentication(required = true)
    @Operation(summary = "下载展会的票务信息")
    @PostMapping("/instead")
    public ResponseResult getbindingEtickinginstead(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject = JSONObject.parseObject(jsonstr);


        String aId = jsobject.getString("aid");
        String ips = jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);
        //  System.out.println("成功+++++++++codelist:"+ ips+"++++++++下载身份证:"+aId);
        List<Map<String, Object>> codelist = entryrecordService.getbindingEtickinginstead(aId, ips);

        if (codelist != null) {
            List<String> listeid = new ArrayList<>();
            if (codelist.size() > 0) {
                for (Map<String, Object> simi : codelist) {
                    listeid.add(simi.get("tId").toString());
                }
                String ipss = ips + ",";
                entryrecordService.getBytickinginstead(aId, listeid, ipss);
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
        JSONObject jsobject = JSONObject.parseObject(jsonstr);
        String aId = jsobject.getString("aid");
        List<Map<String, Object>> codelist = entryrecordService.getByticketingDownloads(aId);

        return ResponseResult.SUCCESS(codelist);
    }


    @Authentication(required = true)
    @Operation(summary = "查询清楚卡信息输给本地")
    @PostMapping("/frontinfoDownloads")
    public ResponseResult getfrontinfoDownloads(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject = JSONObject.parseObject(jsonstr);
        String aId = jsobject.getString("aid");
        List<Map<String, Object>> codelist = entryrecordService.getByfrontinfoDownloads(aId);

        return ResponseResult.SUCCESS(codelist);
    }

    @Authentication(required = true)
    @Operation(summary = "接受玛奥传输过来的信息")
    @PostMapping("/EntryrecordMahault")
    public ResponseResult postEntryrecordMahault(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject = JSONObject.parseObject(jsonstr);
        //  System.out.print("jsobject:"+jsobject);

        String verify = jsobject.getString("verify");
        String aId = jsobject.getString("aId");
        String permissions = entryrecordService.getPermissions(aId);
        String ppp = null;
        if (verify.equals(permissions)) {
            JSONArray listiden = jsobject.getJSONArray("FaceObject");
            if (listiden != null) {
                List list = new ArrayList();
                for (int i = 0; i < listiden.size(); i++) {
                    if (listiden.getJSONObject(i).get("tQrcard") != null) {
                        String aName = listiden.getJSONObject(i).get("aName").toString();
                        String tQrcard = listiden.getJSONObject(i).get("tQrcard").toString();
                        String tIdentitycard = listiden.getJSONObject(i).get("tIdentitycard").toString();
                        String time = listiden.getJSONObject(i).get("time").toString();
                        TicketingMayoDto ticketingMayoDto = new TicketingMayoDto();
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
                ppp = String.valueOf(listiden.size());
            }

        } else {
            ppp = "没有权限不能添加该数据";
        }


        return ResponseResult.SUCCESS(ppp);
    }

    @Authentication(required = true)
    @Operation(summary = "接受上传的摄像头入场记录    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/tloerecord")
    public ResponseResult getbindtloerecord(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        //System.out.print("jsonstr:"+jsonstr);
        int seio = 0;
        JSONObject jsobject = JSONObject.parseObject(jsonstr);
        JSONArray listiden = jsobject.getJSONArray("Entryrecord");

        if (listiden != null) {
            if(listiden.size()>0){
                List list = new ArrayList();
                for (int i = 0; i < listiden.size(); i++) {
                    if (listiden.getJSONObject(i).get("teId") != null) {
                        TloeAddDto tloeAddDto = new TloeAddDto();
                        String teId = listiden.getJSONObject(i).get("teId").toString();
                        String teAisle = listiden.getJSONObject(i).get("teAisle").toString();
                        String teaId = listiden.getJSONObject(i).get("teaId").toString();
                        String teCategory = listiden.getJSONObject(i).get("teCategory").toString();
                        String teMarking = listiden.getJSONObject(i).get("teMarking").toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String teDate = listiden.getJSONObject(i).get("teDate").toString();
                        byte[] teImage = null;
                        if (listiden.getJSONObject(i).get("teImage") != null) {
                            teImage = listiden.getJSONObject(i).get("teImage").toString().getBytes();
                        }
                        tloeAddDto.setTeaId(teaId);
                        tloeAddDto.setTeAisle(teAisle);
                        tloeAddDto.setTeId(teId);
                        tloeAddDto.setTeCategory(teCategory);
                        tloeAddDto.setTeMarking(teMarking);
                        tloeAddDto.setTeDate(teDate);
                        tloeAddDto.setTeImage(teImage);

                        list.add(tloeAddDto);

                    }
                }
                seio=list.size();
                entryrecordService.bindtloerecord(list);
            }
        }


        return ResponseResult.SUCCESS(seio);
    }


    @Authentication(required = true)
    @Operation(summary = "文件下载", description  = "")
    @GetMapping("/Cspfindfile")
    public void  Cspfindfile(String path, HttpServletResponse response)  {

        try {
            // path: 欲下载的文件的路径
            File file = new File(path);
            // 获取文件名 - 设置字符集
            String downloadFileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), "iso-8859-1");
            // 以流的形式下载文件
            InputStream fis;
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + downloadFileName);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();




        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


}