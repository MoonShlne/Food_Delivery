package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/2 20:23
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "商户相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;


    @PutMapping("/{status}")
    @ApiOperation(value = "修改商户状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置营业状态为: {}", status == 1 ? "营业中" : "打烊中");
        shopService.setShopStatus(status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取商户状态")
    public Result<Integer> getStatus() {
        Integer shopStatus = shopService.getShopStatus();
        log.info("获取商户状态为 : {}", shopStatus == 1 ? "营业中" : "打烊中");

        return Result.success(shopStatus);
    }
}
