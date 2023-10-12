package com.tickets.service;

import com.tickets.dto.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface TicketingStaffService {

    Page getByKeys(TicketingStaffSearchDto ticketingStaffSearchDto);
    List<Map<String,Object>> getByKeys(String scanCode ,String aId);
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
    boolean update(String cardId,String Rname,String tid,String datei);

    List<Map<String, Object>> getByapplet(int valid, String aId);

   int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym);
}
