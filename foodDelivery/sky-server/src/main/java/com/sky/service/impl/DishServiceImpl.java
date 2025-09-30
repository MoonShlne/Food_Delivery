package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/27 14:35
 */
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    /**
     * 新增菜品功能
     *
     * @param dishDTO 新增菜品 以及把口味加入数据库
     *                口味要涉及新加的菜品id
     *                需要考虑  菜品主键回显
     */
    @Transactional
    @Override
    public void save(DishDTO dishDTO) {
        //把新菜品存入数据库
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //把dish存入数据库
        dishMapper.insert(dish);
        //回显主键
        Long id = dish.getId();

        //把菜品绑定的口味加入数据库
        List<DishFlavor> flavors = dishDTO.getFlavors();

        //遍历口味集合 并且存入数据库
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(
                    flavor -> {
                        flavor.setDishId(id);
                        dishFlavorMapper.insert(flavor);
                    }
            );
        }


    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //构造分页查询条件
        Page<DishVO> dishPage = new Page<>(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        IPage<DishVO> iPage = dishMapper.pageQuery(dishPage, dishPageQueryDTO);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(iPage.getTotal());
        pageResult.setRecords(iPage.getRecords());

        return Result.success(pageResult);


    }

    /**
     * 删除/批量删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //参数校验
        if (ids == null || ids.isEmpty()) {
            throw new DeletionNotAllowedException("请选择菜品");
        }
        //首先在售菜品不得删除
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId, ids);
        List<Dish> dishes = dishMapper.selectList(wrapper);
        dishes.forEach(
                item -> {
                    if (item.getStatus() == 1) {
                        throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
                    }
                }
        );

        //被套餐关联的菜品不得删除


//        LambdaQueryWrapper<SetmealDish> setMealWrapper = new LambdaQueryWrapper<>();
//        setMealWrapper.in(SetmealDish::getDishId, ids);
//        Long count = setMealDishMapper.selectCount(setMealWrapper);
//
//        if (count > 0) {
//            throw  new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
//        }


        //优化  返回关联套餐的菜品名称
        for (Long id : ids) {
            LambdaQueryWrapper<SetmealDish> setMealWrapper = new LambdaQueryWrapper<>();
            setMealWrapper.eq(SetmealDish::getDishId, id);
            Long count = setMealDishMapper.selectCount(setMealWrapper);

            if (count > 0) {
                //查询关联了套餐的菜品名称 并且返回
                Dish dish = dishMapper.selectById(id);
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL + "关联套餐的菜品名称是" + dish.getName());
            }
        }


        //菜品关联的口味也要删除

        LambdaQueryWrapper<DishFlavor> flavorWrapper = new LambdaQueryWrapper<>();
        flavorWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorMapper.delete(flavorWrapper);

        //支持批量删除
        LambdaQueryWrapper<Dish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.in(Dish::getId, ids);
        dishMapper.delete(dishWrapper);

    }

    /**
     * 根据id查询菜品及其口味信息
     *
     * @param id
     * @return
     */
    @Override
    public Result<DishVO> getByIdWithFlavor(Long id) {

        DishVO dishVO = new DishVO();
        //查询菜品基本信息
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dish, dishVO);

        //查询口味信息
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);

        List<DishFlavor> flavors = dishFlavorMapper.selectList(wrapper);

        dishVO.setFlavors(flavors);

        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        //修改菜品属性
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.updateById(dish);

        //修改口味信息 先删除再添加
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorMapper.delete(wrapper);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(
                    flavor -> {
                        flavor.setDishId(dishDTO.getId());
                        dishFlavorMapper.insert(flavor);
                    }
            );
        }


    }

    /**
     * 起售/停售菜品
     *
     * @param status
     * @param id
     */
    @Override
    public void statusSwitch(Integer status, Long id) {
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, id)
                .set(Dish::getStatus, status);
        dishMapper.update(null, wrapper);
    }

    @Override
    public List<Dish> list(Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        List<Dish> dishes = dishMapper.selectList(wrapper);

        return   dishes;
    }
}
