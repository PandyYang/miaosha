package com.miaoshaproject.controller;

import com.miaoshaproject.controller.viewobject.ItemVo;
import com.miaoshaproject.error.BussinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Pandy
 * @Date: 2019/4/27 17:54
 * @Version 1.0
 * 为什么要有VO层
 * 在企业级开发中 VO用来做controller层的信息的转换 与 恢复
 * 因为前台的数据
 */
@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    //创建商品
    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price")BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl
                                       ) throws BussinessException {
        //封装service请求来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setStock(stock);
        itemModel.setPrice(price);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setTitle(title);

        ItemModel itemModelForReturn = itemService.createItem(itemModel);

        ItemVo itemVo = convertVOFromModel(itemModelForReturn);
        //前后端是使用json传输数据 返回的是对象 必须加上responsebody注解  否则就是404异常
        return CommonReturnType.create(itemVo);

    }


    //商品详情页的浏览功能
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVo itemVo = convertVOFromModel(itemModel);
        return CommonReturnType.create(itemVo);
    }


    //商品列表页面浏览
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem(){
        List<ItemModel> itemVoList = itemService.listItem();
        //使用streamAPI将 list内的itemModel转化为itemVo
        List<ItemVo> itemVos = itemVoList.stream().map(itemModel -> {
            ItemVo itemVo = this.convertVOFromModel(itemModel);
            return itemVo;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemVoList);
    }



    //将前台传过来的值转化为model中的值
    private ItemVo convertVOFromModel(ItemModel itemModel){

        if (itemModel == null){
            return null;
        }
        ItemVo itemVo = new ItemVo();
        BeanUtils.copyProperties(itemModel,itemVo);
        return itemVo;
    }
}
