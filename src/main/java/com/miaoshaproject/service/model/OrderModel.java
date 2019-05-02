package com.miaoshaproject.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Pandy
 * @Date: 2019/5/1 18:02
 * @Version 1.0
 * 解决用户下单的交易模型
 */
@Data
public class OrderModel {
    // 2019 5 1 ... 需要详细说明下单
    private String id;

    //购买商品的单价
    private BigDecimal itemPrice;

    //购买的用户id
    private Integer userId;

    //购买的商品id
    private Integer itemId;

    //购买数量
    private Integer amount;

    //购买金额
    private BigDecimal orderAmount;

    //秒杀价格 若是非空则是以秒杀商品的方式进行下单
    private Integer promoId;


}
