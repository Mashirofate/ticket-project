package com.tickets.service;

import com.alibaba.fastjson.JSONObject;
import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public interface EntersService {


    Page getByKeys( EntersSearchDto entersSearchDto);

    boolean delById(String eid);

    List<Map<String, Object>> getemploys(String aid, String dateUp);

    com.tickets.dao.Page selectNewData(String date,String clientId, int currPage, int pageSize);

    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendSsmemploy(RestTemplate restTemplate , String url, JSONObject jsonObject, Class<String> Str, List<String> listeid, String aid);

    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendSsmapplet(RestTemplate restTemplate , String url, JSONObject jsonObject, Class<String> Str);

    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    JSONObject sendinstead(RestTemplate restTemplate , String url, JSONObject jsonObject, Class<String> Str);


    @Retryable(value = RuntimeException.class,maxAttempts = 10, backoff = @Backoff(value = 2000, multiplier = 2))
    String Entryrecorddownload(String url, String jsonObject );


    String selectAcitType( String aId);


    List<Map<String, Object>> getByEntryrecorddownload( String aId,String ips);
    int getByEntryrecorddownloadlisteid( String aId,List<String>  UploadQuantity,String ips);

    int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym,byte[] fImage);
}
