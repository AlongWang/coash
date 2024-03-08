package com.along.coash.server.admin.config.auth;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.framework.exception.GlobalExceptionHandler;
import com.along.coash.framework.security.ISecurityFilter;
import com.along.coash.framework.utils.ServletUtils;
import com.along.coash.server.admin.constants.SecurityConstants;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.along.coash.server.admin.services.token.TokenService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter implements ISecurityFilter {

    private final TokenService tokenService;

    private final GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (StringUtils.hasLength(token)) {
            try {
                LoginUser loginUser = buildLoginUserByToken(token);

                // 2. 设置当前用户
                if (loginUser != null) {
                    // 创建 Authentication
                    Authentication authentication = buildAuthentication(loginUser, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(ex);
                ServletUtils.writeJSONResponse(response, result);
                return;
            }
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        // 创建 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private LoginUser buildLoginUserByToken(String token) {
        try {
            AccessTokenDO accessToken = tokenService.checkAccessToken(token);
            if (accessToken == null) {
                return null;
            }
            // 构建登录用户
            LoginUser loginUser = new LoginUser();
            loginUser.setId(accessToken.getUserId());
            loginUser.setUserType(accessToken.getUserType());
            return loginUser;
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (!StringUtils.hasText(token)) {
            return null;
        }
        return token;
    }

    @Data
    public static class LoginUser {

        /**
         * 用户编号
         */
        private Long id;
        /**
         * 用户类型
         */
        private Integer userType;

        /**
         * 上下文字段，不进行持久化
         * <p>
         * 1. 用于基于 LoginUser 维度的临时缓存
         */
        @JsonIgnore
        private Map<String, Object> context;

        public void setContext(String key, Object value) {
            if (context == null) {
                context = new HashMap<>();
            }
            context.put(key, value);
        }

        public Object getContext(String key) {
            return context.get(key);
        }
    }
}
