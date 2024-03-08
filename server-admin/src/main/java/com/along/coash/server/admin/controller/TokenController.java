package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.auth.AccessTokenQueryRequest;
import com.along.coash.server.admin.contract.auth.AccessTokenResponse;
import com.along.coash.server.admin.contract.log.enums.LoginLogTypeEnum;
import com.along.coash.server.admin.converter.TokenConverter;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.along.coash.server.admin.services.auth.AdminAuthService;
import com.along.coash.server.admin.services.token.TokenService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin-api/system/token")
public class TokenController {

    @Resource
    private TokenService tokenService;
    @Resource
    private AdminAuthService authService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:oauth2-token:page')")
    public CommonResult<Page<AccessTokenResponse>> getAccessTokenPage(@Valid AccessTokenQueryRequest request) {
        Page<AccessTokenDO> pageResult = tokenService.getAccessTokenPage(request);
        return CommonResult.success(TokenConverter.INSTANCE.convert(pageResult));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:oauth2-token:delete')")
    public CommonResult<Boolean> deleteAccessToken(@RequestParam("accessToken") String accessToken) {
        authService.logout(accessToken, LoginLogTypeEnum.LOGOUT_DELETE);
        return CommonResult.success(true);
    }

}
