package com.tickets.service.impl;

import com.tickets.mapper.EntryrecordMapper;
import com.tickets.service.EntryrecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EntryrecordServiceImpl implements EntryrecordService {

    @Resource
    private EntryrecordMapper entryrecordMapper;




    @Override
    public List<Map<String, Object>> getByEntryrecorddownload( String aId,String ips) {

        return entryrecordMapper.getByEntryrecorddownload(aId,ips) ;
    }
    @Override
    public List<Map<String, Object>> getByemployee( String aId,String ips) {

        return entryrecordMapper.getByemployee(aId,ips) ;
    }
    public List<Map<String, Object>> getbindingEtickinginstead( String aId,String ips) {

        return entryrecordMapper.getbindingEtickinginstead(aId,ips) ;
    }

    @Override
    public int getByEntryrecorddownloadlisteid(String aId, List<String> UploadQuantity ,String ips) {
        return  entryrecordMapper.getByEntryrecorddownloadlisteid(aId, UploadQuantity,ips);
    }

    @Override
    public int getemployeeid(String aId, List<String> UploadQuantity ,String ips) {
        return  entryrecordMapper.getemployeeid(aId, UploadQuantity,ips);
    }
    public int getBytickinginstead(String aId, List<String> UploadQuantity ,String ips) {
        return  entryrecordMapper.getBytickinginstead(aId, UploadQuantity,ips);
    }

    @Override
    public int installEntryrecord(String eId, String aId, String vName, String eName, String tId, String aName, String eDate, String temp, String dWorker, String tQrcard, String tIdentitycard, String autonym) {
        return entryrecordMapper.installEntryrecord(eId,aId,vName,eName,tId,aName,eDate,temp,dWorker,tQrcard,tIdentitycard,autonym) ;
    }

    /**
     * @param aId
     * @return
     */
    @Override
    public String getPermissions(String aId) {

        return  entryrecordMapper.getPermissions(aId);
    }

    /**
     * @param list
     * @return
     */
    @Override
    public int addMayo(List list) {
        return entryrecordMapper.addMayo( list);
    }

    @Override
    public int bindtloerecord(List list) {
        return entryrecordMapper.bindtloerecord( list);
    }

    /**
     * @param aId
     * @return
     */
    @Override
    public List<Map<String, Object>> getByticketingDownloads(String aId) {
        return entryrecordMapper.getByticketingDownloads(aId);
    }

    public List<Map<String, Object>> getByfrontinfoDownloads(String aId) {
        return entryrecordMapper.getByfrontinfoDownloads(aId);
    }

    /**
     * @param list
     * @return
     */
    @Override
    public int installticketing(List list) {
        return entryrecordMapper.installticketing( list);
    }
}
