package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/27 14:33
 */
public interface DishService {
    /**
     * 新增菜品
     */
    void save(DishDTO dishDTO);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除/批量删除菜品
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
