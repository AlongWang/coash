package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.dept.DeptQueryRequest;
import com.along.coash.server.admin.entities.DeptDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapper<DeptDO> {

    default List<DeptDO> selectList(DeptQueryRequest reqVO) {
        LambdaQueryWrapper<DeptDO> queryWrapper = new LambdaQueryWrapper<>();
        if (reqVO.getName() != null) {
            queryWrapper = queryWrapper.like(DeptDO::getName, reqVO.getName());
        }
        if (reqVO.getStatus() != null) {
            queryWrapper = queryWrapper.eq(DeptDO::getStatus, reqVO.getStatus());
        }
        return selectList(queryWrapper);
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(new LambdaQueryWrapper<DeptDO>()
                .eq(DeptDO::getParentId, parentId)
                .eq(DeptDO::getName, name));
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(new LambdaQueryWrapper<DeptDO>().eq(DeptDO::getParentId, parentId));
    }

    default List<DeptDO> selectListByParentId(Collection<Long> parentIds) {
        return selectList(new LambdaQueryWrapper<DeptDO>().in(DeptDO::getParentId, parentIds));
    }

}
