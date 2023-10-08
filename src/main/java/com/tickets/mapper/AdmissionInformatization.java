package com.tickets.mapper;

import org.apache.ibatis.annotations.MapKey;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdmissionInformatization {



    @MapKey("")
    Map<String,Object> selectCountLimitTime(Date date, String vaId);
    @MapKey("")
    Map<String,Object> selectTypeCount(String vaId);
    @MapKey("")
    List<Map<String, Object>> getCountLimitTimeenter(String vaId);
    @MapKey("")
    List<Map<String, Object>> getCountLimitTimeout(String vaId);
    @MapKey("")
    List<String> getoutlist(String vaId);
    @MapKey("")
    List<Map<String, Object>> getSeatList(String vaId);
    @MapKey("")
    List<Map<String, Object>> getGrandList(String vaId);
    @MapKey("")
    List<Map<String, Object>> getGrandsList(String vaId);
    List<String> getenterlist(String vaId);
    List<String> getTIMElist(String vaId);

    Map<String, Object> selectrealnameCount(String vaId);

    Map<String, Object> selectNorealnameCount(String vaId);
}

