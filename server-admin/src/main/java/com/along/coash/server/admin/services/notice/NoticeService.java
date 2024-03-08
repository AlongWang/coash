package com.along.coash.server.admin.services.notice;

import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.notice.NoticeCreateRequest;
import com.along.coash.server.admin.contract.notice.NoticeQueryRequest;
import com.along.coash.server.admin.contract.notice.NoticeUpdateRequest;
import com.along.coash.server.admin.converter.NoticeConverter;
import com.along.coash.server.admin.entities.NoticeDO;
import com.along.coash.server.admin.mapper.NoticeMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 通知公告 Service 实现类
 *
 * @author 芋道源码
 */
@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public Long createNotice(NoticeCreateRequest request) {
        NoticeDO notice = NoticeConverter.INSTANCE.convert(request);
        noticeMapper.insert(notice);
        return notice.getId();
    }

    public void updateNotice(NoticeUpdateRequest request) {
        // 校验是否存在
        validateNoticeExists(Long.valueOf(request.getId()));
        // 更新通知公告
        NoticeDO updateObj = NoticeConverter.INSTANCE.convert(request);
        noticeMapper.updateById(updateObj);
    }

    public void deleteNotice(Long id) {
        // 校验是否存在
        validateNoticeExists(id);
        // 删除通知公告
        noticeMapper.deleteById(id);
    }

    public Page<NoticeDO> getNoticePage(NoticeQueryRequest request) {
        return noticeMapper.selectPage(request);
    }

    public NoticeDO getNotice(Long id) {
        return noticeMapper.selectById(id);
    }

    public void validateNoticeExists(Long id) {
        if (id == null) {
            return;
        }
        NoticeDO notice = noticeMapper.selectById(id);
        if (notice == null) {
            throw new ServiceException(ErrorCodeConstants.NOTICE_NOT_FOUND);
        }
    }

}
