package com.miaoshaproject.controller.viewobject;

import lombok.Data;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 18:47
 * @Version 1.0
 * 模型对象 用来整合向前端传递的数据
 * 因为并非所有的从底层整合查询到的数据
 * 都是前台必须的部分 所以在模型对象中
 * 对数据进行必要的处理
 * 注意要确保字段一致类型一致 否则会出现数据丢失的问题
 */
@Data
public class UserVo {

    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telephone;

}
