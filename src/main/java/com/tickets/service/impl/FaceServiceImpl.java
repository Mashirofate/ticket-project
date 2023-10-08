package com.tickets.service.impl;


import com.tickets.mapper.FaceMapper;
import com.tickets.service.FaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class FaceServiceImpl implements FaceService {
    @Resource
    private FaceMapper faceMapper;
    @Override
    public List<Map<String, Object>> getImageByActivityId(String aId,String  sqlDate,String  sqlDateformerly) {
        return faceMapper.getImageByActivityId(aId,sqlDate,sqlDateformerly);
    }



    @Override
    public List<Map<String, Object>> getImageByActivityIds(String aId) {
        //先查询出图片ID，再加载图片
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String date = sd.format(new Date());
        //date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"");
        if(!ids.isEmpty()) {
            return faceMapper.selectImageByKeys(ids);
        }
        return Collections.emptyList();
    }

    public List<Map<String, Object>> getImageByActivityIdsAbnormal(String aId) {

        String date = "2023-02-16";
        List<String> ids = faceMapper.selectKeyByActivityId(aId,date,"yes");
        if(!ids.isEmpty()) {
            return faceMapper.selectImageByKeys(ids);
        }
        return Collections.emptyList();
    }

    /**
     * @param aid 活动名称
     * @return 返回数据中有身份证信息的数量
     */
    @Override
    public int Queryquantity(String aid) {
        return faceMapper.Queryquantity(aid);
    }

    /**
     * @param tId
     * @param tIdentitycard 更新 身份证信息
     */
    @Override
    public int upquantity(String tId, String tIdentitycard) {
        return faceMapper.upquantity(tId,tIdentitycard);
    }


}
