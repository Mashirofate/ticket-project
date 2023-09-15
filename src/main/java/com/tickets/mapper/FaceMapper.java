package com.tickets.mapper;

import com.tickets.dto.DeviceAddDto;
import com.tickets.dto.DeviceSearchDto;
import com.tickets.entity.Devices;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FaceMapper {

    List<Map<String, Object>> queryFace();

    List<Map<String, Object>> queryTicketing(String ftId);

    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);
    List<Map<String, Object>> getImageByActivityIdcount(String aId,String  sqlDate,String  sqlDateformerly);
    List<Map<String, Object>> selectImageByActivityIds(String aId);


    List<Map<String, Object>> selectImageByActivityIdsAbnormal(String aId);


    List<Map<String, Object>> selectImageByKeys(@Param("ids")List<String> ids);

    List<String> selectKeyByActivityId(String aId, String date, String healthCode);
}
