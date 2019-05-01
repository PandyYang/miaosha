package com.miaoshaproject.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 18:58
 * @Version 1.0
 * 该类负责处理前台返回数值
 * 类型以及必要的异常信息处理
 * 也就是指根据业务逻辑的错误方式去展示 而不是使用
 * 框架默认值
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonReturnType {

    //表名对应返回的请求处理结果 success file等等
    private String status;
    //success
        //data返回json数据
    //fail
        //data使用通用的错误码格式
    private Object data;

    //定义通用的创建方法
        //下面是success所进行的处理
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status){
        CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus(status);
        commonReturnType.setData(result);
        return commonReturnType;
    }
}
