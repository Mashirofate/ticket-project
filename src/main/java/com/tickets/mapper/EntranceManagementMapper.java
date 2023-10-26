package com.tickets.mapper;

import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.EntranceManagementSearchDto;
import com.tickets.entity.EntranceManagement;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface EntranceManagementMapper {

    int insert(EntranceManagement entranceManagement);
    @MapKey("")
    List<Map<String,Object>> selectEntrancePeopleCount(String vaId);

    @MapKey("")
    List<Map<String,Object>> getEachexageCount(String vaId);
    @MapKey("")
    List<Map<String, Object>> selectByKeys(EntranceManagementSearchDto entranceManagementSearchDto);

    int selectCountByKeys(EntranceManagementSearchDto entranceManagementSearchDto);

    int updateEnable(String emId, Character emEnable);


    int update(EntranceManagement entranceManagement);
    @MapKey("")
    Map<String, Object> getTempCount(String vaId);
    @MapKey("")
    List<Map<String, Object>> numberOFarea(String vaId);

    /**
     * 查询开放的入口
     * @return
     */
    List<EntranceManagementAddDto> selectEntranceByEnable();
}
