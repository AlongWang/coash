package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.user.UserQueryRequest;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {

    default AdminUserDO selectByUsername(String username) {
        return selectOne(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getUsername, username));
    }

    default AdminUserDO selectByEmail(String email) {
        return selectOne(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getEmail, email));
    }

    default AdminUserDO selectByMobile(String mobile) {
        return selectOne(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getMobile, mobile));
    }

    default IPage<AdminUserDO> selectPage(UserQueryRequest reqVO, Collection<Long> deptIds) {
        LambdaQueryWrapper<AdminUserDO> query = new LambdaQueryWrapper<>();
        if (reqVO.getUsername() != null) {
            query = query.like(AdminUserDO::getUsername, reqVO.getUsername());
        }
        if (reqVO.getMobile() != null) {
            query = query.like(AdminUserDO::getMobile, reqVO.getMobile());
        }
        if (reqVO.getStatus() != null) {
            query = query.eq(AdminUserDO::getStatus, reqVO.getStatus());
        }
        if (reqVO.getStartCreateTime() != null) {
            query = query.ge(AdminUserDO::getCreateTime, reqVO.getStartCreateTime());
        }
        if (reqVO.getEndCreateTime() != null) {
            query = query.le(AdminUserDO::getCreateTime, reqVO.getEndCreateTime());
        }
        if (deptIds != null && deptIds.size() > 0) {
            query = query.in(AdminUserDO::getDeptId, deptIds);
        }
        query = query.orderByDesc(AdminUserDO::getId);

        IPage<AdminUserDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());

        return selectPage(page, query);
    }

    default List<AdminUserDO> selectListByNickname(String nickname) {
        return selectList(new LambdaQueryWrapper<AdminUserDO>().like(AdminUserDO::getNickname, nickname));
    }

    default List<AdminUserDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getStatus, status));
    }

    default List<AdminUserDO> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getDeptId, deptIds));
    }
}
