package com.tickets.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.tickets.dao.BaseDao;
import com.tickets.dto.EntersMZXXDto;
import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;
import com.tickets.mapper.EntersMapper;
import com.tickets.mapper.EntryrecordMapper;
import com.tickets.service.EntersService;
import com.tickets.sync.SyncConstant;
import com.tickets.utils.HttpUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntersServiceImpl implements EntersService {
    @Resource
    private EntersMapper entersMapper;
    @Resource
    private EntryrecordMapper entryrecordMapper;

    @Autowired
    private BaseDao baseDao;

    @Override
    public Page getByKeys(EntersSearchDto entersSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(entersSearchDto, page);
        int count = entersMapper.selectCountByKeys(entersSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(entersMapper.selectByKeys(entersSearchDto));
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> selectByKeysMXZZ(EntersMZXXDto entersMZXXDto) {
        return entersMapper.selectByKeysMXZZ(entersMZXXDto);
        // return faceMapper.getemploys(aid,dateUp);
    }
    @Override
    public List<Map<String, Object>> getemploys(String aid , String dateUp) {
        List<Map<String, Object>> list =entersMapper.getempssssssssssssssssssssloys(aid,dateUp);
        return list;
        // return faceMapper.getemploys(aid,dateUp);
    }



    public JSONObject sendSsmemploy(RestTemplate restTemplate, String url, JSONObject jsonObject, Class<String> Str, List<String> listeid , String aid) {
        System.out.print("工作证");
        String result = restTemplate.postForObject(url,jsonObject,String.class);
        JSONObject JSONArray=  JSONObject.parseObject(result);
        entersMapper.getUploadQTIAEWemploy(aid,listeid);
        return JSONArray;
    }

    public JSONObject sendSsmapplet(RestTemplate restTemplate, String url, JSONObject jsonObject, Class<String> Str) {
        System.out.print("身份证下拉");
        String result = restTemplate.postForObject(url,jsonObject,String.class);
        JSONObject JSONArray=  JSONObject.parseObject(result);
        return JSONArray;
    }

    public JSONObject sendinstead(RestTemplate restTemplate, String url, JSONObject jsonObject, Class<String> Str) {
        System.out.print("展会信息下拉");
        String result = restTemplate.postForObject(url,jsonObject,String.class);
        JSONObject JSONArray=  JSONObject.parseObject(result);
        return JSONArray;
    }

//    @Override
//    @Retryable(value = { RuntimeException.class },maxAttempts = 5,backoff = @Backoff(delay = 500,multiplier = 1))
    public String Entryrecorddownload(String argUrl, String param) {
        System.out.print("入场记录下拉++++");
        String result = null;
        try {
            result = HttpUtils.postWithJson(argUrl, null, param, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public String selectAcitType(String aId) {
        return entersMapper.selectAcitType(aId);
    }

    @Override
    public boolean delById(String eid) {
        return entersMapper.deleteEs(eid) == 1;
    }
    @Override
    public com.tickets.dao.Page selectNewData(String date, String clientId, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>(0);
        paramMap.put("clientId",clientId);
        paramMap.put("date",date);
        com.tickets.dao.Page page = baseDao.queryForPage(SyncConstant.NameSpaceEnum.ENTER.getNameSpace() + ".selectNewData", paramMap, currPage, pageSize);
        return page;
    }



    @Override
    public List<Map<String, Object>> getByEntryrecorddownload( String aId,String ips) {

        return entersMapper.getByEntryrecorddownloads(aId,ips) ;
    }

    @Override
    public int getByEntryrecorddownloadlisteid(String aId, List<String> UploadQuantity ,String ips) {
        return  entersMapper.getByEntryrecorddownloadlisteid(aId, UploadQuantity,ips);
    }

    @Override
    public int installEntryrecord(String eId, String aId, String vName, String eName, String tId, String aName, String eDate, String temp, String dWorker, String tQrcard, String tIdentitycard, String autonym,byte[] fImage) {
        return entersMapper.installEntryrecord(eId,aId,vName,eName,tId,aName,eDate,temp,dWorker,tQrcard,tIdentitycard,autonym,fImage) ;
    }
}
