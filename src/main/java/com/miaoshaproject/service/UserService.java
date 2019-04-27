package com.miaoshaproject.service;

import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 17:52
 * @Version 1.0
 */
public interface UserService {

    //通过用户id获取用户对象
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BussinessException;

    /**
     *
     * @param telephone 用户注册的手机
     * @param encrptPassword  用户加密后的密码
     * @throws BussinessException
     */
    UserModel validateLogin(String telephone,String encrptPassword) throws BussinessException;
}
