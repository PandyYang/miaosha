package com.miaoshaproject.controller;

import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Pandy
 * @Date: 2019/5/1 19:24
 * @Version 1.0
 */
@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount,
                                        @RequestParam(name = "promoId",required = false) Integer promoId)
                                        throws BussinessException {




        Boolean is_login = (Boolean)this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (is_login == null || !is_login.booleanValue()){
            throw new BussinessException(EnumBussinessError.USER_NOT_LOGIN);
        }
        //获取用户的登录信息
        System.out.println(this.httpServletRequest.getSession().getAttribute("LOGIN_USER"));
        System.out.println(this.httpServletRequest.getSession().getAttribute("IS_LOGIN"));
        UserModel userModel = (UserModel) this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
        OrderModel order = orderService.createOrder(userModel.getId(), itemId, promoId,amount);
        return CommonReturnType.create(null);

    }
}
