package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author polar
 * @version 1.0
 * @since 2025/10/5 15:21
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderServiceMapper, Orders> implements OrderService {

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

    @Override
    public void paySuccess(String orderNumber) {
        // 根据订单号查询订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getNumber, orderNumber);
        Orders ordersDB = orderServiceMapper.selectOne(queryWrapper);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderServiceMapper.updateById(orders);
    }

    @Override
    public OrderVO getOrderDetailById(Long id) {
        //查询订单
        Orders orders = orderServiceMapper.selectById(id);
        if (orders == null) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);

        //查询订单详情
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }


    /**
     * 查询历史订单
     *
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @Override
    public PageResult getHistoryOrders(int page, int pageSize, Integer status) {

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        if (status != null) {
            wrapper.eq(Orders::getStatus, status);
        }
        wrapper.orderByDesc(Orders::getOrderTime);
        IPage<Orders> pageInfo = orderServiceMapper.selectPage(ordersPage, wrapper);

        List<Orders> records = pageInfo.getRecords();
        List<OrderVO> orderVOS = new ArrayList<>();
        for (Orders record : records) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(record, orderVO);
            //查询订单详情
            LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrderDetail::getOrderId, record.getId());
            List<OrderDetail> orderDetailList = orderDetailMapper.selectList(wrapper1);
            orderVO.setOrderDetailList(orderDetailList);
            orderVOS.add(orderVO);
        }
        //封装分页结果
        PageResult pageResult = new PageResult();
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRecords(orderVOS);

        return pageResult;
    }

    /**
     * 取消订单
     *
     * @param number 订单id
     */
    @Override
    public void cancel(Long number) {
        Long userId = BaseContext.getCurrentId();
        //将订单状态改为 6已取消  还有取消时间
//        - 待支付和待接单状态下，用户可直接取消订单
//                - 商家已接单状态下，用户取消订单需电话沟通商家
//                - 派送中状态下，用户取消订单需电话沟通商家
//                - 如果在待接单状态下取消订单，需要给用户退款
//                - 取消订单后需要将订单状态修改为“已取消”
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId).eq(Orders::getId, number);
        Orders order = orderServiceMapper.selectOne(queryWrapper);

        if (order == null) {
            throw new RuntimeException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (order.getStatus().equals(Orders.PENDING_PAYMENT) || order.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 待支付和待接单状态下，用户可直接取消订单
            Orders orders = Orders.builder()
                    .id(order.getId())
                    .status(Orders.CANCELLED)
                    .cancelReason("用户取消")
                    .cancelTime(LocalDateTime.now())
                    .build();
            orderServiceMapper.updateById(orders);
            return;
        } else if (order.getStatus().equals(Orders.CONFIRMED) || order.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            //商家已接单状态下，用户取消订单需电话沟通商家
            //派送中状态下，用户取消订单需电话沟通商家
            throw new RuntimeException(MessageConstant.ORDER_STATUS_ERROR);
        } else {
            //其他状态下不能取消订单
            throw new RuntimeException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }


}
