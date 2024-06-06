package com.tickets.service;

import com.tickets.dto.*;
import com.tickets.entity.Aisle;

import java.util.List;
import java.util.Map;

public interface DeviceService {


    Page getByKeys(DeviceSearchDto entersSearchDto);

    boolean delById(String did);

    boolean save(DeviceAddDto deviceAddDto);

    boolean update(DeviceAddDto deviceAddDto);



    List<Map<String, Object>>  getSingle(String aId);
    List<String>   getSingles(String aId);

    Page getaiKeys(AisleSearchDto aisleSearchDto);
    boolean deaiById(String aid);
    boolean updateai(Aisle aisle);

    boolean saveai(Aisle aisle);

    /**
     * 查询所有胡通道
     * @return
     */
    List<AisleAddDto> selectAll();
}
