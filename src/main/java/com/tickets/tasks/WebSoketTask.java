package com.tickets.tasks;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tickets.service.*;
import com.tickets.socket.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tickets.utils.NativePlace.getNativePlace;

@Component
@EnableScheduling
public class WebSoketTask {

    @Autowired
    private AdmissionInformationService admissionInformationService;
    @Autowired
    private EntranceManagementService entranceManagementService;
    @Autowired
    private TicketingStaffService ticketingStaffService;
    @Autowired
    private TurnoutService turnoutService;
    @Autowired
    private FaceService faceService;
    @Autowired
    private DeviceService deviceService;

    private  int intraFieldINFO;

    private static Logger logger = LoggerFactory.getLogger(WebSoketTask.class);

    /**
     * 推送实时人数信息
     */
    @Scheduled(fixedRate = 2 * 1000)
    public void realTimePeopleData() throws IOException, ParseException {
        Map<String, List<Map<String, Object>>> maps=new HashMap<>();
        List<String> vaIds = RealTimePeopleServer.vaIds;
        if(vaIds!=null & vaIds.size()>0) {
            for (int u = 0; u < vaIds.size(); u++) {
                String vaId = vaIds.get(u);
                if (vaId != null) {
                    //  Map<String, Object> countLimitTime = admissionInformationService.getCountLimitTime(new Date(),vaId);
                    List<Map<String, Object>> rows = new ArrayList<>();
                    List<Map<String, Object>> rows1 = new ArrayList<>();
                    // 票务当天入口统计人数
                    List<Map<String, Object>> ExportenterList = admissionInformationService.getCountLimitTimeenter(vaId);
                    // 票务当天出口统计人数
                    List<Map<String, Object>> ExporoutList = admissionInformationService.getCountLimitTimeout(vaId);

                    for (int i = 0; i < ExporoutList.size(); i++) {
                        Map<String, Object> b = new HashMap<>();
                        Map<String, Object> a = ExporoutList.get(i);
                        // "yyyy-MM-dd HH:mm:ss"
                        // 使用这个方法转化 Date和string
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        // string ---》Date
                        Date date = sdf.parse(a.get("time").toString());

                        //按固定的格式显示时间
                        SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                        String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                        b.put("time", time);
                        b.put("open", "出口");
                        b.put("amount", a.get("value"));
                        rows1.add(b);
                    }

                    for (int i = 0; i < ExportenterList.size(); i++) {
                        Map<String, Object> b = new HashMap<>();
                        Map<String, Object> a = ExportenterList.get(i);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(a.get("time").toString());
                        SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                        String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                        b.put("time", time);
                        b.put("amount", a.get("value"));
                        b.put("open", "入口");
                        rows.add(b);
                    }


                    maps.put("brokenlistrows", rows);
                    maps.put("brokenlistrows1", rows1);
                    // 获取各个入口的人数
                    List<Map<String, Object>> EachexenterList = entranceManagementService.getEntrancePeopleCount(vaId);

                    // 获取各个出口的人数
                    List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId);

                    maps.put("EachexoutList", EachexoutList);
                    maps.put("EachexenterList", EachexenterList);
                    // 如入场记录中大于37.5的人员数量
                    Map<String, Object> tempchartMap = entranceManagementService.getTempCount(vaId);
                    List<Map<String, Object>> tempchartList = new ArrayList<>();
                    tempchartList.add(tempchartMap);
                    maps.put("tempchartList", tempchartList);





                    int intraFields = intraFieldINFO;
                    Map<String, Object> intraFieldmap = new HashMap<>();
                    intraFieldmap.put("intraField", intraFields);
                    List<Map<String, Object>> intraFieList = new ArrayList<>();
                    intraFieList.add(intraFieldmap);
                    maps.put("intraFields", intraFieList);


                    List<Map<String, Object>> numberOFareamap = entranceManagementService.numberOFarea(vaId);

                    maps.put("numberOFareas", numberOFareamap);


                    // 设备状态

                    String jsonStr = JSON.toJSONString(maps);
                    //logger.info(" ####### send realTimePeopleData : " + jsonStr);
                    RealTimePeopleServer.sendMessage(vaId, jsonStr);
                }
            }

        }
    }

    /**
     * 推送实时入口人数信息  新的中间数据
     */
    @Scheduled(fixedRate = 2 * 1000)
    public void realTimeEntrancePeopleData() throws IOException, ParseException {
        List<String> vaIds = RealTimeEntranceServer.vaIds;
        Map<String, List<Map<String, Object>>> maps=new HashMap<>();

        if(vaIds!=null & vaIds.size()>0) {
            for (int u = 0; u < vaIds.size(); u++) {
                String vaId = vaIds.get(u);
                if (vaId != null) {



                    List rows = new ArrayList<>();


                    // 票务当天出口统计人数
                    List ExporoutList = admissionInformationService.getoutlist(vaId);

                    // 票务当天入口统计人数
                    List ExportenterList = admissionInformationService.getenterlist(vaId);

                    List<Map<String, Object>> EachexenterList = entranceManagementService.getEntrancePeopleCount(vaId);
                    // 获取各个出口的人数
                    List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId);
                    maps.put("EachexenterList", EachexenterList);
                    maps.put("EachexoutList", EachexoutList);

                    List TIMElList = admissionInformationService.getTIMElist(vaId);

                    for (int i = 0; i < TIMElList.size(); i++) {


                        // "yyyy-MM-dd HH:mm:ss"Time
                        // 使用这个方法转化 Date和string
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m");
                        // string ---》Date
                        String tme= TIMElList.get(i).toString()+"0";
                        Date date = sdf.parse(tme);

                        //按固定的格式显示时间
                        SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                        String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                        rows.add(time);
                    }


                    maps.put("timelist", rows);
                    maps.put("outlist", ExporoutList);
                    maps.put("enterlist", ExportenterList);

                    // 区域人口数量
                    List<Map<String, Object>> SeatList = admissionInformationService.getSeatList(vaId);

                    maps.put("SeatList", SeatList);

                    //看台人数信息
                    List<Map<String, Object>> GrandList = admissionInformationService.getGrandList(vaId);
                    List<Map<String, Object>> GrandsList = admissionInformationService.getGrandsList(vaId);


                    maps.put("GrandList", GrandList);
                    maps.put("GrandsList", GrandsList);



                        Map<String, Object> rest = new HashMap<>();
                        List<String>  list = deviceService.getSingle(vaId);
                        List<String> listshen = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            // 判断 对象不为空 Optional.ofNullable(obj).isPresent()
                            var obj= list.get(i);
                            if(obj != null && !obj.trim().equals("")){
                                if(obj.length()==18){
                                    int val = Integer.parseInt(obj.substring(0, 6));
                                    String Iden = getNativePlace(val);
                                    listshen.add(Iden);
                                }
                            }
                        }
                        Map<String, Integer> nameMap = Maps.newHashMap();

                        listshen.forEach(name -> {
                            Integer counts = nameMap.get(name);
                            nameMap.put(name, counts == null ? 1 : ++counts);
                        });
                        // System.out.println(nameMap);
                        List<Map<String, Object>> listmap = new ArrayList<>();

                        for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
                            Map<String, Object> nameMap1 = Maps.newHashMap();
                            String str=entry.getKey();
                            nameMap1.put("name",str );
                            nameMap1.put("value", entry.getValue());
                            listmap.add(nameMap1);
                        }













                    maps.put("listmap",listmap);



                    String jsonStr = JSON.toJSONString(maps);
                    //logger.info(" ******* send realTimeEntrancePeopleData : " + jsonStr);
                    RealTimeEntranceServer.sendMessage(vaId, jsonStr);
                }
            }
        }

    }

    /**
     * 其它信息推送
     * lift
     * @throws IOException
     */
    @Scheduled(fixedRate = 2 * 1000)
    public void RealTimeOtherData() throws IOException {
        List<String> vaIds = RealTimeOtherServer.vaIds;
        Map<String, List<Map<String, Object>>> maps=new HashMap<>();
        Map<String, Object> rest = new HashMap<>();
        if(vaIds!=null & vaIds.size()>0){
            for (int u=0;u<vaIds.size();u++){
                String vaId=vaIds.get(u);
                if(vaId!=null){
                    // 入场人数 ：  票务入场 ticketing+工作人员入场 staff+未实名入场 Norealname+实名入场 realname
                   // Map<String, Object> typeCount = admissionInformationService.getTypeCount(vaId);
//                    Map<String, Object> typeCounts = new HashMap<>();
//                    if (typeCount == null) {
//                        typeCounts.put("Norealname", 0);
//                        typeCounts.put("ticketing", 0);
//                        typeCounts.put("staff", 0);
//                        typeCounts.put("realname", 0);
//                        rest.put("typeenterCount", typeCounts);
//                    }

                    // 未实名人数
                    Map<String, Object> Norealname = admissionInformationService.getNorealnameCount(vaId);
                    // 实名人数
                    Map<String, Object> realname = admissionInformationService.getrealnameCount(vaId);
                    rest.put("Norealname",Norealname.get("Norealname"));
                    rest.put("realname",realname.get("realname"));

                    // 获取出场人数
                    Map<String, Object> typeOutCount = turnoutService.getTypeCount(vaId);
                    // 获取当天入场记录中的所有人数去重
                    Map<String, Object> intraFieldCount = turnoutService.getintraFieldCount(vaId);

                    Map<String, Object> intworkCount = turnoutService.getintworkCount(vaId);
                    int work =Integer.parseInt(intworkCount.get("counts").toString());

                    int intraField =0;
                    if(intraFieldCount==null){
                        intraField = -Integer.parseInt(typeOutCount.get("counts").toString());
                    }else{
                        intraField=Integer.parseInt(intraFieldCount.get("counts").toString()) - Integer.parseInt(typeOutCount.get("counts").toString())+ work ;
                    }




                    rest.put("typeOutCount",typeOutCount.get("counts"));



                    rest.put("intraField",intraField);
                    rest.put("work",work);
                    intraFieldINFO =intraField;

                    // 根据身份证判断性别
                    List<String>  list = deviceService.getSingle(vaId);
                    List<String> listshen = new ArrayList<>();
                    int man=0;
                    int girl=0;

                    for (int i = 0; i < list.size(); i++) {
                        // 判断 对象不为空 Optional.ofNullable(obj).isPresent()
                        var obj= list.get(i);
                        if(obj != null && !obj.trim().equals("")){
                            char genderCode = obj.charAt(obj.length() - 2);
                            int genderDigit = Integer.parseInt(String.valueOf(genderCode));
                            if (genderDigit % 2 == 0) {
                                girl+=1;
                            } else {
                                 man +=1;
                            }

                        }
                    }

                    List<Map<String, String>> listmap = new ArrayList<>();
                    Map<String, String> nameMap1 = Maps.newHashMap();
                    Map<String, String> nameMap2 = Maps.newHashMap();

                    nameMap1.put("name","男");
                    nameMap1.put("value", String.valueOf(man));
                    nameMap2.put("name","女");
                    nameMap2.put("value", String.valueOf(girl));

                    listmap.add(nameMap1);
                    listmap.add(nameMap2);

                    rest.put("gender",listmap);


                    String jsonStr = JSON.toJSONString(rest);
                    //logger.info(" &&&&&&& send RealTimeOtherData : " + jsonStr);
                    RealTimeOtherServer.sendMessage(vaId, jsonStr);
                }
            }

        }

    }

    /**
     * 出场人数推送
     * right
     * @throws IOException
     */
    @Scheduled(fixedRate = 2 * 1000)
    public void RealTimeEnterData() throws IOException {
        List<String> vaIds = RealTimeEnterServer.vaIds;
        if(vaIds!=null & vaIds.size()>0) {
            for (int u = 0; u < vaIds.size(); u++) {
                String vaId = vaIds.get(u);
                if (vaId != null) {
                    Map<String, Object> rest = new HashMap<>();

                    // 获取出口的人数照片
                    List<String> listimg = turnoutService.getImageByActivityId(vaId);

                    //rest.put("OutImg",listimg);
                    // 入场人数图片

                    //  List<String> enterImg= faceService.getImageByActivityId(vaId);
                    //rest.put("enterImg",enterImg);

                    List<Map<String, Object>> rows = new ArrayList<>();
                    List<Map<String, Object>> enterM = faceService.getImageByActivityIds(vaId);


                    for (int i = 0; i < enterM.size(); i++) {
                        Map<String, Object> b = new HashMap<>();
                        if(enterM.get(i)!=null){
                            Map<String, Object> a = enterM.get(i);
                            String images =null;
                            if (a.containsKey("image")) {
                                if(null !=a.get("image")){
                                    images = "data:image/jpg;base64," + a.get("image").toString();
                                }

                            }
                            String tIdentitycard = null;
                            if (a.containsKey("tIdentitycard")) {
                                if(null !=a.get("tIdentitycard")){
                                    tIdentitycard = a.get("tIdentitycard").toString();
                                    if (tIdentitycard.length() == 18) {
                                        tIdentitycard = tIdentitycard.substring(0, 6) + "************";
                                    }
                                }

                            }

                            b.put("autonym", a.get("autonym"));
                            b.put("tIdentitycard", tIdentitycard);


                            if (tIdentitycard == null || tIdentitycard.length() != 18) {
                                b.put("healthCode", null);
                            } else {
                                b.put("healthCode", "绿码");
                            }

                            b.put("image", images);
                            rows.add(b);
                        }




                    }


                    // 入场人数 ：  票务入场 ticketing+工作人员入场 staff+未实名入场 Norealname+实名入场 realname
                    // 未实名人数
                    Map<String, Object> Norealname = admissionInformationService.getNorealnameCount(vaId);
                    // 实名人数
                    Map<String, Object> realname = admissionInformationService.getrealnameCount(vaId);
                    rest.put("Norealname",Norealname.get("Norealname"));
                    rest.put("realname",realname.get("realname"));
                    // 获取出场人数
                    Map<String, Object> typeOutCount = turnoutService.getTypeCount(vaId);
                    // 获取当天入场记录中的所有人数去重
                    Map<String, Object> intraFieldCount = turnoutService.getintraFieldCount(vaId);
                    int intraField = 0;
                    if (intraFieldCount == null) {
                        intraField = -Integer.parseInt(typeOutCount.get("counts").toString());
                    } else {
                        intraField = Integer.parseInt(intraFieldCount.get("counts").toString()) - Integer.parseInt(typeOutCount.get("counts").toString());
                    }


                    rest.put("typeOutCount", typeOutCount.get("counts"));
                    // rest.put("typeenterCount", typeCount);
                    rest.put("intraField", intraField);

                    rest.put("enterImg", rows);
                    rest.put("OutImg", listimg);



                    List<Map<String, Object>> EachexenterList = entranceManagementService.getEntrancePeopleCount(vaId);
                    // 获取各个出口的人数
                    List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId);

                    List enterListname = new ArrayList<>();
                    List enterListvalue = new ArrayList<>();
                    List outList = new ArrayList<>();
                    if (EachexenterList!=null & EachexenterList.size()>0)
                    {
                        for (int i = 0; i < EachexenterList.size(); i++) {
                            for (Map.Entry<String, Object> entry : EachexenterList.get(i).entrySet()) {
                                if("name".equals(entry.getKey())){
                                    enterListname.add(entry.getValue());
                                }else{
                                    enterListvalue.add(entry.getValue());
                                }
                            }
                        }

                    }
                    List outListname = new ArrayList<>();
                    List outListvalue = new ArrayList<>();
                    if (EachexoutList!=null & EachexoutList.size()>0)
                    {
                        for (int i = 0; i < EachexoutList.size(); i++) {
                            for (Map.Entry<String, Object> entry : EachexoutList.get(i).entrySet()) {
                                if("name".equals(entry.getKey())){
                                    outListname.add(entry.getValue());
                                }else{
                                    outListvalue.add(entry.getValue());
                                }
                            }
                        }

                    }




                    rest.put("enterListname", enterListname);
                    rest.put("enterListvalue", enterListvalue);
                    rest.put("outListname", outListname);
                    rest.put("outListvalue", outListvalue);











                    String jsonStr = JSON.toJSONString(rest);
                    //logger.info(" $$$$$$$ send RealTimeEnterData : " + jsonStr);
                    RealTimeEnterServer.sendMessage(vaId, jsonStr);
                }

            }
        }
    };


   /*
   *设备情况表
   *
   */
   @Scheduled(fixedRate = 2 * 1000)
    public void UnitTypeTimeEnterData() throws IOException {
        List<String> vaIds = UnitTypeTimeService.vaIds;
       if(vaIds!=null & vaIds.size()>0) {
           for (int u = 0; u < vaIds.size(); u++) {
               String vaId = vaIds.get(u);
               if (vaId != null & !StringUtils.isEmpty(vaId)) {
                   Map<String, Object> rest = new HashMap<>();
                   List<String>  list = deviceService.getSingle(vaId);
                   List<String> listshen = new ArrayList<>();
                   for (int i = 0; i < list.size(); i++) {
                       // 判断 对象不为空 Optional.ofNullable(obj).isPresent()
                       var obj= list.get(i);
                        if(obj != null && !obj.trim().equals("")){
                            int val = Integer.parseInt(obj.substring(0, 6));
                            String Iden = getNativePlace(val);
                            listshen.add(Iden);
                        }
                   }
                   Map<String, Integer> nameMap = Maps.newHashMap();

                   listshen.forEach(name -> {
                       Integer counts = nameMap.get(name);
                       nameMap.put(name, counts == null ? 1 : ++counts);
                   });
                   // System.out.println(nameMap);
                   List<Map<String, String>> listmap = new ArrayList<>();

                   for (Map.Entry<String, Integer> entry : nameMap.entrySet()) {
                       Map<String, String> nameMap1 = Maps.newHashMap();
                       // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                       nameMap1.put("name", entry.getKey());
                       nameMap1.put("value", String.valueOf(entry.getValue()));
                       listmap.add(nameMap1);
                   }

                   String jsonStr = JSON.toJSONString(listmap);
                   //logger.info(" %%%%%%% send UnitTypeTimeEnterData : " + jsonStr);
                   UnitTypeTimeService.sendMessage(vaId, jsonStr);
               }
           }
       }
    };




    @Accessors(chain = true)
    @Data
    class Rest {
        private Object date;
        private int count;
    }

    public boolean pingIp(String ip) throws UnknownHostException, IOException{
        //能ping通放回true 反之 false 超时时间 3000毫秒
        return InetAddress.getByName(ip).isReachable(3000);
    }

}
