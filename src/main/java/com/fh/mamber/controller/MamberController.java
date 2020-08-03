package com.fh.mamber.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import com.fh.mamber.service.MamberService;
import com.fh.util.MessageVerifyUtils;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("MamberController")
@RestController
@CrossOrigin
public class MamberController {
    @Autowired
    private MamberService mamberService;

    @RequestMapping("userNameList")
    @Ignore
    public ServerResponse userNameList(String userName){
        return mamberService.userNameList(userName);
    }

    @RequestMapping("phoneNumberList")
    @Ignore
    public ServerResponse phoneNumberList(String phoneNumber){
        return mamberService.phoneNumberList(phoneNumber);
    }

    // 验证码
    @RequestMapping("nameList")
    @Ignore
    public ServerResponse nameList(String phoneNumber){
        String newcode = MessageVerifyUtils.getNewcode();
        try {
            SendSmsResponse sendSmsResponse = MessageVerifyUtils.sendSms(phoneNumber, newcode);
            if(StringUtils.isNotBlank(sendSmsResponse.getCode()) && sendSmsResponse.getCode().equals("OK")){
                RedisUtil.set(phoneNumber,newcode);
                return ServerResponse.success();
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return ServerResponse.error();
    }

    // 注册
    @RequestMapping("addUserList")
    @Ignore
    public ServerResponse addMamberList(Mamber mamber){
        return mamberService.addMamberList(mamber);
    }

    // 登录  login
    @RequestMapping("login")
    @Ignore
    public ServerResponse login(Mamber mamber){
        return mamberService.login(mamber);
    }

    // 当点击购物车时看用户是否登录
    @RequestMapping("checkLogin")
    @Ignore
    public ServerResponse checkLogin(HttpServletRequest request){
        Mamber mamber = (Mamber) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        if(mamber==null){
            ServerResponse.error();
        }
        return ServerResponse.success();
    }


    // token失效
    @RequestMapping("out")
    @Ignore
    public ServerResponse out(HttpServletRequest request){
        // 让token失效
        String token = (String) request.getSession().getAttribute(SystemConstant.TOKEN_KEY);
        RedisUtil.del(SystemConstant.TOKEN_KEY+token);
        request.getSession().removeAttribute(SystemConstant.TOKEN_KEY);
        // 清除session中的用户信息
        request.getSession().removeAttribute(SystemConstant.SESSION_KEY);
        return ServerResponse.success();
    }

}
