package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.dict.dictType.DictTypeCreateRequest;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeResponse;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeSimple;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeUpdateRequest;
import com.along.coash.server.admin.entities.DictTypeDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictTypeConverter {

    DictTypeConverter INSTANCE = Mappers.getMapper(DictTypeConverter.class);

    DictTypeDO convert(DictTypeCreateRequest request);

    DictTypeDO convert(DictTypeUpdateRequest reqVO);

    Page<DictTypeResponse> convert(Page<DictTypeDO> dictTypePage);

    DictTypeResponse convert(DictTypeDO dictType);

    List<DictTypeSimple> convert(List<DictTypeDO> list);
}
