package com.tickets.mapper;

import com.tickets.dto.DeviceAddDto;
import com.tickets.dto.DeviceSearchDto;
import com.tickets.entity.Devices;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {
    int selectCountByKeys(DeviceSearchDto deviceSearchDto);

    @MapKey("")
    List<Map<String, Object>> selectByKeys(DeviceSearchDto deviceSearchDto);


    int insert(Devices devices);

    int deleteDes(String did);

    int update(DeviceAddDto deviceAddDto);

    List<String> getSingle(String aId);
}
