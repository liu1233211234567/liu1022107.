package com.fh.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.Idempotent;
import com.fh.common.MamberAnnotation;
import com.fh.common.ServerResponse;
import com.fh.mamber.model.Mamber;
import com.fh.order.service.OrderService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {


    @Autowired
    private OrderService orderService;


    @RequestMapping("bulidOrder")
    @Idempotent
    public ServerResponse bulidOrder(String listStr, Integer addressId, Integer payType,@MamberAnnotation Mamber mamber){
      // 判断送货清单中是否有商品
        List<Cart> cartList = new ArrayList<>();
        if(StringUtils.isNotBlank(listStr)){
            cartList = JSONObject.parseArray(listStr, Cart.class);
        }else{
            ServerResponse.error("请选择商品！！！");
        }
        return orderService.bulidOrder(cartList,addressId,payType,mamber);
    }

    // 验证幂等性
    @RequestMapping("getToken")
    public ServerResponse getToken(){
        String mtoken = UUID.randomUUID().toString();
        RedisUtil.set(mtoken,mtoken);
        return ServerResponse.success(mtoken);
    }


}
