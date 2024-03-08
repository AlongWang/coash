package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.post.Post;
import com.along.coash.server.admin.contract.post.PostCreateRequest;
import com.along.coash.server.admin.contract.post.PostSimple;
import com.along.coash.server.admin.contract.post.PostUpdateRequest;
import com.along.coash.server.admin.entities.PostDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PostConverter {
    PostConverter INSTANCE = Mappers.getMapper(PostConverter.class);

    List<PostSimple> convert(List<PostDO> list);

    Page<Post> convert(Page<PostDO> page);

    Post convert(PostDO id);

    PostDO convert(PostCreateRequest bean);

    PostDO convert(PostUpdateRequest reqVO);

    PostSimple postDOToPostSimpleRespVO(PostDO postDO);

    List<Post> postDOListToPostRespVOList(List<PostDO> list);
}
