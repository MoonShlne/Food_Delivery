package com.sky.task;

import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/9 22:15
 */
@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0/1 * * * ?")
    public  void  processTimeoutOrders( ){
        log.info("执行定时任务处理超时订单");
        //当订单状态为待支付且创建时间超过15分钟，自动取消订单
        orderService.processTimeoutOrders();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public  void processDeliveryOrder(){
        //每天凌晨一点 自动把派送中的订单改为已完成
        log.info("执行定时任务处理派送中订单");
        orderService.processDeliveryOrder();


    }
}
