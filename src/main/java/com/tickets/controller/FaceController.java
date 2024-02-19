package com.tickets.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.EntersService;
import com.tickets.service.EntryrecordService;
import com.tickets.service.FaceService;
import com.tickets.service.VenueActiviesService;
import com.tickets.utils.HttpUtils;
import com.tickets.utils.JsonUtil;
import com.tickets.utils.SHACoder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.CDL;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Tag(name  = "人脸接口")
@RestController
@RequestMapping("/fr")
@Slf4j
public class FaceController {

    @Autowired
    private FaceService faceService;
    private final String baseUrl = "https://gaxcx.huizhou.gov.cn/sjcs";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VenueActiviesService venueActiviesService;
    @Autowired
    private EntersService entersService;
    @Autowired
    private EntryrecordService entryrecordService;

    /**
     * 获取主机名称
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取系统首选IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "查询活动")
    @PostMapping ("/search")
    public ResponseResult search(@RequestBody String aid) throws IOException {

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        List<Map<String, Object>> list=new ArrayList<>();
        String url = "https://zeantong.com:8082/wechat/activity/openActivies";

        String ips=  getLocalIP();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aid",aid);
        String param = jsonObject.toJSONString();
        String result = HttpUtils.postWithJson(url, null, param, null);
        JSONObject JSONArray=  JSONObject.parseObject(result);

        JSONArray listiden=  JSONArray.getJSONArray("data");

        if(listiden!=null){
            for(int i=0;i<listiden.size();i++) {
                Map<String, Object> map=new HashMap<>();
                map.put("aId", listiden.getJSONObject(i).get("aId").toString());
                map.put("aName", listiden.getJSONObject(i).get("aName").toString());
                list.add(map);
            }
        }else{
            list=null;
        }

       VenueActivieSearchDto venueActivieSearchDto=new VenueActivieSearchDto();
        BeanUtils.copyProperties(venueActivieSearchDto, page);
        int total =10;
        page.setTotal(total);
        page.setRecords(list);
        return ResponseResult.SUCCESS(page);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "创建活动", description  = "")
    @PostMapping("/onAvtivit")
    public ResponseResult getonAvtivit(@RequestBody String aid) throws Exception {

        VenueActivieAddDto venueActivieAddDto=new VenueActivieAddDto();
        String url = "https://zeantong.com:8082/wechat/activity/toByVaId";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aid",aid);
        String param = jsonObject.toJSONString();
        String result = HttpUtils.postWithJson(url, null, param, null);
        JSONObject Array=  JSONObject.parseObject( result);
        JSONArray listiden=  Array.getJSONArray("data");
        boolean a=false;


        if(listiden!=null){
            if(listiden.size() == 2){
                List listms=new ArrayList();
                JSONArray listm=listiden.getJSONArray(1);

                for(int i=0;i<listm.size();i++) {
                    AManagementDto aManagementDto=new AManagementDto();
                    aManagementDto.setAId( listm.getJSONObject(i).get("aId").toString());
                    aManagementDto.setEId(listm.getJSONObject(i).get("eId").toString());
                    aManagementDto.setEName(listm.getJSONObject(i).get("eName").toString());
                    aManagementDto.setENote(listm.getJSONObject(i).get("eNote").toString());
                    aManagementDto.setEEnable(1);
                    listms.add(aManagementDto);
                }
                int am=venueActiviesService.addAM(listms);

            }

            venueActivieAddDto.setAId( listiden.getJSONObject(0).get("aId").toString());
            venueActivieAddDto.setAName( listiden.getJSONObject(0).get("aName").toString());
            venueActivieAddDto.setVName( listiden.getJSONObject(0).get("vName").toString());
            String aEnable= listiden.getJSONObject(0).get("aEnable").toString();
            venueActivieAddDto.setAEnable(Integer.parseInt(aEnable));
            venueActivieAddDto.setANote( listiden.getJSONObject(0).get("aNote").toString());
            venueActivieAddDto.setATicketnumber((Integer) listiden.getJSONObject(0).get("aTicketnumber"));
            venueActivieAddDto.setAEmployeenumber((Integer) listiden.getJSONObject(0).get("aEmployeenumber"));
            venueActivieAddDto.setAType( listiden.getJSONObject(0).get("aType").toString());
            a=   venueActiviesService.save(venueActivieAddDto);


        }
        int i=0;
        if(a){
            i=1;
        }
        return ResponseResult.SUCCESS(i);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "票务信息下载", description  = "")
    @PostMapping("/ticketing")
    public ResponseResult getpostticketing(@RequestBody String aid) throws Exception {

        String argUrl = "https://www.zeantong.com:8082/Entryrecords/ticketingDownloads";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aid",aid);
        String param = jsonObject.toJSONString();
        String result = HttpUtils.postWithJson(argUrl, null, param, null);
        JSONObject Array=  JSONObject.parseObject( result);
        JSONArray listiden=  Array.getJSONArray("data");
        int  interesting = 0;
        if(listiden!=null){
            List list=new ArrayList();
            for(int i=0;i<listiden.size();i++) {
                if( listiden.getJSONObject(i).get("tId")!=null){
                    TicketingDlsDto ticketingDlsDto=new TicketingDlsDto();
                    String tId= listiden.getJSONObject(i).get("tId").toString();
                    String taId= listiden.getJSONObject(i).get("taId").toString();
                    String tIdentitycard=null;
                    if(listiden.getJSONObject(i).get("tIdentitycard")!=null){
                        tIdentitycard=  listiden.getJSONObject(i).get("tIdentitycard").toString();
                    }
                    String tQrcard=null;
                    if(listiden.getJSONObject(i).get("tQrcard")!=null){
                        tQrcard= listiden.getJSONObject(i).get("tQrcard").toString();
                    }
                    String tSeatingarea=null;
                    if(listiden.getJSONObject(i).get("tSeatingarea")!=null){
                        tSeatingarea= listiden.getJSONObject(i).get("tSeatingarea").toString();
                    }
                    String tRownumber=null;
                    if(listiden.getJSONObject(i).get("tRownumber")!=null){
                        tRownumber= listiden.getJSONObject(i).get("tRownumber").toString();
                    }
                    String tSeat=null;
                    if(listiden.getJSONObject(i).get("tSeat")!=null){
                        tSeat= listiden.getJSONObject(i).get("tSeat").toString();
                    }
                    String tGrandstand=null;
                    if(listiden.getJSONObject(i).get("tGrandstand")!=null){
                        tGrandstand= listiden.getJSONObject(i).get("tGrandstand").toString();
                    }
                    String tRealname=null;
                    if(listiden.getJSONObject(i).get("tRealname")!=null){
                        tRealname= listiden.getJSONObject(i).get("tRealname").toString();
                    }
                    String phone=null;
                    if(listiden.getJSONObject(i).get("phone")!=null){
                        phone= listiden.getJSONObject(i).get("phone").toString();
                    }
                    ticketingDlsDto.setTId(tId);
                    ticketingDlsDto.setTaId(taId);
                    ticketingDlsDto.setTQrcard(tQrcard);
                    ticketingDlsDto.setTIdentitycard(tIdentitycard);
                    ticketingDlsDto.setTSeatingarea(tSeatingarea);
                    ticketingDlsDto.setTRownumber(tRownumber);
                    ticketingDlsDto.setTSeat(tSeat);
                    ticketingDlsDto.setTGrandstand(tGrandstand);
                    ticketingDlsDto.setTRealname(tRealname);
                    ticketingDlsDto.setPhone(phone);
                    list.add(ticketingDlsDto);


                }

            }
            interesting=list.size();
            entryrecordService.installticketing(list);
        }

        return ResponseResult.SUCCESS(interesting);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "小程序数据同步", description  = "")
    @PostMapping("/applet")
    public ResponseResult getpostapplet(@RequestBody String aid) throws Exception {


        Calendar calendar = Calendar.getInstance();

        Date time= new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Timer timer = new Timer();
        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    int QueryInt = faceService.Queryquantity(aid);
                    String url = "https://zeantong.com:8082/wechat/activity/applet";
                    String ips=  getLocalIP();
                    // json数据的方式请求
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("aid",aid);
                    jsonObject.put("ips",ips);
                    JSONObject jsobject =entersService.sendSsmapplet(restTemplate,url,jsonObject,String.class);

//                    //请求
//                    String result = restTemplate.postForObject(url,jsonObject,String.class);
//                    //获取返回的身份证信息
//                    JSONObject jsobject =  JSONObject.parseObject(result);

                    int  interesting = 0;
                    JSONArray listiden=  jsobject.getJSONArray("data");
                    if(listiden!=null){
                        for(int i=0;i<listiden.size();i++) {
                            String tId= listiden.getJSONObject(i).get("tId").toString();
                            String tIdentitycard=listiden.getJSONObject(i).get("tIdentitycard").toString();
                            String tRealname=listiden.getJSONObject(i).get("tRealname").toString();
                            interesting = faceService.upquantity(tId,tIdentitycard,tRealname);
                            System.out.println("身份证成功"+new Date() +"+++++++++"+ listiden.size());
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*5);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "工作证记录上传", description  = "")
    @PostMapping("/identcard")
    public ResponseResult getidentcard(@RequestBody String aid) {

        Date time= new Date();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    Date time= new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateUp = format.format(time);

                    List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
                    List<String> listeid=new ArrayList<>();
                    // 入场记录
                    List<Map<String, Object>> listconet1 = entersService.getemploys(aid,dateUp);
                    //  getImageByActivityIdUP getEntryrecord
                    if(listconet1.size()>0){
                        for (Map<String, Object> simi : listconet1) {

                            Map<String, Object> simis=new HashMap<>();
                            simis.put("eId", simi.get("eId").toString());
                            simis.put("aId",simi.get("aId").toString());
                            simis.put("aName",simi.get("aName").toString());
                            simis.put("vName", simi.get("vName").toString());
                            simis.put("eName", simi.get("eName").toString());

                            if( simi.get("tId")!=null ){
                                simis.put("tId", simi.get("tId").toString());
                            }else{
                                simis.put("tId",null);
                            }
                            simis.put("eDate", simi.get("eDate").toString());
                            if( simi.get("tQrcard")!=null ){
                                simis.put("tQrcard", simi.get("tQrcard").toString());
                            }else{
                                simis.put("tQrcard",null);
                            }

                            listeid.add( simi.get("eId").toString());
                            lists.add(simis);
                        }

                        JSONArray array= JSONArray.parseArray(JSON.toJSONString(lists));
                        //String url = "http://127.0.0.1:5000/register";
                        String url = "https://www.zeantong.com:8082/wechat/activity/employ";

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Entryrecord",array);
                        //请求，如果失败循环五次

                        JSONObject result =entersService.sendSsmemploy(restTemplate,url,jsonObject,String.class,listeid,aid);

                        System.out.println("成功"+new Date() +"+++++++++上传工作证的数据量："+ listeid.size());
                    }else{
                        System.out.println("无工作证数据要上传跳过");

                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*600);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "数据对比", description  = "")
    @PostMapping("/domparison")
    public ResponseResult getdomparison(@RequestBody String aid) {

        // List<Map<String, Object>> listconet= faceService.getdomparison(aid);
        List<Map<String, Object>> listconet= venueActiviesService.getdomparison(aid);

        int interesting =0;
        if(listconet.size()>0){
            for (Map<String, Object> simi : listconet) {
                Object tIdentitycard=  simi.get("tIdentitycard");
                boolean td=false;
                String tId="";
                if(tIdentitycard==null){
                    td=true;
                }else{
                    tId= tIdentitycard.toString();
                }
                if(tIdentitycard==null | "".equals(tId)){
                    String tid=  simi.get("tid").toString();
                    String wtRealname=null;
                    if(simi.get("wtRealname")!=null){
                        wtRealname=simi.get("wtRealname").toString();
                    }
                    String wtIdentitycard=null;
                    if(simi.get("wtIdentitycard")!=null){
                        wtIdentitycard=simi.get("wtIdentitycard").toString();
                    }

                    // String wtIdentitycard=simi.get("wtIdentitycard").toString();
                    // int interestingon = faceService.upquantitys(tid,wtIdentitycard,wtRealname);
                    int interestingon = venueActiviesService.upquantitys(tid,wtIdentitycard,wtRealname);
                    interesting +=interestingon;
                }

            }
        }else{
            System.out.println("无数据要对比同步跳过");
        }

        return ResponseResult.SUCCESS(interesting);
    }


//     @Retryable(recover = "recover", maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "入场记录上传", description  = "")
    @PostMapping("/Entryrecord")
    public ResponseResult getpostEntryrecord(@RequestBody String aid) throws Exception {


        Date time= new Date();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    Date time= new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateUp = format.format(time);

                    List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
                    List<String> listeid=new ArrayList<>();
                    // 入场记录
                    List<Map<String, Object>> listconet1 = faceService.getEntryrecord(aid,dateUp);
                    //  getImageByActivityIdUP getEntryrecord
                    if(listconet1.size()>0){
                        for (Map<String, Object> simi : listconet1) {
                            Map<String, Object> simis=new HashMap<>();
                            simis.put("eId", simi.get("eId").toString());
                            simis.put("aId",simi.get("aId").toString());
                            simis.put("aName",simi.get("aName").toString());
                            simis.put("vName", simi.get("vName").toString());
                            simis.put("eName", simi.get("eName").toString());
                            String tId=null;
                            if(simi.get("tId")!=null){
                                tId=simi.get("tId").toString();
                            }
                            simis.put("tId", tId);
                            simis.put("dWorker", getLocalIP());
                            simis.put("eDate", simi.get("eDate").toString());
                            if( simi.get("tQrcard")!=null ){
                                simis.put("tQrcard", simi.get("tQrcard").toString());
                            }else{
                                simis.put("tQrcard",null);
                            }
                            if( simi.get("tIdentitycard")!=null ){
                                simis.put("tIdentitycard", simi.get("tIdentitycard").toString());
                            }else{
                                simis.put("tIdentitycard",null);
                            }

                            if( simi.get("fImage")!=null ){
                                simis.put("fImage", simi.get("fImage").toString());
                            }else{
                                simis.put("fImage",null);
                            }
                            if( simi.get("temp")!=null ){
                                simis.put("temp", simi.get("temp").toString());
                            }else{
                                simis.put("temp",null);
                            }
                            if( simi.get("autonym")!=null ){
                                simis.put("autonym", simi.get("autonym").toString());
                            }else{
                                simis.put("autonym",null);
                            }
                            listeid.add( simi.get("eId").toString());
                            lists.add(simis);
                        }

                        JSONArray array= JSONArray.parseArray(JSON.toJSONString(lists));
                        //String url = "http://127.0.0.1:5000/register";
                        String url = "https://www.zeantong.com:8082/wechat/activity/Entryrecord";

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Entryrecord",array);
                        //请求，如果失败循环五次
                        JSONObject result =faceService.sendSsm(restTemplate,url,jsonObject,String.class,listeid,aid);

                    }else{
                        System.out.println("无数据要上传跳过");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*20);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "入场记录上传无图片", description  = "")
    @PostMapping("/Entryrecordimg")
    public ResponseResult getpostEntryrecordimg(@RequestBody String aid) throws Exception {


        Date time= new Date();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    Date time= new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateUp = format.format(time);

                    List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
                    List<String> listeid=new ArrayList<>();
                    // 入场记录
                    List<Map<String, Object>> listconet1 = faceService.getEntryrecords(aid,dateUp);
                    //  getImageByActivityIdUP getEntryrecord
                    if(listconet1.size()>0){
                        for (Map<String, Object> simi : listconet1) {
                            Map<String, Object> simis=new HashMap<>();
                            simis.put("eId", simi.get("eId").toString());
                            simis.put("aId",simi.get("aId").toString());
                            simis.put("aName",simi.get("aName").toString());
                            simis.put("vName", simi.get("vName").toString());
                            simis.put("eName", simi.get("eName").toString());
                            String tId=null;
                            if(simi.get("tId")!=null){
                                tId=simi.get("tId").toString();
                            }
                            simis.put("tId", tId);
                            simis.put("dWorker", getLocalIP());
                            simis.put("eDate", simi.get("eDate").toString());
                            if( simi.get("tQrcard")!=null ){
                                simis.put("tQrcard", simi.get("tQrcard").toString());
                            }else{
                                simis.put("tQrcard",null);
                            }
                            if( simi.get("tIdentitycard")!=null ){
                                simis.put("tIdentitycard", simi.get("tIdentitycard").toString());
                            }else{
                                simis.put("tIdentitycard",null);
                            }

                            simis.put("fImage",null);
                            if( simi.get("temp")!=null ){
                                simis.put("temp", simi.get("temp").toString());
                            }else{
                                simis.put("temp",null);
                            }
                            if( simi.get("autonym")!=null ){
                                simis.put("autonym", simi.get("autonym").toString());
                            }else{
                                simis.put("autonym",null);
                            }
                            listeid.add( simi.get("eId").toString());
                            lists.add(simis);
                        }

                        JSONArray array= JSONArray.parseArray(JSON.toJSONString(lists));
                        //String url = "http://127.0.0.1:5000/register";
                        String url = "https://www.zeantong.com:8082/wechat/activity/Entryrecord";

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Entryrecord",array);
                        //请求，如果失败循环五次
                        JSONObject result =faceService.sendSsm(restTemplate,url,jsonObject,String.class,listeid,aid);

                    }else{
                        System.out.println("无数据要上传跳过");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*10);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "入场记录下载", description  = "")
    @PostMapping("/Entryrecorddownload")
    public ResponseResult getpostEntryrecorddownload(@RequestBody String aid) throws Exception {



        Date time= new Date();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {

                    String result = null; //Post请求返回结果
                    String argUrl = "https://www.zeantong.com:8082/Entryrecords/Entryrecorddownloads";
                    /**
                     * 这里数据一般都是JSON
                     *  所在app的 build.gradle 里面引用JSONObject类：api 'com.alibaba:fastjson:1.2.76'
                     *  JSON数据转为String类型传递即可
                     */
                    // 需要传给服务端的数据
                    JSONObject jsonObject = new JSONObject();
                    String ipname= getHostName();
                    String ips=  getLocalIP();
                    System.out.println("ipname:"+ipname +"+++++++++ips:"+ ips);
                    jsonObject.put("aid",aid);
                    jsonObject.put("ips",ips);

                    String param = jsonObject.toJSONString();
                    // 发送post请求
                    result =entersService.Entryrecorddownload(argUrl, param);
                    // 对结果进行处理
                    JSONObject jsobject = null;
                    jsobject = JSONObject.parseObject(result);
                    int  interesting = 0;
                    if(jsobject!=null){
                        JSONArray listiden=  jsobject.getJSONArray("data");
                        if(listiden!=null){
                            for(int i=0;i<listiden.size();i++) {
                                if( listiden.getJSONObject(i).get("eId")!=null){
                                    String eId= listiden.getJSONObject(i).get("eId").toString();
                                    String aId= listiden.getJSONObject(i).get("aId").toString();
                                    String vName=listiden.getJSONObject(i).get("vName").toString();
                                    String eName=listiden.getJSONObject(i).get("eName").toString();
                                    String tId=null;
                                    if(listiden.getJSONObject(i).get("tId")!=null){
                                        tId= listiden.getJSONObject(i).get("tId").toString();
                                    }
                                    String aName= listiden.getJSONObject(i).get("aName").toString();
                                    //               System.out.print("Date:"+listiden.getJSONObject(i).get("eDate").toString());


                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    String eDate = listiden.getJSONObject(i).get("eDate").toString();


                                    String temp=null;
                                    if(listiden.getJSONObject(i).get("temp")!=null){
                                        temp=  listiden.getJSONObject(i).get("temp").toString();
                                    }
                                    String dWorker=null;
                                    if(listiden.getJSONObject(i).get("dWorker")!=null){
                                        dWorker=  listiden.getJSONObject(i).get("dWorker").toString();
                                    }
                                    String tQrcard=null;
                                    if(listiden.getJSONObject(i).get("tQrcard")!=null){
                                        tQrcard= listiden.getJSONObject(i).get("tQrcard").toString();
                                    }
                                    String tIdentitycard=null;
                                    if(listiden.getJSONObject(i).get("tIdentitycard")!=null){
                                        tIdentitycard=  listiden.getJSONObject(i).get("tIdentitycard").toString();
                                    }



                                    String autonym=null;
                                    if(listiden.getJSONObject(i).get("autonym")!=null){
                                        autonym=  listiden.getJSONObject(i).get("autonym").toString();
                                    }



                                    int  seio= entryrecordService.installEntryrecord(eId,aId,vName,eName,tId,aName,eDate,temp,dWorker,tQrcard,tIdentitycard,autonym);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*300);// 这里设定将延时每天固定执行

        return ResponseResult.SUCCESS(1);
    }

    @Authentication(isLogin = false,isRequiredUserInfo = false)
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





    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "江门数据上传", description  = "aid")
    @PostMapping("/posts")
    public ResponseResult getposts(@RequestBody String aid) throws Exception {



        Date time= new Date();
        // System.out.println(time+"-------开始时间--------");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    Date time= new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateUp = format.format(time);
                    // 数据库有的的数据量
                    //int UploadQTIEM=faceService.getUploadQTIAE(aid,dateUp);

                    List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
                    List<Map<String, Object>> listconet = faceService.getImageByActivityIdUP(aid,dateUp);
                    List<String> listeid=new ArrayList<>();
                    if(listconet.size()>0){
                        for (Map<String, Object> simi : listconet) {
                            Map<String, Object> simis=new HashMap<>();
                            String a=null;
                            if( simi.get("fImage")!=null ){
                                a =  simi.get("fImage").toString();
                            }

                            //String a1 = "data:image/png;base64," + a;
                            //时间
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            java.util.Date date= sdf.parse(simi.get("eDate").toString());

                            SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long timestamp = date.getTime();
                            String timestampstr = String.valueOf(timestamp);
                            //身份证
                            String flagstr="0";
                            String tupestr="1";
                            String djid="ych20231028001";
                            simis.put("park_id", "2ea38fda-4fa2-11ee-affe-c81f66ed2833");
                            if( simi.get("tIdentitycard")!=null ){
                                simis.put("license", simi.get("tIdentitycard").toString());
                            }else{
                                simis.put("license","111111111111111111");
                            }

                            simis.put("park", simi.get("aName").toString());
                            simis.put("type", tupestr);
                            simis.put("entertime", timestampstr);
                            simis.put("enter_position",simi.get("eName").toString());
                            simis.put("parktime",timestampstr);
                            simis.put("enter_full_img", a);
                            simis.put("flag", flagstr);
                            simis.put("djid", djid);
                            simis.put("leave_position", simi.get("tQrcard").toString());

                            listeid.add( simi.get("eId").toString());
                            lists.add(simis);
                        }

                        String appid= "lykgyadxhych440703";
                        String times= Timestamp();
                        String secretKey= "phillam123";

                        JSONArray array= JSONArray.parseArray(JSON.toJSONString(lists));


                        String signature= times  +"&"+ appid   +"&"+ secretKey;
                        //String url = "http://127.0.0.1:5000/register";
                        String url = "http://202.104.200.83:8314/api/tccxx/b_pj_tccxxs/addTccxx";

                        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
                        // LinkedMultiValueMap<String,Object> request = new LinkedMultiValueMap<>();


                        // 加密
                        String  hex256= SHACoder.encodeSHA256Hex(signature);

                        //  System.out.println(request);
                        // json数据
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("appid",appid);
                        jsonObject.put("timestamp",times);
                        jsonObject.put("signature", hex256);
                        jsonObject.put("data",array);

                        faceService.sendSsmUP(restTemplate,url,jsonObject,String.class,listeid,aid);
                        //   sendSsmUP(restTemplate,url,jsonObject,String.class,listeid,aid);
                        //String result = restTemplate.postForObject(url,jsonObject,String.class);
                        // JSONObject JSONArray=  JSONObject.parseObject(result);
                        // faceService.getUploadQTIAEW(aid,listeid);

                        // 重试数据上传
                        //
//                                //请求
//                                String result = restTemplate.postForObject(url,jsonObject,String.class);
                    }
                    System.out.println("成功"+new Date()+"+++++++++++++++上传数据:"+listeid.size());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*10);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "惠州人脸数据上传", description  = "")
    @PostMapping("/huiZhouPosts")
    public ResponseResult getPostsForHuizhou(@RequestBody String params) throws Exception {

        /**
         * 方法四：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
         * Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
         * */

        Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制小时
//            calendar.set(Calendar.MINUTE, 0);    // 控制分钟
//            calendar.set(Calendar.SECOND, 0);    // 控制秒


        // Date time = calendar.getTime();//获取当前系统时间
        Date time= new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // System.out.println(time+"-------开始时间--------");

        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    for (int i = 1; i < 35; i++) {

                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = "2023-09-16 18:59:00";
                        Date date1 = ft.parse(time);

                        Date afterDate = new Date(date1.getTime() + 60000 * i);
                        System.out.println(ft.format(afterDate));


                        String time1 = ft.format(afterDate);

                        Date date2 = ft.parse(time1);
                        Date afterDate1 = new Date(date2.getTime() + 60000);
                        String time2 = ft.format(afterDate1);

                        String sqlDate = format.format(getTime3());

                        System.out.println("format : " + time2 + "-------任务执行开始时间--------");
                        String sqlDateformerly = format.format(getTime4());
                        postHuiZhouDate(time2, time1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*60);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }


    public int postHuiZhouDate( String sqlDate,String  sqlDateformerly) throws Exception {

        List<String> lists =new ArrayList<String>();
        String aid="d419dec4-43e7-11ee-ac95-00ffe6920653";
        List<Map<String, Object>> listconet = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);
        String csvHeader = "park_id,license,park,entertime,type,enter_num_id\r\n";
        //图片路径
        String csvFileName = String.valueOf(System.currentTimeMillis());
        String folderPath = "d:/uploadPicture/" + csvFileName;
        if(listconet.size()>0){
            int size = listconet.size();

            //List<Map<String, Object>> list1 = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);
            for (Map<String, Object> simi : listconet) {

                //图片
                /*byte[] bytes = (byte[]) simi.get("fImage");*/
                String a = simi.get("fImage").toString();

                //String a1 = "data:image/png;base64," + a;
                //时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date= sdf.parse(simi.get("eDate").toString());
                SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long timestamp = date.getTime();;
                String timestampstr = String.valueOf(timestamp);

                String  datestr = simi.get("eDate").toString();



                String tupestr="1";
                String djid=simi.get("eId").toString()+".jpg";

                StringBuilder sb = new StringBuilder("");
                //park_id
                sb.append("Black-045011c63f6f432b969004b259163478").append(",");
                //license
                if( simi.get("tIdentitycard")!=null ){
                    sb.append(simi.get("tIdentitycard").toString()).append(",");
                }else{
                    sb.append("111111111111111111").append(",");
                }
                //park
                sb.append(simi.get("aName").toString()).append(",");
                //entertime
                sb.append(datestr).append(",");
                //type
                sb.append(tupestr).append(",");
                //enter_num_id
                sb.append(djid);

                base642Image(folderPath, simi.get("eId").toString(), a);
                lists.add(sb.toString());
            }
        }
        if(!lists.isEmpty()){
            //文件名称
            String fileName = System.currentTimeMillis() + ".csv";
            String uploadFile = "d:/uploadFile";
            //写文件头
            File headFile = FileUtil.writeUtf8String(csvHeader,uploadFile + "/" + fileName);
            //写文件内容
            File file = FileUtil.appendLines(lists,headFile,"UTF-8");
            //FileUtils.writeStringToFile(new File("D:/1.data/demo.csv"), Json2Csv(jsonstr));
            String username = "admin";
            String password = "123456";
            //获取token
            String resultStr = this.getToken(username,password);
            String token = this.getTokeyValue(resultStr);
            String uploadUrl = baseUrl + "/importInfo/cvs";
            //上传文本 文件
            resultStr = this.uploadAttach(uploadUrl,token,file);
            //上传结果
            this.getJsonValue(resultStr,"success");
            //读取目录图片
            // 遍历的文件夹路径
            // 使用FileUtil工具类的loopFiles方法获取文件夹内的所有文件
            List<File> fileList = FileUtil.loopFiles(folderPath);
            String uploadImage = baseUrl + "/importInfo/picture";
            // 遍历文件列表并上传图片
            for (File f : fileList) {
                //System.out.println(file.getAbsolutePath());
                //上传文件
                resultStr = this.uploadAttach(uploadImage,token,f);
                //图片上传结果
                this.getJsonValue(resultStr,"success");
            }

        }

       /* ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());*/

        return 0;
    }





    public String Timestamp(){
        // 创建一个Calendar对象
        Calendar calendar = Calendar.getInstance();
        // 获取当前时间
        java.util.Date date= calendar.getTime();

        // util.date转sql date
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        // 将时间转换为时间戳
        long timestamp = sqlDate.getTime();

        // 输出时间戳（秒）
//        long timestampSeconds = timestamp / 1000;
//        System.out.println("当前时间戳（h秒）：" + timestampSeconds);

        //输出时间戳（毫秒）
        // System.out.println("当前时间戳（h秒）：" + timestamp);

        return String.valueOf(timestamp);
    }



    /**
     *
     * @description: 一秒钟前
     * @author: Jeff
     * @date: 2019年12月20日
     * @return
     */
    private static Date getTime1() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -90);
        return cal.getTime();
    }

    private static Date getTime2() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -100);
        return cal.getTime();
    }
    private static Date getTime3() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -120);
        return cal.getTime();
    }
    private static Date getTime4() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -180);
        return cal.getTime();
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    public String getToken(String username, String password) {
        try {
            String loginUrl = baseUrl + "/sys/login";

            //请求头设置为MediaType.MULTIPART_FORM_DATA类型
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            //构建请求体
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("username",username);
            requestBody.add("password",password);
            // HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",username);
            jsonObject.put("password",password);
            //直接调用远程接口
            ResponseEntity<String> entity = restTemplate.postForEntity(loginUrl,jsonObject, String.class);
            String responseValue = (String)entity.getBody();
            log.info("---->token响应值为：" + responseValue);
            Map map = JsonUtil.readValue(responseValue,Map.class);

            return responseValue;
        } catch (Exception e) {
            log.error("获取token接口调用失败",e);
        }
        return "";
    }

    public String uploadAttach(String uploadUrl, String token, File img) {
        try {
            //String uploadAttach = "http://xxx/uploadAttach";
            FileInputStream fileInputStream=new FileInputStream(img);
            //请求头设置为MediaType.MULTIPART_FORM_DATA类型
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            requestHeaders.add("X-Access-Token", token);
            //构建请求体
            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            CommonInputStreamResource commonInputStreamResource = null;
            try {
                commonInputStreamResource = new CommonInputStreamResource(fileInputStream, img.length(), img.getName());
            } catch (Exception e) {
                log.error("文件输入流转换错误",e);
            }

            requestBody.add("file",commonInputStreamResource);
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
            //直接调用远程接口
            ResponseEntity<String> entity = restTemplate.postForEntity(uploadUrl,requestEntity, String.class);
            String responseValue = (String)entity.getBody();
            log.info("---->文本 文件上传响应值为：" + responseValue);
            return responseValue;
        } catch (Exception e) {
            log.error("上传文件接口调用失败",e);
        }
        return "";
    }

    static class CommonInputStreamResource extends InputStreamResource {
        private long length;
        private String fileName;
        public CommonInputStreamResource(InputStream inputStream, long length, String fileName) {
            super(inputStream);
            this.length = length;
            this.fileName = fileName;
        }

        /**
         * 覆写父类方法
         * 如果不重写这个方法，并且文件有一定大小，那么服务端会出现异常
         * {@code The multi-part request contained parameter data (excluding uploaded files) that exceeded}
         */
        @Override
        public String getFilename() {
            return fileName;
        }

        /**
         * 覆写父类 contentLength 方法
         * 因为 {@link org.springframework.core.io.AbstractResource#contentLength()}方法会重新读取一遍文件，
         * 而上传文件时，restTemplate 会通过这个方法获取大小。然后当真正需要读取内容的时候，发现已经读完，会报如下错误。
         */
        @Override
        public long contentLength() {
            long estimate = length;
            return estimate == 0 ? 1 : estimate;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    private String getJsonValue(String json, String key){
        try {
            Map map = JsonUtil.readValue(json,Map.class);
            Object obj =  map.get(key);
            return null != obj ? obj.toString() : "";
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return "-1";
        }
    }
    private String getTokeyValue(String json){
        try {
            Map map = JsonUtil.readValue(json,Map.class);
            Object obj =  map.get("result");
            if(null != obj){
                if (obj instanceof Map) {
                    Map o = (Map) obj;
                    Object oo = map.get("X-Access-Token");
                    return (null != oo) ? oo.toString():"";
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return "-1";
        }
        return "";
    }

    public String Json2Csv(String jsonstr) throws JSONException {
        org.json.JSONArray jsonArray = new org.json.JSONArray(jsonstr);
        String csv = CDL.toString(jsonArray);
        return csv;
    }

    /**
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     * @param file base64编码字符串
     * @param path 图片路径-具体到文件
     * @param namepath 图片名
     * @return
     */
    public static String generateImage(String file, String path,String namepath, HttpServletRequest request) {
        // 解密
        try {
            // 项目绝对路径
            String savePath = request.getSession().getServletContext().getRealPath("upload");
            // 图片分类路径+图片名+图片后缀
            String imgClassPath = path.concat(namepath).concat(".jpg");
            // 解密
            Base64.Decoder decoder = Base64.getDecoder();
            // 去掉base64前缀 data:image/jpeg;base64,
            file = file.substring(file.indexOf(",", 1) + 1, file.length());
            byte[] b = decoder.decode(file);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 保存图片
            OutputStream out = new FileOutputStream(savePath.concat(imgClassPath));
            out.write(b);
            out.flush();
            out.close();
            // 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
            return imgClassPath;
        } catch (IOException e) {
            return null;
        }
    }

    private void base642Image(String path, String fileName, String str){
        try {
            // 解密
            //byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            Base64.Decoder decoder=Base64.getDecoder();
            byte [] b =decoder.decode(str);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //文件夹不存在则自动创建
            File tempFile = new File(path + "/" + fileName+ ".jpg");
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);

            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
