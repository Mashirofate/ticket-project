package com.tickets.service.impl;

import com.tickets.mapper.TurnoutMapper;
import com.tickets.service.TurnoutService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TurnoutServiceImpl implements TurnoutService {

    @Resource
    private TurnoutMapper turnoutMapper;

    @Override
    public Map<String, Object> getTypeCount(String vaId,int i) {
        if(i==1){
            return turnoutMapper.getTypeCount(vaId);
        }else{
            return turnoutMapper.getTypeenCount(vaId);
        }

    }

    @Override
    public List getEachexportCount(String vaId,int i) {
        return turnoutMapper.getEachexportCount(vaId,i);
    }

    public List getOUTENCount(String vaId,int i,int j) {
        // 1 出口  2 入口 3场内
        List list = new ArrayList();
        if(i==1){
            list=  turnoutMapper.getOUTENCount1(vaId,i,j);
        }
        if(i==2){
            list= turnoutMapper.getOUTENCount2(vaId,i,j);
        }
        if(i==3){
            list= turnoutMapper.getOUTENCount3(vaId,i,j);
        }
        return list;
    }

    @Override
    public List<String> getImageByActivityId(String vaId) {
        //查ID再查询图片
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date());
        //    date = "2023-02-16";
        List<String> ids = turnoutMapper.selectKeyByActivityId(vaId, date);
        if(!ids.isEmpty()) {
            List<String> resultList = turnoutMapper.selectImageByKeys(ids);
            return resultList;
        }
        return Collections.emptyList();
//        return turnoutMapper.getImageByActivityId(vaId);
    }

    @Override
    public Map<String, Object> getintraFieldCount(String vaId) {
        return turnoutMapper.getintraFieldCount(vaId);
    }
    public Map<String, Object> getVotesCount(String vaId) {
        return turnoutMapper.getVotesCount(vaId);
    }
    public Map<String, Object> getintworkCount(String vaId) {
        return turnoutMapper.getintworkCount(vaId);
    }
    public List getTIMElist(String vaId,int i,int j) {
        List list = new ArrayList();
        if(i==1){
            list=turnoutMapper.getTIMElist(vaId,i,j);
        }
        if(i==2){
            list=turnoutMapper.getTIMElist1(vaId,i,j);
        }
        return list ;
    }
    /**
     * @param vaId
     * @return
     */

}
