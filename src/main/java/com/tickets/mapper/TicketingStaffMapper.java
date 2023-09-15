package com.tickets.mapper;

import com.tickets.dto.BuyticketDto;
import com.tickets.dto.TicketingStaffSearchDto;
import com.tickets.dto.VenueMgDto;
import com.tickets.entity.TicketingStaff;

import java.util.List;
import java.util.Map;

public interface TicketingStaffMapper {


    int insert(TicketingStaff ticketingStaff);

    List<Map<String, Object>> selectByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);

    List<Map<String, Object>> getByBuys(BuyticketDto buyticketDto);

    int selectCountByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);
    List<Map<String,Object>> selectCountByscanCode(String scanCode,String aId);
    /**
     * 获取一些统计数据
     * @param vaId
     * @return
     */
    int selectDataByVaId(String vaId);

    List<Map<String,Object>> selectById(String id);

    int deleteTs(String tid);

    int install(TicketingStaff ticketingStaff);

    int update(TicketingStaff ticketingStaff);
    int updates(String cardId, String Rname, String tid,String datei);
}
