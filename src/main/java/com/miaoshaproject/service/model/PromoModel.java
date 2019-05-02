package com.miaoshaproject.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 * @Author: Pandy
 * @Date: 2019/5/2 8:51
 * @Version 1.0
 */
@Data
public class PromoModel {

    private Integer id;
    //秒杀活动状态 1 未开始 2进行中 3已经结束
    private Integer status;
    //秒杀活动名称
    private String promoName;

    private DateTime startDate;

    private DateTime endDate;

    private Integer itemId;

    private BigDecimal promoItemPrice;


}
