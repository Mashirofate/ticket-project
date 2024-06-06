package com.tickets.mapper;

import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface EntryrecordMapper {

    @MapKey("")
    List<Map<String, Object>> getByEntryrecorddownload( String aId,String ips);

    @MapKey("")
    List<Map<String, Object>> getByemployee( String aId,String ips);

    @MapKey("")
    List<Map<String, Object>> getbindingEtickinginstead( String aId,String ips);

    int getByEntryrecorddownloadlisteid(String aId, List<String>  UploadQuantity,String ips);

    int getemployeeid(String aId, List<String>  UploadQuantity,String ips);

    int getBytickinginstead(String aId, List<String>  UploadQuantity,String ips);

    int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym);

    String getPermissions(String aId);

    int  addMayo(List list);
    int  bindtloerecord(List list);
    @MapKey("")
    List<Map<String, Object>> getByticketingDownloads( String aId);

    List<Map<String, Object>> getByfrontinfoDownloads( String aId);
    int  installticketing(List list);
}
