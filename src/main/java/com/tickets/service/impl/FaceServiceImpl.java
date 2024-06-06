package com.tickets.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.tickets.mapper.EntersMapper;
import com.tickets.mapper.FaceMapper;
import com.tickets.service.FaceService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class FaceServiceImpl implements FaceService {
    @Resource
    private FaceMapper faceMapper;
    @Resource
    private EntersMapper entersMapper;

    @Override
    public List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly) {
        return faceMapper.getImageByActivityId(aId,sqlDate,sqlDateformerly);
    }
    @Override
    public List<Map<String, Object>> getImageByActivityticketId(String aId,String  sqlDate,String  sqlDateformerly) {
        return faceMapper.getImageByActivityticketId(aId,sqlDate,sqlDateformerly);
    }
    @Override
    public List<Map<String, Object>> getImageByActivityIdUP(String aid ,String dateUp) {
        return faceMapper.getImageByActivityIdUP(aid,dateUp);
    }
    @Override
    public List<Map<String, Object>> getEntryrecord(String aid ,String dateUp) {
        return faceMapper.getEntryrecord(aid,dateUp);
    }


    @Override
    public List<Map<String, Object>> getEntryrecords(String aid ,String dateUp) {
        return faceMapper.getEntryrecords(aid,dateUp);
    }
    @Override
    public List<Map<String, Object>> getEntryrecordsGA(String aid ,String dateUp) {
        return faceMapper.getEntryrecordsGA(aid,dateUp);
    }
    @Override
    public List<Map<String, Object>> getemploys(String aid ,String dateUp) {
        List<Map<String, Object>> list =entersMapper.getempssssssssssssssssssssloys(aid,dateUp);
        return list;
       // return faceMapper.getemploys(aid,dateUp);
    }
    public JSONObject sendSsm( RestTemplate restTemplate, String url, JSONObject jsonObject, Class<String> Str, List<String> listeid , String aid) {
        System.out.print("上传公安入场日志");
        String result = restTemplate.postForObject(url,jsonObject,String.class);
        System.out.print("jsonObject："+jsonObject);
        JSONObject JSONArray=  JSONObject.parseObject(result);
        System.out.print("JSONArray："+JSONArray);
        for (Map.Entry<String, Object> entry : JSONArray.entrySet()) {
            if("code".equals(entry.getKey())){
              int dt= (int) entry.getValue();
                if(dt==200){
                    faceMapper.getUploadQTIAEW(aid,listeid);
                    System.out.println("成功"+new Date() +"+++++++++上传的数据量："+ listeid.size());
                }else {
                    System.out.println( "上传失败");
                }
              //   System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
        return JSONArray;
    }



    /**
     * @param restTemplate
     * @param url
     * @param jsonObject
     * @param Str
     * @return
     */
    public JSONObject sendSsmUP(RestTemplate restTemplate, String url, JSONObject jsonObject, Class<String> Str,List<String> listeid ,String aid) {
        System.out.print("1");
        String result = restTemplate.postForObject(url,jsonObject,String.class);
        JSONObject JSONArray=  JSONObject.parseObject(result);
        faceMapper.getUploadQTIAEW(aid,listeid);
        return JSONArray;
    }

    @Override
    public List<Map<String, Object>> getImageByActivityIds(String aId) {
        //先查询出图片ID，再加载图片
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date());
        //date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"");
        if(!ids.isEmpty()) {
            return faceMapper.selectImageByKeys(ids);
        }
        return Collections.emptyList();
    }

    /**
     * @param aId 活动名称
     * @return 返回数据中有身份证信息的数量
     */
    @Override
    public List<Map<String, Object>> getdomparison(String aId) {

        return faceMapper.getdomparison(aId);
    }
    public List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId) {

        String date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"yes");
        if(!ids.isEmpty()) {
            return faceMapper.selectImageByKeys(ids);
        }
        return Collections.emptyList();
    }
    public int getUploadQTIAEW(String aid,List<String>  UploadQuantity) {

        return faceMapper.getUploadQTIAEW(aid, UploadQuantity);
    }
    /**
     * @param aid 活动名称
     * @return 返回数据中有身份证信息的数量
     */
    @Override
    public int Queryquantity(String aid) {
        return faceMapper.Queryquantity(aid);
    }
    @Override
    public int getUploadQTIAE(String aid,String dateUp) {
        return faceMapper.getUploadQTIAE(aid, dateUp);
    }
    public List<String> getUploadQuantity(String aid) {
        return faceMapper.getUploadQuantity(aid);
    }

    /**
     * @param tId
     * @param tIdentitycard 更新 身份证信息
     */
    @Override
    public int upquantity(String tId, String tIdentitycard,String tRealname) {
        return faceMapper.upquantity(tId,tIdentitycard,tRealname);
    }
    @Override
    public int upquantitys(String tId, String tIdentitycard,String tRealname) {
        Date time= new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datei = format.format(time);
        return faceMapper.upquantitys(tId,tIdentitycard,tRealname,datei);
    }

}
