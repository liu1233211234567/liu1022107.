package com.fh.cart.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerEnum;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse buy(Integer productId, Integer count, HttpServletRequest request) {
        // 查询该商品是否存在
        Product product = productService.selectProduct(productId);
        if(product==null){
            return ServerResponse.error(ServerEnum.PRODUCT_IS_NOT);// 返回此商品不存在
        }
        // 判断商品是否下架
        if(product.getIsHot()==0){
            return ServerResponse.error(ServerEnum.PRODUCT_IS_DOWN);// 此商品已下架
        }
        // 验证商品中是否有该商品
        Mamber mamber = (Mamber) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        boolean exist = RedisUtil.exist(SystemConstant.CART_KEY + mamber.getUserId(), productId.toString());
        if(!exist){
            // 如果购物车中不存在该商品
            Cart cart = new Cart();
            cart.setProductId(productId);
            cart.setCount(count);
            cart.setName(product.getName());
            cart.setPrice(product.getPrice());
            cart.setFilePath(product.getFilePath());
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+mamber.getUserId(),productId.toString(),jsonString);

        }else{
            // 如果存在就修改商品数量
            String productJson = RedisUtil.hget(SystemConstant.CART_KEY+mamber.getUserId(),productId.toString());
            Cart cart = JSONObject.parseObject(productJson, Cart.class);
            cart.setCount(cart.getCount()+count);
            // 修改完成之后存放到redis中
            String jsonString = JSONObject.toJSONString(cart);
            RedisUtil.hset(SystemConstant.CART_KEY+mamber.getUserId(),productId.toString(),jsonString);
        }
        return ServerResponse.success();
    }

}
