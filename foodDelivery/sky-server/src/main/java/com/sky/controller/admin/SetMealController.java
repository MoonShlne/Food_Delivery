package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/29 18:15
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService SetMealService;


    @PostMapping()
    @ApiOperation(value = "新增套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("套餐信息:{}", setmealDTO);
        SetMealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "套餐分页查询")
    public Result<PageResult> page( SetmealPageQueryDTO setmealPageQueryDTO){
       return SetMealService.pageQuery(setmealPageQueryDTO);
    }

}
