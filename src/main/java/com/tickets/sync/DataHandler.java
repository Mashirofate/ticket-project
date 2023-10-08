package com.tickets.sync;

import cn.hutool.core.util.StrUtil;
import com.tickets.dao.BaseDao;
import com.tickets.tasks.CustomThreadPoolExecutor;
import com.tickets.tasks.DataHandlerRunnable;
import com.tickets.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 上传操作日志数据
 */
@Service
public class DataHandler {

    private Logger logger = LoggerFactory.getLogger(DataHandler.class);

    @Autowired
    private BaseDao baseDao;
    private ExecutorService mExecutor;
    private Map<String, DataHandlerRunnable> mTaskRunnableMap;

    public DataHandler(){
        CustomThreadPoolExecutor exec = new CustomThreadPoolExecutor("→→【数据处理】线程",3,10,30, TimeUnit.MINUTES);
        mExecutor = exec.getCustomThreadPoolExecutor();
        mTaskRunnableMap = new HashMap<String,DataHandlerRunnable>(5);
    }
    /**
     * 同步活动
     */
    public void processor(String appType,String clientId, String result){
        if(!StrUtil.hasBlank(result)) {
            try {
                List list = JsonUtil.readValue(result, List.class);

                //数据处理
                execute(appType,clientId, list);

            } catch (Exception e) {
                logger.error("出错", e);
            }
            //List<?> list = convertList(appType, result);
        }
        //结果反馈
    }

    private void execute(String appType,String clientId,List<?> list) {
        DataHandlerRunnable mTaskRunnable = mTaskRunnableMap.get(appType);
        if (mTaskRunnable == null) {
            mTaskRunnable = new DataHandlerRunnable() {
                private List dataList;
                private String clientId;
                @Override
                public DataHandlerRunnable setDataParam(List list, String clientId) {
                    this.dataList = list;
                    this.clientId = clientId;
                    return this;
                }

                @Override
                public void run() {
                    try {
                        //清空临时表
                        int i = baseDao.delete(SyncConstant.getCurrentNamespace(appType) + ".deleteTemp", this.clientId);
                        //保存至临时表
                        baseDao.insertBatch(SyncConstant.getCurrentNamespace(appType) + ".insertTempBatch", this.dataList, 1001);
                        //新增数据
                        baseDao.insert(SyncConstant.getCurrentNamespace(appType) + ".insertSync", null);

                        if(!SyncConstant.ENTER.equals(appType)) {
                            //更新数据
                            baseDao.update(SyncConstant.getCurrentNamespace(appType) + ".updateSync", null);
                            //删除数据
                            baseDao.delete(SyncConstant.getCurrentNamespace(appType) + ".deleteSync", null);
                        }
                        //保存日志
                    } catch (Exception e) {
                        logger.error("=====异常======", e.getMessage());
                        e.printStackTrace();
                    } finally {
                        logger.error("=====finally======");
                    }
                }
            };
        }
        mTaskRunnableMap.put(appType,mTaskRunnable);
        mExecutor.execute(mTaskRunnable.setDataParam(list,clientId));
    }
}
