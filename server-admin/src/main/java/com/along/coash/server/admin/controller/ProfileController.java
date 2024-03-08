package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.profile.UserProfile;
import com.along.coash.server.admin.contract.profile.UserProfileUpdatePasswordRequest;
import com.along.coash.server.admin.contract.profile.UserProfileUpdateRequest;
import com.along.coash.server.admin.converter.UserConverter;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.entities.PostDO;
import com.along.coash.server.admin.entities.RoleDO;
import com.along.coash.server.admin.services.dept.DeptService;
import com.along.coash.server.admin.services.dept.PostService;
import com.along.coash.server.admin.services.permission.PermissionService;
import com.along.coash.server.admin.services.permission.RoleService;
import com.along.coash.server.admin.services.user.AdminUserService;
import com.along.coash.server.admin.utils.AuthUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin-api/system/user/profile")
public class ProfileController {

    @Autowired
    private AdminUserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PostService postService;

    @GetMapping("/get")
    public CommonResult<UserProfile> profile() {
        // 获得用户基本信息
        AdminUserDO user = userService.getUser(AuthUtils.getLoginUserId());
        List<RoleDO> userRoles = roleService.getRoleListFromCache(permissionService.getUserRoleIdListByUserId(user.getId()));
        // 获得部门信息
        DeptDO dept = null;
        if (user.getDeptId() != null) {
            dept = deptService.getDept(user.getDeptId());
        }
        // 获得岗位信息
        List<PostDO> posts = null;
        if (!CollectionUtils.isEmpty(user.getPostIds())) {
            posts = postService.getPostList(user.getPostIds());
        }

        UserProfile resp = UserConverter.INSTANCE.convertToUserProfile(user, userRoles, dept, posts);

        return CommonResult.success(resp);
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        userService.updateUserProfile(AuthUtils.getLoginUserId(), request);
        return CommonResult.success(true);
    }

    @PutMapping("/update-password")
    public CommonResult<Boolean> updateUserProfilePassword(@Valid @RequestBody UserProfileUpdatePasswordRequest request) {
        userService.updateUserPassword(AuthUtils.getLoginUserId(), request);
        return CommonResult.success(true);
    }
}
