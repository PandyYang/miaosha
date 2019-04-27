package com.miaoshaproject.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author: Pandy
 * @Date: 2019/4/27 11:48
 * @Version 1.0
 */
@Data
public class ItemModel {
    private Integer id;

    @NotBlank(message = "商品名称不能为空")
    private String title;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存必填")
    private Integer stock;

    @NotBlank(message = "商品描述信息不能为空")
    private String description;

    private Integer sales;

    @NotBlank(message = "图片信息你不能为空")
    private String imgUrl;
}
