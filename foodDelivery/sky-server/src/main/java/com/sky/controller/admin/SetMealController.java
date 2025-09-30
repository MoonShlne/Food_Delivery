package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询:{}", setmealPageQueryDTO);
        return SetMealService.pageQuery(setmealPageQueryDTO);
    }


    @PostMapping("/status/{status}")
    @ApiOperation(value = "套餐状态修改")
    public Result statusSwitch(@PathVariable Integer status, @RequestParam Long id) {
        log.info("套餐状态修改:status={},id={}", status, id);
        SetMealService.statusSwitch(status, id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐信息")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据id查询套餐信息:id={}", id);
        SetmealVO setmealVO = SetMealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    @PutMapping()
    @ApiOperation(value = "修改套餐信息")
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        log.info("修改套餐信息:{}", setmealDTO);
        SetMealService.update(setmealDTO);
        return Result.success();
    }


    @DeleteMapping
    @ApiOperation(value = "批量删除套餐信息")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐信息:{}", ids);
        SetMealService.deleteWithDishes(ids);
        return Result.success();

    }
}
