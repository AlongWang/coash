package com.along.coash.server.admin.services.permission;

import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.constants.RedisKeyConstants;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.menu.MenuCreateRequest;
import com.along.coash.server.admin.contract.menu.MenuQueryRequest;
import com.along.coash.server.admin.contract.menu.MenuUpdateRequest;
import com.along.coash.server.admin.contract.menu.enums.MenuTypeEnum;
import com.along.coash.server.admin.converter.MenuConverter;
import com.along.coash.server.admin.entities.MenuDO;
import com.along.coash.server.admin.mapper.MenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#request.permission",
            condition = "#request.permission != null")
    public Long createMenu(MenuCreateRequest request) {
        // 校验父菜单存在
        validateParentMenu(Long.parseLong(request.getParentId()), null);
        // 校验菜单（自己）
        validateMenu(Long.parseLong(request.getParentId()), request.getName(), null);

        // 插入数据库
        MenuDO menu = MenuConverter.INSTANCE.convert(request);
        initMenuProperty(menu);
        menuMapper.insert(menu);
        // 返回
        return menu.getId();
    }

    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void updateMenu(MenuUpdateRequest request) {
        // 校验更新的菜单是否存在
        if (menuMapper.selectById(request.getId()) == null) {
            throw new ServiceException(ErrorCodeConstants.MENU_NOT_EXISTS);
        }
        // 校验父菜单存在
        validateParentMenu(Long.parseLong(request.getParentId()), Long.parseLong(request.getId()));
        // 校验菜单（自己）
        validateMenu(Long.parseLong(request.getParentId()), request.getName(), Long.parseLong(request.getId()));

        // 更新到数据库
        MenuDO updateObject = MenuConverter.INSTANCE.convert(request);
        initMenuProperty(updateObject);
        menuMapper.updateById(updateObject);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, allEntries = true)
    public void deleteMenu(Long id) {
        // 校验是否还有子菜单
        if (menuMapper.selectCountByParentId(id) > 0) {
            throw new ServiceException(ErrorCodeConstants.MENU_EXISTS_CHILDREN);
        }
        // 校验删除的菜单是否存在
        if (menuMapper.selectById(id) == null) {
            throw new ServiceException(ErrorCodeConstants.MENU_NOT_EXISTS);
        }
        // 标记删除
        menuMapper.deleteById(id);
    }

    public List<MenuDO> getMenuList() {
        return menuMapper.selectList(new MenuQueryRequest());
    }

    public List<MenuDO> getMenuList(MenuQueryRequest request) {
        return menuMapper.selectList(request);
    }

    @Cacheable(value = RedisKeyConstants.PERMISSION_MENU_ID_LIST, key = "#permission")
    public List<Long> getMenuIdListByPermissionFromCache(String permission) {
        List<MenuDO> menus = menuMapper.selectListByPermission(permission);
        return menus.stream().map(MenuDO::getId).collect(Collectors.toList());
    }

    public MenuDO getMenu(Long id) {
        return menuMapper.selectById(id);
    }

    public List<MenuDO> getMenuList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return menuMapper.selectBatchIds(ids);
    }

    private void validateParentMenu(Long parentId, Long childId) {
        if (parentId == null || 0L == parentId) {
            return;
        }
        // 不能设置自己为父菜单
        if (parentId.equals(childId)) {
            throw new ServiceException(ErrorCodeConstants.MENU_PARENT_ERROR);
        }
        MenuDO menu = menuMapper.selectById(parentId);
        // 父菜单不存在
        if (menu == null) {
            throw new ServiceException(ErrorCodeConstants.MENU_PARENT_NOT_EXISTS);
        }
        // 父菜单必须是目录或者菜单类型
        if (!MenuTypeEnum.DIR.getType().equals(menu.getType())
                && !MenuTypeEnum.MENU.getType().equals(menu.getType())) {
            throw new ServiceException(ErrorCodeConstants.MENU_PARENT_NOT_DIR_OR_MENU);
        }
    }

    private void validateMenu(Long parentId, String name, Long id) {
        MenuDO menu = menuMapper.selectByParentIdAndName(parentId, name);
        if (menu == null) {
            return;
        }
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
        if (!menu.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.MENU_NAME_DUPLICATE);
        }
    }

    private void initMenuProperty(MenuDO menu) {
        if (MenuTypeEnum.BUTTON.getType().equals(menu.getType())) {
            menu.setComponent("");
            menu.setComponentName("");
            menu.setIcon("");
            menu.setPath("");
        }
    }
}
