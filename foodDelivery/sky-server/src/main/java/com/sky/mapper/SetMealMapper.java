package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/22 20:13
 */
public interface SetMealMapper extends BaseMapper<Setmeal> {

    IPage<SetmealVO> pageQuery(Page<Setmeal> page,@Param("dto") SetmealPageQueryDTO setmealPageQueryDTO);

    List<DishItemVO> getDishItemById(Long id);
}
