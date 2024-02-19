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
    List<Map<String,Object>> selectCountByscanCode1(String scanCode,String aId);
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
    int updates(String cardId, String Rname, String tid,String datei);
    int updatewe(String cardId, String Rname, String wtid,String datei);
    int installwtid(String cardId,String scanCode, String Rname, String aid,String datei,String tid);
    int installwtidexhibition(String cardId, String Rname,String Phone, String aid,String datei,String tid);
    List<Map<String, Object>> getByapplet( String aId, String ips);
    int getByappletlisteid(String aId, List<String>  UploadQuantity, String ips);

    int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym,byte[] fImage);
    int installemploy(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String tQrcard);

    int installcamera(String teId,String teImage,String teDate,String teAisle,String teaId,String teCategory,String teMarking);

    List<Map<String, Object>> getticketing(String aId,String cardId,String scanCode);
    List<Map<String, Object>> getenueing(String aId,String cardId,String scanCode);

}
