package com.miaoshaproject.controller.viewobject;

import lombok.Data;

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

}
