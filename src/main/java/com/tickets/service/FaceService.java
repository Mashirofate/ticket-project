package com.tickets.service;

import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.tickets.dto.DeviceAddDto;
import com.tickets.dto.DeviceSearchDto;
import com.tickets.dto.Page;

import java.util.List;
import java.util.Map;

public interface FaceService {

    List<FaceInfo> detectFaces(ImageInfo rgbData);

    byte[] extractFaceFeaqture(ImageInfo rgbData, FaceInfo faceInfo);

    List<Map<String, Object>> queryFace();

    Float compareFace(ImageInfo rgbData1, ImageInfo rgbData);

    byte[] extractFaceFeature(ImageInfo imageInfo,FaceInfo faceInfo);

    List<Map<String, Object>> queryTicketing(String ftId);

    // 数据显示页面的人脸照片推送
    List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly);
    List<Map<String, Object>> getImageByActivityIdcount(String aId,String  sqlDate,String  sqlDateformerly);
    List<Map<String, Object>> getImageByActivityIds(String aId);

    List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId);


    /**
     * 获取最新图片的主键
     * @param aId
     * @return
     */
    List<String> getKeyByActivityId(String aId);


    /**
     * 获取最新图片的主皱
     * @param ids
     * @return
     */
    List<Map<String, Object>> getImageByKeys(List<String> ids);
}
