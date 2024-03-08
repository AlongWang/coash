package com.along.coash.server.admin.services.dict;

import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeCreateRequest;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeQueryRequest;
import com.along.coash.server.admin.contract.dict.dictType.DictTypeUpdateRequest;
import com.along.coash.server.admin.converter.DictTypeConverter;
import com.along.coash.server.admin.entities.DictTypeDO;
import com.along.coash.server.admin.mapper.DictDataMapper;
import com.along.coash.server.admin.mapper.DictTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 字典类型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class DictTypeService {

    @Autowired
    private DictDataMapper dictDataMapper;

    @Autowired
    private DictTypeMapper dictTypeMapper;

    public Page<DictTypeDO> getDictTypePage(DictTypeQueryRequest request) {
        return dictTypeMapper.selectPage(request);
    }

    public DictTypeDO getDictType(Long id) {
        return dictTypeMapper.selectById(id);
    }

    public DictTypeDO getDictType(String type) {
        return dictTypeMapper.selectByType(type);
    }


    public Long createDictType(DictTypeCreateRequest request) {
        // 校验正确性
        validateDictType(null, request.getName(), request.getType());

        // 插入字典类型
        DictTypeDO dictType = DictTypeConverter.INSTANCE.convert(request);
        dictType.setDeletedTime(LocalDateTime.MIN);
        dictTypeMapper.insert(dictType);
        return dictType.getId();
    }


    public void updateDictType(DictTypeUpdateRequest reqVO) {
        // 校验正确性
        validateDictType(Long.valueOf(reqVO.getId()), reqVO.getName(), null);

        // 更新字典类型
        DictTypeDO updateObj = DictTypeConverter.INSTANCE.convert(reqVO);
        dictTypeMapper.updateById(updateObj);
    }


    public void deleteDictType(Long id) {
        // 校验是否存在
        DictTypeDO dictType = validateDictTypeExists(id);
        // 校验是否有字典数据
        if (dictDataMapper.selectCountByDictType(dictType.getType()) > 0) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_HAS_CHILDREN);
        }
        // 删除字典类型
        dictTypeMapper.updateToDelete(id, LocalDateTime.now());
    }


    public List<DictTypeDO> getDictTypeList() {
        return dictTypeMapper.selectList(new LambdaQueryWrapper<>());
    }

    private void validateDictType(Long id, String name, String type) {
        // 校验自己存在
        validateDictTypeExists(id);
        // 校验字典类型的名字的唯一性
        validateDictTypeNameUnique(id, name);
        // 校验字典类型的类型的唯一性
        validateDictTypeUnique(id, type);
    }

    void validateDictTypeNameUnique(Long id, String name) {
        DictTypeDO dictType = dictTypeMapper.selectByName(name);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_NAME_DUPLICATE);
        }
    }

    void validateDictTypeUnique(Long id, String type) {
        if (!StringUtils.hasLength(type)) {
            return;
        }
        DictTypeDO dictType = dictTypeMapper.selectByType(type);
        if (dictType == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的字典类型
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE);
        }
        if (!dictType.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_TYPE_DUPLICATE);
        }
    }

    DictTypeDO validateDictTypeExists(Long id) {
        if (id == null) {
            return null;
        }
        DictTypeDO dictType = dictTypeMapper.selectById(id);
        if (dictType == null) {
            throw new ServiceException(ErrorCodeConstants.DICT_TYPE_NOT_EXISTS);
        }
        return dictType;
    }

}
