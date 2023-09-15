package com.tickets.mapper;

import com.tickets.dto.AisleAddDto;
import com.tickets.dto.AisleSearchDto;
import com.tickets.dto.DeviceSearchDto;
import com.tickets.dto.Page;
import com.tickets.entity.Aisle;

import java.util.List;
import java.util.Map;

public interface AisleMapper {
    int deleteByPrimaryKey(String aid);

    int insert(Aisle record);

    int insertSelective(Aisle record);

    Aisle selectByPrimaryKey(String aid);

    int updateByPrimaryKeySelective(Aisle record);

    int updateByPrimaryKey(Aisle record);

    List<Map<String, Object>> selectByKeys(AisleSearchDto aisleSearchDto);
    int selectCountByKeys(AisleSearchDto aisleSearchDto);

    int deleteDes(String did);

    List<AisleAddDto> selectAll();
}