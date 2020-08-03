package com.fh.product.controller;


import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 重新轮播图
    @RequestMapping("queryProductList")
    @Ignore
    public ServerResponse queryProductList(){
        List<Product> productList = productService.queryProductList();
        return ServerResponse.success(productList);
    }

    // 重新左浮图片
    @RequestMapping("queryList")
    @Ignore
    public ServerResponse queryList(){
        List<Product> productList = productService.queryList();
        return ServerResponse.success(productList);
    }

    //无限滚动  （分页）
    @RequestMapping("queryProductListPage")
    @Ignore
    public ServerResponse queryProductListPage(Long currentPage, Long pageSize){
        return productService.queryProductListPage(currentPage,pageSize);
    }



}
