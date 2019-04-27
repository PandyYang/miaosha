package com.miaoshaproject.error;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 19:23
 * @Version 1.0
 * 包装器业务异常类实现
 * 可以传入覆盖 也可以使用自定义的错误值类型
 */

public class BussinessException extends Exception implements CommonError  {

    private CommonError commonError;

    //直接接受EmbussinessError的传参用于构造业务异常
    public BussinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }

    //接受自定义errMsg的方式构造业务异常
    public BussinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrorMsg(errMsg);
    }



    @Override
    public int getErrorCode() {
        return this.commonError.getErrorCode();
    }

    @Override
    public String getErrorMsg() {
        return this.commonError.getErrorMsg();
    }

    @Override
    public CommonError setErrorMsg(String ErrorMsg) {
        this.commonError.setErrorMsg(ErrorMsg);
        return this;
    }
}
