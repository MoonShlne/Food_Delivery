package com.sky.service.impl;

import com.sky.mapper.OrderServiceMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/10 20:25
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderServiceMapper orderServiceMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //统计时间内的订单 把时间和营业额封装到TurnoverReportVO对象中返回
        //先把前端传来的天，封装为日期 string 格式
        StringBuilder dateList = new StringBuilder();
        if (begin != null && end != null) {

            while (!begin.isAfter(end)) {
                dateList.append(begin.toString()).append(",");
                begin = begin.plusDays(1);
            }
            //去掉最后一个逗号
            if (dateList.length() > 0) {
                dateList.deleteCharAt(dateList.length() - 1);
            }
        }

        //把每天的营业额查询并且封装返回
        StringBuilder turnoverList = new StringBuilder();
        if (dateList.length() > 0) {
            String[] dates = dateList.toString().split(",");
            for (String date : dates) {
                Double turnover = orderServiceMapper.sumByOrderAmount(
                        LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN),
                        LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX)
                );
                turnoverList.append(turnover).append(",");
            }
            //去掉最后一个逗号
            if (turnoverList.length() > 0) {
                turnoverList.deleteCharAt(turnoverList.length() - 1);
            }
        }


        return TurnoverReportVO.builder()
                .dateList(dateList.toString())
                .turnoverList(turnoverList.toString())
                .build();
    }
}
