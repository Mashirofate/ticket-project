package com.tickets.mapper;

import com.tickets.entity.WecharUser;

import java.util.Map;


public interface WeCharUserinfoMapper {

    int insert(WecharUser wecharUser);

    String selectIsExist(String wOpenid);

    Map<String,Object> selectById(String id);

}
