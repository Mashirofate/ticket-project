package com.tickets.controller.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.ResponseResult;
import com.tickets.service.EntersService;
import com.tickets.service.FaceService;
import com.tickets.service.TicketingStaffService;
import com.tickets.service.VenueActiviesService;
import com.tickets.utils.AESUtils;
import com.tickets.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Tag(name = "微信用户权限接口")
@RestController
@RequestMapping("/wechat/activity")
public class ActivityController {


    @Autowired
    private EntersService entersService;
    @Autowired
    private FaceService faceService;
    @Autowired
    private VenueActiviesService venueActiviesService;
    @Autowired
    private TicketingStaffService ticketingStaffService;

    /**
     * ECB加密,不要IV
     *
     * @param key  密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("desede");
        deskey = factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] out = cipher.doFinal(data);
        return out;
    }

    /**
     * ECB解密,不要IV
     *
     * @param key  密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("desede");
        deskey = factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] out = cipher.doFinal(data);
        return out;
    }

    /**
     * CBC加密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  明文
     * @return Base64编码的密文
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("desede");
        deskey = factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] out = cipher.doFinal(data);
        return out;
    }
    @Authentication(required = false)
    @Operation(summary = "根据id获取活动信息")
    @GetMapping("/")
    public ResponseResult getByVaId(@RequestParam String aId) {
        return ResponseResult.SUCCESS(venueActiviesService.getByVaId(aId));
    }

    /**
     * CBC解密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("desede");
        deskey = factory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] out = cipher.doFinal(data);
        return out;
    }

    // Base64解码
    public static String encryptBASE64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(data);
        return encode;
    }

    // Base64编码
    public static byte[] decryptBASE64(String data) throws Exception {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] buffer = decoder.decode(data);
        return buffer;
    }

    // required =  false  @GetMapping("/open")
    @Authentication(required = true)
    @Operation(summary = "启用的演唱会")
    @PostMapping("/open")
    public ResponseResult openActivies(@RequestBody String x) {
        String s =x;
      /*  List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMaps = new HashMap<String, Object>();
        myMaps.put("title","调用二维码小程序码");
        myMaps.put("page","getMiniProgramCode");
        List<Map<String, Object>> list1= new ArrayList<>();
        list1.add(myMaps);

        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("title","云函数1");
        myMap.put("tip","安全、免鉴权运行业务代码1");
        myMap.put("showItem",false);
        myMap.put("item",list1);
        list.add(myMap);
        Map<String, Object> myMap1 = new HashMap<String, Object>();
        myMap1.put("title","数据库");
        myMap1.put("tip","安全、免鉴权运行业务代码1");
        myMap1.put("showItem",false);
        myMap1.put("item",list1);
        list.add(myMap1);
        Map<String, Object> myMap2 = new HashMap<String, Object>();
        myMap2.put("title","云存储1");
        myMap2.put("tip","自带CDN加速文件存储1");
        myMap2.put("showItem",false);
        list.add(myMap2);
        return ResponseResult.SUCCESS(list);*/
        return ResponseResult.SUCCESS(venueActiviesService.getOpenActivies());
    }

    @Authentication(required = true)
    @Operation(summary = "启用的活动")
    @PostMapping("/openActivies")
    public ResponseResult openActiviess(@RequestBody String x) {
        String s =x;
        return ResponseResult.SUCCESS(venueActiviesService.getOpenActiviess());

    }

    @Authentication(required = true)
    @Operation(summary = "启用的展会")
    @PostMapping("/openexhibition")
    public ResponseResult openActiviesexhibition(@RequestBody String x) {
        String s =x;
        return ResponseResult.SUCCESS(venueActiviesService.getOpenActiviesex());

    }

    @Authentication(required = true)
    @Operation(summary = "根据id获取活动信息 ，用于本地活动的创建")
    @PostMapping("/toByVaId")
    public ResponseResult getByVaIdde(@RequestBody String jsonstr) {

        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        String aId= jsobject.getString("aid");
        List list =new ArrayList<>();
        list.add(venueActiviesService.getByVaId(aId));
        list.add(venueActiviesService.getemVaId(aId));
        return ResponseResult.SUCCESS(list);
    }

    @Authentication(required = true)
    @Operation(summary = "根据id 根据票务绑定身份证信息， 演唱会模式")
    @PostMapping("/binding")
    public ResponseResult getbindingaId(@RequestParam String aId,String cardId,String Rname,String scanCode,String datei) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        String i="0";
        String tid=null;
        String tIdentitycard=null;
        String wtid=null;
        String wtIdentitycard=null;

        Boolean cardIdBOO=true;
        if(cardId.length()==18){
            // 正确身份证信息
            cardIdBOO=checkIdCardNum(cardId);
        }

        byte[] DESkey = decryptBASE64("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");// key为abcdefghijklmnopqrstuvwx的Base64编码
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] data = cardId.getBytes("UTF-8");


        String aes= AESUtils.AESEncode("1234567890qwerty",cardId);
        //System.out.println("aes:"+aes.length()+":++aes:"+aes);


//        String aes1= AESUtils.AESDecode("1234567890qwerty",aes);
//        System.out.println("aes1:"+aes1.length()+":++aes1解密:"+aes1);

//        System.out.println("ECB模式：");// 当模式为ECB时，IV无用
//        byte[] encode = des3EncodeECB(DESkey, data);
//       // byte[] decode = ees3DecodeECB(DESkey, encode);
//        String encryptBASE64=  encryptBASE64(encode);
//        System.out.println("encryptBASE64:"+encryptBASE64.length());
//        System.out.println("加密：" + encryptBASE64(encode));
//        System.out.println("解密：" + new String(decode, "UTF-8"));
//        System.out.println();

//        System.out.println("CBC模式：");
//        encode = des3EncodeCBC(DESkey, keyiv, data);
//        decode = des3DecodeCBC(DESkey, keyiv, encode);
//        System.out.println("加密：" + encryptBASE64(encode));
//        System.out.println("解密：" + new String(decode, "UTF-8"));



        if(cardIdBOO){
            // 查询是否是有效票务
            List<Map<String,Object>> codelist= ticketingStaffService.getByKeys(scanCode,aId);
            List<Map<String,Object>> codelist1= ticketingStaffService.getByKeys1(scanCode,aId);

            for (Map<String, Object> map : codelist) {
                for (String key : map.keySet()) {
                    if(key.equals("tId") & map.get(key)!=null){
                        tid= map.get(key).toString();
                    }
                    if(key.equals("tIdentitycard") & map.get(key)!=null){
                        tIdentitycard=map.get(key).toString();
                    }
                }
            }

            for (Map<String, Object> map : codelist1) {
                for (String key : map.keySet()) {
                    if(key.equals("wtid") & map.get(key)!=null){
                        wtid= map.get(key).toString();
                    }
                    if(key.equals("wtIdentitycard") & map.get(key)!=null){
                        wtIdentitycard=map.get(key).toString();
                    }
                }
            }
            // 查询有没有票务信息、
            // 没有 票务信息
            if((tid == null || tid.length() == 0) &( wtid == null || wtid.length() == 0)  ){
                String tidnu=null;
                String wtidnu=null;
                List<Map<String,Object>> cardIdlist= ticketingStaffService.getcardId(cardId,aId,tid);
                List<Map<String,Object>> cardIdlist2 = ticketingStaffService.getcardId2(cardId,aId,wtid);
                for (Map<String, Object> map : cardIdlist) {
                    for (String key : map.keySet()) {
                        if(key.equals("tId") & map.get(key)!=null){
                            tidnu= map.get(key).toString();
                        }
                    }
                }

                for (Map<String, Object> map : cardIdlist2) {
                    for (String key : map.keySet()) {
                        if(key.equals("wtid") & map.get(key)!=null){
                            wtidnu= map.get(key).toString();
                        }
                    }
                }
                if((tidnu == null || tidnu.length() == 0) &( wtidnu == null || wtidnu.length() == 0) ){
                    boolean boll= ticketingStaffService.installwtid(cardId, scanCode,Rname, aId,datei);
                    if(boll){
                        i=aes;
                    }else {
                        i="2";
                    }
                }else{
                    i="5";
                }
            }else{
            /*  boolean result = tIdentitycard==null;
                boolean result1 =StringUtils.isBlank(tIdentitycard);
                查询有没有重复的身份证信息*/
                List<Map<String,Object>> cardIdlist= ticketingStaffService.getcardId(cardId,aId,tid);
                List<Map<String,Object>> cardIdlist2 = ticketingStaffService.getcardId2(cardId,aId,wtid);

                String tidnu=null;
                String wtidnu=null;
                for (Map<String, Object> map : cardIdlist) {
                    for (String key : map.keySet()) {
                        if(key.equals("tId") & map.get(key)!=null){
                            tidnu= map.get(key).toString();
                        }
                    }
                }

                for (Map<String, Object> map : cardIdlist2) {
                    for (String key : map.keySet()) {
                        if(key.equals("wtid") & map.get(key)!=null){
                            wtidnu= map.get(key).toString();
                        }
                    }
                }
                // 没有重复的身份证信息
                if((tidnu == null || tidnu.length() == 0) &( wtidnu == null || wtidnu.length() == 0)   ){
                    // 有票单没有身份证就绑定
                    if((tIdentitycard==null || StringUtils.isBlank(tIdentitycard)) & (wtIdentitycard==null || StringUtils.isBlank(wtIdentitycard))){
                        if(tid!=null){
                            boolean boll= ticketingStaffService.update(cardId, Rname, tid,datei);
                            if(boll){
                                i=aes;
                            }else {
                                i="2";
                            }
                        }
                        if(wtid!=null){
                            boolean boll= ticketingStaffService.updatewe(cardId, Rname, wtid,datei);
                            if(boll){
                                i=aes;
                            }else {
                                i="2";
                            }
                        }

                    }else{
                        if(cardId.equals(tIdentitycard)){
                            boolean boll= ticketingStaffService.update(cardId, Rname, tid,datei);
                            if(boll){
                                i=aes;
                            }else {
                                i="2";
                            }
                        }else{
                            i="4";
                        }
                        // 有票单有身份证就比对身份证信息是否一至，一直就绑定
                        if( cardId.equals(wtIdentitycard)){
                            boolean boll= ticketingStaffService.updatewe(cardId, Rname, wtid,datei);
                            if(boll){
                                i=aes;
                            }else {
                                i="2";
                            }
                        }else{
                            i="4";
                        }
                    }
                }else{
                    // 有重复的身份证信息
                    i="5";
                }

            }
        }else{
            i="6";
        }
        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("codes",i);
        list.add(myMap);
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据二维码和身份证获取票务信息")
    @PostMapping("/inquire")
    public ResponseResult getinquireId(@RequestParam String aId,String cardId,String aType,String scanCode) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        List<Map<String, Object>> list=null;
        if(aType.equals("ticketing")){
            list= ticketingStaffService.getticketing(aId,cardId,scanCode);
        }else {
            list= ticketingStaffService.getenueing(aId,cardId,scanCode);

            if(list!=null & list.size()>0 ){
                // e.tIdentitycard as tIdentitycard, e.tQrcard as tQrcard,e.autonym as tRealname, e.fImage   as fImage, e.eDate as Date,t.tSeatingarea as tSeatingarea ,t.tRownumber as tRownumber, t.tSeat as tSeat
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    String a1=null;
                    if(map.containsKey("fImage")){
                        byte[]  by= (byte[])map.get("fImage");
                        if(null !=by){
                            String a = new String(by);
                            a1 = a;
                        }
                    }
                    map.put("fImage", a1);
                    list.set(i, map);
                }
            }
        }

        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据没有票务绑定 展会模式")
    @PostMapping("/bindingexhibition")
    public ResponseResult getbindingaIdexhibition(@RequestParam String aId,String cardId,String Rname,String Phone,String datei) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        String i="0";
        String tid=null;
        String tIdentitycard=null;
        String wtid=null;
        String wtIdentitycard=null;

        Boolean cardIdBOO=true;
        if(cardId.length()==18){
            // 正确身份证信息
            cardIdBOO=checkIdCardNum(cardId);
        }



        String aes= AESUtils.AESEncode("1234567890qwerty",cardId);



        if(cardIdBOO){
            boolean boll= ticketingStaffService.installwtidexhibition(cardId,Rname,Phone, aId,datei);
            if(boll){
                i=aes;
            }else {
                i="2";
            }
        }else{
            i="6";
        }
        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("codes",i);
        list.add(myMap);
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据id获取活动票务标准中的身份证信息，并返回身份证信息    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/applet")
    public ResponseResult getbindingapplet(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);


        String aId= jsobject.getString("aid");
        String ips= jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);

        List<Map<String,Object>> codelist=  ticketingStaffService.getByapplet(aId,ips);

        if(codelist!=null){
            List<String> listeid=new ArrayList<>();
            if(codelist.size()>0){
                for (Map<String, Object> simi : codelist) {
                    listeid.add( simi.get("tId").toString());
                }
                String ipss= ips+",";
                ticketingStaffService.getByappletlisteid(aId,listeid,ipss);
            }
          //  System.out.println("成功+++++++++codelist:"+ codelist.size()+"++++++++下载身份证:"+listeid.size());
        }

        return ResponseResult.SUCCESS(codelist);


    }

    @Authentication(required = true)
    @Operation(summary = "接受上传的入场记录    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/Entryrecord")
    public ResponseResult getbindEntryrecord(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        //System.out.print("jsonstr:"+jsonstr);
    int seio=0;
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        JSONArray listiden=  jsobject.getJSONArray("Entryrecord");
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
//                    System.out.print("Date:"+listiden.getJSONObject(i).get("eDate").toString());


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

                    byte[] fImage=null;
                    if(listiden.getJSONObject(i).get("fImage")!=null){
                        fImage = listiden.getJSONObject(i).get("fImage").toString().getBytes();
                    }

                    seio= ticketingStaffService.installEntryrecord(eId,aId,vName,eName,tId,aName,eDate,temp,dWorker,tQrcard,tIdentitycard,autonym,fImage);
                }
            }
        }




        return ResponseResult.SUCCESS(seio);


    }

    @Authentication(required = true)
    @Operation(summary = "接受上传的工作证入场记录    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/employ")
    public ResponseResult getbindemploy(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        //System.out.print("jsonstr:"+jsonstr);

        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        JSONArray listiden=  jsobject.getJSONArray("Entryrecord");
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
                   // System.out.print("Date:"+listiden.getJSONObject(i).get("eDate").toString());


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String eDate = listiden.getJSONObject(i).get("eDate").toString();


                    String tQrcard=null;
                    if(listiden.getJSONObject(i).get("tQrcard")!=null){
                        tQrcard= listiden.getJSONObject(i).get("tQrcard").toString();
                    }
                    String tIdentitycard=null;


                    ticketingStaffService.installemploy(eId,aId,vName,eName,tId,aName,eDate,tQrcard);
                }
            }
        }




        return ResponseResult.SUCCESS("1");


    }

    @Authentication(required = true)
    @Operation(summary = "接受摄像头记录    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/camera")
    public ResponseResult getbindcamera(@RequestBody String jsonstr) {
        // json传输过来的活动id 摄像头信息
       System.out.print("jsonstr:"+jsonstr);

        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        JSONArray listiden=  jsobject.getJSONArray("camera");
        if(listiden!=null){
            for(int i=0;i<listiden.size();i++) {
                if( listiden.getJSONObject(i).get("teId")!=null){
                    String teId= listiden.getJSONObject(i).get("teId").toString();
                    String teImage= listiden.getJSONObject(i).get("teImage").toString();
                    String teDate=listiden.getJSONObject(i).get("teDate").toString();
                    String teAisle=listiden.getJSONObject(i).get("teAisle").toString();
                    String teaId= listiden.getJSONObject(i).get("teaId").toString();
                    String teCategory= listiden.getJSONObject(i).get("teCategory").toString();
                    String teMarking= listiden.getJSONObject(i).get("teMarking").toString();

                    int a=ticketingStaffService.installcamera(teId,teImage,teDate,teAisle,teaId,teCategory,teMarking);

                }
            }
        }




        return ResponseResult.SUCCESS("1");


    }

    /*
        身份证号码的严格校验

    */
    public boolean checkIdCardNum(String idNum) throws ParseException
    {
        idNum=idNum.toUpperCase(); //将末尾可能存在的x转成X

        String regex="";
        regex+="^[1-6]\\d{5}"; //前6位地址码。后面仍需打表校验

        regex+="(18|19|20)\\d{2}"; //年份。后面仍需校验
        regex+="((0[1-9])|(1[0-2]))"; //月份。后面仍需校验
        regex+="(([0-2][1-9])|10|20|30|31)"; //日期。后面仍需校验

        regex+="\\d{3}"; //3位顺序码

        regex+="[0-9X]"; //检验码。后面仍需验证

        if(!idNum.matches(regex))
            return false;

        //第1，2位(省)打表进一步校验
        int[] d={11,12,13,14,15,
                21,22,23,31,32,33,34,35,36,37,
                41,42,43,
                44,45,46,
                50,51,52,53,53,
                61,62,63,64,65,
                83,81,82};
        boolean flag=false;
        int prov=Integer.parseInt(idNum.substring(0, 2));
        for(int i=0;i<d.length;i++)
            if(d[i]==prov)
            {
                flag=true;
                break;
            }
        if(!flag)
            return false;

        //生日校验：生日的时间不能比当前时间（指程序检测用户输入的身份证号码的时候）晚
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date birthDate=sdf.parse(idNum.substring(6, 14));
        Date curDate=new Date();
        if(birthDate.getTime()>curDate.getTime())
            return false;

        //生日校验：每个月的天数不一样（有的月份没有31），还要注意闰年的二月
        int year=Integer.parseInt(idNum.substring(6, 10));
        int leap=((year%4==0 && year%100!=0) || year%400==0)?1:0;
        final int[] month={0,31,28+leap,31,30,31,30,31,31,30,31,30,31};
        int mon=Integer.parseInt(idNum.substring(10, 12));
        int day=Integer.parseInt(idNum.substring(12, 14));
        if(day>month[mon])
        {
            //System.out.println(day+" "+month[mon]+"\n");
            //System.out.println("---");
            return false;
        }


        //检验码
        if(idNum.charAt(17)!=getLastChar(idNum))
            return false;

        return true;
    }

    /*根据身份证号码的前17位计算校验码*/
    public char getLastChar(String idNum) //由于这个功能比较独立，就分离出来
    {
        final int[] w={0,7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
        final char[] ch={'1','0','X','9','8','7','6','5','4','3','2'}; //这就是为什么一开始将末尾可能存在的x转成X的原因
        int res=0;
        for(int i=0;i<17;i++)
        {
            int t=idNum.charAt(i)-'0';
            res+=(t*w[i+1]);
        }
        return ch[res%11];
    }

    //根据身份证号码的出生日期计算年龄
    public int getYear(String idNum)
    {
        int yearBirth=Integer.parseInt(idNum.substring(6, 10));
        int monBirth=Integer.parseInt(idNum.substring(10, 12));
        int dayBirth=Integer.parseInt(idNum.substring(12, 14));

        Calendar cur=Calendar.getInstance();
        int yearCur=cur.get(Calendar.YEAR);
        int monCur=cur.get(Calendar.MONTH)+1; //不要忘了+1
        int dayCur=cur.get(Calendar.DATE);


        //System.out.println(yearCur+" "+monCur+" "+dayCur);
        int age=yearCur-yearBirth;
        if(monCur<monBirth || (monCur==monBirth && dayCur<dayBirth))
            age--;

        return age;
    }

    //出生的所在那一周是出生那年的第几周
    public int getBirthWeek(String idNum) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date birthDate=sdf.parse(idNum.substring(6, 14));  //默认0时0分0秒
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(birthDate);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    //从出生到现在过去了多少周
    public int getWeeks(String idNum) throws ParseException
    {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date birthDate=sdf.parse(idNum.substring(6, 14));  //默认0时0分0秒
        Date curDate=new Date();
        return  (int)( (curDate.getTime()-birthDate.getTime())/(long)(7*24*60*60*1000) ) ; //不建议把分子转成int，可能会溢出
    }
}
