package com.tickets.mapper;

import com.tickets.dto.EntersSearchDto;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface EntersMapper {

    int selectCountByKeys(EntersSearchDto entersSearchDto);
    @MapKey("")
    List<Map<String, Object>> selectByKeys(EntersSearchDto entersSearchDto);

    int deleteEs(String eid);
}
