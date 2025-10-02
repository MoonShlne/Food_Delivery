package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.result.Result;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/2 20:28
 */
public interface ShopService {

    /**
     * 设置商户状态
     * @param status 商户状态
     */
    void setShopStatus(Integer status);

    /**
     * 获取商户状态
     * @return 商户状态
     */
    Integer getShopStatus();
}
