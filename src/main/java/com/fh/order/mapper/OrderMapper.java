package com.fh.order.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.order.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Insert("insert into t_order (mamberId,addressId,payType,status,totalPrice,createDate) values (#{mamberId},#{addressId},#{payType},#{status},#{totalPrice},#{createDate})")
    void addOrder(Order order);
}
