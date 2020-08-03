package com.fh.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.order.model.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderInFoMapper extends BaseMapper<OrderInfo> {

    @Insert("insert into t_order_info (orderId,productId,filePath,name,count,price) values (#{orderId},#{productId},#{filePath},#{name},#{count},#{price})")
    void addInFo(OrderInfo orderInfo);
}
