package com.miaoshaproject.service;

import com.miaoshaproject.service.model.PromoModel;

/**
 * @Author: Pandy
 * @Date: 2019/5/2 10:33
 * @Version 1.0
 */
public interface PromoService {
    //根据itemid获得即将或者正在进行的商品
    PromoModel getPromoByItemId(Integer itemId);
}
