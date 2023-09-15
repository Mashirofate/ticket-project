package com.tickets.dao;

import java.util.List;


public interface BaseDao {

    /**
     * 强制清除缓存信息
     */
    public void clearCache();

    /**
     * 强制提交事务
     */
    public void commit();

    /**
     * 有选择性提交事务
     *
     * @param force
     *            true/false 强制或不强制
     */
    public void commit(boolean force);

    /**
     * 功能：向数据库中插入一条数据记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param vo
     *            VO对象
     * @return VO对象
     */
    public Object insert(String statementId, Object parameter) ;

    /**
     * 功能：向数据库中批量插入指定数据
     *
     * @param statementId sql语句的定义名称
     * @param parameter 参数
     * @param size 一次保存的记录数
     * @return VO对象
     */
    public Object insertBatch(String statementId, Object parameter,int size) ;

    /**
     * 功能：向数据库中更新一条数据记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param vo
     *            VO对象
     * @return VO对象
     */
    public Object update(String statementId, Object parameter) ;

    /**
     * 功能：删除数据库中某些记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param param
     *            sql语句所要用到的参数
     * @return 删除记录个数
     */
    public int delete(String statementId, Object parameter) ;

    /**
     * 执行sql查询,且只有一个字段返回
     *
     * @param <T>
     *            相应select元素的resultType属性值所代表的类型
     * @param statementId
     *            MyBatis配置：select元素对应id属性值
     * @param parameter
     *            指定id的select元素的parameterType属性值
     * @return select结果/resultType
     *
     *         resultType指相应select元素的resultType属性值所代表的类型
     */
    public <T> T getFieldValue(String statementId, Object parameter);

    /**
     * 执行count(*) 的sql查询结果
     *
     * @param statementId
     *            MyBatis配置：select元素对应id属性值
     * @param parameter
     *            指定id的select元素的parameterType属性值
     * @return count(*)
     *
     *
     *         select配置形如select count(*) from tableName [where conditions]
     *
     */
    public long getRecordCount(String statementId, Object parameter);

    /**
     * 功能：查询获取查询结果
     *
     * @param statementId
     *            sql语句的定义名称
     * @param parameter
     *            查询所要用到的参数
     * @return List对象，里面是resultType定义对象。
     */
    public <T> List<T> queryForList(String statementId, Object parameter);

    /**
     * 功能：查询获取查询结果的某一页的记录数据
     *
     * @param statementId
     *            sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @param starNum
     *            开始记录的位置
     * @param num
     *            获取的记录个数
     * @return List对象，里面是resultType定义对象。
     */
    public <T> List<T> queryForPageList(String statementId, Object parameter, int starNum, int num);

    /**
     * 功能：查询获取查询结果的某一页对象
     *
     * @param statementId
     *            获取查询结果sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @param currPage
     *            当前页码
     * @param pageSize
     *            每页记录个数
     * @return Page对象
     */
    public <T> Page<T>  queryForPage(String statementId, Object parameter, int currPage, int pageSize);

    /**
     * 功能：查询获取查询结果的某一页对象
     *
     * @param statementId
     *            获取查询结果sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @param currPage
     *            当前页码
     * @param pageSize
     *            每页记录个数
     * @param getCountstatementId
     *            获取记录总数sql语句的定义名称
     * @return Page对象
     */
    public <T> Page<T> queryForPage(String statementId, Object param, int currPage,	int pageSize, String getCountstatementId);

    /**
     * 功能：查询获取查询结果,只有一条记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @return Object对象。
     */
    public <T> T queryObject(String statementId, Object param);

    /**
     * 功能：向数据库中更新一条或多条数据记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param vo
     *            VO对象
     * @return int对象
     * 			  更新条数
     */
    public Object update1(String statementId, Object parameter);
}

