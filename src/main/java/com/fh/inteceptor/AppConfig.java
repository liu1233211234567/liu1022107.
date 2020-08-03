package com.fh.inteceptor;


import com.fh.resolver.MamberResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

    @Autowired
    private MamberResolver mamberResolver;

    @Bean
    public LoginInteceptor loginInteceptor(){
        return new LoginInteceptor();
    }
    @Bean
    public IdempoTentInteceptor idempoTentInteceptor(){
        return new IdempoTentInteceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(loginInteceptor()).addPathPatterns("/**");
        // 接口的幂等性
        registry.addInterceptor(idempoTentInteceptor()).addPathPatterns("/**");
    }

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(mamberResolver);
    }
}