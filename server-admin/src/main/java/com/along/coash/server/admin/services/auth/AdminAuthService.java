package com.along.coash.server.admin.services.auth;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.framework.utils.ServletUtils;
import com.along.coash.server.admin.contract.auth.LoginRequest;
import com.along.coash.server.admin.contract.auth.LoginResponse;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.log.LoginLogCreateRequest;
import com.along.coash.server.admin.contract.log.enums.LoginLogTypeEnum;
import com.along.coash.server.admin.contract.log.enums.LoginResultEnum;
import com.along.coash.server.admin.contract.user.UserTypeEnum;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.services.log.LoginLogService;
import com.along.coash.server.admin.services.token.TokenService;
import com.along.coash.server.admin.services.user.AdminUserService;
import com.along.coash.server.admin.converter.AuthConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminAuthService {

    @Autowired
    private AdminUserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Autowired
    private TokenService oauth2TokenService;

    public LoginResponse login(LoginRequest request) {
        // 使用账号密码，进行登录
        AdminUserDO user = authenticate(request.getUsername(), request.getPassword());

        // 创建 Token 令牌，记录登录日志
        return createLoginToken(user.getId(), LoginLogTypeEnum.LOGIN_USERNAME, request.getUsername());
    }

    public void logout(String token, LoginLogTypeEnum logTypeEnum) {
        // 删除访问令牌
        AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        // 删除成功，则记录登出日志
        createLogoutLog(accessTokenDO.getUserId(), logTypeEnum);
    }


    private AdminUserDO authenticate(String username, String password) {
        // 校验账号是否存在
        AdminUserDO user = userService.getUserByUsername(username);
        if (user == null) {
            createLoginLog(null, username, LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.BAD_CREDENTIALS);
            throw new ServiceException(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), username, LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.BAD_CREDENTIALS);
            throw new ServiceException(ErrorCodeConstants.AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
            createLoginLog(user.getId(), username, LoginLogTypeEnum.LOGIN_USERNAME, LoginResultEnum.USER_DISABLED);
            throw new ServiceException(ErrorCodeConstants.AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }


    public LoginResponse refreshToken(String refreshToken) {
        AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken);
        return AuthConverter.INSTANCE.convert(accessTokenDO);
    }

    private void createLoginLog(Long userId, String username, LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        LoginLogCreateRequest request = LoginLogCreateRequest.builder()
                .userId(String.valueOf(userId))
                .username(username)
                .logType(logTypeEnum.getType())
                .userAgent(ServletUtils.getUserAgent())
                .userIp(ServletUtils.getClientIP())
                .result(loginResult.getResult())
                .build();
        loginLogService.createLoginLog(request);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    private LoginResponse createLoginToken(Long userId, LoginLogTypeEnum logTypeEnum, String username) {
        // 插入登陆日志
        createLoginLog(userId, username, logTypeEnum, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, UserTypeEnum.ADMIN.getValue());
        // 构建返回结果
        return AuthConverter.INSTANCE.convert(accessTokenDO);
    }

    private void createLogoutLog(Long userId, LoginLogTypeEnum logTypeEnum) {
        createLoginLog(userId, getUsername(userId), logTypeEnum, LoginResultEnum.SUCCESS);
    }

    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }
}
