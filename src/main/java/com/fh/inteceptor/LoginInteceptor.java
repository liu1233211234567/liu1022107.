package com.fh.inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.common.LoginException;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInteceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 处理客户端传过来的自定义的头信息
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,mtoken,content-type");
        //理客户端传过来的 get ,delete,post
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT,POST,DELETE,GET");

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断该方法是否被拦截，如果有@ignore注解就放行
        if(method.isAnnotationPresent(Ignore.class)){
            return true;
        }

        // 获取请求头信息里的 token
        String token = request.getHeader("x-auth");
        // 如果token为空则返回登录页面
        if(StringUtils.isEmpty(token)){
            throw new LoginException();
        }

        // 验证token是否失效
        boolean exist = RedisUtil.exist(SystemConstant.TOKEN_KEY+token);
        if(!exist){
            // token失效
            throw new LoginException();
        }

        //验证token
        boolean res = JwtUtil.verify(token);
        if(res){
            String userString = JwtUtil.getUser(token);
            String jsonUser = URLDecoder.decode(userString,"utf-8");
            Mamber mamber = JSONObject.parseObject(jsonUser, Mamber.class);
            request.getSession().setAttribute(SystemConstant.SESSION_KEY,mamber);
            request.getSession().setAttribute(SystemConstant.TOKEN_KEY,token);
            System.out.println(mamber.toString());
        }else{
            throw new LoginException();
        }



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
