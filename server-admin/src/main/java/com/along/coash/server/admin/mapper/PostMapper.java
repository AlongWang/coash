package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.post.PostQueryRequest;
import com.along.coash.server.admin.entities.PostDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<PostDO> {

    default List<PostDO> selectList(Collection<Long> ids, Collection<Integer> statuses) {
        LambdaQueryWrapper<PostDO> query = new LambdaQueryWrapper<>();
        if (ids != null) {
            query = query.in(PostDO::getId, ids);
        }
        if (statuses != null && statuses.size() > 0) {
            query = query.in(PostDO::getStatus, statuses);
        }
        return selectList(query);
    }

    default Page<PostDO> selectPage(PostQueryRequest reqVO) {
        LambdaQueryWrapper<PostDO> queryWrapper = new LambdaQueryWrapper<>();
        if (reqVO.getCode() != null) {
            queryWrapper = queryWrapper.like(PostDO::getCode, reqVO.getCode());
        }
        if (reqVO.getName() != null) {
            queryWrapper = queryWrapper.like(PostDO::getName, reqVO.getName());
        }
        if (reqVO.getStatus() != null) {
            queryWrapper = queryWrapper.eq(PostDO::getStatus, reqVO.getStatus());
        }
        queryWrapper = queryWrapper.orderByDesc(PostDO::getId);
        Page<PostDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        return selectPage(page, queryWrapper);
    }


    default PostDO selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<PostDO>().eq(PostDO::getName, name));
    }

    default PostDO selectByCode(String code) {
        return selectOne(new LambdaQueryWrapper<PostDO>().eq(PostDO::getCode, code));
    }

}
