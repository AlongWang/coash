package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.role.RoleQueryRequest;
import com.along.coash.server.admin.entities.RoleDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

    default Page<RoleDO> selectPage(RoleQueryRequest reqVO) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        if (reqVO.getName() != null) {
            queryWrapper.like(RoleDO::getName, reqVO.getName());
        }
        if (reqVO.getCode() != null) {
            queryWrapper.like(RoleDO::getCode, reqVO.getCode());
        }
        if (reqVO.getStatus() != null) {
            queryWrapper.eq(RoleDO::getStatus, reqVO.getStatus());
        }
        if (reqVO.getStartCreateTime() != null) {
            queryWrapper.gt(RoleDO::getCreateTime, reqVO.getStartCreateTime());
        }
        if (reqVO.getEndCreateTime() != null) {
            queryWrapper.le(RoleDO::getCreateTime, reqVO.getEndCreateTime());
        }
        queryWrapper.orderByDesc(RoleDO::getId);
        Page<RoleDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        return selectPage(page, queryWrapper);
    }

    default RoleDO selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<RoleDO>().eq(RoleDO::getName, name));
    }

    default RoleDO selectByCode(String code) {
        return selectOne(new LambdaQueryWrapper<RoleDO>().eq(RoleDO::getCode, code));
    }

    default List<RoleDO> selectListByStatus(@Nullable Collection<Integer> statuses) {
        return selectList(new LambdaQueryWrapper<RoleDO>().eq(RoleDO::getStatus, statuses));
    }

}
