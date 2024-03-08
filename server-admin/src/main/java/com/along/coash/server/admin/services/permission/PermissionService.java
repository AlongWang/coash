package com.along.coash.server.admin.services.permission;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.utils.SpringUtils;
import com.along.coash.server.admin.constants.RedisKeyConstants;
import com.along.coash.server.admin.entities.MenuDO;
import com.along.coash.server.admin.entities.RoleDO;
import com.along.coash.server.admin.entities.RoleMenuDO;
import com.along.coash.server.admin.entities.UserRoleDO;
import com.along.coash.server.admin.mapper.RoleMenuMapper;
import com.along.coash.server.admin.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 权限 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class PermissionService {


    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;


    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void processUserDeleted(Long userId) {
        userRoleMapper.deleteListByUserId(userId);
    }

    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        return userRoleMapper.selectListByUserId(userId).stream().map(UserRoleDO::getRoleId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return Collections.emptySet();
        }

        // 如果是管理员的情况下，获取全部菜单编号
        if (roleService.hasAnySuperAdmin(roleIds)) {
            return menuService.getMenuList().stream().map(MenuDO::getId).collect(Collectors.toSet());
        }
        // 如果是非管理员的情况下，获得拥有的菜单编号
        return roleMenuMapper.selectListByRoleId(roleIds).stream().map(RoleMenuDO::getMenuId)
                .collect(Collectors.toSet());
    }

    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public void processMenuDeleted(Long menuId) {
        roleMenuMapper.deleteListByMenuId(menuId);
    }

    @Cacheable(value = RedisKeyConstants.MENU_ROLE_ID_LIST, key = "#menuId")
    public Set<Long> getMenuRoleIdListByMenuIdFromCache(Long menuId) {
        return roleMenuMapper.selectListByMenuId(menuId).stream()
                .map(RoleMenuDO::getRoleId)
                .collect(Collectors.toSet());
    }

    public boolean hasAnyPermissions(Long loginUserId, String... permissions) {
        // 如果为空，说明已经有权限
        if (permissions == null || permissions.length == 0) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        List<RoleDO> roles = getEnableUserRoleListByUserIdFromCache(loginUserId);
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        // 情况一：遍历判断每个权限，如果有一满足，说明有权限
        for (String permission : permissions) {
            if (hasAnyPermission(roles, permission)) {
                return true;
            }
        }

        // 情况二：如果是超管，也说明有权限
        return roleService.hasAnySuperAdmin(roles.stream().map(RoleDO::getId).collect(Collectors.toSet()));
    }

    private boolean hasAnyPermission(List<RoleDO> roles, String permission) {
        List<Long> menuIds = menuService.getMenuIdListByPermissionFromCache(permission);
        // 采用严格模式，如果权限找不到对应的 Menu 的话，也认为没有权限
        if (CollectionUtils.isEmpty(menuIds)) {
            return false;
        }

        // 判断是否有权限
        Set<Long> roleIds = roles.stream().map(RoleDO::getId).collect(Collectors.toSet());
        for (Long menuId : menuIds) {
            // 获得拥有该菜单的角色编号集合
            Set<Long> menuRoleIds = getSelf().getMenuRoleIdListByMenuIdFromCache(menuId);
            // 如果有交集，说明有权限
            if (CollectionUtils.containsAny(menuRoleIds, roleIds)) {
                return true;
            }
        }
        return false;
    }

    private List<RoleDO> getEnableUserRoleListByUserIdFromCache(Long userId) {
        // 获得用户拥有的角色编号
        Set<Long> roleIds = getSelf().getUserRoleIdListByUserIdFromCache(userId);
        // 获得角色数组，并移除被禁用的
        List<RoleDO> roles = roleService.getRoleListFromCache(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus()));
        return roles;
    }

    @Cacheable(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public Set<Long> getUserRoleIdListByUserIdFromCache(Long userId) {
        return getUserRoleIdListByUserId(userId);
    }

    public boolean hasAnyRoles(Long loginUserId, String... roles) {
        // 如果为空，说明已经有权限
        if (roles == null || roles.length == 0) {
            return true;
        }

        // 获得当前登录的角色。如果为空，说明没有权限
        List<RoleDO> roleList = getEnableUserRoleListByUserIdFromCache(loginUserId);
        if (CollectionUtils.isEmpty(roleList)) {
            return false;
        }

        // 判断是否有角色
        Set<String> userRoles = roleList.stream().map(RoleDO::getCode).collect(Collectors.toSet());
        return CollectionUtils.containsAny(userRoles, List.of(roles));
    }

    @Transactional
    @CacheEvict(value = RedisKeyConstants.MENU_ROLE_ID_LIST, allEntries = true)
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 获得角色拥有菜单编号
        Set<Long> dbMenuIds = roleMenuMapper.selectListByRoleId(roleId).stream()
                .map(RoleMenuDO::getMenuId).collect(Collectors.toSet());
        // 计算新增和删除的菜单编号
        Set<Long> createMenuIds = new HashSet<>(menuIds);
        createMenuIds.removeAll(dbMenuIds);
        Collection<Long> deleteMenuIds = new HashSet<>(dbMenuIds);
        deleteMenuIds.removeAll(menuIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtils.isEmpty(createMenuIds)) {
            roleMenuMapper.insertBatch(createMenuIds.stream().map(p -> {
                RoleMenuDO entity = new RoleMenuDO();
                entity.setRoleId(roleId);
                entity.setMenuId(p);
                return entity;
            }).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(deleteMenuIds)) {
            roleMenuMapper.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
    }

    public void assignRoleDataScope(Long roleId, Integer dataScope, Set<Long> dataScopeDeptIds) {
        roleService.updateRoleDataScope(roleId, dataScope, dataScopeDeptIds);
    }

    @Transactional
    @CacheEvict(value = RedisKeyConstants.USER_ROLE_ID_LIST, key = "#userId")
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // 获得角色拥有角色编号
        Set<Long> dbRoleIds = userRoleMapper.selectListByUserId(userId).stream()
                .map(UserRoleDO::getRoleId).collect(Collectors.toSet());
        // 计算新增和删除的角色编号
        Collection<Long> createRoleIds = new HashSet<>(roleIds);
        createRoleIds.removeAll(dbRoleIds);
        Collection<Long> deleteMenuIds = new HashSet<>(dbRoleIds);
        deleteMenuIds.removeAll(roleIds);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (!CollectionUtils.isEmpty(createRoleIds)) {
            userRoleMapper.insertBatch(createRoleIds.stream().map(roleId -> {
                UserRoleDO entity = new UserRoleDO();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(deleteMenuIds)) {
            userRoleMapper.deleteListByUserIdAndRoleIdIds(userId, deleteMenuIds);
        }
    }

    private PermissionService getSelf() {
        return SpringUtils.getBean(getClass());
    }
}
