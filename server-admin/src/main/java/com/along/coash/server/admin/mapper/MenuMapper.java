package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.menu.MenuQueryRequest;
import com.along.coash.server.admin.entities.MenuDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

    default MenuDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getParentId, parentId)
                .eq(MenuDO::getName, name));
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getParentId, parentId));
    }

    default List<MenuDO> selectList(MenuQueryRequest reqVO) {
        LambdaQueryWrapper<MenuDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(reqVO.getName())) {
            wrapper = wrapper.like(MenuDO::getName, reqVO.getName());
        }
        if (reqVO.getStatus() != null) {
            wrapper = wrapper.eq(MenuDO::getStatus, reqVO.getStatus());
        }
        return selectList(wrapper);
    }

    default List<MenuDO> selectListByPermission(String permission) {
        return selectList(new LambdaQueryWrapper<MenuDO>().eq(MenuDO::getPermission, permission));
    }
}
