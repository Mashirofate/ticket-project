package com.tickets.service;

import com.tickets.dto.Page;
import com.tickets.dto.VenueManagementAddDto;
import com.tickets.dto.VenueMgDto;
import com.tickets.entity.VenueManagement;

import java.util.List;
import java.util.Map;

public interface VenueManagementService {

    /**
     * 条件分页
     *
     * @param venueMgDto
     * @return
     */
    Page getByKeys(VenueMgDto venueMgDto);

    /**
     * 更新场馆是否启用信息
     *
     * @param vmId
     * @param vmEnable
     * @return
     */
    boolean update(String vmId, Character vmEnable);


    /**
     * 保存条数据
     * @param venueManagementAddDto
     * @return
     */
    boolean save(VenueManagementAddDto venueManagementAddDto);

    /**
     * 删除场馆
     * @param id
     * @return
     */
    boolean delById(String id);

   List<Map<String, Object>> getSimpleList();

    /**
     * 更新场馆信息
     * @param venueManagementAddDto
     * @return
     */
    boolean updates(VenueManagementAddDto venueManagementAddDto);


    /**
     * 查询所有开放的场馆
     * @return
     */
    List<VenueManagementAddDto> selectVenuesByEnable();
}
