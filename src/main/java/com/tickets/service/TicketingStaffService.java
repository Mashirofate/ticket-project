package com.tickets.service;

import com.tickets.dto.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public interface TicketingStaffService {

    Page getByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);
    List<Map<String,Object>> getByKeys(String scanCode ,String aId);
    List<Map<String,Object>> getByKeysun(String scanCode ,String cardId,String aId);
    List<Map<String,Object>> getByKeys1(String scanCode ,String aId);
    int getscanCode(String scanCode );
    List<Map<String,Object>> getcardId(String cardId ,String aId,String tid);
    List<Map<String,Object>> getcardId2(String cardId ,String aId,String tid);
    int getByKeycardIds(String cardId ,String aId);
    List<Map<String, Object>>  getByBuys(BuyticketDto ticketingStaffSearchDto);
    /**
     * 下载票务导入的模板
     *
     * @param response
     */
    void exportExcel(HttpServletResponse response);


    void saveBath(List<Map<String, Object>> list,String aId);

    boolean save(TicketingSaveDto ticketingSaveDto);

    List<Map<String,Object>> getById(String wId);

    boolean remove(String tid);

    boolean install(TicketingAddDto ticketingAddDto);

    boolean update(TicketingAddDto ticketingAddDto);
    boolean update(String cardId,String Rname,String tid,String datei,String BIND_MZXX);
    boolean updatewe(String cardId,String Rname,String wtid,String datei,String BIND_MZXX);
    boolean installwtid(String cardId,String scanCode,String Rname,String aId,String datei,String BIND_MZXX);
    boolean installwtidexhibition(String cardId,String Rname,String Phone,String aId,String datei);
    List<Map<String, Object>> getByapplet( String aId, String ips);

    List<Map<String, Object>> getByapplets( String aId, String ips);
    int getByappletlisteid( String aId,List<String>  UploadQuantity,String ips);



   int installEntryrecord(List listen,List listf,List listi);
    int installemploy(List list);

    int installcamera(String teId,String teDate,String teAisle,String teaId,String teCategory,String teMarking);


    String getticketing(String eId);
    List<Map<String, Object>> getenueing(String aId,String cardId,String scanCode,String tSeatingarea,String tRownumber,String tSeat,String BIND_MZXX);
}
