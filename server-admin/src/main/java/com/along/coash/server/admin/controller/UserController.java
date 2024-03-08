package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.contract.user.*;
import com.along.coash.server.admin.converter.UserConverter;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.services.dept.DeptService;
import com.along.coash.server.admin.services.user.AdminUserService;
import com.along.coash.server.admin.converter.DeptConverter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin-api/system/user")
public class UserController {

    @Autowired
    private AdminUserService userService;
    @Autowired
    private DeptService deptService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateRequest request) {
        Long id = userService.createUser(request);
        return CommonResult.success(id);
    }

    @PutMapping("update")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return CommonResult.success(true);
    }

    @PutMapping("/update-password")
    @PreAuthorize("@ss.hasPermission('system:user:update-password')")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdateRequest request) {
        userService.updateUserPassword(Long.parseLong(request.getId()), request.getPassword());
        return CommonResult.success(true);
    }

    @PutMapping("/update-status")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateRequest reqVO) {
        userService.updateUserStatus(Long.parseLong(reqVO.getId()), reqVO.getStatus());
        return CommonResult.success(true);
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<IPage<UserPageItem>> getUserPage(@Valid UserQueryRequest request) {
        // 获得用户分页列表
        IPage<AdminUserDO> pageResult = userService.getUserPage(request);
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return CommonResult.success(new Page<>(0, pageResult.getTotal())); // 返回空
        }

        // 获得拼接需要的数据
        Collection<Long> deptIds = pageResult.getRecords().stream().map(AdminUserDO::getDeptId).collect(Collectors.toList());
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        // 拼接结果返回
        List<UserPageItem> userList = new ArrayList<>(pageResult.getRecords().size());
        pageResult.getRecords().forEach(user -> {
            UserPageItem userPageItem = UserConverter.INSTANCE.convert(user);
            userPageItem.setDept(UserConverter.INSTANCE.convert(deptMap.get(user.getDeptId())));
            userList.add(userPageItem);
        });
        IPage<UserPageItem> page = new Page<>();
        page.setRecords(userList);
        page.setTotal(pageResult.getTotal());
        return CommonResult.success(page);
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<UserSimple>> getSimpleUserList() {
        // 获用户列表，只要开启状态的
        List<AdminUserDO> list = userService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        return CommonResult.success(UserConverter.INSTANCE.convert(list));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('system:user:query')")
    public CommonResult<UserPageItem> getUser(@RequestParam("id") String id) {
        AdminUserDO user = userService.getUser(Long.parseLong(id));
        // 获得部门数据
        DeptDO dept = deptService.getDept(user.getDeptId());
        UserPageItem usr = UserConverter.INSTANCE.convert(user);
        usr.setDept(DeptConverter.INSTANCE.convertToDept(dept));
        return CommonResult.success(usr);
    }
}
