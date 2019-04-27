package com.miaoshaproject.controller;

import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 20:30
 * @Version 1.0
 */
public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";


    //定义exceptionghandler解决为被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {

        //必须自己封装data对象，否则data为exception反序列化的对象
        Map<String,Object> responseData = new HashMap<>();

        if(ex instanceof BussinessException){
            BussinessException bussinessException = (BussinessException) ex;
            responseData.put("errCode",bussinessException.getErrorCode());
            responseData.put("errMsg",bussinessException.getErrorMsg());
        } else {
            responseData.put("errCode", EnumBussinessError.UNKNOW_ERROR.getErrorCode());
            responseData.put("errMsg",EnumBussinessError.UNKNOW_ERROR.getErrorMsg());

        }
        return CommonReturnType.create(responseData,"fail");

    }
}
