package com.tickets.service.impl;

import com.tickets.dto.EntranceManagementAddDto;
import com.tickets.dto.EntranceManagementSearchDto;
import com.tickets.dto.Page;
import com.tickets.entity.EntranceManagement;
import com.tickets.mapper.EntranceManagementMapper;
import com.tickets.service.EntranceManagementService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class EntranceManagementServiceImpl implements EntranceManagementService {

    @Resource
    private EntranceManagementMapper entranceManagementMapper;


    @Override
    public List<Map<String, Object>> getEntrancePeopleCount(String vaId) {
        return entranceManagementMapper.selectEntrancePeopleCount(vaId);
    }

    @Override
    public Page getByKeys(EntranceManagementSearchDto entranceManagementSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(entranceManagementSearchDto, page);
        int count = entranceManagementMapper.selectCountByKeys(entranceManagementSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(entranceManagementMapper.selectByKeys(entranceManagementSearchDto));
        }
        return page;
    }

    @Override
    public boolean save(EntranceManagementAddDto entranceManagementAddDto) {
        EntranceManagement entranceManagement = new EntranceManagement();
        BeanUtils.copyProperties(entranceManagementAddDto,entranceManagement);
        return entranceManagementMapper.insert(entranceManagement) == 1;
    }

    @Override
    public boolean delById(String emId) {
        return entranceManagementMapper.updateEnable(emId,'0') == 1;
    }

    @Override
    public boolean updateEnable(String emId, Character emEnable) {
        return entranceManagementMapper.updateEnable(emId,emEnable) == 1;
    }

    @Override
    public boolean update(EntranceManagementAddDto entranceManagementAddDto) {
        EntranceManagement entranceManagement = new EntranceManagement();
        BeanUtils.copyProperties(entranceManagementAddDto,entranceManagement);
        return entranceManagementMapper.update(entranceManagement) == 1;
    }
    public Map<String, Object> getTempCount(String vaId) {
        return entranceManagementMapper.getTempCount(vaId);
    }
    public  List<Map<String, Object>> numberOFarea(String vaId) {
        return entranceManagementMapper.numberOFarea(vaId);
    }

    @Override
    public List<EntranceManagementAddDto> selectEntranceByEnable() {
        return entranceManagementMapper.selectEntranceByEnable();
    }
}
