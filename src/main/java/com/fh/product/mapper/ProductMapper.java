package com.fh.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.common.ServerResponse;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    // 查询轮播图
    @Select("select * from t_product where isHot = 1")
    List<Product> queryProductList();

    // 查询左浮动图片
    @Select("select * from t_product")
    List<Product> queryList();

    @Select("select count(*) from t_product")
    Long queryTotalCount();

    // 查询无线滚动的商品 （涉及分页）
    @Select("select * from t_product order by id desc limit #{start},#{pageSize}")
    List<Product> queryListPage(@Param("start") Long start,@Param("pageSize") Long pageSize);

    // 购物车查询该商品是否存在
    @Select("select * from t_product where id = #{id}")
    Product selectProduct(@Param("id") Integer productId);

    long updateStock(@Param("id") Integer id,@Param("count") int count);
}
