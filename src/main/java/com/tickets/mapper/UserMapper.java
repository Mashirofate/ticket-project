package com.tickets.mapper;

import com.tickets.dto.UserSeachDto;
import com.tickets.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    /**
     * 插入数据
     * <p>
     * 已测试
     *
     * @param user
     * @return
     */
    int insert(User user);

    /**
     * 真实删除
     *
     * @param uId
     * @return
     */
    int deleteReal(String uId);

    /**
     * 更新用户的状态
     * @param uId
     * @param startusing
     * @return
     */
    int updateStartusing(String uId, String startusing);

    /**
     * 逻辑删除
     *
     * @param uId
     * @return
     */
    int delete(String uId);

    /**
     * 获取 u_user的数量
     *
     * @param uUser
     * @return
     */
    int selectCout(String uUser);

    /**
     * 条件分页
     *
     * @param userSeachDto
     * @return
     */
    List<Map<String, Object>> selectByKeys(UserSeachDto userSeachDto);

    /**
     * 根据条件获取数据条数
     *
     * @param userSeachDto
     * @return
     */
    int selectCountByKeys(UserSeachDto userSeachDto);

    /**
     * 根据用户名获取条数
     * @param uUser
     * @return
     */
    int selectCountByuUser(String uUser);

    /**
     * 返回 主键Id
     * @param uUser
     * @param uPassword
     * @return
     */
    String selectIsExist(String uUser, String uPassword);

    Map<String,Object> selectById(String uId);

    int update(User user);
}
