package com.tickets.service.impl;

import com.tickets.dto.Page;
import com.tickets.dto.UserSeachDto;
import com.tickets.dto.UserSinInDto;
import com.tickets.entity.User;
import com.tickets.mapper.UserMapper;
import com.tickets.service.UserService;
import com.tickets.utils.PwdUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean save(UserSinInDto userSinInDto) {

        User user = new User();
        BeanUtils.copyProperties(userSinInDto, user);
        user.setUPassword(PwdUtil.digest(user.getUPassword()));
        if (StringUtils.isEmpty(user.getUStartusing())) {
            user.setUStartusing(1);
        }


        user.setUPassword(PwdUtil.digest(user.getUPassword()));
        return userMapper.insert(user) == 1;
    }

    public static void main(String[] args) {
        System.out.println(PwdUtil.digest("ZAT"));
    }


    @Override
    public boolean saveAll(List<User> users) {
        return false;
    }

    @Override
    public boolean remove(String uId) {
        return userMapper.delete(uId) == 1;
    }

    @Override
    public boolean removeBatch(String[] uIds) {
        for (String uId : uIds) {
            userMapper.delete(uId);
        }
        return true;
    }

    @Override
    public boolean removeReal(String uId) {
        return userMapper.deleteReal(uId) == 1;
    }

    @Override
    public Page getByKeys(UserSeachDto userSeachDto) {
        Page<Map<String, Object>> page = new Page<Map<String, Object>>();
        BeanUtils.copyProperties(userSeachDto, page);
        int total = userMapper.selectCountByKeys(userSeachDto);
        page.setTotal(total);
        if (total != 0) {
            page.setRecords(userMapper.selectByKeys(userSeachDto));
        }
        return page;
    }

    @Override
    public boolean updateStartusing(String uId, String uStartusing) {
        return userMapper.updateStartusing(uId, uStartusing) == 1;
    }

    @Override
    public boolean isUUserExist(String uUser) {
        return userMapper.selectCountByuUser(uUser) > 0;
    }

    @Override
    public String isLogin(String uUser, String uPassword) {
        uPassword = PwdUtil.digest(uPassword);
        return userMapper.selectIsExist(uUser, uPassword);
    }

    @Override
    public Map<String, Object> getById(String uId) {
        return userMapper.selectById(uId);
    }

    @Override
    public boolean update(UserSinInDto userSinInDto) {
        User user = new User();
        BeanUtils.copyProperties(userSinInDto,user);
        return userMapper.update(user) == 1;
    }


}
