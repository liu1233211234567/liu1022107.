package com.fh.common;

public class SystemConstant {

    public final  static  String SESSION_KEY="user";
    public final  static  int COOKIE_EXPIRY_TIME=7*24*60*60;
    public final  static  String COOKIE_COOLOE_TIME="cooike_user";
    public final  static  int COOKIE_REIDS_TIME=5*60;
    public final  static  String CART_KEY="cart:";
    public final  static  String TOKEN_KEY="token:";
    public final  static int TOKEN_EXPIRE_TIME = 30 * 60 * 1000;
    public final  static int ORDER_STATUS_WAIT = 1;
    public final  static int ORDER_STATUS_FINISH = 2;

    public final  static String REDIS_CATEGORY_KEY= "categoryList";
}
