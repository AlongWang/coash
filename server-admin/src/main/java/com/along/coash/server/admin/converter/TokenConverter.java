package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.auth.AccessTokenResponse;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenConverter {

    TokenConverter INSTANCE = Mappers.getMapper(TokenConverter.class);


    Page<AccessTokenResponse> convert(Page<AccessTokenDO> page);


}
