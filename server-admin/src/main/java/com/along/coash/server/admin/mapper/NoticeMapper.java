package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.notice.NoticeQueryRequest;
import com.along.coash.server.admin.entities.NoticeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

@Mapper
public interface NoticeMapper extends BaseMapper<NoticeDO> {

    default Page<NoticeDO> selectPage(NoticeQueryRequest request) {
        LambdaQueryWrapper<NoticeDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(request.getTitle())) {
            wrapper = wrapper.like(NoticeDO::getTitle, request.getTitle());
        }
        if (request.getStatus() != null) {
            wrapper = wrapper.eq(NoticeDO::getStatus, request.getStatus());
        }
        wrapper.orderByDesc(NoticeDO::getId);
        Page<NoticeDO> page = new Page<>(request.getPageNo(), request.getPageSize());
        return selectPage(page, wrapper);
    }

}
