package com.fh.mamber.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.mamber.mapper.MamberMapper;
import com.fh.mamber.model.Mamber;
import com.fh.mamber.service.MamberService;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class MamberServiceImpl implements MamberService{
    @Autowired
    private MamberMapper mamberMapper;

    @Override
    public ServerResponse userNameList(String userName) {
      Mamber mamber= mamberMapper.userNameList(userName);
      if(mamber==null){
        return ServerResponse.success();
      }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse phoneNumberList(String phoneNumber) {
        Mamber mamber=mamberMapper.phoneNumberList(phoneNumber);
        if(mamber==null){
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    @Override
    public ServerResponse addMamberList(Mamber mamber) {
        String cadel = RedisUtil.get(mamber.getPhoneNumber());
        if(cadel==null){
            return ServerResponse.error("验证码失效");
        }
        if(!cadel.equals(mamber.getUserCode())){
            return ServerResponse.error("验证码不正确");
        }
        mamberMapper.addMamberList(mamber);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(Mamber mamber) {

       /* QueryWrapper<Mamber> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName",mamber.getUserName());
        queryWrapper.or();
        queryWrapper.eq("phoneNumber",mamber.getUserName());

        Mamber mamberDb = mamberMapper.selectOne(queryWrapper);*/
        Mamber mamberDb = mamberMapper.selectUserName(mamber.getUserName(),mamber.getPhoneNumber());
        // 判断用户名
        if(mamberDb == null){
            return ServerResponse.error("该用户名不存在！！！");
        }
        // 判断密码
        if(!mamber.getUserPassword().equals(mamberDb.getUserPassword())){
            return ServerResponse.error("密码错误！！！");
        }

        // 用户名 密码 正确   生成 token
        String token = "";
        try {
            String toJSONString = JSONObject.toJSONString(mamberDb);
            String encodejson = URLEncoder.encode(toJSONString, "utf-8");
            token = JwtUtil.sign(encodejson);
            RedisUtil.setex(SystemConstant.TOKEN_KEY+token,token,SystemConstant.TOKEN_EXPIRE_TIME);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ServerResponse.success(token);
    }
}
