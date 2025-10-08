package com.sky.controller.admin;

import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/8 20:00
 */
@Api(tags = "商家订单相关接口")
@RestController
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("条件分页查询订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件分页查询订单: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);

    }


    @GetMapping("/statistics")
    @ApiOperation("订单统计")
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(orderService.statistics());
    }

    @GetMapping("/details/{id}")
    @ApiOperation("根据id查询订单详情")
    public  Result<OrderVO> getOrderDetailById(@PathVariable Long id){
        OrderVO orderVO = orderService.getOrderDetailById(id);
        return Result.success(orderVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("商家确认订单")
    public  Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
       log.info("商家确认订单: {}", ordersConfirmDTO);
         orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

}
