package com.tickets.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tickets.dto.WeCharUserinfoAddDto;
import com.tickets.entity.WecharUser;
import com.tickets.mapper.WeCharUserinfoMapper;
import com.tickets.service.WeCharUserinfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class WeCharUserinfoServiceImpl implements WeCharUserinfoService {

    private String baseurl = "https://api.weixin.qq.com/sns/jscode2session?";
    private String appId = "wxfa728685c87fa6b7";
    private String secret = "7f5a394fccf88d5c74491db02982cfab";
    private String grantType = "authorization_code";

    @Resource
    private WeCharUserinfoMapper weCharUserinfoMapper;

    @Override
    public String login(WeCharUserinfoAddDto weCharUserinfoAddDto) {
        final String url = baseurl +
                "appid=" + appId +
                "&" +
                "secret=" + secret +
                "&" +
                "js_code=" + weCharUserinfoAddDto.getCode() +
                "&" +
                "grant_type=" + grantType;
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(url, String.class);
        JSONObject jsonObject = JSONArray.parseObject(forObject);
        String openid = (String) jsonObject.get("openid");
        String sessionKey = (String) jsonObject.get("session_key");
        String wId = weCharUserinfoMapper.selectIsExist(openid);
        if (StringUtils.isEmpty(wId)) {
            WecharUser wecharUser = new WecharUser();
            BeanUtils.copyProperties(weCharUserinfoAddDto,wecharUser);
            wecharUser.setWcOpenid(openid);
            wecharUser.setWcSessionKey(sessionKey);
            weCharUserinfoMapper.insert(wecharUser);
            return wecharUser.getWcId();
        }
        return wId;
    }

    @Override
    public boolean save(WecharUser wecharUser) {
        return weCharUserinfoMapper.insert(wecharUser) == 1;
    }

    @Override
    public Map<String, Object> getById(String wId) {
        return weCharUserinfoMapper.selectById(wId);
    }

}
