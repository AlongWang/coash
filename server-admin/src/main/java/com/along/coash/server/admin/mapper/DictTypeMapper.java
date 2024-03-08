package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.dict.dictType.DictTypeQueryRequest;
import com.along.coash.server.admin.entities.DictTypeDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Mapper
public interface DictTypeMapper extends BaseMapper<DictTypeDO> {

    default Page<DictTypeDO> selectPage(DictTypeQueryRequest reqVO) {
        LambdaQueryWrapper<DictTypeDO> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasLength(reqVO.getName())) {
            queryWrapper = queryWrapper.like(DictTypeDO::getName, reqVO.getName());
        }
        if (StringUtils.hasLength(reqVO.getType())) {
            queryWrapper = queryWrapper.like(DictTypeDO::getType, reqVO.getType());
        }
        if (reqVO.getStatus() != null) {
            queryWrapper = queryWrapper.eq(DictTypeDO::getStatus, reqVO.getType());
        }
        if (reqVO.getStartCreateTime() != null) {
            queryWrapper = queryWrapper.gt(DictTypeDO::getCreateTime, reqVO.getStartCreateTime());
        }
        if (reqVO.getEndCreateTime() != null) {
            queryWrapper = queryWrapper.le(DictTypeDO::getCreateTime, reqVO.getEndCreateTime());
        }
        queryWrapper = queryWrapper.orderByDesc(DictTypeDO::getId);

        Page<DictTypeDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());

        return selectPage(page, queryWrapper);
    }

    default DictTypeDO selectByType(String type) {
        return selectOne(new LambdaQueryWrapper<DictTypeDO>().eq(DictTypeDO::getType, type));
    }

    default DictTypeDO selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<DictTypeDO>().eq(DictTypeDO::getName, name));
    }

    int deleteById(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);

    @Update("UPDATE system_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);
}
