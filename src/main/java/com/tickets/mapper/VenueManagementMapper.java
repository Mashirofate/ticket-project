package com.tickets.mapper;

import com.tickets.dto.VenueManagementAddDto;
import com.tickets.dto.VenueMgDto;
import com.tickets.entity.VenueManagement;

import java.util.List;
import java.util.Map;

public interface VenueManagementMapper {

    /**
     * 插入一条数据
     *
     * @param venueManagement
     * @return
     */
    int insert(VenueManagement venueManagement);

    /**
     * 条件查询
     *
     * @param venueMgDto
     * @return
     */
    List<Map<String, Object>> selectByKeys(VenueMgDto venueMgDto);

    /**
     * 根据条件查询 数据条数
     *
     * @param venueMgDto
     * @return
     */
    int selectCountByKeys(VenueMgDto venueMgDto);

    /**
     * 根据 vmdId 更新enabel 0不起启用， 1不启用
     *
     * @param vmId
     * @param enable
     * @return
     */
    int updateEnable(String vmId, Character enable);


    /**
     * 获取简单的字段，vmId and emName
     * @return
     */
    List<Map<String,Object>> selectSimple();

    /*
    * 更新场馆信息
    * @param venueManagement
    * @return
    * */
    int updates(VenueManagement venueManagement);

    /**
     * 查询所有开放的场馆
     * @return
     */
    List<VenueManagementAddDto> selectVenuesByEnable();
}
