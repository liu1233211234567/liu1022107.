package com.fh.product.service;

import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> queryProductList();

    List<Product> queryList();


    ServerResponse queryProductListPage(Long currentPage, Long pageSize);

    Product selectProduct(Integer productId);

    long updateStock(Integer id, int count);
}
