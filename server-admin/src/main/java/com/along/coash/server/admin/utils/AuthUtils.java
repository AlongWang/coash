package com.along.coash.server.admin.utils;


import com.along.coash.server.admin.config.auth.AuthenticationTokenFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static Long getLoginUserId() {
        AuthenticationTokenFilter.LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    public static AuthenticationTokenFilter.LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof AuthenticationTokenFilter.LoginUser ?
                (AuthenticationTokenFilter.LoginUser) authentication.getPrincipal() : null;
    }

    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }
}
