package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.auth.LoginResponse;
import com.along.coash.server.admin.contract.auth.PermissionInfo;
import com.along.coash.server.admin.contract.menu.enums.MenuTypeEnum;
import com.along.coash.server.admin.entities.AccessTokenDO;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.MenuDO;
import com.along.coash.server.admin.entities.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface AuthConverter {

    AuthConverter INSTANCE = Mappers.getMapper(AuthConverter.class);

    LoginResponse convert(AccessTokenDO bean);

    default PermissionInfo.MenuVO convertTreeNode(MenuDO menu) {
        if (menu == null) {
            return null;
        }

        PermissionInfo.MenuVO.MenuVOBuilder menuVO = PermissionInfo.MenuVO.builder();

        if (menu.getId() != null) {
            menuVO.id(String.valueOf(menu.getId()));
        }
        if (menu.getParentId() != null) {
            menuVO.parentId(String.valueOf(menu.getParentId()));
        }
        menuVO.name(menu.getName());
        menuVO.path(menu.getPath());
        menuVO.component(menu.getComponent());
        menuVO.componentName(menu.getComponentName());
        menuVO.icon(menu.getIcon());
        menuVO.visible(menu.getVisible());
        menuVO.keepAlive(menu.getKeepAlive());
        menuVO.alwaysShow(menu.getAlwaysShow());

        return menuVO.build();
    }

    default PermissionInfo convert(AdminUserDO user, List<RoleDO> roleList, List<MenuDO> menuList) {
        return PermissionInfo.builder()
                .user(PermissionInfo.UserVO.builder().id(String.valueOf(user.getId())).nickname(user.getNickname())
                        .avatar(user.getAvatar()).build())
                .roles(roleList.stream().map(RoleDO::getCode).collect(Collectors.toSet()))
                // 权限标识信息
                .permissions(menuList.stream().map(MenuDO::getPermission).collect(Collectors.toSet()))
                // 菜单树
                .menus(buildMenuTree(menuList))
                .build();
    }

    default List<PermissionInfo.MenuVO> buildMenuTree(List<MenuDO> menuList) {
        // 移除按钮
        menuList.removeIf(menu -> menu.getType().equals(MenuTypeEnum.BUTTON.getType()));
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(MenuDO::getSort));

        // 构建菜单树
        // 使用 LinkedHashMap 的原因，是为了排序 。实际也可以用 Stream API ，就是太丑了。
        Map<String, PermissionInfo.MenuVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(String.valueOf(menu.getId()), convertTreeNode(menu)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !node.getParentId().equals("0")).forEach(childNode -> {
            // 获得父节点
            PermissionInfo.MenuVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        return treeNodeMap.values().stream().filter(p -> "0".equals(p.getParentId())).collect(Collectors.toList());
    }
}
