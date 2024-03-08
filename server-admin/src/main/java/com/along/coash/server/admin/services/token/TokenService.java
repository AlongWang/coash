package com.along.coash.server.admin.services.token;

import com.along.coash.framework.contract.GlobalErrorCodeConstants;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.framework.utils.IdUtils;
import com.along.coash.server.admin.constants.RedisKeyConstants;
import com.along.coash.server.admin.contract.auth.AccessTokenQueryRequest;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.along.coash.server.admin.entities.RefreshTokenDO;
import com.along.coash.server.admin.mapper.AccessTokenMapper;
import com.along.coash.server.admin.mapper.RefreshTokenMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private AccessTokenMapper accessTokenMapper;
    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Transactional
    public AccessTokenDO createAccessToken(Long userId, Integer userType) {
        // 创建刷新令牌
        RefreshTokenDO refreshTokenDO = createRefreshToken(userId, userType);
        // 创建访问令牌
        return createAccessTokenData(refreshTokenDO);
    }

    public AccessTokenDO refreshAccessToken(String refreshToken) {
        // 查询访问令牌
        RefreshTokenDO refreshTokenDO = refreshTokenMapper.selectByRefreshToken(refreshToken);
        if (refreshTokenDO == null) {
            throw new ServiceException(GlobalErrorCodeConstants.BAD_REQUEST, "无效的刷新令牌");
        }

        // 移除相关的访问令牌
        List<AccessTokenDO> accessTokens = accessTokenMapper.selectListByRefreshToken(refreshToken);
        if (!CollectionUtils.isEmpty(accessTokens)) {
            for (AccessTokenDO accessToken : accessTokens) {
                removeAccessToken(accessToken.getAccessToken());
            }
        }

        // 已过期的情况下，删除刷新令牌
        if (LocalDateTime.now().isAfter(refreshTokenDO.getExpiresTime())) {
            refreshTokenMapper.deleteById(refreshTokenDO.getId());
            throw new ServiceException(GlobalErrorCodeConstants.UNAUTHORIZED, "刷新令牌已过期");
        }

        // 创建访问令牌
        return createAccessTokenData(refreshTokenDO);
    }

    @Cacheable(value = RedisKeyConstants.AUTH_ACCESS_TOKEN, key = "#accessToken", unless = "#result == null")
    public AccessTokenDO getAccessToken(String accessToken) {
        return accessTokenMapper.selectByAccessToken(accessToken);
    }

    public AccessTokenDO checkAccessToken(String accessToken) {
        AccessTokenDO accessTokenDO = getAccessToken(accessToken);
        if (accessTokenDO == null) {
            throw new ServiceException(GlobalErrorCodeConstants.UNAUTHORIZED, "访问令牌不存在");
        }
        if (LocalDateTime.now().isAfter(accessTokenDO.getExpiresTime())) {
            throw new ServiceException(GlobalErrorCodeConstants.UNAUTHORIZED, "访问令牌已过期");
        }
        return accessTokenDO;
    }

    @CacheEvict(value = RedisKeyConstants.AUTH_ACCESS_TOKEN, key = "#accessToken")
    public AccessTokenDO removeAccessToken(String accessToken) {
        // 删除访问令牌
        AccessTokenDO accessTokenDO = accessTokenMapper.selectByAccessToken(accessToken);
        if (accessTokenDO == null) {
            return null;
        }
        accessTokenMapper.deleteById(accessTokenDO.getId());
        // 删除刷新令牌
        refreshTokenMapper.deleteByRefreshToken(accessTokenDO.getRefreshToken());
        return accessTokenDO;
    }

    public Page<AccessTokenDO> getAccessTokenPage(AccessTokenQueryRequest request) {
        return accessTokenMapper.selectPage(request);
    }

    private AccessTokenDO createAccessTokenData(RefreshTokenDO refreshTokenDO) {
        AccessTokenDO accessTokenDO = AccessTokenDO.builder()
                .accessToken(generateAccessToken())
                .userId(refreshTokenDO.getUserId())
                .userType(refreshTokenDO.getUserType())
                .refreshToken(refreshTokenDO.getRefreshToken())
                .expiresTime(LocalDateTime.now().plusMinutes(30))
                .build();
        accessTokenDO.setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        accessTokenMapper.insert(accessTokenDO);
        return accessTokenDO;
    }

    private RefreshTokenDO createRefreshToken(Long userId, Integer userType) {
        RefreshTokenDO refreshToken = RefreshTokenDO.builder()
                .refreshToken(generateRefreshToken())
                .userId(userId)
                .userType(userType)
                .expiresTime(LocalDateTime.now().plusMinutes(60 * 4))
                .build();
        refreshToken.setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        refreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    private static String generateAccessToken() {
        return IdUtils.getUUID();
    }

    private static String generateRefreshToken() {
        return IdUtils.getUUID();
    }

}
