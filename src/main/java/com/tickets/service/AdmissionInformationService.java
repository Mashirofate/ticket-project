package com.tickets.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdmissionInformationService {

    /**
     * 获取过去5分钟新增数据量
     * @return
     */
    Map<String,Object> getCountLimitTime(Date date, String vaId);

    /**
     * 获取 票务入场， 工作人员入场， 未实名入场， 实名入场
     * @param vaId
     * @return
     */
    Map<String,Object> getTypeCount(String vaId);

    List<Map<String, Object>> getCountLimitTimeenter(String vaId);

    List<Map<String, Object>> getCountLimitTimeout(String vaId);

    List getoutlist(String vaId);

    List<Map<String, Object>> getBIND_MZXX(String vaId);
    List<Map<String, Object>> getSeatList(String vaId);
    List<Map<String, Object>> getGrandList(String vaId);
    List<Map<String, Object>> getGrandsList(String vaId);
    List getenterlist(String vaId);
    List getTIMElist(String vaId);


    Map<String, Object> getrealnameCount(String vaId);

    Map<String, Object> getNorealnameCount(String vaId);
}
