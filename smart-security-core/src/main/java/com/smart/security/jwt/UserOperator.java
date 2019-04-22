package com.smart.security.jwt;


import com.smart.security.constants.ConstantsSecurity;
import com.smart.security.exception.SmartSecurityException;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户操作
 * @author guwenchang
 * @date 2019-04-22 15:52
 */
@Slf4j
@AllArgsConstructor
public class UserOperator {
    private static final String SMART_SECURITY_REQ_ATTR_USER = "smart-security-user";
    private static final int SEVEN = 7;

    private final JwtOperator jwtOperator;

    public User getUser() {
        try {
            HttpServletRequest request = getRequest();
            String token = getTokenFromRequest(request);
            Boolean isValid = jwtOperator.validateToken(token);
            if (!isValid) {
                return null;
            }

            Object userInReq = request.getAttribute(SMART_SECURITY_REQ_ATTR_USER);
            if (userInReq != null) {
                return (User) userInReq;
            }
            User user = getUserFromToken(token);
            request.setAttribute(SMART_SECURITY_REQ_ATTR_USER, user);
            return user;
        } catch (Exception e) {
            log.info("发生异常", e);
            throw new SmartSecurityException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private User getUserFromToken(String token) {
        // 从token中获取user
        Claims claims = jwtOperator.getClaimsFromToken(token);
        Object perms = claims.get(JwtOperator.PERMS);
        Object userId = claims.get(JwtOperator.USER_ID);
        Object username = claims.get(JwtOperator.USERNAME);

        return User.builder()
                .id((Integer) userId)
                .username((String) username)
                .perms((List<String>) perms)
                .build();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(ConstantsSecurity.AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(header)) {
            throw new SmartSecurityException("没有找到名为Authorization的header");
        }
        if (!header.startsWith(ConstantsSecurity.BEARER)) {
            throw new SmartSecurityException("token必须以'Bearer '开头");
        }
        if (header.length() <= SEVEN) {
            throw new SmartSecurityException("token非法，长度 <= 7");
        }
        return header.substring(SEVEN);
    }


    private static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if ((requestAttributes == null)) {
            throw new SmartSecurityException("requestAttributes为null");
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
