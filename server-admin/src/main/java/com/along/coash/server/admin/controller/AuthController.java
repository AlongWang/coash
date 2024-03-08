package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.constants.SecurityConstants;
import com.along.coash.server.admin.contract.auth.LoginRequest;
import com.along.coash.server.admin.contract.auth.LoginResponse;
import com.along.coash.server.admin.contract.auth.PermissionInfo;
import com.along.coash.server.admin.contract.log.enums.LoginLogTypeEnum;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.MenuDO;
import com.along.coash.server.admin.entities.RoleDO;
import com.along.coash.server.admin.services.auth.AdminAuthService;
import com.along.coash.server.admin.services.permission.MenuService;
import com.along.coash.server.admin.services.permission.PermissionService;
import com.along.coash.server.admin.services.permission.RoleService;
import com.along.coash.server.admin.services.user.AdminUserService;
import com.along.coash.server.admin.utils.AuthUtils;
import com.along.coash.server.admin.converter.AuthConverter;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin-api/system/auth")
public class AuthController {

    @Autowired
    private AdminAuthService authService;
    @Autowired
    private AdminUserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;


    @PostMapping("/login")
    public CommonResult<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return CommonResult.success(authService.login(request));
    }

    @PostMapping("/logout")
    @PermitAll
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (StringUtils.hasLength(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF);
        }
        return CommonResult.success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    public CommonResult<LoginResponse> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return CommonResult.success(authService.refreshToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    public CommonResult<PermissionInfo> getPermissionInfo() {
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(AuthUtils.getLoginUserId());
        if (user == null) {
            return null;
        }

        // 1.2 获得角色列表
        Set<Long> roleIds = permissionService.getUserRoleIdListByUserId(AuthUtils.getLoginUserId());
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色

        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(roles.stream().map(RoleDO::getId)
                .collect(Collectors.toSet()));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus())); // 移除禁用的菜单

        // 2. 拼接结果返回
        return CommonResult.success(AuthConverter.INSTANCE.convert(user, roles, menuList));
    }
}
