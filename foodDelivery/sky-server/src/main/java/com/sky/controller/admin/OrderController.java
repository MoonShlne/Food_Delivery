package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private  OrderService orderService;

    @GetMapping("/conditionSearch")
    @ApiOperation("条件分页查询订单")
    public Result<PageResult> conditionSearch( OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件分页查询订单: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);

    }

}
