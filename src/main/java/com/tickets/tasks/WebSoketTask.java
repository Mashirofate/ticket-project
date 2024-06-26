package com.tickets.tasks;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tickets.service.*;
import com.tickets.socket.*;
import lombok.Data;
import lombok.experimental.Accessors;
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
import java.util.regex.Pattern;

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
     * 推送实时人数信息 展会
     */
    @Scheduled(fixedRate = 2 * 1000)
    public void realTimePeopleData() throws IOException, ParseException {
        //Map<String, List<Map<String, Object>>> maps=new HashMap<>();
        Map<String, Object> maps = new HashMap<>();
        List<String> vaIds = RealTimePeopleServer.vaIds;
        if(vaIds!=null & vaIds.size()>0) {
            for (String vaId : vaIds) {
                if (vaId != null) {
                    //出口
                    List EoutList = turnoutService.getOUTENCount(vaId,1,2);
                    //入口
                    List EenList = turnoutService.getOUTENCount(vaId,2,1);

                    // 场内人数 intra
                    List intraList = turnoutService.getOUTENCount(vaId,3,1);
                    // 以出口为主的时间轴
                    List rows = new ArrayList<>();
                    List TIMElList1 = turnoutService.getTIMElist(vaId,1,2);
                    List TIMElList2 = turnoutService.getTIMElist(vaId,2,2);
                    if(TIMElList1.size()>0 && TIMElList2.size()>0){
                        if(TIMElList1.get(TIMElList1.size()-1)!=null){
                            for (int i = 0; i < TIMElList1.size(); i++) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m");
                                String tme= TIMElList1.get(i).toString()+"0";
                                Date date = sdf.parse(tme);
                                SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                                String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                                rows.add(time);
                            }
                        }else{
                            for (int i = 0; i < TIMElList2.size(); i++) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m");

                                if(TIMElList2.get(i)!=null){
                                    String tme= TIMElList2.get(i).toString()+"0";
                                    Date date = sdf.parse(tme);
                                    SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                                    String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                                    rows.add(time);
                                }


                            }
                        }
                    }




                    maps.put("timelist", rows);

                    maps.put("timelist1", TIMElList1);
                    maps.put("timelist2", TIMElList2);


                    maps.put("EoutList", EoutList);
                    maps.put("EenList", EenList);

                    List listin=new ArrayList<>();
                    for (int i = 0; i < intraList.size(); i++) {
                        int li=0;
                        if(i==0){
                            li= Integer.parseInt(intraList.get(i).toString());
                        }else{
                            for (int j = 0; j <= i; j++) {
                                li += Integer.parseInt(intraList.get(j).toString());
                            }
                        }

                        listin.addLast(li);
                    }
                    maps.put("intraLists", intraList);
                    maps.put("intraList", listin);

                    // 获取各个出口的人数
                    List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId,1);
                    // 获取各个 入口的人数
                    List<Map<String, Object>> EachexenterList = turnoutService.getEachexportCount(vaId,2);

                    maps.put("EachexoutList", EachexoutList);
                    maps.put("EachexenterList", EachexenterList);


                    // 获取出场人数
                    Map<String, Object> typeOutCount = turnoutService.getTypeCount(vaId,1);

                    Map<String, Object> typeenCount = turnoutService.getTypeCount(vaId,2);
                    // 获取当天入场记录中的所有人数去重

                    int intraField = Integer.parseInt(typeenCount.get("counts").toString()) -  Integer.parseInt(typeOutCount.get("counts").toString());
                    maps.put("typeOutCount", typeOutCount.get("counts"));
                    maps.put("typeenCount", typeenCount.get("counts"));
                    // rest.put("typeenterCount", typeCount);
                    maps.put("intraField", intraField);

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
    @Scheduled(fixedRate = 7 * 1000)
    public void realTimeEntrancePeopleData() throws IOException, ParseException {
        List<String> vaIds = RealTimeEntranceServer.vaIds;
        Map<String, List<Map<String, Object>>> maps=new HashMap<>();

        if(vaIds!=null & vaIds.size()>0) {
            for (int u = 0; u < vaIds.size(); u++) {
                String vaId = vaIds.get(u);
                if (vaId != null) {



                    List rows = new ArrayList<>();


                    // 票务当天出口统计人数
                    // List ExporoutList = admissionInformationService.getoutlist(vaId);

                    // 票务当天入口统计人数
                    List ExportenterList = admissionInformationService.getenterlist(vaId);

                  //  List<Map<String, Object>> EachexenterList = entranceManagementService.getEntrancePeopleCount(vaId);
                    // 获取各个出口的人数
                  //  List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId,1);
                   // maps.put("EachexenterList", EachexenterList);
                   // maps.put("EachexoutList", EachexoutList);

                    List TIMElList = admissionInformationService.getTIMElist(vaId);

                    for (int i = 0; i < TIMElList.size(); i++) {
                        // "yyyy-MM-dd HH:mm:ss"Time
                        // 使用这个方法转化 Date和string
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m");
                        // string ---》Date
                        String tme= TIMElList.get(i).toString();
                        Date date = sdf.parse(tme);

                        //按固定的格式显示时间
                        SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                        String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                        rows.add(time);
                    }






                    List listin=new ArrayList<>();
                    for (int i = 0; i < ExportenterList.size(); i++) {
                        int li=0;
                        if(i==0){
                            li= Integer.parseInt(ExportenterList.get(i).toString());
                        }else{
                            for (int j = 0; j <= i; j++) {
                                li += Integer.parseInt(ExportenterList.get(j).toString());
                            }
                        }

                        listin.addLast(li);
                    }

                    maps.put("listin", listin);

                    maps.put("timelist", rows);
                 //   maps.put("outlist", ExporoutList);
                    maps.put("enterlist", ExportenterList);

                    // 民族数量
                    List<Map<String, Object>> MZXXList = admissionInformationService.getBIND_MZXX(vaId);


                    if(MZXXList.size()>0){
                        List<Map<String, Object>> MZXX = new ArrayList<>();
                        List<Map<String, Object>> MZXXs = new ArrayList<>();
                        List<Map<String, Object>> MZXXss = new ArrayList<>();
                        for (int i = 0; i < MZXXList.size(); i++) {
                            if(i<20){
                                Map<String, Object> map1=new HashMap<>();
                                Map<String, Object> map = MZXXList.get(i);
                                for (String key : map.keySet()) {
                                    Object value = map.get(key);
                                    // 进行处理
                                    if(key!=null){
                                        // System.out.println("Key: " + key + ", Value: " + value);
                                        map1.put(key,value);
                                    }

                                }

                                MZXX.add(map1);
                            }else if (i > 20 && i < 40){
                                Map<String, Object> map1=new HashMap<>();
                                Map<String, Object> map = MZXXList.get(i);
                                for (String key : map.keySet()) {
                                    Object value = map.get(key);
                                    if(key!=null){
                                        // System.out.println("Key: " + key + ", Value: " + value);
                                        map1.put(key,value);
                                    }
                                }
                                MZXXs.add(map1);
                            }else {
                                Map<String, Object> map1=new HashMap<>();
                                Map<String, Object> map = MZXXList.get(i);
                                for (String key : map.keySet()) {
                                    Object value = map.get(key);
                                    if(key!=null){
                                        // System.out.println("Key: " + key + ", Value: " + value);
                                        map1.put(key,value);
                                    }
                                }
                                MZXXss.add(map1);
                            }
                        }


                        maps.put("MZXX", MZXX);
                        maps.put("MZXXs", MZXXs);
                        maps.put("MZXXss", MZXXss);
                        /*maps.put("MZXX", null);
                        maps.put("MZXXs", null);
                        maps.put("MZXXss", null);*/
                    }

                    //看台人数信息
                    // List<Map<String, Object>> GrandList = admissionInformationService.getGrandList(vaId);
                    // List<Map<String, Object>> GrandsList = admissionInformationService.getGrandsList(vaId);

                    //  maps.put("GrandList", GrandList);
                    // maps.put("GrandsList", GrandsList);


                        String aId =vaId;
                        List<String>  list = deviceService.getSingles(vaId);
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

                    if(listmap.size()>0){
                       List<Map<String, Object>> firstHalf = new ArrayList<>();
                        List<Map<String, Object>> secondHalf = new ArrayList<>();
                          for (int i = 0; i < listmap.size(); i++) {
                              if(i<18){
                                  Map<String, Object> map1=new HashMap<>();
                                  Map<String, Object> map = listmap.get(i);
                                  for (String key : map.keySet()) {
                                      Object value = map.get(key);
                                      // 进行处理
                                      if(key!=null){
                                         // System.out.println("Key: " + key + ", Value: " + value);
                                          map1.put(key,value);
                                      }

                                  }

                                  firstHalf.add(map1);
                                }else{
                                  Map<String, Object> map1=new HashMap<>();
                                  Map<String, Object> map = listmap.get(i);
                                  for (String key : map.keySet()) {
                                      Object value = map.get(key);
                                      if(key!=null){
                                          // System.out.println("Key: " + key + ", Value: " + value);
                                          map1.put(key,value);
                                      }
                                  }
                                  secondHalf.add(map1);
                                }
                            }

                        Map<String, Object> map2=new HashMap<>();
                      /*  int foshanResidents = countFoshanResidents(list,"4412");
                        map2.put("name","肇庆市");
                        map2.put("value",foshanResidents);*/

                       // System.out.println("惠州vaId: " + vaId+bp);
                       if("Supper0615".equals(vaId)){
                           int foshanResidents = countFoshanResidents(list,"4412");
                           map2.put("name","肇庆市");
                           map2.put("value",foshanResidents);
                       }else {
                           int foshanResidents = countFoshanResidents(list,"4406");
                           map2.put("name","佛山市");
                           map2.put("value",foshanResidents);
                       }
                        secondHalf.add(map2);
                        maps.put("firstHalf",firstHalf);
                        maps.put("secondHalf",secondHalf);


                    }









                    maps.put("listmap",listmap);



                    String jsonStr = JSON.toJSONString(maps);
                    //logger.info(" ******* send realTimeEntrancePeopleData : " + jsonStr);
                    RealTimeEntranceServer.sendMessage(vaId, jsonStr);
                }
            }
        }

    }
    // 获取佛山市的人数
    public static int countFoshanResidents(List<String> idCards,String codex) {
        int count = 0;
        for (String idCard : idCards) {
            // 获取身份证号码的前6位
            String prefix = idCard.substring(0, 4);
            // 判断是否为佛山市的居民
            if (codex.equals(prefix)) {
                count++;
            }
        }
        return count;
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
                    // 未实名人数
                 //   Map<String, Object> Norealname = admissionInformationService.getNorealnameCount(vaId);
                    // 实名人数
                 //   Map<String, Object> realname = admissionInformationService.getrealnameCount(vaId);


                    // 获取出场人数 在tloe表查询入场和出场人数
                     Map<String, Object> typeOutCount = turnoutService.getTypeCount(vaId,1);
                    // Map<String, Object> typeenCount = turnoutService.getTypeCount(vaId,2);


                    // 获取当天入场记录中的所有人数去重
                    Map<String, Object> intraFieldCount = turnoutService.getintraFieldCount(vaId);

                    Map<String, Object> intworkCount = turnoutService.getintworkCount(vaId);
                    int work =0;
                    if(intworkCount.get("counts") !=null) {
                        work = Integer.parseInt(intworkCount.get("counts").toString());
                    }
                    int typeOutCou=0;
                    if(typeOutCount.get("counts") !=null) {
                        typeOutCou = Integer.parseInt(typeOutCount.get("counts").toString());
                    }

                    int intraField =0;
                    if(intraFieldCount!=null){
                        intraField=Integer.parseInt(intraFieldCount.get("counts").toString())-typeOutCou;
                    }

                   // rest.put("Norealname",Norealname.get("Norealname"));
                   // rest.put("realname",realname.get("realname"));

                    rest.put("intraField",intraField);
                    rest.put("work",work);
                    intraFieldINFO =intraField;

                    // 根据身份证判断性别
                    List<Map<String, Object>>  listmap1 = deviceService.getSingle(vaId);

                    rest.put("gender",listmap1);

                    // 各个年龄段的人数
                    List<Map<String, Object>> EachexageList = entranceManagementService.getEachexageCount(vaId);

                    rest.put("ageList", EachexageList);


                    String jsonStr = JSON.toJSONString(rest);
                    //logger.info(" &&&&&&& send RealTimeOtherData : " + jsonStr);
                    RealTimeOtherServer.sendMessage(vaId, jsonStr);
                }
            }

        }

    }
    // 字符转是否是数字的验证
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

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

                    Map<String, Object> Norealname = admissionInformationService.getNorealnameCount(vaId);
                    // 实名人数
                    Map<String, Object> realname = admissionInformationService.getrealnameCount(vaId);
                    rest.put("Norealname",Norealname.get("Norealname"));

                    rest.put("realname",realname.get("realname"));



                    // 获取出场人数
                   Map<String, Object> typeOutCount = turnoutService.getTypeCount(vaId,1);

                    /*  Map<String, Object> typeenCount = turnoutService.getTypeCount(vaId,2);*/
                    // 获取当天入场记录中的所有人数去重
                    Map<String, Object> intraFieldCount = turnoutService.getintraFieldCount(vaId);
                    int intraField = 0;
                    if (intraFieldCount != null) {

                        intraField = Integer.parseInt(intraFieldCount.get("counts").toString());
                    }


                   rest.put("typeOutCount", typeOutCount.get("counts"));
                    // rest.put("typeenterCount", typeCount);
                    rest.put("intraField", intraField);




                    List<Map<String, Object>> EachexenterList = entranceManagementService.getEntrancePeopleCount(vaId);


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

                    rest.put("enterListname", enterListname);
                    rest.put("enterListvalue", enterListvalue);


                    List<String> areaList = entranceManagementService.numberOFarea(vaId);
                    List<String> areaList1 = entranceManagementService.numberOFarea1(vaId);
                    List<String> areaList2 = entranceManagementService.numberOFarea2(vaId);

                    rest.put("areaList", areaList);
                    rest.put("areaList1", areaList1);
                    rest.put("areaList2", areaList2);



                   /*
                     // 获取各个出口的人数
                    List<Map<String, Object>> EachexoutList = turnoutService.getEachexportCount(vaId,1);

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
                   rest.put("outListname", outListname);
                    rest.put("outListvalue", outListvalue);*/







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
                   List<String>  list = deviceService.getSingles(vaId);
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
