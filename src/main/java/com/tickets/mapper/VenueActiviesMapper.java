package com.tickets.mapper;

import com.tickets.dto.VenueActivieSearchDto;
import com.tickets.entity.VenueActivies;

import java.awt.*;
import java.util.List;
import java.util.Map;

public interface VenueActiviesMapper {

    /**
     * 创建一条记录
     * @param venueActivies
     * @return
     */
    int insert(VenueActivies venueActivies);


    List<Map<String, Object>> selectByKeys(VenueActivieSearchDto venueActivieSearchDto);

    List<Map<String, Object>> selectByNames();

    int selectCountByKeys(VenueActivieSearchDto venueActivieSearchDto);

    int updateEnable(String aId, Character aEnable);

    List<Map<String,Object>> selectByEnable(Character enable);

    Map<String,Object> selectByVaId(String aId);

    int update(VenueActivies venueActivies);

    List<Map<String, Object>> selectSimple();

    Object activitiyAmount();

    Object venuesAmount();

    Object deviceAmount();

    Object ticketingAmount();
}
