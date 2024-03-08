package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.contract.menu.*;
import com.along.coash.server.admin.converter.MenuConverter;
import com.along.coash.server.admin.entities.MenuDO;
import com.along.coash.server.admin.services.permission.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/admin-api/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:menu:create')")
    public CommonResult<Long> createMenu(@Valid @RequestBody MenuCreateRequest request) {
        Long menuId = menuService.createMenu(request);
        return CommonResult.success(menuId);
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:menu:update')")
    public CommonResult<Boolean> updateMenu(@Valid @RequestBody MenuUpdateRequest request) {
        menuService.updateMenu(request);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:menu:delete')")
    public CommonResult<Boolean> deleteMenu(@RequestParam("id") String id) {
        menuService.deleteMenu(Long.parseLong(id));
        return CommonResult.success(true);
    }

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public CommonResult<List<Menu>> getMenuList(MenuQueryRequest request) {
        List<MenuDO> list = menuService.getMenuList(request);
        list.sort(Comparator.comparing(MenuDO::getSort));
        return CommonResult.success(MenuConverter.INSTANCE.convert(list));
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<MenuSimple>> getSimpleMenuList() {
        // 获得菜单列表，只要开启状态的
        MenuQueryRequest queryRequest = new MenuQueryRequest();
        queryRequest.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<MenuDO> list = menuService.getMenuList(queryRequest);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(MenuDO::getSort));
        return CommonResult.success(MenuConverter.INSTANCE.convertToSimple(list));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('system:menu:query')")
    public CommonResult<Menu> getMenu(String id) {
        MenuDO menu = menuService.getMenu(Long.parseLong(id));
        return CommonResult.success(MenuConverter.INSTANCE.convert(menu));
    }
}
