package com.tickets.controller;

import cn.hutool.core.io.FileUtil;
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
import com.tickets.utils.JsonUtil;
import com.tickets.utils.SHACoder;
import com.tickets.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.json.CDL;
import org.json.JSONException;
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
import sun.net.www.protocol.http.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api(tags = "人脸接口")
@RestController
@RequestMapping("/fr")
@Slf4j
public class FaceController {

    @Autowired
    private FaceService faceService;

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl = "https://gaxcx.huizhou.gov.cn/sjcs";

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
    @ApiOperation(value = "江门数据上传", notes = "")
    @PostMapping("/posts")
    public ResponseResult getposts(@RequestBody String params) throws Exception {


//
//         方法四：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
//         Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)



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
        String  sqlDate1 = "2023-09-16 18:42:00";
        timer.scheduleAtFixedRate (new TimerTask() {
            public void run() {
                try {
                    for (int i = 1; i < 35; i++) {

                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = "2023-09-16 19:25:00";
                        Date date1 = ft.parse(time);

                        Date afterDate = new Date(date1 .getTime() + 60000*i);
                        System.out.println(ft.format(afterDate ));


                        String time1 = ft.format(afterDate);

                        Date date2 = ft.parse(time1);
                        Date afterDate1 = new Date(date2 .getTime() + 60000);
                        String time2 = ft.format(afterDate1);

                        String  sqlDate = format.format(getTime1());;
                        System.out.println("format : " +time2+"-------任务执行开始时间--------");
                        String  sqlDateformerly = format.format(getTime2());
                        System.out.println("format : " +time1+"-------任务执行以前时间--------");
                        postdate( time2,  time1);
                    }
                  /*  String  sqlDate = format.format(getTime1());;
                    System.out.println("format : " +sqlDate+"-------任务执行开始时间--------");
                    String  sqlDateformerly = format.format(getTime2());
                    System.out.println("format : " +sqlDateformerly+"-------任务执行以前时间--------");
                    postdate( sqlDate, sqlDateformerly );*/

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }, time, 1000*10);// 这里设定将延时每天固定执行


        return ResponseResult.SUCCESS(1);
    }


    @Authentication(isLogin = true,isRequiredUserInfo = true)
    @ApiOperation(value = "惠州人脸数据上传", notes = "")
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
                        ;
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

    public int postdate( String  sqlDate,String  sqlDateformerly) throws Exception {

        List<Map<String,Object>> lists =new ArrayList<Map<String,Object>>();
        String aid="2ea38fda-4fa2-11ee-affe-c81f66ed2833";
        List<Map<String, Object>> listconet = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);

        if(listconet.size()>0){
            List<Map<String, Object>> list1 = faceService.getImageByActivityId(aid,sqlDate,sqlDateformerly);
            for (Map<String, Object> simi : list1) {
                Map<String, Object> simis=new HashMap<>();
                //图片
                //String a = new String((byte[]) simi.get("fImage"));

                String a =  simi.get("fImage").toString();
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
                commonInputStreamResource = new CommonInputStreamResource(fileInputStream,img.length(),img.getName());
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

    class CommonInputStreamResource extends InputStreamResource {
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
