package com.sky.service.impl;

import ch.qos.logback.core.ContextBase;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderServiceMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:21
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private OrderServiceMapper orderServiceMapper;

    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        //1判断信息是否真实如果地址为空，或者购物车为空，抛出异常
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new RuntimeException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }


        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(wrapper);
        if (shoppingCartList == null) {
            throw new RuntimeException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2添加order信息
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);  //设置orders属性
        orders.setPayStatus(Orders.UN_PAID);  //设置支付状态
        orders.setStatus(Orders.PENDING_PAYMENT);  //设置订单状态
        orders.setNumber(System.currentTimeMillis() + "" + currentId);  //设置订单号
        orders.setUserId(currentId); //设置用户id
        orders.setPhone(addressBook.getPhone()); //设置手机号
        orders.setConsignee(addressBook.getConsignee());  //设置收货人
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())  //设置地址
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        //插入数据
        orderServiceMapper.insert(orders);
        //添加orderDetail信息
        Long orderId = orders.getId(); //获取订单id
        shoppingCartList.forEach(item -> {  //遍历购物车
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(item, orderDetail); //设置orderDetail属性
            orderDetail.setOrderId(orderId); //设置订单id
            orderDetailMapper.insert(orderDetail); //插入数据
        });
        //批量删除购物车信息
        shoppingCartMapper.delete(wrapper);


        //返回订单vo

        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orderId);
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(ordersSubmitDTO.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());

        return orderSubmitVO;
    }
}
