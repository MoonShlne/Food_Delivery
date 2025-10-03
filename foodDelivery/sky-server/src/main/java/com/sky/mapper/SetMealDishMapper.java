package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/27 22:49
 */
@Mapper
public interface SetMealDishMapper  extends BaseMapper<SetmealDish> {
}
