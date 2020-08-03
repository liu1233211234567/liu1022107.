package com.fh.cart.controller;


import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.MamberAnnotation;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.fh.common.ServerEnum.CART_IS_NULL;

@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request){

        System.out.println("111");

        return cartService.buy(productId,count,request);
    }


    // 查询购物车中商品的数量
    @RequestMapping("queryCartProductCount")
    public ServerResponse queryCartProductCount(@MamberAnnotation Mamber mamber){
        //Mamber mamber = (Mamber) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + mamber.getUserId());
        long totalCount = 0;
        if(stringList != null && stringList.size()>0){
            for(String str:stringList){
                Cart cart = JSONObject.parseObject(str, Cart.class);
                totalCount+=cart.getCount();
            }
        }else{
          return ServerResponse.success(0);
        }
        return ServerResponse.success(totalCount);
    }

    // 查询购物车中的商品
    @RequestMapping("queryList")
    public ServerResponse queryList(@MamberAnnotation Mamber mamber){
        //Mamber mamber = (Mamber) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstant.CART_KEY + mamber.getUserId());
        List<Cart> cartList = new ArrayList<>();
        if(stringList != null && stringList.size()>0){
            for(String str:stringList){
                Cart cart = JSONObject.parseObject(str, Cart.class);
                cartList.add(cart);
            }
        }else{
            return ServerResponse.error(ServerEnum.CART_IS_NULL.getMsg());
        }
        return ServerResponse.success(cartList);
    }

    @RequestMapping("deleteCart/{productId}")
    public ServerResponse deleteCart(@MamberAnnotation Mamber mamber,@PathVariable("productId") Integer productId){
        RedisUtil.hdel(SystemConstant.CART_KEY + mamber.getUserId(),productId.toString());
        return ServerResponse.success();
    }



}
