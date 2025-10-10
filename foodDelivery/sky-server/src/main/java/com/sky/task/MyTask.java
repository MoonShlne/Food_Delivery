package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/9 22:07
 */
@Component
@Slf4j

public class MyTask {

    //测试定时任务功能
//    @Scheduled(cron = "0/5 * * * * ?")
//    public  void  myTask( ){
//        log.info("执行定时任务{}",new Date());
//    }
}
