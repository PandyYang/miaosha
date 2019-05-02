package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.OrderDOMapper;
import com.miaoshaproject.dao.SequenceDOMapper;
import com.miaoshaproject.dataobject.OrderDO;
import com.miaoshaproject.dataobject.SequenceDO;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: Pandy
 * @Date: 2019/5/1 18:20
 * @Version 1.0
 */
@Service
public class OrderServiceImpl implements OrderService
{

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;


    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId,Integer promoId, Integer amount) throws BussinessException {
        //校验下单状态 下单的商品是否存在 用户是否合法 数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"商品信息不存在");
        }

        UserModel userModel = userService.getUserById(userId);
        if (userModel == null){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"用户状态非法");
        }

        if (amount <=0 || amount >99){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        //校验活动信息
        if (promoId!=null){
            //1.校验对应的活动是否存在这个使用商品
            if (promoId.intValue()!=itemModel.getPromoModel().getId()){
                throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"活动信息不正确");
            }else if (itemModel.getPromoModel().getStatus() != 2){
                throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"未在活动时间内");
            }
        }

        //落单减去库存 支付减库存 后者加延迟
        boolean result = itemService.decreaseStock(itemId, amount);

        if (!result){
            throw new BussinessException(EnumBussinessError.STOCK_NOT_ENOUGH);
        }

        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        if (promoId != null){
            orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else {
            orderModel.setItemPrice(itemModel.getPrice());
        }
        orderModel.setPromoId(promoId);
        orderModel.setOrderAmount(itemModel.getPrice().multiply(new BigDecimal(amount)));

        //生成交易流水号

        orderModel.setId(generateOrderNo());
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //增加销量
        itemService.increaseSales(itemId,amount);
        //返回前端
        return orderModel;

    }

    //将orderModel 转化为orderDO 以便分别插入不同表中
    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if (orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderAmount().doubleValue());
        return orderDO;
    }

    //生成订单流水号
        //无论外部事务如何 此代码块中的事务 都会提交
    @Transactional(propagation = Propagation.REQUIRES_NEW)
     String generateOrderNo(){
        StringBuilder stringBuilder = new StringBuilder();
        //订单号有16位
        //前八位是时间信息 年月日
        LocalDateTime now = LocalDateTime.now();
        String newDate = now.format(DateTimeFormatter.ISO_DATE).replaceAll("-", "");
        stringBuilder.append(newDate);
        //中间六位是自增序列
        int sequence = 0;
        //获取当前sequence的方法
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        for (int i = 0;i<6-sequenceStr.length();i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //最后两位是分库分表位
        stringBuilder.append("00");
        return stringBuilder.toString();
    }
}
