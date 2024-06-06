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
    List<Map<String, Object>> getImageByActivityId(String aid,String  sqlDate,String  sqlDateformerly);
    @MapKey("")
    List<Map<String, Object>> getImageByActivityticketId(String aid,String  sqlDate,String  sqlDateformerly);
    @MapKey("")
    List<Map<String, Object>> getImageByActivityIdUP(String aid, String dateUp);
    @MapKey("")
    List<Map<String, Object>> getEntryrecord(String aid,String  dateUp);
    @MapKey("")
    List<Map<String, Object>> getEntryrecords(String aid,String  dateUp);
    @MapKey("")
    List<Map<String, Object>> getEntryrecordsGA(String aid,String  dateUp);

    @MapKey("")
    List<Map<String, Object>> getemploys(String aid,String  dateUp);
    @MapKey("")
    List<Map<String, Object>> getImageByActivityIdcount(String aId,String  sqlDate,String  sqlDateformerly);
    @MapKey("")
    List<Map<String, Object>> selectImageByActivityIds(String aId);

    @MapKey("")
    List<Map<String, Object>> selectImageByActivityIdsAbnormal(String aId);

    List<Map<String, Object>> getdomparison(String aId);

    @MapKey("")
    List<Map<String, Object>> selectImageByKeys(@Param("ids")List<String> ids);

    List<String> selectKeyByActivityId(String aId, String date, String healthCode);

    int Queryquantity(String aid);
    int getUploadQTIAE(String aid,String dateUp);
    List<String> getUploadQuantity(String aid);
    int upquantity(String tId, String tIdentitycard,String tRealname);
    int upquantitys(String tId, String tIdentitycard,String tRealname,String datei);
    int getUploadQTIAEW(String aid, List<String>  UploadQuantity);

}
