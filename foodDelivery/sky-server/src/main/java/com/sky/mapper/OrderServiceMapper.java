package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:24
 */
public interface OrderServiceMapper extends BaseMapper<Orders> {
    Double sumByOrderAmount(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    ArrayList<GoodsSalesDTO> salesTop10(@Param("begin") LocalDateTime localDateTime,@Param("end") LocalDateTime localDateTime1);


    Double sumByMap(Map map);
}
