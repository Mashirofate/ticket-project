package com.tickets.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public interface FaceService {



    // 数据显示页面的人脸照片推送
    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);

    List<Map<String, Object>> getEntryrecord(String aId,String  sqlDate,String  sqlDateformerly);
    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendSsm(String url, JSONObject jsonObject, Class<String> Str, RestTemplate restTemplate);

    List<Map<String, Object>> getImageByActivityIds(String aId);

    List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId);


    int Queryquantity(String aid);

    int upquantity(String tId, String tIdentitycard);
}
