package com.tickets.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component

@Slf4j

//@Async

public class TaskService {



    @Async
    public void doTaskOne() throws Exception {

        log.info("开始做任务一");

        long start = System.currentTimeMillis();

        Thread.sleep(1000);

        long end = System.currentTimeMillis();

        log.info("完成任务一，耗时：" + (end - start) + "毫秒");

    }



    @Async

    public void doTaskTwo() throws Exception {

        log.info("开始做任务二");

        long start = System.currentTimeMillis();

        Thread.sleep(1000);

        long end = System.currentTimeMillis();

        log.info("完成任务二，耗时：" + (end - start) + "毫秒");

    }



    @Async

    public void doTaskThree() throws Exception {

        log.info("开始做任务三");

        long start = System.currentTimeMillis();

        Thread.sleep(1000);

        long end = System.currentTimeMillis();

        log.info("完成任务三，耗时：" + (end - start) + "毫秒");

    }

}
