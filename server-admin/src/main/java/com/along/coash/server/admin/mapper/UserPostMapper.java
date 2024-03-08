package com.along.coash.server.admin.mapper;


import com.along.coash.server.admin.entities.UserPostDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserPostMapper extends BaseMapper<UserPostDO> {

    default List<UserPostDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapper<UserPostDO>().eq(UserPostDO::getUserId, userId));
    }

    default void deleteByUserIdAndPostId(Long userId, Collection<Long> postIds) {
        delete(new LambdaQueryWrapper<UserPostDO>()
                .eq(UserPostDO::getUserId, userId)
                .in(UserPostDO::getPostId, postIds));
    }

    default List<UserPostDO> selectListByPostIds(Collection<Long> postIds) {
        return selectList(new LambdaQueryWrapper<UserPostDO>().eq(UserPostDO::getPostId, postIds));
    }

    default void deleteByUserId(Long userId) {
        delete(Wrappers.lambdaUpdate(UserPostDO.class).eq(UserPostDO::getUserId, userId));
    }

    default void insertBatch(Collection<UserPostDO> list) {
        for (UserPostDO userPostDO : list) {
            insert(userPostDO);
        }
    }
}
