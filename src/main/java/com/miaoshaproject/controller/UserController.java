package com.miaoshaproject.controller;

import com.alibaba.druid.util.StringUtils;
import com.miaoshaproject.controller.viewobject.UserVo;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 17:49
 * @Version 1.0
 */
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam("telephone")String telephone,
                                  @RequestParam("password")String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if (org.apache.commons.lang3.StringUtils.isEmpty(telephone)
                || org.apache.commons.lang3.StringUtils.isEmpty(password)){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserModel userModel = userService.validateLogin(telephone,this.EncodeByMd5(password));

        //加入到用户登录成功的session中
            //加入会话标识
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN_USER",userModel);

        return CommonReturnType.create(null);

    }






    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam("telephone")String telephone,
                                     @RequestParam("otpCode")String otpCode,
                                     @RequestParam("name")String name,
                                     @RequestParam("gender")Byte gender,
                                     @RequestParam("age")Integer age,
                                     @RequestParam("password")String password)
            throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号与otpcode符合
        String inSessopnOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telephone);
        if (!StringUtils.equals(otpCode,inSessopnOtpCode)){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }


    //用户获取opt短信的接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam("telephone")String telephone){
        //生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String optCode = String.valueOf(randomInt);


        //将otp验证码与用户的手机号码关联
            //使用httpSession绑定手机号与optcode
        httpServletRequest.getSession().setAttribute(telephone,optCode);



        //将otp验证码通过短信通道发送给用户
        System.out.println("telephone =" + telephone + "&otpCode = " +optCode);
        return CommonReturnType.create(null);
    }


    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BussinessException{
        //调用service服务获取对应id的对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的对象用户信息不存在
        if (userModel == null){
            throw new BussinessException(EnumBussinessError.USER_NOT_EXIST);
        }





        //将核心领域模型用户对象可供UI使用的viewObject
        UserVo userVo =  convertFromModel(userModel);

        //返回通用对象(正确的信息)
            //status字段 success
            //data字段 data
        return CommonReturnType.create(userVo);
    }

    //把userModel的数据传递的数据处理之后传递给前台
    private UserVo convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }


    /*//定义exceptionHandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(HttpServletRequest request,Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if (ex instanceof BussinessException){
            //当status为fail的时候 返回值强制转为errCode + errMsg的形式
            BussinessException bussinessException = (BussinessException)ex;
            responseData.put("errCode",bussinessException.getErrorCode());
            responseData.put("errMsg",bussinessException.getErrorMsg());
        }else {
            responseData.put("errCode",EnumBussinessError.UBKNOW_ERROR.getErrorCode());
            responseData.put("errMsg",EnumBussinessError.UBKNOW_ERROR.getErrorMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }*/
}
