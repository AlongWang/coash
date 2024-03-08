package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.entities.RefreshTokenDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper extends BaseMapper<RefreshTokenDO> {

    default int deleteByRefreshToken(String refreshToken) {
        return delete(new LambdaQueryWrapper<RefreshTokenDO>()
                .eq(RefreshTokenDO::getRefreshToken, refreshToken));
    }

    default RefreshTokenDO selectByRefreshToken(String refreshToken) {
        return selectOne(new LambdaQueryWrapper<RefreshTokenDO>().eq(RefreshTokenDO::getRefreshToken, refreshToken));
    }

}
