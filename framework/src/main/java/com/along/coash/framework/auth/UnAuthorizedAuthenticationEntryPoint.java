package com.along.coash.framework.auth;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.GlobalErrorCodeConstants;
import com.along.coash.framework.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class UnAuthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ServletUtils.writeJSONResponse(response, CommonResult.error(GlobalErrorCodeConstants.UNAUTHORIZED));
    }
}
