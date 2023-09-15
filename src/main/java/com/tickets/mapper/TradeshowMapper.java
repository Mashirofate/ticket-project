package com.tickets.mapper;

import com.tickets.entity.Tradeshow;


public interface TradeshowMapper {

    int deleteByPrimaryKey(String tid);

    int insert(Tradeshow record);

    int insertSelective(Tradeshow record);

    Tradeshow selectByPrimaryKey(String tid);

    int updateByPrimaryKeySelective(Tradeshow record);

    int updateByPrimaryKey(Tradeshow record);
}
