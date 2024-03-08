package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.dict.dictType.*;
import com.along.coash.server.admin.entities.DictTypeDO;
import com.along.coash.server.admin.services.dict.DictTypeService;
import com.along.coash.server.admin.converter.DictTypeConverter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-api/system/dict-type")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:dict:create')")
    public CommonResult<String> createDictType(@Valid @RequestBody DictTypeCreateRequest reqVO) {
        Long dictTypeId = dictTypeService.createDictType(reqVO);
        return CommonResult.success(String.valueOf(dictTypeId));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:dict:update')")
    public CommonResult<Boolean> updateDictType(@Valid @RequestBody DictTypeUpdateRequest reqVO) {
        dictTypeService.updateDictType(reqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:dict:delete')")
    public CommonResult<Boolean> deleteDictType(String id) {
        dictTypeService.deleteDictType(Long.valueOf(id));
        return CommonResult.success(true);
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<Page<DictTypeResponse>> pageDictTypes(@Valid DictTypeQueryRequest reqVO) {
        return CommonResult.success(DictTypeConverter.INSTANCE.convert(dictTypeService.getDictTypePage(reqVO)));
    }

    @GetMapping(value = "/get")
    @PreAuthorize("@ss.hasPermission('system:dict:query')")
    public CommonResult<DictTypeResponse> getDictType(@RequestParam("id") String id) {
        return CommonResult.success(DictTypeConverter.INSTANCE.convert(dictTypeService.getDictType(Long.valueOf(id))));
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<DictTypeSimple>> getSimpleDictTypeList() {
        List<DictTypeDO> list = dictTypeService.getDictTypeList();
        return CommonResult.success(DictTypeConverter.INSTANCE.convert(list));
    }
}
