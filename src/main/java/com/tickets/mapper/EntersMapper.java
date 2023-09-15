package com.tickets.mapper;

import com.tickets.dto.EntersSearchDto;

import java.util.List;
import java.util.Map;

public interface EntersMapper {

    int selectCountByKeys(EntersSearchDto entersSearchDto);

    List<Map<String, Object>> selectByKeys(EntersSearchDto entersSearchDto);

    int deleteEs(String eid);
}
