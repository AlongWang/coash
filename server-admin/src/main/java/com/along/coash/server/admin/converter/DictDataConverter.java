package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.dict.dictData.DictDataCreateRequest;
import com.along.coash.server.admin.contract.dict.dictData.DictDataSimple;
import com.along.coash.server.admin.contract.dict.dictData.DictDataUpdateRequest;
import com.along.coash.server.admin.entities.DictDataDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictDataConverter {
    DictDataConverter INSTANCE = Mappers.getMapper(DictDataConverter.class);


    List<DictDataSimple> convertList(List<DictDataDO> list);

    DictDataSimple convert(DictDataDO data);

    DictDataDO convert(DictDataCreateRequest request);

    DictDataDO convert(DictDataUpdateRequest request);
}
