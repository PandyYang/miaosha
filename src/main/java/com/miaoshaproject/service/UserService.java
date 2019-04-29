package com.miaoshaproject.service;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.BaseController;
import com.miaoshaproject.controller.UserController;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 17:52
 * @Version 1.0
 */
public interface UserService {

    //通过用户id获取用户对象
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BussinessException;
    UserModel validateLogin(String telephone,String encrptPassword) throws BussinessException;

}
