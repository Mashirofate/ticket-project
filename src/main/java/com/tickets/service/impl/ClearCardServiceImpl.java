package com.tickets.service.impl;

import com.tickets.dto.*;
import com.tickets.entity.Devices;
import com.tickets.entity.Frontinfo;
import com.tickets.mapper.ClearCardMapper;
import com.tickets.mapper.DeviceMapper;
import com.tickets.service.ClearCardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ClearCardServiceImpl implements ClearCardService {
    @Resource
    private ClearCardMapper clearCardMapper;

    @Override
    public Page getByKeys(ClearCardSearchDto clearCardSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(clearCardSearchDto, page);
        int count = clearCardMapper.selectCountByKeys(clearCardSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(clearCardMapper.selectByKeys(clearCardSearchDto));
        }
        return page;
    }

    @Override
    public boolean delById(String fid) {
        return clearCardMapper.deleteDes(fid) == 1;
    }

    @Override
    public boolean save(ClearCardAddDto clearCardAddDto) {
        Frontinfo frontinfo=new Frontinfo();
        BeanUtils.copyProperties(clearCardAddDto,frontinfo);
        return clearCardMapper.insert(frontinfo) == 1;
    }

    @Override
    public boolean update(ClearCardAddDto clearCardAddDto) {
        return clearCardMapper.update(clearCardAddDto) == 1;
    }
}
