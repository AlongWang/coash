package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.server.admin.contract.notice.NoticeCreateRequest;
import com.along.coash.server.admin.contract.notice.NoticeQueryRequest;
import com.along.coash.server.admin.contract.notice.NoticeResponse;
import com.along.coash.server.admin.contract.notice.NoticeUpdateRequest;
import com.along.coash.server.admin.converter.NoticeConverter;
import com.along.coash.server.admin.services.notice.NoticeService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-api/system/notice")
@Validated
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:notice:create')")
    public CommonResult<Long> createNotice(@Valid @RequestBody NoticeCreateRequest request) {
        Long noticeId = noticeService.createNotice(request);
        return CommonResult.success(noticeId);
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:notice:update')")
    public CommonResult<Boolean> updateNotice(@Valid @RequestBody NoticeUpdateRequest request) {
        noticeService.updateNotice(request);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:notice:delete')")
    public CommonResult<Boolean> deleteNotice(@RequestParam("id") Long id) {
        noticeService.deleteNotice(id);
        return CommonResult.success(true);
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:notice:query')")
    public CommonResult<Page<NoticeResponse>> getNoticePage(@Validated NoticeQueryRequest request) {
        return CommonResult.success(NoticeConverter.INSTANCE.convertPage(noticeService.getNoticePage(request)));
    }

    @GetMapping("/get")
    @PreAuthorize("@ss.hasPermission('system:notice:query')")
    public CommonResult<NoticeResponse> getNotice(@RequestParam("id") String id) {
        return CommonResult.success(NoticeConverter.INSTANCE.convert(noticeService.getNotice(Long.valueOf(id))));
    }

}
