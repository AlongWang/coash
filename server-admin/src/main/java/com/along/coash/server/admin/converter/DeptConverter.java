package com.along.coash.server.admin.converter;


import com.along.coash.server.admin.contract.dept.*;
import com.along.coash.server.admin.contract.user.UserPageItem;
import com.along.coash.server.admin.entities.DeptDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DeptConverter {

    DeptConverter INSTANCE = Mappers.getMapper(DeptConverter.class);

    List<DeptInfo> convert(List<DeptDO> deptList);

    List<DeptInfoSimple> convertSimple(List<DeptDO> deptList);

    UserPageItem.Dept convertToDept(DeptDO dept);

    DeptInfo convert(DeptDO dept);

    DeptDO convert(DeptCreateRequest request);

    DeptDO convert(DeptUpdateRequest request);

    DeptInfoSimple deptDOToDeptSimpleRespVO(DeptDO deptDO);
}
