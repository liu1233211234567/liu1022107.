package com.fh.cart.service;

import com.fh.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    ServerResponse buy(Integer productId, Integer count, HttpServletRequest request);
}
