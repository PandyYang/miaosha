package com.miaoshaproject.service.impl;

import com.miaoshaproject.dao.ItemDOMapper;
import com.miaoshaproject.dao.ItemStockDOMapper;
import com.miaoshaproject.dataobject.ItemDO;
import com.miaoshaproject.dataobject.ItemStockDO;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.error.EnumBussinessError;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Pandy
 * @Date: 2019/4/27 12:18
 * @Version 1.0
 */
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private ItemDOMapper itemDOMapper;

    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    //将传入的类型转换为商品普通属性
    private ItemDO convertItemDOFromItemModel(ItemModel itemModel){
        if(itemModel == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        //itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    //将传入的类型转换为商品库存属性
    private ItemStockDO convertItemStockDOFromItemModel(ItemModel itemModel){
        if (itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());//itemModel 是前台传递的pojo类型 取出其中的id作为库存的id 这个需要在mapper文件中进行修改并进行特别的指定
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    //创建新的商品 需要同时修改通用属性以及相关的库存属性
    /**
     * 前台传递过来的属性在这里分成两个部分 一个是itemDO中需要存储的
     * 一个是
     * @param itemModel
     * @return
     * @throws BussinessException
     */
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BussinessException {

        //校验入参
        if (validator.validate(itemModel).isHasErrors()){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }

        //转化itemmodel ->dataobject
            //数据转换过滤  只要往item传送的数据
        ItemDO itemDO = this.convertItemDOFromItemModel(itemModel);
            //item普通属性写入数据库(剥离库存)
        System.out.println("------------------->");
        itemDOMapper.insertSelective(itemDO);
        System.out.println("-------------------<");

        //为itemstock关联itemid
        itemModel.setId(itemDO.getId());
            //数据转换过滤 只要往itemstock中传送数据
        ItemStockDO itemStockDO = this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }


    @Override
    public List<ItemModel> listItem() {
        List<ItemDO> itemDOList = itemDOMapper.listItem();
        //java8的语法 将itemDO map成itemModel 函数式得进行参数的拼接
        List<ItemModel> itemModels = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = this.convertModelFromObject(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModels;
    }

    /**
     * 根据id传回对象
     * @param id
     * @return
     */
    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null){
            return null;
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);
        //将dataobject-->model
        ItemModel itemModel = convertModelFromObject(itemDO,itemStockDO);
        return itemModel;
    }


    //读取数据转换之后向前台传递
    private ItemModel convertModelFromObject(ItemDO itemDO,ItemStockDO itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);

        //将itemdo中的属性赋值给model之后 取出stock中的库存  转换itemdo中的价格 也赋值给itemmodel  然后返回
        //itemModel.setPrice(new BigDecimal(itemDO.getPrice()));

        //
        itemModel.setStock(itemStockDO.getStock());

        return itemModel;
    }

}
