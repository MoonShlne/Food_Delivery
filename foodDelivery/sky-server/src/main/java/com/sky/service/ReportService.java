package com.sky.service;

import com.sky.vo.TurnoverReportVO;

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
}
