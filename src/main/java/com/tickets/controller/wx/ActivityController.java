package com.tickets.controller.wx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.annotations.Authentication;
import com.tickets.dto.*;
import com.tickets.service.*;
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
import java.util.stream.Collectors;

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
    @Autowired
    private TurnoutService turnoutService;

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
    @Authentication(isLogin = true,isRequiredUserInfo = true)
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
    public ResponseResult getbindingaId(@RequestParam String aId,String cardId,String Rname,String scanCode,String BIND_MZXX,String base64Image) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        String i="0";
        String tid=null;
        String tIdentitycard=null;
        String wtid=null;
        String wtIdentitycard=null;


        byte[] DESkey = decryptBASE64("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");// key为abcdefghijklmnopqrstuvwx的Base64编码
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] data = cardId.getBytes("UTF-8");


        String aes= AESUtils.AESEncode("1234567890qwerty",cardId);
        //System.out.println("aes:"+aes.length()+":++aes:"+aes);

         //出去空格
        scanCode = scanCode.chars()
                .filter(c -> !Character.isWhitespace(c))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());

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
        // 验证扫到的二维码是正确的票务二维码

            // 查询是否是有效票务
            List<Map<String,Object>> codelist= ticketingStaffService.getByKeys(scanCode,aId);
            List<Map<String,Object>> codelist1= ticketingStaffService.getByKeys1(scanCode,aId);

            for (Map<String, Object> map : codelist) {
                for (String key : map.keySet()) {
                    if(key.equals("tId") & map.get(key)!=null){
                        tid= map.get(key).toString();
                    }
                    if(key.equals("BIND_CARD") & map.get(key)!=null){
                        tIdentitycard= map.get(key).toString();
                    }
                }
            }

            for (Map<String, Object> map : codelist1) {
                for (String key : map.keySet()) {
                    if(key.equals("wtid") & map.get(key)!=null){
                        wtid= map.get(key).toString();
                    }
                }
            }
            // 查询有没有票务信息、
            // 没有 票务信息
            if((tid == null || tid.length() == 0) &( wtid == null || wtid.length() == 0)  ){
                boolean boll= ticketingStaffService.installwtid(cardId, scanCode,Rname, aId,base64Image,BIND_MZXX);
                if(boll){
                    i=aes;
                }else {
                    i="2";
                }
            }else{

                // 有票务信息就更新信息
                if(tid != null){
                    if(tIdentitycard !=null){
                        if(cardId.equals(tIdentitycard)){
                            boolean boll= ticketingStaffService.update(cardId, Rname, tid,base64Image,BIND_MZXX);
                            if(boll){
                                i=aes;
                            }else {
                                i="2";
                            }
                        }else{
                            i="4";
                        }
                    }else{
                        boolean boll= ticketingStaffService.update(cardId, Rname, tid,base64Image,BIND_MZXX);
                        if(boll){
                            i=aes;
                        }else {
                            i="2";
                        }
                    }


                }else{
                    boolean boll= ticketingStaffService.updatewe(cardId, Rname, wtid,base64Image,BIND_MZXX);
                    if(boll){
                        i=aes;
                    }else {
                        i="2";
                    }
                }
            }



        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();
        myMap.put("codes",i);
        list.add(myMap);
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据id 根据票务绑定身份证信息， 演唱会模式")
    @PostMapping("/bindingin")
    public ResponseResult getbindinginaId(@RequestParam String aId,String cardId,String scanCode) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        String i="0";
        String tid=null;
        String tIdentitycard=null;


        byte[] DESkey = decryptBASE64("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");// key为abcdefghijklmnopqrstuvwx的Base64编码
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] data = cardId.getBytes("UTF-8");


        String aes= AESUtils.AESEncode("1234567890qwerty",cardId);
        //System.out.println("aes:"+aes.length()+":++aes:"+aes);

        //出去空格
        scanCode = scanCode.chars()
                .filter(c -> !Character.isWhitespace(c))
                .mapToObj(c -> String.valueOf((char) c))
                .collect(Collectors.joining());

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



        // 证扫到的二维码是正确的票务二维码
        int ling= ticketingStaffService.getscanCode(scanCode);
        if(ling==0){
            // 查询是否是有效票务
            List<Map<String,Object>> codelist= ticketingStaffService.getByKeysun(scanCode,cardId,aId);

            List<Map<String,Object>> codelist1= ticketingStaffService.getByKeys(scanCode,aId);

            int tcoun= ticketingStaffService.getByKeycardIds(null,aId);



            for (Map<String, Object> map : codelist) {
                for (String key : map.keySet()) {
                    if(key.equals("BIND_CARD") & map.get(key)!=null){
                        tIdentitycard=map.get(key).toString();
                    }
                }
            }
           // System.out.println("解密：" +tcoun+"解密：" +codelist1);
            // 已经验证的身份证信息
            if(cardId.equals(tIdentitycard)){
                i=aes;
            }else if (tcoun>0 && codelist1.isEmpty()){
                i="2";
            }else{
                //没有验证的身份证信息
                i="100";
            }
        }else{
            // 二维码信息不对
            i="7";
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
    public ResponseResult getinquireId(@RequestParam String aId,String cardId,String aType,String scanCode,String tSeatingarea,String tRownumber,String tSeat,String BIND_MZXX) throws Exception {
        // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据
        List<Map<String, Object>> list=null;
       /* if(aType.equals("ticketing")){
            list= ticketingStaffService.getticketing(aId,cardId,scanCode,tSeatingarea,tRownumber,tSeat);
        }else {*/

            list= ticketingStaffService.getenueing(aId,cardId,scanCode,tSeatingarea,tRownumber,tSeat,BIND_MZXX);
        return ResponseResult.SUCCESS(list);
    }

    @Authentication(required = true)
    @Operation(summary = "根据二维码和身份证获取票务信息")
    @PostMapping("/inquireimg")
    public ResponseResult getinquireimg(@RequestParam String eId) throws Exception {

        String  list= ticketingStaffService.getticketing(eId);

        /*    if(list!=null & list.size()>0 ){
            for (int i = 0; i < list.size(); i++) {
                Map map = (Map) list.get(i);
                String a1=null;
                if(map.containsKey("BIND_PHOTO")){
                    byte[]  by= (byte[])map.get("BIND_PHOTO");
                    if(null !=by){
                        String a = new String(by);
                        a1 = a;
                    }
                }
                map.put("BIND_PHOTO", a1);
                list.set(i, map);
            }
        }*/
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据tid清楚票务的身份证信息")
    @PostMapping("/eliminate")
    public ResponseResult geteliminateId(@RequestParam String aId,String tId) throws Exception {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datei=formatter.format(date);
        String BIND_MZXX=null;
        boolean boll= ticketingStaffService.update(null, null, tId,datei, null);
        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();
        String  i="1";
        if(boll){
            i="1";
        }else{
            i="2";
        }
        myMap.put("codes",i);
        list.add(myMap);
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据tid查询票务数量")
    @PostMapping("/attendance")
    public ResponseResult getattendance(@RequestParam String aId) throws Exception {
        // 获取当天入场记录中的所有人数去重
        Map<String, Object> intraFieldCount = turnoutService.getintraFieldCount(aId);
        Map<String, Object> VotesCount = turnoutService.getVotesCount(aId); //活动的总数
        Map<String, Object> intworkCount = turnoutService.getintworkCount(aId);
        int work =0;
        if(intworkCount.get("counts") !=null) {

            work = Integer.parseInt(intworkCount.get("counts").toString());
        }

        int intraField =0;
        if(intraFieldCount!=null){

            intraField=Integer.parseInt(intraFieldCount.get("counts").toString());
        }

        int Votes =0;
        if(VotesCount!=null){

            Votes=Integer.parseInt(VotesCount.get("counts").toString());
        }


        List<Map<String, Object>> list= new ArrayList<>();
        Map<String, Object> myMap = new HashMap<String, Object>();




        // rest.put("Norealname",Norealname.get("Norealname"));
        // rest.put("realname",realname.get("realname"));
        myMap.put("intraField",intraField);
        myMap.put("Votes",Votes);
        myMap.put("work",work);
        list.add(myMap);
        return ResponseResult.SUCCESS(list);


    }

    @Authentication(required = true)
    @Operation(summary = "根据没有票务绑定 展会模式 新增票务")
    @PostMapping("/bindingexhibitioning")
    public ResponseResult getbindingaIdexhibitioning(@RequestParam String aId,String cardId,String Rname,String Phone,String datei,String scanCode) throws Exception {
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


        // 展会添加数据
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
    @Operation(summary = "根据没有票务绑定 展会模式 自带票务信息")
    @PostMapping("/bindingexhibition")
    public ResponseResult getbindingaIdexhibition(@RequestParam String aId,String cardId,String Rname,String Phone,String datei,String scanCode,String BIND_MZXX) throws Exception {
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

        // 查询是否是有效票务
        List<Map<String,Object>> codelist= ticketingStaffService.getByKeys(scanCode,aId);
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

        System.out.println("scanCode:"+scanCode+"cardId:"+cardId+"tIdentitycard:"+tIdentitycard+"tid:"+tid);
        if(cardIdBOO){
           String  tidnu=null;
            List<Map<String,Object>> cardIdlist= ticketingStaffService.getcardId(cardId,aId,tid);
            for (Map<String, Object> map : cardIdlist) {
                for (String key : map.keySet()) {
                    if(key.equals("tId") & map.get(key)!=null){
                        tidnu= map.get(key).toString();
                    }
                }
            }
            // 查询身份证有没有重复
            if((tidnu == null || tidnu.length() == 0)){
                // 说明没有绑定票务信息
                if(tIdentitycard==null){
                    // 绑定身份证和票务信息
                    boolean boll= ticketingStaffService.update(cardId, Rname, tid,datei, BIND_MZXX);
                    if(boll){
                        i=aes;
                    }else {
                        i="2";
                    }
                }else{
                    //绑定票务信息绑定了票务信息
                    // 验证是否是同一人，是返回身份证信息
                    if(cardId.equals(tIdentitycard)){
                        i=aes;
                    }else{
                        //不是同一人，返回错误提示信息
                        i="4";
                    }
                }
            }else{
                if(tidnu.equals(tid)){
                    i=aes;
                }else{
                    // 重复绑定多张
                    i="5";
                }
            }



        }else{
            i="6";
        }
        // 展会添加数据
/*
        if(cardIdBOO){

            boolean boll= ticketingStaffService.installwtidexhibition(cardId,Rname,Phone, aId,datei);
            if(boll){
                i=aes;
            }else {
                i="2";
            }
        }else{
            i="6";
        }*/



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
    @Operation(summary = "根据id获取活动票务标准中的身份证信息，并返回身份证信息  不传输照片   // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/applets")
    public ResponseResult getbindingapplets(@RequestBody String jsonstr) {
        // json传输过来的活动id和已有身份证信息的条数
        JSONObject jsobject =  JSONObject.parseObject(jsonstr);


        String aId= jsobject.getString("aid");
        String ips= jsobject.getString("ips");
        // 根据活动id返有身份证的票务数量
        //int ints = faceService.Queryquantity(aId);

        List<Map<String,Object>> codelist=  ticketingStaffService.getByapplets(aId,ips);

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
            if(listiden.size()>0){
                List listen = new ArrayList();
                List listf = new ArrayList();
                List listi = new ArrayList();
                for(int i=0;i<listiden.size();i++) {
                    if( listiden.getJSONObject(i).get("eId")!=null){
                        String eId= listiden.getJSONObject(i).get("eId").toString();
                        String aId= listiden.getJSONObject(i).get("aId").toString();
                        String eName=listiden.getJSONObject(i).get("eName").toString();
                        String tId=null;
                        if(listiden.getJSONObject(i).get("tId")!=null){
                            tId= listiden.getJSONObject(i).get("tId").toString();
                        }
                        String CHECK_CERT_TYPE= listiden.getJSONObject(i).get("CHECK_CERT_TYPE").toString();

                        String BIND_NAME=null;
                        if(listiden.getJSONObject(i).get("BIND_NAME")!=null){
                            BIND_NAME=  listiden.getJSONObject(i).get("BIND_NAME").toString();
                        }
                        String BIND_CARD=null;
                        if(listiden.getJSONObject(i).get("BIND_CARD")!=null){
                            BIND_CARD= listiden.getJSONObject(i).get("BIND_CARD").toString();
                        }
                        String BIND_GENDER=null;
                        if(listiden.getJSONObject(i).get("BIND_GENDER")!=null){
                            BIND_GENDER=  listiden.getJSONObject(i).get("BIND_GENDER").toString();
                        }
                        String BIND_MZXX=null;
                        if(listiden.getJSONObject(i).get("BIND_MZXX")!=null){
                            BIND_MZXX=  listiden.getJSONObject(i).get("BIND_MZXX").toString();
                        }
                        String BIND_HJDXX=null;
                        if(listiden.getJSONObject(i).get("BIND_HJDXX")!=null){
                            BIND_HJDXX= listiden.getJSONObject(i).get("BIND_HJDXX").toString();
                        }
                        String QR_CODE=null;
                        if(listiden.getJSONObject(i).get("QR_CODE")!=null){
                            QR_CODE=  listiden.getJSONObject(i).get("QR_CODE").toString();
                        }
                        String Device_CODE=null;
                        if(listiden.getJSONObject(i).get("Device_CODE")!=null){
                            Device_CODE=  listiden.getJSONObject(i).get("Device_CODE").toString();
                        }
                        String Device_NAME=null;
                        if(listiden.getJSONObject(i).get("Device_NAME")!=null){
                            Device_NAME= listiden.getJSONObject(i).get("Device_NAME").toString();
                        }

                        String stamp=null;
                        if(listiden.getJSONObject(i).get("stamp")!=null){
                            stamp=  listiden.getJSONObject(i).get("stamp").toString();
                        }
                        String ENTRY_TIME = listiden.getJSONObject(i).get("ENTRY_TIME").toString();



                        //fimge

                        byte[] BIND_PHOTO=null;
                        if(listiden.getJSONObject(i).get("BIND_PHOTO")!=null){
                            BIND_PHOTO = listiden.getJSONObject(i).get("BIND_PHOTO").toString().getBytes();
                        }
                        byte[] BIND_CARD_PHOTO=null;
                        if(listiden.getJSONObject(i).get("BIND_CARD_PHOTO")!=null){
                            BIND_CARD_PHOTO = listiden.getJSONObject(i).get("BIND_CARD_PHOTO").toString().getBytes();
                        }


                        EntertfDto entertfDto =new EntertfDto();
                        entertfDto.setaId(aId);
                        entertfDto.seteId(eId);
                        entertfDto.seteName(eName);
                        entertfDto.settId(tId);
                        entertfDto.setBIND_NAME(BIND_NAME);
                        entertfDto.setBIND_CARD(BIND_CARD);
                        entertfDto.setQR_CODE(QR_CODE);
                        entertfDto.setStamp(stamp);
                        entertfDto.setENTRY_TIME(ENTRY_TIME);
                        entertfDto.setStandby("0");
                        entertfDto.setShunde("0");

                        listen.add(entertfDto);

                        enter_info enter_in=new enter_info();
                        enter_in.seteId(eId);
                        enter_in.setCHECK_CERT_TYPE(CHECK_CERT_TYPE);
                        enter_in.setBIND_GENDER(BIND_GENDER);
                        enter_in.setBIND_MZXX(BIND_MZXX);
                        enter_in.setBIND_HJDXX(BIND_HJDXX);
                        enter_in.setDevice_CODE(Device_CODE);
                        enter_in.setDevice_NAME(Device_NAME);
                        enter_in.setStamp("0");
                        listi.add(enter_in);

                        enter_fimage enter_fimae=new enter_fimage();
                        enter_fimae.seteId(eId);
                        enter_fimae.setaId(aId);
                        enter_fimae.setBIND_PHOTO(BIND_PHOTO);
                        enter_fimae.setBIND_CARD_PHOTO(BIND_CARD_PHOTO);
                        enter_fimae.setStamp("0");
                        enter_fimae.setStandby("0");
                        listf.add(enter_fimae);

                   }
                }

                seio= ticketingStaffService.installEntryrecord(listen,listf,listi);

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
            if(listiden.size()>0) {
                List listen = new ArrayList();
                for (int i = 0; i < listiden.size(); i++) {
                    if (listiden.getJSONObject(i).get("eId") != null) {
                        enter_employee enterEmployee = new enter_employee();


                        String eId = listiden.getJSONObject(i).get("eId").toString();
                        String aId = listiden.getJSONObject(i).get("aId").toString();
                        String tId = null;
                        if (listiden.getJSONObject(i).get("tId") != null) {
                            tId = listiden.getJSONObject(i).get("tId").toString();
                        }
                        String eName = listiden.getJSONObject(i).get("eName").toString();

                        String ENTRY_TIME = listiden.getJSONObject(i).get("ENTRY_TIME").toString();
                        String stamp = listiden.getJSONObject(i).get("stamp").toString();

                        String ENTERID = null;
                        if (listiden.getJSONObject(i).get("ENTERID") != null) {
                            ENTERID = listiden.getJSONObject(i).get("ENTERID").toString();
                        }

                        byte[] FACEPHOTO = null;
                        if (listiden.getJSONObject(i).get("FACEPHOTO") != null) {
                            FACEPHOTO = listiden.getJSONObject(i).get("FACEPHOTO").toString().getBytes();
                        }
                        enterEmployee.seteId(eId);
                        enterEmployee.setaId(aId);
                        enterEmployee.settId(tId);
                        enterEmployee.setENTERID(ENTERID);
                        enterEmployee.seteName(eName);
                        enterEmployee.setENTRY_TIME(ENTRY_TIME);
                        enterEmployee.setStandby("0");
                        enterEmployee.setStamp(stamp);
                        enterEmployee.setFACEPHOTO(FACEPHOTO);

                        listen.add(enterEmployee);
                    }
                }
                ticketingStaffService.installemploy(listen);
            }
        }
        return ResponseResult.SUCCESS("1");
    }

    @Authentication(required = true)
    @Operation(summary = "接受摄像头记录    // RequestParam 表示接受的是param数据 ，RequestBody表示接受的是json数据 ")
    @PostMapping("/camera")
    public ResponseResult getbindcamera(@RequestBody String jsonstr) {
        // json传输过来的活动id 摄像头信息
       //System.out.print("jsonstr:"+jsonstr);

        JSONObject jsobject =  JSONObject.parseObject(jsonstr);
        JSONArray listiden=  jsobject.getJSONArray("camera");
        if(listiden!=null){
            for(int i=0;i<listiden.size();i++) {
                if( listiden.getJSONObject(i).get("teId")!=null){
                    String teId= listiden.getJSONObject(i).get("teId").toString();
//                    String teImage= listiden.getJSONObject(i).get("teImage").toString();
                    String teDate=listiden.getJSONObject(i).get("teDate").toString();
                    String teAisle=listiden.getJSONObject(i).get("teAisle").toString();
                    String teaId= listiden.getJSONObject(i).get("teaId").toString();
                    String teCategory= listiden.getJSONObject(i).get("teCategory").toString();
                    String teMarking= listiden.getJSONObject(i).get("teMarking").toString();

                    int a=ticketingStaffService.installcamera(teId,teDate,teAisle,teaId,teCategory,teMarking);

                }
            }
        }




        return ResponseResult.SUCCESS(listiden.size());


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


}
