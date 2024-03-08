package com.along.coash.server.admin.services.permission;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.framework.utils.SpringUtils;
import com.along.coash.server.admin.constants.RedisKeyConstants;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.permission.enums.DataScopeEnum;
import com.along.coash.server.admin.contract.role.RoleCreateRequest;
import com.along.coash.server.admin.contract.role.RoleQueryRequest;
import com.along.coash.server.admin.contract.role.RoleUpdateRequest;
import com.along.coash.server.admin.contract.role.enums.RoleCodeEnum;
import com.along.coash.server.admin.contract.role.enums.RoleTypeEnum;
import com.along.coash.server.admin.converter.RoleConverter;
import com.along.coash.server.admin.entities.RoleDO;
import com.along.coash.server.admin.mapper.RoleMapper;
import com.along.coash.server.admin.mapper.RoleMenuMapper;
import com.along.coash.server.admin.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    public List<RoleDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectBatchIds(ids);
    }

    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }

        return ids.stream().anyMatch(id -> {
            RoleDO role = SpringUtils.getBean(getClass()).getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id", unless = "#result == null")
    public RoleDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }

    public List<RoleDO> getRoleListFromCache(Set<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        RoleService self = getSelf();
        return roleIds.stream().map(self::getRoleFromCache).collect(Collectors.toList());
    }

    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        RoleDO updateObject = new RoleDO();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    private void validateRoleForUpdate(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        if (roleDO == null) {
            throw new ServiceException(ErrorCodeConstants.ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getType())) {
            throw new ServiceException(ErrorCodeConstants.ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateRequest request, Integer type) {
        // 校验角色
        validateRoleDuplicate(request.getName(), request.getCode(), null);
        // 插入到数据库
        RoleDO role = RoleConverter.INSTANCE.convert(request);
        role.setType(type == null ? RoleTypeEnum.CUSTOM.getType() : type);
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setDataScope(DataScopeEnum.ALL.getScope());
        roleMapper.insert(role);
        // 返回
        return role.getId();
    }

    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#request.id")
    public void updateRole(RoleUpdateRequest request) {
        // 校验是否可以更新
        validateRoleForUpdate(Long.parseLong(request.getId()));
        // 校验角色的唯一字段是否重复
        validateRoleDuplicate(request.getName(), request.getCode(), Long.parseLong(request.getId()));

        // 更新到数据库
        RoleDO updateObj = RoleConverter.INSTANCE.convert(request);
        roleMapper.updateById(updateObj);
    }

    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleStatus(Long id, Integer status) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新状态
        RoleDO updateObj = new RoleDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        roleMapper.updateById(updateObj);
    }

    public RoleDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    public Page<RoleDO> getRolePage(RoleQueryRequest request) {
        return roleMapper.selectPage(request);
    }

    public List<RoleDO> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, allEntries = true),
            @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, allEntries = true),
            @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    })
    public void deleteRole(Long id) {
        // 校验是否可以更新
        validateRoleForUpdate(id);
        // 标记删除
        roleMapper.deleteById(id);
        // 标记删除 UserRole
        userRoleMapper.deleteListByRoleId(id);
        // 标记删除 RoleMenu
        roleMenuMapper.deleteListByRoleId(id);
    }

    private void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw new ServiceException(ErrorCodeConstants.ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.ROLE_CODE_DUPLICATE, code);
        }
    }

    public RoleService getSelf() {
        return SpringUtils.getBean(getClass());
    }

}
