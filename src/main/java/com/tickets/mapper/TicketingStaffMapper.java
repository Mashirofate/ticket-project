package com.tickets.mapper;

import com.tickets.dto.BuyticketDto;
import com.tickets.dto.TicketingStaffSearchDto;
import com.tickets.entity.TicketingStaff;

import java.util.List;
import java.util.Map;

public interface TicketingStaffMapper {


    int insert(TicketingStaff ticketingStaff);

    List<Map<String, Object>> selectByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);

    List<Map<String, Object>> getByBuys(BuyticketDto buyticketDto);

    int selectCountByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);
    List<Map<String,Object>> selectCountByscanCode(String scanCode,String aId);
    List<Map<String,Object>> selectCountByscanCodeun(String scanCode,String cardId,String aId);
    List<Map<String,Object>> selectCountByscanCode1(String scanCode,String aId);
    int getscanCode(String scanCode);

    List<Map<String,Object>> getcardId(String cardId,String aId,String tId);

    List<Map<String,Object>> getcardId2(String cardId,String aId,String tId);

    List<Map<String,Object>> getcardIdto(String cardId,String aId,String tId);

    List<Map<String,Object>> getcardId2to(String cardId,String aId,String tId);
    int getByKeycardIds(String cardId,String aId);
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
    int updates(String cardId, String Rname, String tid,String datei,String BIND_MZXX);
    int updatewe(String cardId, String Rname, String wtid,String datei,String BIND_MZXX);
    int installwtid(String cardId,String scanCode, String Rname, String aid,String datei,String tid,String BIND_MZXX);
    int installwtidexhibition(String cardId, String Rname,String Phone, String aid,String datei,String tid);
    List<Map<String, Object>> getByapplet( String aId, String ips);
    List<Map<String, Object>> getByapplets( String aId, String ips);

    int getByappletlisteid(String aId, List<String>  UploadQuantity, String ips);

    int installEntryrecord(List list);
    int installEntryrecordinfo(List list);
    int installEntryrecordfimage(List list);
    int installemploy(List list);

    int installcamera(String teId,String teDate,String teAisle,String teaId,String teCategory,String teMarking);

    String getticketing(String eId);
    List<Map<String, Object>> getenueing(String aId,String cardId,String scanCode,String tSeatingarea,String tRownumber,String tSeat,String BIND_MZXX);

}
