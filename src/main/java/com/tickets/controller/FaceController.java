package com.tickets.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.EntersService;
import com.tickets.service.EntryrecordService;
import com.tickets.service.FaceService;
import com.tickets.service.VenueActiviesService;
import com.tickets.utils.*;
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

import javax.net.ssl.*;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.zip.GZIPOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

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


    private final static String DES = "DES";

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
     * 获取当前系统CPU序列，可区分linux系统和windows系统
     */
    public static String getCpuId() throws Exception {
        String cpuId;
        // 获取当前操作系统名称
        String os = System.getProperty("os.name");
        os = os.toUpperCase();
        System.out.println(os);

        // linux系统用Runtime.getRuntime().exec()执行 dmidecode -t processor 查询cpu序列
        // windows系统用 wmic cpu get ProcessorId 查看cpu序列
        if ("LINUX".equals(os)) {
            cpuId = getLinuxCpuId("dmidecode -t processor | grep 'ID'", "ID", ":");
        } else {
            cpuId = getWindowsCpuId();
        }
        return cpuId.toUpperCase().replace(" ", "");
    }

    /**
     * 获取linux系统CPU序列
     */
    public static String getLinuxCpuId(String cmd, String record, String symbol) throws Exception {
        String execResult = executeLinuxCmd(cmd);
        String[] infos = execResult.split("\n");
        for (String info : infos) {
            info = info.trim();
            if (info.indexOf(record) != -1) {
                info.replace(" ", "");
                String[] sn = info.split(symbol);
                return sn[1];
            }
        }
        return null;
    }

    public static String executeLinuxCmd(String cmd) throws Exception {
        Runtime run = Runtime.getRuntime();
        Process process;
        process = run.exec(cmd);
        InputStream in = process.getInputStream();
        BufferedReader bs = new BufferedReader(new InputStreamReader(in));
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[8192];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        in.close();
        process.destroy();
        return out.toString();
    }

    /**
     * 获取windows系统CPU序列
     */
    public static String getWindowsCpuId() throws Exception {
        Process process = Runtime.getRuntime().exec(
                new String[]{"wmic", "cpu", "get", "ProcessorId"});
        process.getOutputStream().close();
        Scanner sc = new Scanner(process.getInputStream());
        sc.next();
        String serial = sc.next();
        return serial;
    }


    @Authentication(isLogin = true, isRequiredUserInfo = true)
    @Operation(summary  = "查询活动")
    @PostMapping ("/search")
    public ResponseResult search(@RequestBody String aid) throws Exception {

        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        List<Map<String, Object>> list=new ArrayList<>();
        String url = "https://zeantong.com:8082/wechat/activity/openActivies";

        String ips=  getCpuId();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aid",aid);
        String param = jsonObject.toJSONString();
        String result = HttpUtils.postWithJson(url, null, param, null);
        JSONObject JSONArray=  JSONObject.parseObject(result);


        com.alibaba.fastjson.JSONArray listiden=  JSONArray.getJSONArray("data");

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

        com.alibaba.fastjson.JSONArray  listiden=  Array.getJSONArray("data");
        boolean a=false;


        if(listiden!=null){
            if(listiden.size() == 2){
                List listms=new ArrayList();

                com.alibaba.fastjson.JSONArray  listm=listiden.getJSONArray(1);

                for(int i=0;i<listm.size();i++) {
                    AManagementDto aManagementDto=new AManagementDto();
                    aManagementDto.setAId( listm.getJSONObject(i).get("aId").toString());
                    aManagementDto.setEId(listm.getJSONObject(i).get("eId").toString());
                    aManagementDto.setEName(listm.getJSONObject(i).get("eName").toString());
                    aManagementDto.setENote(listm.getJSONObject(i).get("eNote").toString());
                    aManagementDto.setEEnable(1);
                    listms.add(aManagementDto);
                }
                venueActiviesService.addAM(listms);

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

        com.alibaba.fastjson.JSONArray listiden=  Array.getJSONArray("data");
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
                String AType= entersService.selectAcitType(aid);

                if("展会".equals(AType)){
                    // 本地没有票务信息，实时从云端下载
                    try {
                        int QueryInt = faceService.Queryquantity(aid);
                        String url = "https://zeantong.com:8082/Entryrecords/instead";
                        String ips=  getCpuId();
                        // json数据的方式请求
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("aid",aid);
                        jsonObject.put("ips",ips);
                        JSONObject jsobject =entersService.sendinstead(restTemplate,url,jsonObject,String.class);
                        int  interesting = 0;

                        com.alibaba.fastjson.JSONArray listiden=  jsobject.getJSONArray("data");
                        if(listiden!=null){
                            List list=new ArrayList();
                            for(int i=0;i<listiden.size();i++) {
                                TicketingDlsDto ticketingDlsDto=new TicketingDlsDto();

                                String tId= listiden.getJSONObject(i).get("tId").toString();
                                String taId= listiden.getJSONObject(i).get("taId").toString();
                                String tIdentitycard=null;
                                if(listiden.getJSONObject(i).get("tIdentitycard")!=null){
                                    tIdentitycard=  listiden.getJSONObject(i).get("tIdentitycard").toString();
                                }
                                String tQrcard=null;
                                if(listiden.getJSONObject(i).get("tQrcard")!=null){
                                    tQrcard=  listiden.getJSONObject(i).get("tQrcard").toString();
                                }
                                String tRealname=null;
                                if(listiden.getJSONObject(i).get("tRealname")!=null){
                                    tRealname=  listiden.getJSONObject(i).get("tRealname").toString();
                                }
                                String phone=null;
                                if(listiden.getJSONObject(i).get("phone")!=null){
                                    phone=  listiden.getJSONObject(i).get("phone").toString();
                                }


                                ticketingDlsDto.setTId(tId);
                                ticketingDlsDto.setTaId(taId);
                                ticketingDlsDto.setTQrcard(tQrcard);
                                ticketingDlsDto.setTIdentitycard(tIdentitycard);
                                ticketingDlsDto.setTRealname(tRealname);
                                ticketingDlsDto.setPhone(phone);
                                list.add(ticketingDlsDto);
                            }

                            interesting=list.size();
                            if(interesting>0){
                                entryrecordService.installticketing(list);
                                System.out.println("从云端新添加了："+interesting+"数据+++++++++++"+new Date() );
                            }else{
                                System.out.println("展会无数据+++++++++++"+new Date() );
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    // 身份证信息下载
                    try {
                        int QueryInt = faceService.Queryquantity(aid);
                        String url = "https://zeantong.com:8082/wechat/activity/applet";
                        String ips = getCpuId();
                        // json数据的方式请求
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("aid", aid);
                        jsonObject.put("ips", ips);
                        JSONObject jsobject = entersService.sendSsmapplet(restTemplate, url, jsonObject, String.class);

//                    //请求
//                    String result = restTemplate.postForObject(url,jsonObject,String.class);
//                    //获取返回的身份证信息
//                    JSONObject jsobject =  JSONObject.parseObject(result);

                        int interesting = 0;

                        com.alibaba.fastjson.JSONArray listiden = jsobject.getJSONArray("data");
                        if (listiden != null) {
                            for (int i = 0; i < listiden.size(); i++) {
                                String tId = listiden.getJSONObject(i).get("tId").toString();
                                String tIdentitycard = listiden.getJSONObject(i).get("tIdentitycard").toString();
                                String tRealname = listiden.getJSONObject(i).get("tRealname").toString();
                                interesting = faceService.upquantity(tId, tIdentitycard, tRealname);
                                System.out.println("身份证成功" + new Date() + "+++++++++" + listiden.size());
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                // 下载入场记录
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
                    String ips=  getCpuId();
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

                        com.alibaba.fastjson.JSONArray listiden=  jsobject.getJSONArray("data");
                        if(listiden!=null){
                            for(int i=0;i<listiden.size();i++) {
                                if( listiden.getJSONObject(i).get("eId")!=null){
                                    String eId= listiden.getJSONObject(i).get("eId").toString();
                                    String aId= listiden.getJSONObject(i).get("aId").toString();
                                    String vName=null;
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
        }, time, 1000*10);// 这里设定将延时每天固定执行


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


                        com.alibaba.fastjson.JSONArray  array=com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(lists));
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

                    String tid=  simi.get("tid").toString();
                    String wtRealname=null;
                    if(simi.get("wtRealname")!=null){
                        wtRealname=simi.get("wtRealname").toString();
                    }
                    String wtIdentitycard=null;
                    if(simi.get("wtIdentitycard")!=null){
                        wtIdentitycard=simi.get("wtIdentitycard").toString();
                    }
                    String MZXX=null;
                    if(simi.get("MZXX")!=null){
                        MZXX=simi.get("MZXX").toString();
                    }
                    int interestingon = venueActiviesService.upquantitys(tid,wtIdentitycard,wtRealname,MZXX);
                    interesting +=interestingon;
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
                            simis.put("eName", simi.get("eName").toString());
                            String tId=null;
                            if(simi.get("tId")!=null){
                                tId=simi.get("tId").toString();
                            }
                            simis.put("tId", tId);
                            simis.put("dWorker","1");
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


                        com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(lists));
                        //String url = "http://127.0.0.1:5000/register";
                        String url = "https://www.zeantong.com:8082/wechat/activity/Entryrecord";

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Entryrecord",array);
                        //请求，如果失败循环五次
                        JSONObject result =faceService.sendSsm(restTemplate,url,jsonObject,String.class,listeid,aid);
                        System.out.println("返回的值："+ result);
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
    @Operation(summary = "入场记录上传无图片 被上传公安占用", description  = "")
    @PostMapping("/Entryrecordimg")
    public ResponseResult getpostEntryrecordimg(@RequestBody String aid) throws Exception {


        Date time= new Date();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                // 票务入场记入上传（无照片）
                try {

                    Date time= new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateUp = format.format(time);


                    List<String> listeid=new ArrayList<>();
                    System.out.println("开始查询:"+new Date() );
                    // 入场记录
                    List<Map<String, Object>> listconet1 = faceService.getEntryrecordsGA(aid,dateUp);

                    System.out.println("查询结束开始发送:"+new Date() +"listconet1.size():"+listconet1.size());
                    if(listconet1.size()>0){
                        for (Map<String, Object> simi : listconet1) {
                            listeid.add( simi.get("ID").toString());
                        }

                       // JSONArray objects = JSONUtil.parseArray(listconet1);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("performId", aid+"01");
                        jsonObject.put("projectId", aid);
                        jsonObject.put("tickets", listconet1);
                        String constants = null;
                        try {

                            String str =jsonObject.toString();

                            constants = jiaMiAndCompress(str, CONSTANTS.KEY);
                         // constants=objects.toString();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        //System.out.println(constants);
                        //  String url="https://39.99.237.255:33633/police/testApi";
                        //    String url ="https://39.99.237.255:33633/police/admissionInfo";
                        String url = "https://125.95.19.6:33633/police/ticketChecked";
                        String response = sendPostRequest(url, constants,aid,listeid,faceService);
                        System.out.println("Response: " + response);
                        // JavaHttpClient(url, constants,aid,listeid,faceService);

                        //  HttpsUtil.doPostToJson(url, constants);



/*                        List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
                        com.alibaba.fastjson.JSONArray array=  com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(listconet1));
                        String PWD_KEY_TEST = CONSTANTS.KEY;
                        String cont = array.toString();
                        String cont2 = jieMiAndDecompression(jiaMiAndCompresss(cont, PWD_KEY_TEST), PWD_KEY_TEST);
                        String cont3 =jiaMiAndCompresss(cont, PWD_KEY_TEST);
                        if (cont.equals(cont2)) {
                          //  System.out.println("比对成功:" + cont2);
                           // System.out.println("cont3:" + cont3);
                            String url = "http://39.99.237.255:33633/police/admissionInfo";
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("content", cont3);
                            //System.out.println("cont3:"+cont3);
                            //System.out.println("jsonObject:"+jsonObject);
                            //请求，如果失败循环五次
                            JSONObject result = faceService.sendSsm(restTemplate, url, jsonObject, String.class, listeid, aid);
                        }else{
                            System.out.println("比对失败:");
                        }*/


                    }else{
                        System.out.println("无数据公安数据要上传跳过");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                // 工作证入场记入上传
            }
        }, time, 1000*60);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }



    public static void JavaHttpClient(String url, String payload,String aid,List<String> listeid,FaceService faceService) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        try {
            // 调用禁用SSL验证的方法
            HttpsUtil.disableSSLVerification();

            // 创建HttpClient实例
            HttpClient client = HttpClient.newBuilder()
                    .sslContext(createInsecureSslContext())
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            // 创建HttpRequest实例
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            // 发送请求并接收响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 打印响应状态码和响应体
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static SSLContext createInsecureSslContext() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String sendPostRequest(String url, String postData,String aid,List<String> listeid,FaceService faceService) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        // 创建URL对象
        URL apiUrl = new URL(url);
        // 打开URL连接
       // HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
        // 设置请求方法为POST
        connection.setRequestMethod("POST");
        // 设置请求头部信息
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager[] tm = { new MyX509TrustManager() };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());

        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        connection.setSSLSocketFactory(ssf);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("accept-encoding","gzip, deflate, br");
        // 允许输入输出流
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setHostnameVerifier(new HostnameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        // 获取输出流，将数据写入请求体
        OutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(postData.toString().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();
        // 发送请求并获取响应
        try {
            // 尝试向服务器写入数据的代码
            int responseCode = connection.getResponseCode();
            if(responseCode==200){
                faceService.getUploadQTIAEW(aid,listeid);
                System.out.println("成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
            }
           //  System.err.println("responseCode: " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing to server: " + e.getMessage());
            // 可以添加更多的日志信息，帮助调试
        }

        // 读取响应数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        // 关闭连接
        connection.disconnect();
        // 返回响应数据
        return response.toString();
    }

    //处理明文，压缩，加密，在压缩
    public static String jiaMiAndCompress(String cont, String PWD_KEY_TEST) throws Exception {
//        System.out.println("明文长度:" + cont.length());
// 第一次压缩
        cont = compress(cont);
//        System.out.println("压缩后：" + cont.length());
// 第一次加密
        cont = encrypt(cont, PWD_KEY_TEST);
//        System.out.println("加密长度：" + cont.length());
// 第二次压缩
        cont = compress(cont);
//        System.out.println("再压缩:" + cont.length());

        return cont;
    }
    // 压缩
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes("GB18030"));
        gzip.close();
        return out.toString("ISO-8859-1");
    }


/**
     * Description 根据键值进行加密 第一步
     *
     * @param data
     * @param key
     * 加密键byte数组
     * @return
     * @throws Exception
     */

    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes("GB18030"), key.getBytes("GB18030"));
       Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(bt);
        return encoded;
    }


    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
// 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

// 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

// Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

// 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }






//处理明文，压缩，加密，在压缩
    public static String jiaMiAndCompresss(String cont, String PWD_KEY_TEST) throws Exception {
    // System.out.println("明文长度:" + cont.length());
    // 第一次压缩
        cont = ZipUtil2.compress(cont);
    // System.out.println("压缩后：" + cont.length());
    // 第一次加密
        cont = DesUtil.encrypt(cont, PWD_KEY_TEST);
    // System.out.println("加密长度：" + cont.length());
    // 第二次压缩
        cont = ZipUtil2.compress(cont);
    // System.out.println("再压缩:" + cont.length());

        return cont;
    }

    //处理密文，解压缩，解密，在解压缩
    public static String jieMiAndDecompression(String cont, String PWD_KEY_TEST) throws Exception {
    // System.out.println("-----------------------------");
    // System.out.println("密文长度:" + cont.length());

    // 第一次解压缩
        cont = ZipUtil2.uncompress(cont);
    // System.out.println("解压缩：" + cont.length());

    // 第一次解密
        cont = DesUtil.decrypt(cont, PWD_KEY_TEST);
    // System.out.println("解密后：" + cont.length());

    // 第二次解压缩
        cont = ZipUtil2.uncompress(cont);
    // System.out.println("再解压：" + cont.length());

        return cont;
    }




    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @Operation(summary = "入场记录上传无图片", description  = "")
    @PostMapping("/Entryrecordimgs")
    public ResponseResult getpostEntryrecordimgs(@RequestBody String aid) throws Exception {


        Date time= new Date();
        Timer timer = new Timer();

        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                // 票务入场记入上传（无照片）
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
                            simis.put("eName", simi.get("eName").toString());
                            String tId=null;
                            if(simi.get("tId")!=null){
                                tId=simi.get("tId").toString();
                            }
                            simis.put("tId", tId);
                            simis.put("dWorker", "1");
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


                        com.alibaba.fastjson.JSONArray array= com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(lists));
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
                // 工作证入场记入上传
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


                        com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(lists));
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
                    String ips=  getCpuId();
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

                        com.alibaba.fastjson.JSONArray listiden=  jsobject.getJSONArray("data");
                        if(listiden!=null){
                            for(int i=0;i<listiden.size();i++) {
                                if( listiden.getJSONObject(i).get("eId")!=null){
                                    String eId= listiden.getJSONObject(i).get("eId").toString();
                                    String aId= listiden.getJSONObject(i).get("aId").toString();
                                    String vName=null;
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


                        com.alibaba.fastjson.JSONArray array=com.alibaba.fastjson.JSONArray.parseArray(JSON.toJSONString(lists));


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
    public ResponseResult getPostsForHuizhou(@RequestBody String aid) throws Exception {

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
                    System.out.println("惠州开始"+new Date());
                    // 入场信息
                    // postHuiZhouDate(aid);
                    // 票务信息
                    postHuiZhouticketDate(aid);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*2700);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    public int postHuiZhouticketDate( String aid) throws Exception {
        List<String> lists =new ArrayList<String>();
        List<Map<String, Object>> listconet = faceService.getImageByActivityticketId(aid,null,null);
        String csvHeader = "\"park_id\",\"park\",\"venueName\",\"qrCode\",\"bindUserName\",\"bindCard\",\"bindPhone\",\"floorName\",\"standName\",\"seatRow\",\"seatCol\"";
        //图片路径park_id	park	venueName		qrCode	bindUserName	bindCard	bindPhone	floorName	standName	seatRow	seatCol
        String csvFileName = String.valueOf(System.currentTimeMillis());
        String folderPath = "d:/uploadPicture/" + csvFileName;
        List<String> listeid=new ArrayList<>();
        if(listconet.size()>0){
            for (Map<String, Object> simi : listconet) {

                StringBuilder sb = new StringBuilder("");
                sb.append("\"");
                sb.append(simi.get("park_id").toString()).append("\",");
                sb.append("\"");
                sb.append(simi.get("park").toString()).append("\",");
                sb.append("\"");
                sb.append(simi.get("venueName").toString()).append("\",");
                sb.append("\"");
                sb.append(simi.get("qrCode").toString()).append("\",");
                //身份证姓名
                sb.append("\"");
                if( simi.get("bindUserName")!=null ){
                    sb.append(simi.get("bindUserName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //身份证信息
                sb.append("\"");
                if( simi.get("bindCard")!=null ){
                    sb.append(simi.get("bindCard").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //身份证 民族
                sb.append("\"");
                if( simi.get("bindPhone")!=null ){
                    sb.append(simi.get("bindPhone").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                sb.append("\"");
                if( simi.get("floorName")!=null ){
                    sb.append(simi.get("floorName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务区域
                sb.append("\"");
                if( simi.get("standName")!=null ){
                    sb.append(simi.get("standName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务 排
                sb.append("\"");
                if( simi.get("seatRow")!=null ){
                    sb.append(simi.get("seatRow").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务 号
                sb.append("\"");
                if( simi.get("seatCol")!=null ){
                    sb.append(simi.get("seatCol").toString()).append("\"");
                }else{
                    sb.append("_").append("\"");
                }
                lists.add(sb.toString());
            }
        }else{
            System.out.println("惠州无数据要上上传"+new Date());
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
            String username = "hzych20240601";
            String password = "hzych@Hz0601";
            //获取token
            String resultStr = this.getToken(username,password);
            String token = this.getTokeyValue(resultStr);

            String uploadUrl = baseUrl + "/api/webApi/importhd/cvs";

            //上传文本 文件
            resultStr = this.uploadAttach(uploadUrl,token,file);

            Map map = JsonUtil.readValue(resultStr,Map.class);
            Object obj =  map.get("code");
            // System.out.println("上传文本obj"+obj.toString());
            if("200".equals(obj.toString())){
                faceService.getUploadQTIAEW(aid,listeid);
                System.out.println("惠州成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
            }

            //System.out.println("上传文本"+new Date() +"+++++++++resultStr："+ resultStr);
            //上传结果
            // this.getJsonValue(resultStr,"success");

        }

     /*   if(listeid.size()>0){
            faceService.getUploadQTIAEW(aid,listeid);
            System.out.println("惠州成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
        }*/
       /* ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());*/

        return 0;
    }

    public int postHuiZhouDate( String aid) throws Exception {
        List<String> lists =new ArrayList<String>();
        List<Map<String, Object>> listconet = faceService.getImageByActivityId(aid,null,null);
        String csvHeader = "\"park_id\",\"park\",\"venueName\",\"checkPass\",\"bindUserName\",\"bindCard\",\"mzxx\",\"hjdxx\",\"floorName\",\"standName\",\"seatRow\",\"seatCol\",\"entryTime\",\"checkCertType\",\"enter_num_id\"";
        //图片路径
        String csvFileName = String.valueOf(System.currentTimeMillis());
        String folderPath = "d:/uploadPicture/" + csvFileName;
        List<String> listeid=new ArrayList<>();
        if(listconet.size()>0){
            for (Map<String, Object> simi : listconet) {
                listeid.add( simi.get("ID").toString());
                //图片
                /*byte[] bytes = (byte[]) simi.get("fImage");*/


                String a = simi.get("bindPhoto").toString();
                //String a1 = "data:image/png;base64," + a;
                String djid=simi.get("ID").toString()+".jpg";

                StringBuilder sb = new StringBuilder("");
                //设备ID
                sb.append("\"");
                sb.append(simi.get("deviceCode").toString()).append("\",");
                
                //活动名称
                sb.append("\"");
                sb.append("2024杨千嬅巡回演唱会-惠州站").append("\",");
                //活动场馆

                sb.append("\"");
                if( simi.get("venueName")!=null ){
                    sb.append(simi.get("venueName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
               // sb.append("惠州奥林匹克体育场").append("\",");
                //活动入口
                sb.append("\"");
                sb.append(simi.get("checkPass").toString()).append("\",");
                //身份证姓名
                sb.append("\"");
                if( simi.get("bindUserName")!=null ){
                    sb.append(simi.get("bindUserName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //身份证信息
                sb.append("\"");
                if( simi.get("bindCard")!=null ){
                    sb.append(simi.get("bindCard").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //身份证 民族
                sb.append("\"");
                if( simi.get("mzxx")!=null ){
                    sb.append(simi.get("mzxx").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //身份证地址
                sb.append("\"");
                if( simi.get("hjdxx")!=null ){
                    sb.append(simi.get("hjdxx").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务楼层
                sb.append("\"");
                if( simi.get("floorName")!=null ){
                    sb.append(simi.get("floorName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务区域
                sb.append("\"");
                if( simi.get("standName")!=null ){
                    sb.append(simi.get("standName").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务 排
                sb.append("\"");
                if( simi.get("seatRow")!=null ){
                    sb.append(simi.get("seatRow").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //票务 号
                sb.append("\"");
                if( simi.get("seatCol")!=null ){
                    sb.append(simi.get("seatCol").toString()).append("\",");
                }else{
                    sb.append("_").append("\",");
                }
                //入场时间
                sb.append("\"");
                sb.append(simi.get("entryTime").toString()).append("\",");

                //类型
                sb.append("\"");
                sb.append(simi.get("checkCertType").toString()).append("\",");

                //enter_num_id
                sb.append("\"");
                sb.append(djid);
                sb.append("\"");

                base642Image(folderPath, simi.get("ID").toString(), a);
                lists.add(sb.toString());
            }
        }else{
            System.out.println("惠州无数据要上上传"+new Date());
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
            String username = "hzych20240601";
            String password = "hzych@Hz0601";
            //获取token
            String resultStr = this.getToken(username,password);
            String token = this.getTokeyValue(resultStr);

            String uploadUrl = baseUrl + "/api/webApi/importhd/cvs";

            //读取目录图片
            // 遍历的文件夹路径
            // 使用FileUtil工具类的loopFiles方法获取文件夹内的所有文件
            List<File> fileList = FileUtil.loopFiles(folderPath);
            String uploadImage = baseUrl + "/api/webApi/importhd/picture";
            // 遍历文件列表并上传图片
            for (File f : fileList) {
                //System.out.println(file.getAbsolutePath());
                //上传文件
                resultStr = this.uploadAttach(uploadImage,token,f);

                //图片上传结果
               // this.getJsonValue(resultStr,"success");

            }


            //上传文本 文件
            resultStr = this.uploadAttach(uploadUrl,token,file);

            Map map = JsonUtil.readValue(resultStr,Map.class);
            Object obj =  map.get("code");
           // System.out.println("上传文本obj"+obj.toString());
            if("200".equals(obj.toString())){
                faceService.getUploadQTIAEW(aid,listeid);
                System.out.println("惠州成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
            }

            //System.out.println("上传文本"+new Date() +"+++++++++resultStr："+ resultStr);
            //上传结果
           // this.getJsonValue(resultStr,"success");

        }

     /*   if(listeid.size()>0){
            faceService.getUploadQTIAEW(aid,listeid);
            System.out.println("惠州成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
        }*/
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
            String loginUrl = baseUrl + "/api/sys/apiLogin";

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
            //log.info("---->token响应值为：" + responseValue);
            Map map = JsonUtil.readValue(responseValue,Map.class);

            return responseValue;
        } catch (Exception e) {
            log.error("获取token接口调用失败",e);
        }
        return "";
    }

    public String uploadAttach(String uploadUrl, String token, File img) {
        try {

            FileInputStream fileInputStream=new FileInputStream(img);
            //请求头设置为MediaType.MULTIPART_FORM_DATA类型
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            requestHeaders.add("Content-Type","application/x-www-form-urlencoded charset=utf-8");
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
            //log.info("---->文本 文件上传响应值为：" + responseValue);
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
        String tokey="";
        try {
            Map map = JsonUtil.readValue(json,Map.class);
            Object obj =  map.get("result");
            if(null != obj){
                /*if (obj instanceof Map) {
                    Map o = (Map) obj;
                    Object oo = map.get("X-Access-Token");
                    return (null != oo) ? oo.toString():"";
                }*/
                tokey= (String) ((Map<?, ?>) obj).get("X-Access-Token");
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return "-1";
        }
        return tokey;
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
