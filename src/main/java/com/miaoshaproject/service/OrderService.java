package com.miaoshaproject.service;

import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.service.model.OrderModel;

/**
 * @Author: Pandy
 * @Date: 2019/5/1 18:17
 * @Version 1.0
 * 处理订单交易的service
 */
public interface OrderService {
    //用户 商品 以及购买数量 实现交易
    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BussinessException;
}
