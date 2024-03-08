package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.log.LoginLogCreateRequest;
import com.along.coash.server.admin.contract.log.LoginLogResponse;
import com.along.coash.server.admin.entities.LoginLogDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface LoginLogConverter {

    LoginLogConverter INSTANCE = Mappers.getMapper(LoginLogConverter.class);

    Page<LoginLogResponse> convert(Page<LoginLogDO> page);

    LoginLogDO convert(LoginLogCreateRequest bean);

}
