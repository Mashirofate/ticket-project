package com.tickets.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheListener{
    Logger logger = LoggerFactory.getLogger("cacheLog");
    private CacheManagerImpl cacheManagerImpl;
    public CacheListener(CacheManagerImpl cacheManagerImpl) {
        this.cacheManagerImpl = cacheManagerImpl;
    }

    public void startListen() {
        new Thread(){
            public void run() {
                while (true) {
                    for(String key : cacheManagerImpl.getAllKeys()) {
                        if (cacheManagerImpl.isTimeOut(key)) {
                            cacheManagerImpl.clearByKey(key);
                            logger.info(key + "缓存被清除");
                        }
                    }
                }
            }
        }.start();
    }
}
