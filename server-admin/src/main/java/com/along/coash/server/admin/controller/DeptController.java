package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.contract.dept.*;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.services.dept.DeptService;
import com.along.coash.server.admin.converter.DeptConverter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/admin-api/system/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @PostMapping("create")
    @PreAuthorize("@ss.hasPermission('system:dept:create')")
    public CommonResult<Long> createDept(@Valid @RequestBody DeptCreateRequest request) {
        Long deptId = deptService.createDept(request);
        return CommonResult.success(deptId);
    }

    @PutMapping("update")
    @PreAuthorize("@ss.hasPermission('system:dept:update')")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DeptUpdateRequest request) {
        deptService.updateDept(request);
        return CommonResult.success(true);
    }

    @DeleteMapping("delete")
    @PreAuthorize("@ss.hasPermission('system:dept:delete')")
    public CommonResult<Boolean> deleteDept(@RequestParam("id") String id) {
        deptService.deleteDept(Long.parseLong(id));
        return CommonResult.success(true);
    }

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public CommonResult<List<DeptInfo>> getDeptList(DeptQueryRequest request) {
        List<DeptDO> list = deptService.getDeptList(request);
        list.sort(Comparator.comparing(DeptDO::getSort));
        return CommonResult.success(DeptConverter.INSTANCE.convert(list));
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<DeptInfoSimple>> getSimpleDeptList() {
        // 获得部门列表，只要开启状态的
        DeptQueryRequest reqVO = new DeptQueryRequest();
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<DeptDO> list = deptService.getDeptList(reqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(DeptDO::getSort));
        return CommonResult.success(DeptConverter.INSTANCE.convertSimple(list));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public CommonResult<DeptInfo> getDept(@RequestParam("id") String id) {
        return CommonResult.success(DeptConverter.INSTANCE.convert(deptService.getDept(Long.parseLong(id))));
    }
}
