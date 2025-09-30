package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/29 18:59
 */

public interface SetMealService  extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDTO
     */
    void save(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

}
