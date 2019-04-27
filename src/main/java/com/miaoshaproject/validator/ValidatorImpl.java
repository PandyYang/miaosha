package com.miaoshaproject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @Auther: Zihaoo
 * @Date: 2019/4/4
 */
@Component
public class ValidatorImpl implements InitializingBean {


    private Validator validator;

    public ValidationResult validate(Object bean) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);//校验bean  有错误放在数组中
        if (constraintViolationSet.size() > 0) {
            // 有错误
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation -> {
                String errMsg = constraintViolation.getMessage();//遍历数组 获取错误信息
                String propertyName = constraintViolation.getPropertyPath().toString(); //获取相关路径
                result.getErrorMsgMap().put(propertyName, errMsg);//填入构造器
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将hibernate validator通过工厂的初始化方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

}
