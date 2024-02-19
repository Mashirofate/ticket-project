package com.tickets.mapper;

import com.tickets.dto.EntersSearchDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface EntersMapper {

    int selectCountByKeys(EntersSearchDto entersSearchDto);
    @MapKey("")
    List<Map<String, Object>> selectByKeys(EntersSearchDto entersSearchDto);

    int deleteEs(String eid);

    @MapKey("")
    List<Map<String, Object>> getempssssssssssssssssssssloys(String aid,String  dateUp);

    int getUploadQTIAEWemploy(String aid, List<String>  UploadQuantity);
    @MapKey("")
    List<Map<String, Object>> getByEntryrecorddownloads( String aId,String ips);
    int getByEntryrecorddownloadlisteid(String aId, List<String>  UploadQuantity,String ips);

    int installEntryrecord(String eId,String aId,String vName,String eName,String tId,String aName,String eDate,String temp,String dWorker,String tQrcard,String tIdentitycard,String autonym,byte[] fImage);
}
