package com.tickets.mapper;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.EntranceManagementSearchDto;
import com.tickets.dto.UserSeachDto;
import com.tickets.entity.EntranceManagement;

import java.util.List;
import java.util.Map;

public interface EntranceManagementMapper {

    int insert(EntranceManagement entranceManagement);

    List<Map<String,Object>> selectEntrancePeopleCount(String vaId);

    List<Map<String, Object>> selectByKeys(EntranceManagementSearchDto entranceManagementSearchDto);

    int selectCountByKeys(EntranceManagementSearchDto entranceManagementSearchDto);

    int updateEnable(String emId, Character emEnable);


    int update(EntranceManagement entranceManagement);

    Map<String, Object> getTempCount(String vaId);

    List<Map<String, Object>> numberOFarea(String vaId);

    /**
     * 查询开放的入口
     * @return
     */
    List<EntranceManagementAddDto> selectEntranceByEnable();
}
