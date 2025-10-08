package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:19
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @ApiOperation(value = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) {
        log.info("订单支付：{}", ordersPaymentDTO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success();
    }


    @GetMapping("/orderDetail/{id}")
    @ApiOperation("根据订单id查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable("id") Long id) {
        log.info("根据订单id查询订单详情: {}", id);
        OrderVO orderVO = orderService.getOrderDetailById(id);
        return Result.success(orderVO);
    }


    @GetMapping("/historyOrders")
    @ApiOperation("查询历史订单")
    public  Result<PageResult> historyOrders(int page, int pageSize,Integer status) {
        log.info("查询历史订单: {},{},{}", page, pageSize,status);
        PageResult result = orderService.getHistoryOrders(page, pageSize,status);
        return Result.success(result);
    }

    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) {
        log.info("取消订单: {}", id);
        orderService.cancel(id);
        return Result.success();
    }


    @PostMapping("repetition/{id}")
    @ApiOperation("再次下单")
    public Result repetition(@PathVariable("id") Long id){
        log.info("再次下单: {}", id);
        orderService.repetition(id);
        return Result.success();
    }

}
