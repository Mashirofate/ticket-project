package com.tickets.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FaceMapper {
    @MapKey("")
    List<Map<String, Object>> queryFace();
    @MapKey("")
    List<Map<String, Object>> queryTicketing(String ftId);


    @MapKey("")
    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);
    @MapKey("")
    List<Map<String, Object>> getImageByActivityIdcount(String aId,String  sqlDate,String  sqlDateformerly);
    @MapKey("")
    List<Map<String, Object>> selectImageByActivityIds(String aId);

    @MapKey("")
    List<Map<String, Object>> selectImageByActivityIdsAbnormal(String aId);

    @MapKey("")
    List<Map<String, Object>> selectImageByKeys(@Param("ids")List<String> ids);

    List<String> selectKeyByActivityId(String aId, String date, String healthCode);

    int Queryquantity(String aid);

    int upquantity(String tId, String tIdentitycard);
}
