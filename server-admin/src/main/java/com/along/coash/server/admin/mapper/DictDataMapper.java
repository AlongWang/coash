package com.along.coash.server.admin.mapper;

import com.along.coash.server.admin.contract.dict.dictData.DictDataQueryRequest;
import com.along.coash.server.admin.entities.DictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mapper
public interface DictDataMapper extends BaseMapper<DictDataDO> {

    default DictDataDO selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(new LambdaQueryWrapper<DictDataDO>().eq(DictDataDO::getDictType, dictType)
                .eq(DictDataDO::getValue, value));
    }

    default DictDataDO selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(new LambdaQueryWrapper<DictDataDO>().eq(DictDataDO::getDictType, dictType)
                .eq(DictDataDO::getLabel, label));
    }

    default List<DictDataDO> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<DictDataDO>().eq(DictDataDO::getDictType, dictType)
                .in(DictDataDO::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(new LambdaQueryWrapper<DictDataDO>().eq(DictDataDO::getDictType, dictType));
    }

    default IPage<DictDataDO> selectPage(DictDataQueryRequest reqVO) {
        LambdaQueryWrapper<DictDataDO> queryWrapper = new LambdaQueryWrapper<>();
        if (reqVO.getDictType() != null) {
            queryWrapper = queryWrapper.eq(DictDataDO::getDictType, reqVO.getDictType());
        }
        if (reqVO.getLabel() != null) {
            queryWrapper = queryWrapper.like(DictDataDO::getLabel, reqVO.getLabel());
        }
        if (reqVO.getStatus() != null) {
            queryWrapper = queryWrapper.eq(DictDataDO::getStatus, reqVO.getStatus());
        }
        queryWrapper = queryWrapper.orderByDesc(Arrays.asList(DictDataDO::getDictType, DictDataDO::getSort));

        IPage<DictDataDO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        return selectPage(page, queryWrapper);
    }

}
