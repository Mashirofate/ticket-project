package com.tickets.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("BaseDao")
public class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
        super.setSqlSessionTemplate(sqlSessionTemplate);
        sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
    }

    @Override
    public void clearCache() {
        getSqlSession().clearCache();
    }

    @Override
    public void commit() {
        getSqlSession().commit();
    }

    @Override
    public void commit(boolean force) {
        getSqlSession().commit(force);
    }

    /**
     * 功能：向数据库中插入一条数据记录
     * @param statementId	sql语句的定义名称
     * @param parameter		VO对象
     * @return	VO对象
     */
    @Override
    public  Object  insert(String  statementId,Object parameter){
        getSqlSession().insert(statementId,parameter);
        return  parameter;
    }

    /**
     * 功能：向数据库中更新一条数据记录
     * @param statementId	sql语句的定义名称
     * @param parameter		VO对象
     * @return	VO对象
     */
    @Override
    public  Object  update(String  statementId,Object parameter){
        getSqlSession().update(statementId,parameter);
        return  parameter;
    }

    /**
     * 功能：向数据库中更新数据记录
     * @param statementId	sql语句的定义名称
     * @param parameter		VO对象
     * @return	int对象 更新条数
     */
    @Override
    public  Object  update1(String  statementId,Object parameter){
        int i = getSqlSession().update(statementId,parameter);
        return  i;
    }

    /**
     * 功能：删除数据库中某些记录
     * @param statementId	sql语句的定义名称
     * @param param	sql语句所要用到的参数
     * @return		删除记录个数
     */
    @Override
    public int delete(String statementId, Object parameter) {
        return getSqlSession().delete(statementId, parameter);
    }

    @Override
    public  <T> T getFieldValue(String statementId, Object parameter) {
        return queryObject(statementId, parameter);
    }

    @Override
    public long getRecordCount(String statementId, Object parameter) {
        Object obj = queryObject(statementId, parameter);
        //BigDecimal big = new BigDecimal(obj.toString());
        long total = NumberUtils.toLong(obj.toString());
        return total;
    }
    /**
     * 功能：查询获取查询结果
     *
     * @param statementId
     *            sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @return List对象，里面是VO对象。
     */
    @Override
    public <T> List<T> queryForList(String statementId, Object param) {
        return getSqlSession().selectList(statementId, param);
    }

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
     * @return List对象，里面是VO对象。
     */
    @Override
    public <T> List<T> queryForPageList(String statementId, Object param, int starNum, int num) {
        return getSqlSession().selectList(statementId, param,new RowBounds(starNum, num));
    }

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
    @Override
    public <T> Page<T>  queryForPage(String statementId, Object param, int currPage,int pageSize) {
        return this.queryForPage(statementId, param, currPage, pageSize, statementId + "Count");
    }

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
    @Override
    public <T> Page<T> queryForPage(String statementId, Object param, int currPage, int pageSize, String getCountstatementId) {
        if (getCountstatementId == null) {
            getCountstatementId = statementId + "Count";
        }

        /* 获取查询结果的记录总数 */
        Object obj = getSqlSession().selectOne(getCountstatementId,param);
        //BigDecimal big = new BigDecimal(obj.toString());
        long total = NumberUtils.toLong(obj.toString());
        //((Long) getSqlSession().selectOne(getCountstatementId,param)).intValue();
        int pageIndex = currPage - 1;
        pageIndex = pageIndex < 0 ? 0 : pageIndex;
        currPage = currPage < 1 ? 1 : currPage;
        /* 开始记录的位置 */
        int startNum = 0;
        startNum = pageIndex * (pageSize < 1 ? 1 : pageSize);

        List<T> list = getSqlSession().selectList(statementId, param, new RowBounds(startNum, pageSize));
        return new Page<T>(list, pageSize, total, currPage);
    }


    /**
     * 功能：查询获取查询结果,只有一条记录
     *
     * @param statementId
     *            sql语句的定义名称
     * @param param
     *            查询所要用到的参数
     * @return Object对象。
     */
    @Override
    public <T> T queryObject(String statementId, Object param) {
        return getSqlSession().selectOne(statementId, param);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object insertBatch(String statementId, Object parameter, int size) {
        SqlSession batchSqlSession = null;
        try {
            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);//获取批量方式的sqlsession
            int batchCount = size;//每批commit的个数
            int batchLastIndex = batchCount - 1;//每批最后一个的下标
            List list = new ArrayList();
            if(parameter instanceof List) {
                list = (List) parameter;
            }

            for(int index = 0; index <= list.size()-1;){
                if(batchLastIndex > list.size()-1){
                    batchLastIndex = list.size();
                    batchSqlSession.insert(statementId, list.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:"+index+"     batchLastIndex:"+batchLastIndex);
                    break;//数据插入完毕,退出循环
                }else{
                    batchSqlSession.insert(statementId, list.subList(index, batchLastIndex));
                    batchSqlSession.commit();
                    System.out.println("index:"+index+"     batchLastIndex:"+batchLastIndex);
                    index = batchLastIndex + 1;//设置下一批下标
                    batchLastIndex = index + (batchCount - 1);
                }
            }
        }finally{
            batchSqlSession.close();
        }
        return null;
    }

}
