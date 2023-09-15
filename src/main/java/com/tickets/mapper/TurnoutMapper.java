package com.tickets.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TurnoutMapper {
     Map<String, Object> getTypeCount(String vaId);

    List<Map<String, Object>> getEachexportCount(String vaId);

    List<String> getImageByActivityId(String vaId);

    Map<String, Object> getintraFieldCount(String vaId);

    Map<String, Object> getintworkCount(String vaId);

    List<String> selectImageByKeys(@Param("ids") List<String> ids);

    List<String> selectKeyByActivityId(String vaId, String date);
}
