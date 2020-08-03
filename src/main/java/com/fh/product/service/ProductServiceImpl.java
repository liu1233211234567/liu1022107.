package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService{

    @Resource
    private ProductMapper productMapper;


    @Override
    public List<Product> queryProductList() {
        List<Product> productList = productMapper.queryProductList();
        return productList;
    }

    @Override
    public List<Product> queryList() {
        return productMapper.queryList();
    }

    @Override
    public ServerResponse queryProductListPage(Long currentPage, Long pageSize) {
        Long start = (currentPage-1)*pageSize;
        // 查询总条数
        Long TotalCount = productMapper.queryTotalCount();

        List<Product> productList = productMapper.queryListPage(start,pageSize);
        Long totalPage = TotalCount%pageSize==0?TotalCount/pageSize:TotalCount%pageSize+1;
        Map map = new HashMap<>();
        map.put("list",productList);
        map.put("totalPage",totalPage);
        return ServerResponse.success(map);
    }


    // 购物车查询该商品是否存在
    @Override
    public Product selectProduct(Integer productId) {
        return productMapper.selectProduct(productId);
    }

    // 减库存 避免商品超卖的问题
    @Override
    public long updateStock(Integer id, int count) {
        return productMapper.updateStock(id,count);
    }

}
