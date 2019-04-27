package com.miaoshaproject.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 18:03
 * @Version 1.0
 * 模型对象 用来整合给controller层传递的数据
 * 该model用来整合两张表之间的数据
 * 由于用户表中没有包含密码属性
 * 而是将加密的密码单独放在了一张表中
 * 所以现在将其进行转换
 */
@Data
public class UserModel {
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    private String name;
    @NotNull(message = "性别为必填项")
    private Byte gender;
    @NotNull(message = "年龄不能不填写")
    @Min(value=0,message = "年龄必须大于0岁")
    @Max(value = 150,message = "年龄必须小于150岁")
    private Integer age;
    @NotBlank(message = "密码不能为空")
    private String telephone;
    private String registerMode;
    private String thirdPartId;

    private String encrptPassword;
}
