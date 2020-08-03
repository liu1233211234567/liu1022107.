package com.fh.resolver;

import com.fh.common.MamberAnnotation;
import com.fh.common.SystemConstant;
import com.fh.mamber.model.Mamber;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class MamberResolver implements  HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if(methodParameter.hasParameterAnnotation(MamberAnnotation.class)){
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest WebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = WebRequest.getNativeRequest(HttpServletRequest.class);
        Mamber mamber = (Mamber) request.getSession().getAttribute(SystemConstant.SESSION_KEY);
        return mamber ;
    }
}
