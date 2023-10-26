package com.tickets.service.impl;

import com.tickets.dao.BaseDao;
import com.tickets.dto.EntersSearchDto;
import com.tickets.dto.Page;
import com.tickets.mapper.EntersMapper;
import com.tickets.service.EntersService;
import com.tickets.sync.SyncConstant;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EntersServiceImpl implements EntersService {
    @Resource
    private EntersMapper entersMapper;
    @Autowired
    private BaseDao baseDao;

    @Override
    public Page getByKeys(EntersSearchDto entersSearchDto) {
        Page<Map<String, Object>> page = new Page<>();
        BeanUtils.copyProperties(entersSearchDto, page);
        int count = entersMapper.selectCountByKeys(entersSearchDto);
        page.setTotal(count);
        if (count != 0) {
            page.setRecords(entersMapper.selectByKeys(entersSearchDto));
        }
        return page;
    }

    @Override
    public boolean delById(String eid) {
        return entersMapper.deleteEs(eid) == 1;
    }

    @Override
    public com.tickets.dao.Page selectNewData(String date, String clientId, int currPage, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>(0);
        paramMap.put("clientId",clientId);
        paramMap.put("date",date);
        com.tickets.dao.Page page = baseDao.queryForPage(SyncConstant.NameSpaceEnum.ENTER.getNameSpace() + ".selectNewData", paramMap, currPage, pageSize);
        return page;
    }
}
