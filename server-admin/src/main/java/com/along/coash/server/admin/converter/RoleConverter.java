package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.role.Role;
import com.along.coash.server.admin.contract.role.RoleCreateRequest;
import com.along.coash.server.admin.contract.role.RoleSimple;
import com.along.coash.server.admin.contract.role.RoleUpdateRequest;
import com.along.coash.server.admin.entities.RoleDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    Page<Role> convert(Page<RoleDO> page);

    RoleDO convert(RoleUpdateRequest request);

    Role convert(RoleDO roleDO);

    RoleDO convert(RoleCreateRequest request);

    List<RoleSimple> convert(List<RoleDO> roles);

    RoleSimple convertToSimple(RoleDO roleDO);
}
