package com.sky.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author polar
 * @version 1.0
 * @since 2025/9/22 20:13
 */
public interface DishMapper extends BaseMapper<Dish> {

    IPage<DishVO> pageQuery(Page<DishVO> dishPage,  DishPageQueryDTO dishPageQueryDTO);
}
