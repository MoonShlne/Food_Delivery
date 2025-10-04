package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/4 21:03
 */
@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private DishMapper dishMapper;

    /**
     * 购物车加购
     *
     * @param shoppingCartDTO
     */
    //todo 没有计算amount
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        log.info("购物车加购: {}", shoppingCartDTO);
        //完善购物车信息
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //先判断是套餐 还是 菜品
        if (shoppingCart.getDishId() == null) {
            //是套餐  再根据userid判断购物车中是否已经存在该套餐
            LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                    .eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());

            long count = this.count(wrapper);
            if (count > 0) {
                //如果存在，则数量加一  并且重新计算amount金额
                ShoppingCart existingCartItem = this.getOne(wrapper);
                existingCartItem.setNumber(existingCartItem.getNumber() + 1);
                this.updateById(existingCartItem);
            } else {
                //如果不存在，则添加到购物车 并设置 名称 价格 数量等冗余字段，数量默认就是一
                Setmeal setmeal = setMealMapper.selectById(shoppingCart.getSetmealId());

                shoppingCart.setNumber(1);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                this.save(shoppingCart);
            }
        } else {
            //是菜品  再根据userid判断购物车中是否已经存在该菜品
            LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                    .eq(ShoppingCart::getDishId, shoppingCart.getDishId());

            if (shoppingCart.getDishFlavor() != null) {
                wrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
            }

            long count = this.count(wrapper);
            if (count > 0) {
                //如果存在，则数量加一  并且重新计算amount金额
                ShoppingCart existingCartItem = this.getOne(wrapper);
                existingCartItem.setNumber(existingCartItem.getNumber() + 1);
                this.updateById(existingCartItem);
            } else {
                //如果不存在，则添加到购物车 并设置 名称 价格 数量等冗余字段，数量默认就是一
                Dish dish = dishMapper.selectById(shoppingCart.getDishId());
                shoppingCart.setNumber(1);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                this.save(shoppingCart);
            }
        }
    }
}