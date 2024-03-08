package com.along.coash.server.admin.services.dept;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.post.PostCreateRequest;
import com.along.coash.server.admin.contract.post.PostQueryRequest;
import com.along.coash.server.admin.contract.post.PostUpdateRequest;
import com.along.coash.server.admin.entities.PostDO;
import com.along.coash.server.admin.mapper.PostMapper;
import com.along.coash.server.admin.converter.PostConverter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Validated
public class PostService {

    @Autowired
    private PostMapper postMapper;

    public Long createPost(PostCreateRequest request) {
        // 校验正确性
        validate(null, request.getName(), request.getCode());

        // 插入岗位
        PostDO post = PostConverter.INSTANCE.convert(request);
        postMapper.insert(post);
        return post.getId();
    }

    public void updatePost(PostUpdateRequest request) {
        // 校验正确性
        validate(Long.parseLong(request.getId()), request.getName(), request.getCode());

        // 更新岗位
        PostDO updateObj = PostConverter.INSTANCE.convert(request);
        postMapper.updateById(updateObj);
    }

    public void deletePost(Long id) {
        // 校验是否存在
        validatePostExists(id);
        // 删除部门
        postMapper.deleteById(id);
    }

    public List<PostDO> getPostList(Collection<Long> ids, Integer... statuses) {
        return postMapper.selectList(ids, Arrays.asList(statuses));
    }

    public Page<PostDO> getPostPage(PostQueryRequest request) {
        return postMapper.selectPage(request);
    }

    public PostDO getPost(Long id) {
        return postMapper.selectById(id);
    }

    public void validatePostList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<PostDO> posts = postMapper.selectBatchIds(ids);
        Map<Long, PostDO> postMap = posts.stream().collect(Collectors.toMap(PostDO::getId, p -> p));
        // 校验
        ids.forEach(id -> {
            PostDO post = postMap.get(id);
            if (post == null) {
                throw new ServiceException(ErrorCodeConstants.POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(post.getStatus())) {
                throw new ServiceException(ErrorCodeConstants.POST_NOT_ENABLE, post.getName());
            }
        });
    }

    private void validate(Long id, String name, String code) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name);
        // 校验岗位编码的唯一性
        validatePostCodeUnique(id, code);
    }

    private void validatePostNameUnique(Long id, String name) {
        PostDO post = postMapper.selectByName(name);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.POST_NAME_DUPLICATE);
        }
    }

    private void validatePostCodeUnique(Long id, String code) {
        PostDO post = postMapper.selectByCode(code);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.POST_CODE_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.POST_CODE_DUPLICATE);
        }
    }

    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw new ServiceException(ErrorCodeConstants.POST_NOT_FOUND);
        }
    }
}
