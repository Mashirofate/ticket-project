package com.tickets.service;

import java.util.List;
import java.util.Map;

public interface EntryrecordService {



    List<Map<String, Object>> getByEntryrecorddownload( String aId,String ips);
    int getByEntryrecorddownloadlisteid( String aId,List<String>  UploadQuantity,String ips);

    int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym);

    String getPermissions(String aId);

   int  addMayo(List list);

    List<Map<String, Object>> getByticketingDownloads( String aId);
    int  installticketing(List list);

}
