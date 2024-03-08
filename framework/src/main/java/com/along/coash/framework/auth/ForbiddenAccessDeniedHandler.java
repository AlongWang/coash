package com.along.coash.framework.auth;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.GlobalErrorCodeConstants;
import com.along.coash.framework.utils.ServletUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class ForbiddenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ServletUtils.writeJSONResponse(response, CommonResult.error(GlobalErrorCodeConstants.FORBIDDEN));
    }
}
