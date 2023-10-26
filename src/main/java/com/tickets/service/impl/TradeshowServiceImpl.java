package com.tickets.service.impl;

import com.tickets.dto.TradeshowPartDto;
import com.tickets.entity.Tradeshow;
import com.tickets.mapper.TradeshowMapper;
import com.tickets.service.TradeshowService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class TradeshowServiceImpl implements TradeshowService {

    @Resource
    private TradeshowMapper tradeshowMapper;


    @Override
    public boolean save(TradeshowPartDto tradeshowPartDto) {
        Tradeshow tradeshow = new Tradeshow();
        tradeshow.setTid(UUID.randomUUID().toString());
        tradeshow.setTqrcard(tradeshowPartDto.getTQrcard());
        tradeshow.setTtype(tradeshowPartDto.getTType());
        tradeshow.setAid(tradeshowPartDto.getAId());
        return tradeshowMapper.insert(tradeshow ) == 1;
    }
}