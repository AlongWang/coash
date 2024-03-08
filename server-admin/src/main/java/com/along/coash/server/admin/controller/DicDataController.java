package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.dict.dictData.DictDataSimple;
import com.along.coash.server.admin.converter.DictDataConverter;
import com.along.coash.server.admin.entities.DictDataDO;
import com.along.coash.server.admin.services.dict.DictDataService;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-api/system/dict-data")
public class DicDataController {

    @Resource
    private DictDataService dictDataService;

    /**
     * 获得全部字典数据列表
     *
     * @return
     */
    @GetMapping("/list-all-simple")
    @PermitAll
    public CommonResult<List<DictDataSimple>> getSimpleDictDataList() {
        List<DictDataDO> list = dictDataService.getDictDataList();
        return CommonResult.success(DictDataConverter.INSTANCE.convertList(list));
    }
}
