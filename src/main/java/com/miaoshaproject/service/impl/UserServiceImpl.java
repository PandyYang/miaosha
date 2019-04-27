package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.UserDOMapper;
import com.miaoshaproject.dao.UserPasswordDoMapper;
import com.miaoshaproject.dataobject.UserDO;
import com.miaoshaproject.dataobject.UserPasswordDo;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import javax.xml.bind.ValidationEvent;
import java.lang.annotation.Target;

/**
 * @Author: Pandy
 * @Date: 2019/4/26 17:52
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDoMapper userPasswordDoMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdaomapper获取到对应的用户dataobject 获取到的数据中不包含密码属性
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (userDO == null){
            return null;
        }

        //通过用户id获得用户的加密密码信息 用户不为空则获取相应的密码
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(id);

        //在返回值中将 用户普通属性部分  与密码部分进行整合 返回新的model对象
        return convertFromDataObject(userDO,userPasswordDo);
    }

    //用户注册
    @Override
    @Transactional
    public void register(UserModel userModel) throws BussinessException {
        if (userModel == null){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        //将usermodel作为bean  执行错误的校验规则
        ValidationResult validate = validator.validate(userModel);
        if (validate.isHasErrors()){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,validate.getErrMsg());
        }


        /*if(StringUtils.isEmpty(userModel.getName())
                || userModel.getGender() == null
                || userModel.getAge() == null
                || StringUtils.isEmpty(userModel.getTelephone())){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }*/
        //UserDO userDO = new UserDO();
        //ValidationResult result =
        //实现model->dataobject方法
            //实现一般属性的转换
        UserDO userDO = convertFromModel(userModel);

        try {
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException ex){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }

        //实现加密属性的转换
            //获取到userId  这个是password表的特有属性 必须在psswordmapper.xml文件中指定自增类型以及 相关的主键 否则事务回滚插入失败
        userModel.setId(userDO.getId());
        UserPasswordDo userPasswordDo = convertPasswordFromModel(userModel);
            //判断不为null 加入 如果为null 则不会修改
        userPasswordDoMapper.insertSelective(userPasswordDo);
            //insert直接加入
        //userPasswordDoMapper.insert(userPasswordDo);
    }




    //验证登录的合法性
    @Override
    public UserModel validateLogin(String telephone, String encrptPassword) throws BussinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelephone(telephone);
            //没有找到手机号
        if (userDO == null){
            throw new BussinessException(EnumBussinessError.USER_LOGIN_FAIL);
        }
            //通过手机号找到正确的加密信息 与用户的加密信息相比对
        UserPasswordDo userPasswordDo = userPasswordDoMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDo);


        //通过用户的信息 比对用户的密码和加密的密码是不是相匹配
        if (!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BussinessException(EnumBussinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }





    //将model对象重新转为原来的user普通对象属性(没有加密密码)
    private UserDO convertFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
    //将model对象重新转变为原来的user对象中的密码的加密属性
    private UserPasswordDo convertPasswordFromModel(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserPasswordDo passwordDo = new UserPasswordDo();
        //不能使用copy 之前使用是属性全部转换 现在这个两个属性中只转换一个
        passwordDo.setEncrptPassword(userModel.getEncrptPassword());
        passwordDo.setUserId(userModel.getId());
        return passwordDo;
    }

    //整合用户普通属性与加密密码属性
    private UserModel convertFromDataObject(UserDO userDo, UserPasswordDo userPasswordDo){
        if (userDo==null){
            return null;
        }

        //新创建的userModel中包含了 整合之后的所有需要的字段
        UserModel userModel = new UserModel();
        //ff问我为何源码中的 断言没有作废 方法问题 版本问题 将后者中的属性赋给前者
        BeanUtils.copyProperties(userDo,userModel);

        //将加密密码注入进model 注意不能使用上面的copy  因为会存在属性重复问题
        if (userPasswordDo != null){
            userModel.setEncrptPassword(userPasswordDo.getEncrptPassword());
        }

        return userModel;
    }
}
