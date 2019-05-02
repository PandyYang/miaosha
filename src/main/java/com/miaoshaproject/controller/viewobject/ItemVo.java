package com.miaoshaproject.controller.viewobject;

import lombok.Data;
import org.joda.time.DateTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: Pandy
 * @Date: 2019/4/27 17:54
 * @Version 1.0
 */
@Data
public class ItemVo {

    private Integer id;

    private String title;

    private BigDecimal price;

    private Integer stock;

    private String description;

    private Integer sales;

    private String imgUrl;

    //记录商品是否在秒杀活动中 以及记录对应的状态
        //0 没有秒杀 1 待开始 2进行中
    private Integer promoStatus;

    //秒杀活动价格
    private BigDecimal promoPrice;

    private Integer promoId;

    //秒杀活动的开始时间 用来做倒计时展示
    private String startDate;



}
