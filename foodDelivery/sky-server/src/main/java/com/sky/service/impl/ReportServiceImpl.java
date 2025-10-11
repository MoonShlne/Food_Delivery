package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderServiceMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

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
    @Autowired
    private UserMapper  userMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //统计时间内的订单 把时间和营业额封装到TurnoverReportVO对象中返回
        StringBuilder dateList = getList(begin, end);

        //把每天的营业额查询并且封装返回
        StringBuilder turnoverList = new StringBuilder();
        if (dateList.length() > 0) {
            String[] dates = dateList.toString().split(",");
            for (String date : dates) {
                Double turnover = orderServiceMapper.sumByOrderAmount(
                        LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN),
                        LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX)
                );
                //如果某一天没有营业额，默认返回0
                if (turnover == null) {
                    turnover = 0.0;
                }
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


    /**
     * 获取日期列表
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期列表字符串
     */
    private static StringBuilder getList(LocalDate begin, LocalDate end) {
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
        return dateList;
    }

    /**
     * 用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //统计时间内的订单 把时间封装
        StringBuilder dateList = getList(begin, end);
        //新增用户数
        StringBuilder newUserList = new StringBuilder();

        //每天总用户数量
        StringBuilder totalUser = new StringBuilder();

        //把每天的新增用户数查询并且封装返回
        if (!dateList.isEmpty()) {
            String[] dates = dateList.toString().split(",");
            for (String date : dates) {
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.ge(User::getCreateTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN));
                queryWrapper.le(User::getCreateTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX));
                Long count = userMapper.selectCount(queryWrapper);

                //如果某一天没有新增用户，默认返回0
                if (count == null) {
                    count = 0L;
                }
                //把每天的新增用户数拼接成字符串
                newUserList.append(count).append(",");
            }
            //去掉最后一个逗号
            if (!newUserList.isEmpty()) {
                newUserList.deleteCharAt(newUserList.length() - 1);
            }
        }

        //把每天的总用户数查询并且封装返回
        if (!dateList.isEmpty()) {
            String[] dates = dateList.toString().split(",");
            for (String date : dates) {
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.le(User::getCreateTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX));
                Long count = userMapper.selectCount(queryWrapper);

                //如果某一天没有用户，默认返回0
                if (count == null) {
                    count = 0L;
                }
                //把每天的总用户数拼接成字符串
                totalUser.append(count).append(",");
            }
            //去掉最后一个逗号
            if (!totalUser.isEmpty()) {
                totalUser.deleteCharAt(totalUser.length() - 1);
            }
        }


        //返回vo对象
        return UserReportVO.builder()
                .dateList(dateList.toString())
                .newUserList(newUserList.toString())
                .totalUserList(totalUser.toString())
                .build();





    }

    /**
     * 订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return  订单统计数据
     * //日期，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
     *     private String dateList;
     *     //每日订单数，以逗号分隔，例如：260,210,215
     *     private String orderCountList;
     *     //每日有效订单数，以逗号分隔，例如：20,21,10
     *     private String validOrderCountList;
     *     //订单总数
     *     private Integer totalOrderCount;
     *     //有效订单数
     *     private Integer validOrderCount;
     *     //订单完成率
     *     private Double orderCompletionRate;
     *
     */
    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        //获取时间列表 x轴信息
        StringBuilder dateList = getList(begin, end);

        //每日订单数 和每日有效订单数
        StringBuilder orderCountList = new StringBuilder();
        StringBuilder validOrderCountList = new StringBuilder();
        if (!dateList.isEmpty()) {
            String[] dates = dateList.toString().split(",");
            for (String date : dates) {
                //每日订单数
                LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
                wrapper.ge(Orders::getOrderTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN));
                wrapper.le(Orders::getOrderTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX));

                Long orderCount = orderServiceMapper.selectCount(wrapper);

                orderCountList.append(orderCount).append(",");

                wrapper.clear();
                //每日有效订单数
                wrapper.eq(Orders::getStatus, Orders.COMPLETED)
                        .ge(Orders::getOrderTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MIN))
                        .le(Orders::getOrderTime, LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX));

                Long validOrderCount = orderServiceMapper.selectCount(wrapper);

                validOrderCountList.append(validOrderCount).append(",");
            }
            //去掉最后一个逗号
            if (!orderCountList.isEmpty()) {
                orderCountList.deleteCharAt(orderCountList.length() - 1);
            }
            if (!validOrderCountList.isEmpty()) {
                validOrderCountList.deleteCharAt(validOrderCountList.length() - 1);
            }
        }

        //订单总数
        LambdaQueryWrapper<Orders> totalWrapper = new LambdaQueryWrapper<>();
        if (begin != null) {
            totalWrapper.ge(Orders::getOrderTime, LocalDateTime.of(begin, LocalTime.MIN));
        }
        if (end != null) {
            totalWrapper.le(Orders::getOrderTime, LocalDateTime.of(end, LocalTime.MAX));
        }
//        Integer totalOrderCount = Math.toIntExact(orderServiceMapper.selectCount(totalWrapper));
        Long totalOrderCount = orderServiceMapper.selectCount(totalWrapper);

        //有效订单数
        LambdaQueryWrapper<Orders> validWrapper = new LambdaQueryWrapper<>();
        validWrapper.eq(Orders::getStatus, Orders.COMPLETED);
        if (begin != null) {
            validWrapper.ge(Orders::getOrderTime, LocalDateTime.of(begin, LocalTime.MIN));
        }
        if (end != null) {
            validWrapper.le(Orders::getOrderTime, LocalDateTime.of(end, LocalTime.MAX));
        }
//        Integer validOrderCount = Math.toIntExact(orderServiceMapper.selectCount(validWrapper));
        Long validOrderCount = orderServiceMapper.selectCount(validWrapper);
        //订单完成率
        Double orderCompletionRate = totalOrderCount == 0 ? 0.0 : (validOrderCount.doubleValue() / totalOrderCount);

        return OrderReportVO.builder()
                .dateList(dateList.toString())
                .orderCountList(orderCountList.toString())
                .validOrderCountList(validOrderCountList.toString())
                .totalOrderCount(Math.toIntExact(totalOrderCount))
                .validOrderCount(Math.toIntExact(validOrderCount))
                .orderCompletionRate(orderCompletionRate)
                .build();


    }
}
