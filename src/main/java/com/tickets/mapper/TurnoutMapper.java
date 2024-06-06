package com.tickets.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TurnoutMapper {
     Map<String, Object> getTypeCount(String vaId);
    Map<String, Object> getTypeenCount(String vaId);
    List<Map<String, Object>> getEachexportCount(String vaId,int i);
    List<String> getOUTENCount1(String vaId,int i,int j);
    List<String> getOUTENCount2(String vaId,int i,int j);
    List<String> getOUTENCount3(String vaId,int i,int j);
    List<String> getImageByActivityId(String vaId);

    Map<String, Object> getintraFieldCount(String vaId);
    Map<String, Object> getVotesCount(String vaId);
    Map<String, Object> getintworkCount(String vaId);

    List<String> selectImageByKeys(@Param("ids") List<String> ids);

    List<String> selectKeyByActivityId(String vaId, String date);
    List<String> getTIMElist(String vaId,int i,int j);
    List<String> getTIMElist1(String vaId,int i,int j);

}
