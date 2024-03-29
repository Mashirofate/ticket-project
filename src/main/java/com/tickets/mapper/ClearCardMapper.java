package com.tickets.mapper;

import com.tickets.dto.ClearCardAddDto;
import com.tickets.dto.ClearCardSearchDto;
import com.tickets.entity.Frontinfo;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface ClearCardMapper {
    int selectCountByKeys(ClearCardSearchDto clearCardSearchDto);
    @MapKey("")
    List<Map<String, Object>> selectByKeys(ClearCardSearchDto clearCardSearchDto);


    int insert(Frontinfo frontinfo);

    int deleteDes(String fid);

    int update(ClearCardAddDto clearCardAddDto);
}
