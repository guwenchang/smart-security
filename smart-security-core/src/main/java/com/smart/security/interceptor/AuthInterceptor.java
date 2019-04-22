package com.smart.security.interceptor;


import com.smart.security.annotation.Action;
import com.smart.security.annotation.Login;
import com.smart.security.annotation.Permission;
import com.smart.security.exception.SmartSecurityException;
import com.smart.security.jwt.User;
import com.smart.security.jwt.UserOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 安全拦截器
 * @author guwenchang
 * @date 2019-04-22 16:28
 */
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {
    private final UserOperator userOperator;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //默认都需要权限
            Login needLogin = handlerMethod.getMethodAnnotation(Login.class);
            if (needLogin == null || needLogin.action().equals(Action.NORMAL)) {
                User user = userOperator.getUser();
                //登录校验
                if (user == null) {
                    log.warn("需要登陆:{},ParameterMap:{}",request.getRequestURI(),request.getParameterMap());
                    throw new SmartSecurityException("需要登陆:" + request.getRequestURI() + ",ParameterMap:" + request.getParameterMap());
                }
                //权限校验
                boolean checkResult;
                Permission permission = handlerMethod.getMethodAnnotation(Permission.class);
                if (permission != null) {
                    String perm = permission.value();
                    if (StringUtils.isEmpty(perm)){
                        checkResult = true;
                    }
                    List<String> userPerms = user.getPerms();
                    if (CollectionUtils.isEmpty(userPerms)) {
                        checkResult = false;
                    }else {
                        checkResult = userPerms.contains(perm);
                    }
                    if (!checkResult) {
                        log.warn("没有权限，userPermsFromToken = {}, perm = {}", userPerms, perm);
                        throw new SmartSecurityException("需要登陆:" + request.getRequestURI() + ",ParameterMap:" + request.getParameterMap());
                    }
                }
            }
            return super.preHandle(request, response, handler);
        }
        return true;
    }
}

