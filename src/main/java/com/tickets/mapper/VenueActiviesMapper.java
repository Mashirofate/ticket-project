package com.tickets.mapper;

import com.tickets.dto.VenueActivieSearchDto;
import com.tickets.entity.VenueActivies;

import java.util.List;
import java.util.Map;

public interface VenueActiviesMapper {

    /**
     * 创建一条记录
     * @param venueActivies
     * @return
     */
    int insert(VenueActivies venueActivies);
    int  addAM(List list);

    List<Map<String, Object>> selectByKeys(VenueActivieSearchDto venueActivieSearchDto);
    List<Map<String, Object>> getdomparison(String aId);
    List<Map<String, Object>> getdomparisons(String aId);
    int upquantitys(String tId, String tIdentitycard,String tRealname,String datei,String MZXX);

    List<Map<String, Object>> selectByNames();

    int selectCountByKeys(VenueActivieSearchDto venueActivieSearchDto);

    int updateEnable(String aId, Character aEnable);

    List<Map<String,Object>> selectByEnable(Character enable);
    List<Map<String,Object>> selectByEnables(Character enable);
    List<Map<String,Object>> selectByEnableex(Character enable);
    Map<String,Object> selectByVaId(String aId);
    List<Map<String,Object>> selectemVaId(String aId);
    int update(VenueActivies venueActivies);

    List<Map<String, Object>> selectSimple();

    Object activitiyAmount();



    Object deviceAmount();

    Object ticketingAmount();
}
