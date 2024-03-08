package com.along.coash.server.admin.services.log;

import com.along.coash.server.admin.contract.log.LoginLogCreateRequest;
import com.along.coash.server.admin.contract.log.LoginLogQueryRequest;
import com.along.coash.server.admin.converter.LoginLogConverter;
import com.along.coash.server.admin.entities.LoginLogDO;
import com.along.coash.server.admin.mapper.LoginLogMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 登录日志 Service 实现
 */
@Service
@Validated
public class LoginLogService {

    @Resource
    private LoginLogMapper loginLogMapper;

    public Page<LoginLogDO> getLoginLogPage(LoginLogQueryRequest request) {
        return loginLogMapper.selectPage(request);
    }

    public void createLoginLog(LoginLogCreateRequest request) {
        LoginLogDO loginLog = LoginLogConverter.INSTANCE.convert(request);
        loginLogMapper.insert(loginLog);
    }

}
