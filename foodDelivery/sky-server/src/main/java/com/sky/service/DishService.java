package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.io.Serializable;
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

    /**
     * 根据id查询菜品及其口味信息
     * @param id
     * @return
     */
    Result<DishVO> getByIdWithFlavor(Long id);

    /**
     * 修改菜品
     * @param dishDTO
     */
    void update(DishDTO dishDTO);
    /**
     * 起售/停售菜品
     * @param status
     * @param id
     */
    void statusSwitch(Integer status, Long id);

    /**
     * 根据id获取菜品数据
     * @param id
     * @return
     */
    List<Dish> list(Long id);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
