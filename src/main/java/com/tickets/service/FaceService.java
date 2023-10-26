package com.tickets.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public interface FaceService {



    // 数据显示页面的人脸照片推送
    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);

    List<Map<String, Object>> getImageByActivityIdUP(String aid,String dateUp );
    List<String> getUploadQuantity(String aid);

    List<Map<String, Object>> getEntryrecord(String aId,String  sqlDate,String  sqlDateformerly);


    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendSsm(String url, JSONObject jsonObject, Class<String> Str, RestTemplate restTemplate);

    @Retryable(value = ResourceAccessException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendSsmUP(RestTemplate restTemplate ,String url, JSONObject jsonObject, Class<String> Str,List<String> listeid,String aid);


    List<Map<String, Object>> getImageByActivityIds(String aId);

    List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId);

    int getUploadQTIAEW(String aid ,List<String>  UploadQuantity);
    int Queryquantity(String aid);
    int getUploadQTIAE(String aid,String dateUp);
    int upquantity(String tId, String tIdentitycard);
}
