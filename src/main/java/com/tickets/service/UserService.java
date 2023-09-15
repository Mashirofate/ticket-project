package com.tickets.service;

import com.tickets.dto.Page;
import com.tickets.dto.UserSeachDto;
import com.tickets.dto.UserSinInDto;
import com.tickets.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * 保存一个用户
     * <p>
     * 已测试
     *
     * @param UserSinInDto
     * @return
     */
    boolean save(UserSinInDto UserSinInDto);

    /**
     * 批量保存用户
     *
     * @param users
     * @return
     */
    boolean saveAll(List<User> users);

    /**
     * 逻辑删除
     *
     * @param uId
     * @return
     */
    boolean remove(String uId);


    /**
     * 批量逻辑删除
     *
     * @param uIds
     * @return
     */
    boolean removeBatch(String[] uIds);

    /**
     * 真实删除
     *
     * @param uId
     * @return
     */
    boolean removeReal(String uId);

    /**
     * 条件分页
     *
     * @param userSeachDto
     * @return
     */
    Page getByKeys(UserSeachDto userSeachDto);

    /**
     * 更新用户的状态
     * @param uId
     * @param uStartusing
     * @return
     */
    boolean updateStartusing(String uId, String uStartusing);

    /**
     * 判断用户名是否存在
     * @param uUser
     * @return
     */
    boolean isUUserExist(String uUser);

    /**
     * 返回主键Id
     * @param uUser
     * @param uPassword
     * @return
     */
    String isLogin(String uUser, String uPassword);


    Map<String, Object> getById(String uId);

    boolean update(UserSinInDto userSinInDto);
}
