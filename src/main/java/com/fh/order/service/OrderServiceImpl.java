package com.fh.order.service;


import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import com.fh.order.mapper.OrderInFoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderInFoMapper orderInFoMapper;
    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public ServerResponse bulidOrder(List<Cart> cartList, Integer addressId, Integer payType, Mamber mamber) {


        String orderId = IdUtil.createId();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        //商品总价格
        BigDecimal totalPrice = new BigDecimal("0.00");

        //库存不足的集合
        List<String> stockNotFull = new ArrayList<>();

        for (Cart cart : cartList) {
            Product product = productService.selectProduct(cart.getProductId());
            if (product.getStatus() < cart.getCount()) {
                //库存不足
                stockNotFull.add(cart.getName());
            }
            //减库存   判断库存是否充足
            Long res = productService.updateStock(product.getId(), cart.getCount());
            if (res == 1) {
                //库存充足 生成订单详情
                OrderInfo orderInfo = buildOrderInfo(orderId, cart);
                orderInfoList.add(orderInfo);
                BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(), cart.getCount() + "");
                totalPrice = BigDecimalUtil.add(totalPrice, subTotal);
            } else {
                //库存不足
                stockNotFull.add(cart.getName());
            }
        }

        //生成订单 先判断是否有库存不足的商品
        if (orderInfoList != null && orderInfoList.size() == cartList.size()) {
            //库存都足  保存订单详细
            for (OrderInfo orderInfo : orderInfoList) {
                orderInFoMapper.addInFo(orderInfo);
                //更新redis购物车
                updateRedisCart(mamber, orderInfo);
            }
            //  生成订单
            //buildOrder(payType, addressId, mamber, orderId, totalPrice);
            return ServerResponse.success(orderId);
        } else {
            return ServerResponse.error(stockNotFull);
        }
    }

    private void buildOrder(Integer payType, Integer addressId, Mamber mamber, Integer orderId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setId(orderId);
        order.setMamberId(mamber.getUserId());
        order.setTotalPrice(totalPrice);
        order.setStatus(SystemConstant.ORDER_STATUS_WAIT);
        orderMapper.addOrder(order);
    }

    private OrderInfo buildOrderInfo(String orderId, Cart cart) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setName(cart.getName());
        orderInfo.setFilePath(cart.getFilePath());
        orderInfo.setPrice(cart.getPrice());
        orderInfo.setOrderId(orderId);
        orderInfo.setProductId(cart.getProductId());
        orderInfo.setCount(cart.getCount());
        return orderInfo;
    }


    private void updateRedisCart(Mamber member, OrderInfo orderInfo) {
        String cartJson = RedisUtil.hget(SystemConstant.CART_KEY + member.getUserId(), orderInfo.getProductId().toString());
        if(StringUtils.isNotEmpty(cartJson)){
            Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
            if(cart1.getCount()<=orderInfo.getCount()){
                //删除购物车中该商品
                RedisUtil.hdel(SystemConstant.CART_KEY + member.getUserId(), orderInfo.getProductId().toString());
            }else{
                //更新购物车
                cart1.setCount(cart1.getCount()-orderInfo.getCount());
                String s = JSONObject.toJSONString(cart1);
                RedisUtil.hset(SystemConstant.CART_KEY + member.getUserId(), orderInfo.getProductId().toString(),s);
            }
        }
    }


}
