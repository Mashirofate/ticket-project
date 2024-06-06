package com.tickets.service.impl;

import com.tickets.dto.*;
import com.tickets.entity.Aisle;
import com.tickets.entity.Devices;
import com.tickets.mapper.AisleMapper;
import com.tickets.mapper.DeviceMapper;
import com.tickets.service.DeviceService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private AisleMapper aisleMapper;

    @Override
    public Page getByKeys(DeviceSearchDto deviceSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(deviceSearchDto, page);
        int count = deviceMapper.selectCountByKeys(deviceSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(deviceMapper.selectByKeys(deviceSearchDto));
        }
        return page;
    }

    @Override
    public Page getaiKeys(AisleSearchDto aisleSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(aisleSearchDto, page);
        int count = aisleMapper.selectCountByKeys(aisleSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(aisleMapper.selectByKeys(aisleSearchDto));
        }
        return page;
    }


    @Override
    public boolean delById(String did) {
        return deviceMapper.deleteDes(did) == 1;
    }
    @Override
    public boolean deaiById(String aid) {
        return aisleMapper.deleteDes(aid) == 1;
    }
    @Override
    public boolean save(DeviceAddDto deviceAddDto) {
        Devices devices=new Devices();
        BeanUtils.copyProperties(deviceAddDto,devices);
        return deviceMapper.insert(devices) == 1;
    }

    @Override
    public boolean saveai(Aisle aisle) {
        return aisleMapper.insert(aisle) == 1;
    }

    @Override
    public List<AisleAddDto> selectAll() {
        return aisleMapper.selectAll();
    }

    @Override
    public boolean update(DeviceAddDto deviceAddDto) {
        return deviceMapper.update(deviceAddDto) == 1;
    }

    @Override
    public boolean updateai(Aisle aisle ) {
        return aisleMapper.updateByPrimaryKey(aisle) == 1;
    }
    @Override
    public List<Map<String, Object>>  getSingle(String aId) {
        return deviceMapper.getSingle(aId);
    }

    public List<String>  getSingles(String aId) {
        return deviceMapper.getSingles(aId);
    }
}
