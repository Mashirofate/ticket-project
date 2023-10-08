package com.tickets.service.impl;

import com.tickets.mapper.TurnoutMapper;
import com.tickets.service.TurnoutService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;



@Service
public class TurnoutServiceImpl implements TurnoutService {

    @Resource
    private TurnoutMapper turnoutMapper;

    @Override
    public Map<String, Object> getTypeCount(String vaId) {
       return turnoutMapper.getTypeCount(vaId);
    }

    @Override
    public List<Map<String, Object>> getEachexportCount(String vaId) {
        return turnoutMapper.getEachexportCount(vaId);
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
    public Map<String, Object> getintworkCount(String vaId) {
        return turnoutMapper.getintworkCount(vaId);
    }

    /**
     * @param vaId
     * @return
     */

}
