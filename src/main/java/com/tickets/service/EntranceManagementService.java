package com.tickets.service;

import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.EntranceManagementSearchDto;
import com.tickets.dto.Page;

import java.util.List;
import java.util.Map;

public interface EntranceManagementService {

    /**
     * 获取各个入口的人数
     * @return
     */
    List<Map<String,Object>> getEntrancePeopleCount(String vaId);
    List<Map<String,Object>> getEachexageCount(String vaId);

    Page getByKeys(EntranceManagementSearchDto entranceManagementSearchDto);


    boolean save(EntranceManagementAddDto entranceManagementAddDto);


    boolean delById(String emId);

    boolean updateEnable(String emId, Character emEnable);

    boolean update(EntranceManagementAddDto entranceManagementAddDto);

    Map<String, Object> getTempCount(String vaId);
    List<String> numberOFarea(String vaId);
    List<String> numberOFarea1(String vaId);
    List<String> numberOFarea2(String vaId);

    /**
     * 查询开放的入口
     * @return
     */
    List<EntranceManagementAddDto> selectEntranceByEnable();
}
