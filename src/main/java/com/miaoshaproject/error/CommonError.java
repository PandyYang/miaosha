package com.miaoshaproject.error;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 19:15
 * @Version 1.0
 */
public interface CommonError {
    public int getErrorCode();
    public String getErrorMsg();
    public CommonError setErrorMsg(String ErrorMsg);


}
