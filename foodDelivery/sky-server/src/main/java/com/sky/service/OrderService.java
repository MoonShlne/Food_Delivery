package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:21
 */
public interface OrderService  extends IService<com.sky.entity.Orders> {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 支付成功，修改订单状态
     * @param orderNumber
     */
    void paySuccess(String orderNumber);

    /**
     * 根据订单id查询订单详情
     * @param id
     * @return
     */
    OrderVO getOrderDetailById(Long id);

    /**
     * 查询历史订单
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult getHistoryOrders(int page, int pageSize, Integer status);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id);

    /**
     * 再来一单
     * @param id
     * @return
     */
    void repetition(Long id);

    /**
     * 条件分页查询订单 客户端
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 订单概览数据
     * @return
     */
    OrderStatisticsVO statistics();


    /**
     * 商家确认订单
     * @param
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);
}
