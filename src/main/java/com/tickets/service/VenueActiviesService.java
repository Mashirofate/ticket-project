package com.tickets.service;

import com.tickets.dto.Page;
import com.tickets.dto.VenueActivieAddDto;
import com.tickets.dto.VenueActivieSearchDto;

import java.util.List;
import java.util.Map;

public interface VenueActiviesService {

    boolean save(VenueActivieAddDto venueActivieAddDto);
    int  addAM(List list);

    Page getByKeys(VenueActivieSearchDto venueActivieSearchDto);
    List<Map<String, Object>> getdomparison(String aId);
    List<Map<String, Object>> getdomparisons(String aId);
    int upquantitys(String tId, String tIdentitycard,String tRealname,String MZXX);

    List<Map<String, Object>> selectByNames();

    boolean updateEnable(String aId, Character aEnable);

    boolean remove(String aId);

    /**
     * 获取启用的活动
     *
     * @return
     */
    List<Map<String, Object>> getOpenActivies();
    List<Map<String, Object>> getOpenActiviess();
    List<Map<String, Object>> getOpenActiviesex();
    Map<String,Object> getByVaId(String aId);
    List<Map<String,Object>> getemVaId(String aId);
    boolean update(VenueActivieAddDto venueActivieAddDto);

    List<Map<String, Object>> getSimpleList();

    Object activitiyAmount();


    Object deviceAmount();

    Object ticketingAmount();
}
