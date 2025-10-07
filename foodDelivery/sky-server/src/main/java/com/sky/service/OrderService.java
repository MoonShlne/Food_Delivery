package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:21
 */
public interface OrderService {
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

}
