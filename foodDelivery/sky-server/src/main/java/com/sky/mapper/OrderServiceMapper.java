package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:24
 */
public interface OrderServiceMapper extends BaseMapper<Orders> {
    Double sumByOrderAmount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
}
