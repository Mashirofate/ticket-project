package com.tickets.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.tickets.annotations.Authentication;
import com.tickets.dto.FaceSaveDto;
import com.tickets.dto.Page;
import com.tickets.dto.ResponseResult;
import com.tickets.service.FaceService;
import com.tickets.utils.Base64Util;
import com.tickets.utils.SHACoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "人脸接口")
@RestController
@RequestMapping("/fr")
public class FaceController {

    @Autowired
    private FaceService faceService;

    @Autowired
    private RestTemplate restTemplate;

    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "人脸对比条件搜索", notes = "")
    @PostMapping("/search")
    public ResponseResult search(@RequestBody FaceSaveDto faceSaveDto) throws ParseException {
        Page<Map<String, Object>> page = new Page<>();

        if(faceSaveDto.getImage()!=null &  !"".equals(faceSaveDto.getImage())){
            // 将base64的图片数据传化为byte[] 数组
            byte[] bytes = Base64Util.base64ToBytes(faceSaveDto.getImage());

            // 将byte[] 数组传化为imageinfo 用于提取特征数据
            ImageInfo rgbData = ImageFactory.getRGBData(bytes);

            // 获取传入照片的人脸数据
            List<FaceInfo> faceInfoList = faceService.detectFaces(rgbData);

            if(faceInfoList.size()>0){
                // 获取传入照片的人脸特征数据
                byte[] feature = faceService.extractFaceFeaqture(rgbData, faceInfoList.get(0));

                // 返回给前端的数据
                List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

                //相似度
                float passRate =  Float.parseFloat(faceSaveDto.getSimilar());

                // 查询到数据库中所有的人脸数据
                List<Map<String,Object>> arr=faceService.queryFace();

                //遍历所有人脸数据
                for (Map<String, Object> stringObjectMap : arr) {

                    // 提取人脸特征
                    byte[] farr = (byte[]) stringObjectMap.get("fFeature");

                    String str1= new String((byte[]) stringObjectMap.get("fImage"));
                    byte[] bytes2= Base64Util.base64ToBytes(str1);
                    ImageInfo rgbData2 = ImageFactory.getRGBData(bytes2);

                    // 获取传入照片的人脸数据
                    List<FaceInfo> faceInfoListf = faceService.detectFaces(rgbData2);
                    if(faceInfoListf.size()>0){
                        // 提取人脸照片
                        String str = new String((byte[]) stringObjectMap.get("fImage"));

                        // 将人脸照片（beca64）转化为 byte[]
                        byte[] bytes1 = Base64Util.base64ToBytes(str);

                        // 在将照片 （byte[]）转化为 Image数据
                        ImageInfo rgbData1 = ImageFactory.getRGBData(bytes1);

                        //根据两个人脸照片的 ImageInfo数据对比，得出相似度
                        Float similar = faceService.compareFace(rgbData1, rgbData);
                        if(similar!=null){
                            // 根据相似的的设定显示有关信息
                            if (similar > passRate) {
                                // 形似度大于设定的形似的，根据人脸数据中的票务id 去查询出活动名，入场时间和显示人脸数据
                                String eId = stringObjectMap.get("eId").toString();
                                List<Map<String, Object>> list1 = faceService.queryTicketing(eId);
                                for (Map<String, Object> simi : list1) {

                                    String a = new String((byte[]) simi.get("fImage"));


                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    java.util.Date date= sdf.parse(simi.get("eDate").toString());
                                    SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String time = format0.format(date.getTime());

                                    String a1 = "data:image/png;base64," + a;
                                    simi.put("fImage", a1);
                                    simi.put("eDate", time);
                                    simi.put("similar", similar);

                                    list.add(simi);
                                }

                       /* Map<String, Object> arr1 = faceService.queryTicketing(ftId);
                        String a = new String((byte[]) arr1.get("fImage"));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.Date date= sdf.parse(arr1.get("eDate").toString());
                        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format0.format(date.getTime());

                        String a1 = "data:image/png;base64," + a;
                        arr1.put("fImage", a1);
                        arr1.put("eDate", time);
                        arr1.put("similar", similar);

                        list.add(arr1);*/
                            }
                        }
                    }


                }
                int i=  list.size();
                if (i != 0) {
                    page.setRecords(list);
                }
//            double j=10;
//            double de= i/j;
//            page.setTotal((int) Math.ceil(de));
                page.setTotal(i);
                page.setCurrent((long) 0);
                page.setSize((long) 10);
            }


        }

        return ResponseResult.SUCCESS(page);
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "数据上传", notes = "")
    @PostMapping("/posts")
    public ResponseResult getposts(@RequestBody String params) throws Exception {

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
                        Date timed= new Date();
                        String  sqlDate = format.format(timed);
                        System.out.println("format : " +sqlDate+"-------任务执行开始时间--------");
                        String  sqlDateformerly = format.format(getTime1());
                        System.out.println("format : " +sqlDateformerly+"-------任务执行以前时间--------");
                        postdate( sqlDate,  sqlDateformerly);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }, time, 1000*10);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }

    public int postdate( String  sqlDate,String  sqlDateformerly) throws Exception {

        List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
        String aid="2ea38fda-4fa2-11ee-affe-c81f66ed2833";
        List<Map<String, Object>> listconet = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);

        if(listconet.size()>0){
            List<Map<String, Object>> list1 = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);
            for (Map<String, Object> simi : list1) {
                Map<String, Object> simis=new HashMap<>();
                //图片
                String a = new String((byte[]) simi.get("fImage"));
                //String a1 = "data:image/png;base64," + a;
                //时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date= sdf.parse(simi.get("eDate").toString());
                SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long timestamp = date.getTime();;
                String timestampstr = String.valueOf(timestamp);
                //身份证
                String flagstr="0";
                String tupestr="1";
                String djid="ych20230916001";
                simis.put("park_id", "Black-045011c63f6f432b969004b259163478");
                simis.put("license", simi.get("tIdentitycard").toString());
                simis.put("park", simi.get("aName").toString());
                simis.put("type", tupestr);
                simis.put("entertime", timestampstr);
                simis.put("enter_position",simi.get("eName").toString());
                simis.put("parktime",timestampstr);
                simis.put("enter_num_img", a);
                simis.put("flag", flagstr);
                simis.put("car_type", flagstr);
                simis.put("djid", djid);
                lists.add(simis);
            }

            String appid= "zbhbfxhych440703";
            String times= Timestamp();
            String secretKey= "pakho123";

            JSONArray array= JSONArray.parseArray(JSON.toJSONString(lists));


            String signature= times  +"&"+ appid   +"&"+ secretKey;
            //String url = "http://127.0.0.1:5000/register";
            String url = "http://202.104.200.83:8314/api/tccxx/b_pj_tccxxs/addTccxx";

            //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
            LinkedMultiValueMap<String,Object> request = new LinkedMultiValueMap<>();


            // 加密
            String  hex256= SHACoder.encodeSHA256Hex(signature);
            //入参
//        request.set("appid",appid);
//        request.set("timestamp",times);
//        request.set("signature", hex256);
//        request.set("data",array);


            //  System.out.println(request);
            // json数据
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appid",appid);
            jsonObject.put("timestamp",times);
            jsonObject.put("signature", hex256);
            jsonObject.put("data",array);

//            System.out.println("String.valueOf(jsonObject)"+String.valueOf(jsonObject));


            //请求
            String result = restTemplate.postForObject(url,jsonObject,String.class);
            System.out.println("成功"+new Date());
        }


       /* ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());*/

        return 0;
    }


    public ResponseResult getposts111(@RequestBody String params) throws ParseException {


        String url = "http://127.0.0.1:5000/login";

        //LinkedMultiValueMap一个键对应多个值，对应format-data的传入类型
        LinkedMultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        //入参
        request.set("username","baihui");
        request.set("password", "123456");
        request.set("sex", "0");
        request.set("telephone", "13172724946");
        //请求
        String result = restTemplate.postForObject(url,request,String.class);
        System.out.println(result);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        System.out.println("responseEntity.getBody() = " + responseEntity.getBody());

        return ResponseResult.SUCCESS(1);
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
       System.out.println("当前时间戳（h秒）：" + timestamp);

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
        cal.add(Calendar.SECOND, -10);
        return cal.getTime();
    }

}
