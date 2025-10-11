package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/10 20:25
 */
public interface ReportService {

    /**
     * 订单营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计数据
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 用户统计数据
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);


    /**
     * 订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计数据
     */
    OrderReportVO orderStatistics(LocalDate begin, LocalDate end);

    /**
     * 销量前十统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量前十统计数据
     */
    SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出数据报表
     *
     * @param response 响应对象
     */
    void exportDate(HttpServletResponse response);
}
