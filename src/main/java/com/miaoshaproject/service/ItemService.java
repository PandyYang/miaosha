package com.miaoshaproject.service;

import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.service.model.ItemModel;

import java.util.List;

/**
 * @Author: Pandy
 * @Date: 2019/4/27 12:15
 * @Version 1.0
 */
public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BussinessException;

    //商品列表浏览
    List<ItemModel> listItem();

    //商品详情浏览
    ItemModel getItemById(Integer id);

    //库存扣减
    boolean decreaseStock(Integer itemId,Integer amount) throws BussinessException;

    //销量增加
    void increaseSales(Integer itemId,Integer amount) throws BussinessException;
}
