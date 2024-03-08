package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.log.LoginLogQueryRequest;
import com.along.coash.server.admin.contract.log.LoginLogResponse;
import com.along.coash.server.admin.converter.LoginLogConverter;
import com.along.coash.server.admin.entities.LoginLogDO;
import com.along.coash.server.admin.services.log.LoginLogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin-api/system/login-log")
@Validated
public class LoginLogController {

    @Resource
    private LoginLogService loginLogService;

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:login-log:query')")
    public CommonResult<Page<LoginLogResponse>> getLoginLogPage(@Valid LoginLogQueryRequest request) {
        Page<LoginLogDO> page = loginLogService.getLoginLogPage(request);
        return CommonResult.success(LoginLogConverter.INSTANCE.convert(page));
    }

}
