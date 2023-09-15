package com.tickets.service.impl;

import com.tickets.mapper.AdminssionInfomationMapper;
import com.tickets.service.AdmissionInformationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdmissionInformationImpl implements AdmissionInformationService {

    @Resource
    private AdminssionInfomationMapper adminssionInfomationMapper;

    @Override
    public Map<String,Object> getCountLimitTime(Date date, String vaId) {
        return adminssionInfomationMapper.selectCountLimitTime(date,vaId);
    }

    @Override
    public Map<String, Object> getTypeCount(String vaId) {
        return adminssionInfomationMapper.selectTypeCount(vaId);
    }

    @Override
    public List<Map<String, Object>> getCountLimitTimeenter(String vaId) {
        return adminssionInfomationMapper.getCountLimitTimeenter(vaId);
    }

    @Override
    public List<Map<String, Object>> getCountLimitTimeout(String vaId) {
        return adminssionInfomationMapper.getCountLimitTimeout(vaId) ;
    }

    @Override
    public List getoutlist(String vaId) {
        return adminssionInfomationMapper.getoutlist(vaId) ;
    }

    public List<Map<String, Object>> getSeatList(String vaId) {
        return adminssionInfomationMapper.getSeatList(vaId) ;
    }
    public List<Map<String, Object>> getGrandList(String vaId) {
        return adminssionInfomationMapper.getGrandList(vaId) ;
    }
    public List<Map<String, Object>> getGrandsList(String vaId) {
        return adminssionInfomationMapper.getGrandsList(vaId) ;
    }
    public List getenterlist(String vaId) {
        return adminssionInfomationMapper.getenterlist(vaId) ;
    }

    public List getTIMElist(String vaId) {
        return adminssionInfomationMapper.getTIMElist(vaId) ;
    }
}
