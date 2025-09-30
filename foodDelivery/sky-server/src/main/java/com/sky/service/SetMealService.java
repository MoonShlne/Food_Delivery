package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.SetmealVO;

import java.util.List;

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

    /**
     * 套餐状态修改
     * @param status
     * @param id
     */
    void statusSwitch(Integer status, Long id);

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteWithDishes(List<Long> ids);
}
