package com.tickets.service.impl;

import com.tickets.dto.Page;
import com.tickets.dto.VenueActivieAddDto;
import com.tickets.dto.VenueActivieSearchDto;
import com.tickets.entity.VenueActivies;
import com.tickets.mapper.VenueActiviesMapper;
import com.tickets.service.VenueActiviesService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class VenueActiviesServiceImpl implements VenueActiviesService {

    @Resource
    private VenueActiviesMapper venueActiviesMapper;

    @Override
    public boolean save(VenueActivieAddDto venueActivieAddDto) {
        VenueActivies venueActivies = new VenueActivies();
        BeanUtils.copyProperties(venueActivieAddDto, venueActivies);
        return venueActiviesMapper.insert(venueActivies) == 1;
    }

    @Override
    public Page getByKeys(VenueActivieSearchDto venueActivieSearchDto) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        BeanUtils.copyProperties(venueActivieSearchDto, page);
        int total = venueActiviesMapper.selectCountByKeys(venueActivieSearchDto);
        page.setTotal(total);
        if (total != 0) {
            page.setRecords(venueActiviesMapper.selectByKeys(venueActivieSearchDto));
        }
        return page;
    }

    @Override
    public List<Map<String, Object>> selectByNames( ) {
        return venueActiviesMapper.selectByNames();
    }

    @Override
    public boolean updateEnable(String aId, Character aEnable) {
        return venueActiviesMapper.updateEnable(aId,aEnable) == 1;
    }

    @Override
    public boolean remove(String aId) {
        return venueActiviesMapper.updateEnable(aId, '0') == 1;
    }

    @Override
    public List<Map<String, Object>> getOpenActivies() {
        return venueActiviesMapper.selectByEnable('1');
    }

    @Override
    public Map<String,Object> getByVaId(String vaId) {
        return venueActiviesMapper.selectByVaId(vaId);
    }

    @Override
    public boolean update(VenueActivieAddDto venueActivieAddDto) {
        VenueActivies venueActivies = new VenueActivies();
        BeanUtils.copyProperties(venueActivieAddDto, venueActivies);
        return venueActiviesMapper.update(venueActivies) == 1;
    }

    @Override
    public List<Map<String, Object>> getSimpleList() {
        return venueActiviesMapper.selectSimple();
    }

    @Override
    public Object activitiyAmount() {
        return venueActiviesMapper.activitiyAmount();
    }

    @Override
    public Object venuesAmount() {
        return venueActiviesMapper.venuesAmount();
    }

    @Override
    public Object deviceAmount() {
        return venueActiviesMapper.deviceAmount();
    }

    @Override
    public Object ticketingAmount() {
        return venueActiviesMapper.ticketingAmount();
    }
}
