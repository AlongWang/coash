package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.notice.NoticeCreateRequest;
import com.along.coash.server.admin.contract.notice.NoticeResponse;
import com.along.coash.server.admin.contract.notice.NoticeUpdateRequest;
import com.along.coash.server.admin.entities.NoticeDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NoticeConverter {

    NoticeConverter INSTANCE = Mappers.getMapper(NoticeConverter.class);

    Page<NoticeResponse> convertPage(Page<NoticeDO> page);

    NoticeResponse convert(NoticeDO bean);

    NoticeDO convert(NoticeUpdateRequest request);

    NoticeDO convert(NoticeCreateRequest request);

}
