package com.along.coash.server.admin.services.dept;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.constants.RedisKeyConstants;
import com.along.coash.server.admin.contract.dept.DeptCreateRequest;
import com.along.coash.server.admin.contract.dept.DeptIdEnum;
import com.along.coash.server.admin.contract.dept.DeptQueryRequest;
import com.along.coash.server.admin.contract.dept.DeptUpdateRequest;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.converter.DeptConverter;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.mapper.DeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 部门 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public Long createDept(DeptCreateRequest request) {
        // 校验正确性
        if (request.getParentId() == null) {
            request.setParentId(String.valueOf(DeptIdEnum.ROOT.getId()));
        }
        validate(null, Long.parseLong(request.getParentId()), request.getName());
        // 插入部门
        DeptDO dept = DeptConverter.INSTANCE.convert(request);
        deptMapper.insert(dept);
        return dept.getId();
    }

    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public void updateDept(DeptUpdateRequest request) {
        // 校验正确性
        if (request.getParentId() == null) {
            request.setParentId(String.valueOf(DeptIdEnum.ROOT.getId()));
        }
        validate(Long.parseLong(request.getId()), Long.parseLong(request.getParentId()), request.getName());
        // 更新部门
        DeptDO updateObj = DeptConverter.INSTANCE.convert(request);
        deptMapper.updateById(updateObj);
    }

    @CacheEvict(cacheNames = RedisKeyConstants.DEPT_CHILDREN_ID_LIST, allEntries = true)
    public void deleteDept(Long id) {
        // 校验是否存在
        validateDeptExists(id);
        // 校验是否有子部门
        if (deptMapper.selectCountByParentId(id) > 0) {
            throw new ServiceException(ErrorCodeConstants.DEPT_EXITS_CHILDREN);
        }
        // 删除部门
        deptMapper.deleteById(id);
    }

    public DeptDO getDept(Long id) {
        return deptMapper.selectById(id);
    }


    public List<DeptDO> getChildDeptList(Long id) {
        List<DeptDO> children = new LinkedList<>();
        // 遍历每一层
        Collection<Long> parentIds = Collections.singleton(id);
        for (int i = 0; i < Short.MAX_VALUE; i++) { // 使用 Short.MAX_VALUE 避免 bug 场景下，存在死循环
            // 查询当前层，所有的子部门
            List<DeptDO> depts = deptMapper.selectListByParentId(parentIds);
            // 1. 如果没有子部门，则结束遍历
            if (CollectionUtils.isEmpty(depts)) {
                break;
            }
            // 2. 如果有子部门，继续遍历
            children.addAll(depts);
            parentIds = depts.stream().map(DeptDO::getId).collect(Collectors.toList());
        }
        return children;
    }

    public void validateDeptList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 获得科室信息
        Map<Long, DeptDO> deptMap = getDeptMap(ids);
        // 校验
        ids.forEach(id -> {
            DeptDO dept = deptMap.get(id);
            if (dept == null) {
                throw new ServiceException(ErrorCodeConstants.DEPT_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(dept.getStatus())) {
                throw new ServiceException(ErrorCodeConstants.DEPT_NOT_ENABLE, dept.getName());
            }
        });
    }

    public Map<Long, DeptDO> getDeptMap(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<DeptDO> list = getDeptList(ids);
        return list.stream().collect(Collectors.toMap(DeptDO::getId, p -> p));
    }

    public List<DeptDO> getDeptList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return deptMapper.selectBatchIds(ids);
    }

    public List<DeptDO> getDeptList(DeptQueryRequest request) {
        return deptMapper.selectList(request);
    }

    private void validate(Long id, Long parentId, String name) {
        // 校验自己存在
        validateDeptExists(id);
        // 校验父部门的有效性
        validateParentDept(id, parentId);
        // 校验部门名的唯一性
        validateDeptNameUnique(id, parentId, name);
    }

    private void validateDeptExists(Long id) {
        if (id == null) {
            return;
        }
        DeptDO dept = deptMapper.selectById(id);
        if (dept == null) {
            throw new ServiceException(ErrorCodeConstants.DEPT_NOT_FOUND);
        }
    }

    private void validateParentDept(Long id, Long parentId) {
        if (parentId == null || DeptIdEnum.ROOT.getId().equals(parentId)) {
            return;
        }
        // 不能设置自己为父部门
        if (parentId.equals(id)) {
            throw new ServiceException(ErrorCodeConstants.DEPT_PARENT_ERROR);
        }
        // 父岗位不存在
        DeptDO dept = deptMapper.selectById(parentId);
        if (dept == null) {
            throw new ServiceException(ErrorCodeConstants.DEPT_PARENT_NOT_EXITS);
        }
        // 父部门不能是原来的子部门
        List<DeptDO> children = getChildDeptList(id);
        if (children.stream().anyMatch(dept1 -> dept1.getId().equals(parentId))) {
            throw new ServiceException(ErrorCodeConstants.DEPT_PARENT_IS_CHILD);
        }
    }

    private void validateDeptNameUnique(Long id, Long parentId, String name) {
        DeptDO dept = deptMapper.selectByParentIdAndName(parentId, name);
        if (dept == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
        if (!id.equals(dept.getId())) {
            throw new ServiceException(ErrorCodeConstants.DEPT_NAME_DUPLICATE);
        }
    }

}
