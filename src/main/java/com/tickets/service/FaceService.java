package com.tickets.service;

import java.util.List;
import java.util.Map;

public interface FaceService {


    // 数据显示页面的人脸照片推送
    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);
    List<Map<String, Object>> getImageByActivityIds(String aId);

    List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId);


    int Queryquantity(String aid);

    int upquantity(String tId, String tIdentitycard);
}
