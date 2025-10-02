package com.sky.service.impl;

import com.sky.result.Result;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/2 20:28
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置商户状态
     * @param status 商户状态
     */
    @Override
    public void setShopStatus(Integer status) {

        redisTemplate.opsForValue().set("SHOP_STATUS", status);

    }

    /**
     * 获取商户状态
     * @return 商户状态
     */
    @Override
    public Integer getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        return status;
    }
}
