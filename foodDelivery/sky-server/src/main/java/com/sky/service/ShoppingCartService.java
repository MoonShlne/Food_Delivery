package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/4 21:02
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 购物车加购
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

}
