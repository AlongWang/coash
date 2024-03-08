package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.log.LoginLogQueryRequest;
import com.along.coash.server.admin.contract.log.enums.LoginResultEnum;
import com.along.coash.server.admin.entities.LoginLogDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;


@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogDO> {
    default Page<LoginLogDO> selectPage(LoginLogQueryRequest request) {
        LambdaQueryWrapper<LoginLogDO> query = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(request.getUserIp())) {
            query = query.like(LoginLogDO::getUserIp, request.getUserIp());
        }
        if (StringUtils.hasLength(request.getUsername())) {
            query = query.like(LoginLogDO::getUsername, request.getUsername());
        }
        if (request.getStartTime() != null) {
            query = query.gt(LoginLogDO::getCreateTime, request.getStartTime());
        }
        if (request.getEndTime() != null) {
            query = query.le(LoginLogDO::getCreateTime, request.getEndTime());
        }
        if (Boolean.TRUE.equals(request.getStatus())) {
            query.eq(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        } else if (Boolean.FALSE.equals(request.getStatus())) {
            query.gt(LoginLogDO::getResult, LoginResultEnum.SUCCESS.getResult());
        }
        query.orderByDesc(LoginLogDO::getCreateTime);
        Page<LoginLogDO> page = new Page<>(request.getPageNo(), request.getPageSize());
        return selectPage(page, query);
    }
}
