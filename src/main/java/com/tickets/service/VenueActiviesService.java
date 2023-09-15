package com.tickets.service;

import com.tickets.dto.Page;
import com.tickets.dto.UserSeachDto;
import com.tickets.dto.VenueActivieAddDto;
import com.tickets.dto.VenueActivieSearchDto;

import java.util.List;
import java.util.Map;

public interface VenueActiviesService {

    boolean save(VenueActivieAddDto venueActivieAddDto);


    Page getByKeys(VenueActivieSearchDto venueActivieSearchDto);

    List<Map<String, Object>> selectByNames();

    boolean updateEnable(String aId, Character aEnable);

    boolean remove(String aId);

    /**
     * 获取启用的活动
     *
     * @return
     */
    List<Map<String, Object>> getOpenActivies();


    Map<String,Object> getByVaId(String aId);

    boolean update(VenueActivieAddDto venueActivieAddDto);

    List<Map<String, Object>> getSimpleList();

    Object activitiyAmount();

    Object venuesAmount();

    Object deviceAmount();

    Object ticketingAmount();
}
