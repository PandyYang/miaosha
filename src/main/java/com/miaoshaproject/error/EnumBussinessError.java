package com.miaoshaproject.error;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 19:17
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
public enum EnumBussinessError implements CommonError {

    //通用错误类型00001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOW_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或者密码不合法"),
    USER_NOT_LOGIN(20003,"用户未登录"),
    //30000开头 为交易错误
    STOCK_NOT_ENOUGH(30001,"库存不足")
    ;


    private int errCode;
    private String errMsg;
    @Override
    public int getErrorCode() {
        return this.errCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
