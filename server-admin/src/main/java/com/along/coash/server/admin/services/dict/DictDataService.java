package com.along.coash.server.admin.services.dict;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.contract.dict.dictData.DictDataCreateRequest;
import com.along.coash.server.admin.contract.dict.dictData.DictDataQueryRequest;
import com.along.coash.server.admin.contract.dict.dictData.DictDataUpdateRequest;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.converter.DictDataConverter;
import com.along.coash.server.admin.entities.DictDataDO;
import com.along.coash.server.admin.entities.DictTypeDO;
import com.along.coash.server.admin.mapper.DictDataMapper;
import com.along.coash.server.admin.mapper.DictTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 字典数据 Service 实现类
 *
 * @author ruoyi
 */
@Service
public class DictDataService {

    /**
     * 排序 dictType > sort
     */
    private static final Comparator<DictDataDO> COMPARATOR_TYPE_AND_SORT = Comparator
            .comparing(DictDataDO::getDictType)
            .thenComparingInt(DictDataDO::getSort);

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    public List<DictDataDO> getDictDataList() {
        List<DictDataDO> list = dictDataMapper.selectList(new LambdaQueryWrapper<>());
        list.sort(COMPARATOR_TYPE_AND_SORT);
        return list;
    }

    public IPage<DictDataDO> getDictDataPage(DictDataQueryRequest request) {
        return dictDataMapper.selectPage(request);
    }

    public DictDataDO getDictData(Long id) {
        return dictDataMapper.selectById(id);
    }

    public Long createDictData(DictDataCreateRequest request) {
        // 校验正确性
        validateDictData(null, request.getValue(), request.getDictType());

        // 插入字典类型
        DictDataDO dictData = DictDataConverter.INSTANCE.convert(request);
        dictDataMapper.insert(dictData);
        return dictData.getId();
    }

    public void updateDictData(DictDataUpdateRequest request) {
        // 校验正确性
        validateDictData(request.getId(), request.getValue(), request.getDictType());

        // 更新字典类型
        DictDataDO updateObj = DictDataConverter.INSTANCE.convert(request);
        dictDataMapper.updateById(updateObj);
    }

    public void deleteDictData(Long id) {
        // 校验是否存在
        validateDictDataExists(id);

        // 删除字典数据
        dictDataMapper.deleteById(id);
    }

    private void validateDictData(Long id, String value, String dictType) {
        // 校验自己存在
        validateDictDataExists(id);
        // 校验字典类型有效
        validateDictTypeExists(dictType);
        // 校验字典数据的值的唯一性
        validateDictDataValueUnique(id, dictType, value);
    }

    public void validateDictDataValueUnique(Long id, String dictType, String value) {
        DictDataDO dictData = dictDataMapper.selectByDictTypeAndValue(dictType, value);
        if (dictData == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典数据
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE);
        }
        if (!dictData.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.DICT_DATA_VALUE_DUPLICATE);
        }
    }

    public void validateDictDataExists(Long id) {
        if (id == null) {
            return;
        }
        DictDataDO dictData = dictDataMapper.selectById(id);
        if (dictData == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_DATA_NOT_EXISTS);
        }
    }

    public void validateDictTypeExists(String type) {
        DictTypeDO dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_NOT_EXISTS);
        }
        if (!CommonStatusEnum.ENABLE.getStatus().equals(dictType.getStatus())) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_NOT_ENABLE);
        }
    }

    public void validateDictDataList(String dictType, Collection<String> values) {
        if (CollectionUtils.isEmpty(values)) {
            return;
        }
        Map<String, DictDataDO> dictDataMap = dictDataMapper.selectByDictTypeAndValues(dictType, values)
                .stream().collect(Collectors.toMap(DictDataDO::getValue, p -> p));
        // 校验
        values.forEach(value -> {
            DictDataDO dictData = dictDataMap.get(value);
            if (dictData == null) {
                throw new ServiceException(ErrorCodeConstants.DICT_DATA_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dictData.getStatus())) {
                throw new ServiceException(ErrorCodeConstants.DICT_DATA_NOT_ENABLE, dictData.getLabel());
            }
        });
    }

    public DictDataDO getDictData(String dictType, String value) {
        return dictDataMapper.selectByDictTypeAndValue(dictType, value);
    }

    public DictDataDO parseDictData(String dictType, String label) {
        return dictDataMapper.selectByDictTypeAndLabel(dictType, label);
    }

}
