package com.sky.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
