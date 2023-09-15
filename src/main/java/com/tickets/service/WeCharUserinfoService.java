package com.tickets.service;

import com.tickets.dto.WeCharUserinfoAddDto;
import com.tickets.entity.WecharUser;

import java.util.Map;

public interface WeCharUserinfoService {

    /**
     * 微信用户登录
     * @param weCharUserinfoAddDto
     * @return
     */
    String login(WeCharUserinfoAddDto weCharUserinfoAddDto);

    boolean save(WecharUser wecharUser);

    Map<String,Object> getById(String wId);
}
