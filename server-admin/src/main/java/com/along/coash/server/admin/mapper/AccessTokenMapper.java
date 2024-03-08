package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.auth.AccessTokenQueryRequest;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AccessTokenMapper extends BaseMapper<AccessTokenDO> {

    default AccessTokenDO selectByAccessToken(String accessToken) {
        return selectOne(new LambdaQueryWrapper<AccessTokenDO>().eq(AccessTokenDO::getAccessToken, accessToken));
    }

    default List<AccessTokenDO> selectListByRefreshToken(String refreshToken) {
        return selectList(new LambdaQueryWrapper<AccessTokenDO>().eq(AccessTokenDO::getRefreshToken, refreshToken));
    }

    default Page<AccessTokenDO> selectPage(AccessTokenQueryRequest request) {
        LambdaQueryWrapper<AccessTokenDO> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(request.getUserId())) {
            query = query.eq(AccessTokenDO::getUserId, request.getUserId());
        }
        if (request.getUserType() != null) {
            query = query.eq(AccessTokenDO::getUserType, request.getUserType());
        }
        query = query.gt(AccessTokenDO::getExpiresTime, LocalDateTime.now())
                .orderByDesc(AccessTokenDO::getId);

        Page<AccessTokenDO> pageParam = new Page<>(request.getPageNo(), request.getPageSize());
        return selectPage(pageParam, query);

    }

}
