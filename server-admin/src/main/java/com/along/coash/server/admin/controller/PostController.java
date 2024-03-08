package com.along.coash.server.admin.controller;

import com.along.coash.framework.contract.CommonResult;
import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.server.admin.contract.post.*;
import com.along.coash.server.admin.converter.PostConverter;
import com.along.coash.server.admin.entities.PostDO;
import com.along.coash.server.admin.services.dept.PostService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/admin-api/system/post")
public class PostController {


    @Autowired
    private PostService postService;

    @PostMapping("/create")
    @PreAuthorize("@ss.hasPermission('system:post:create')")
    public CommonResult<String> createPost(@Valid @RequestBody PostCreateRequest request) {
        Long postId = postService.createPost(request);
        return CommonResult.success(String.valueOf(postId));
    }

    @PutMapping("/update")
    @PreAuthorize("@ss.hasPermission('system:post:update')")
    public CommonResult<Boolean> updatePost(@Valid @RequestBody PostUpdateRequest request) {
        postService.updatePost(request);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@ss.hasPermission('system:post:delete')")
    public CommonResult<Boolean> deletePost(@RequestParam("id") String id) {
        postService.deletePost(Long.valueOf(id));
        return CommonResult.success(true);
    }

    @GetMapping(value = "/get")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public CommonResult<Post> getPost(@RequestParam("id") String id) {
        return CommonResult.success(PostConverter.INSTANCE.convert(postService.getPost(Long.parseLong(id))));
    }

    @GetMapping("/list-all-simple")
    public CommonResult<List<PostSimple>> getSimplePostList() {
        // 获得岗位列表，只要开启状态的
        List<PostDO> list = postService.getPostList(null, CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        list.sort(Comparator.comparing(PostDO::getSort));
        return CommonResult.success(PostConverter.INSTANCE.convert(list));
    }

    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermission('system:post:query')")
    public CommonResult<IPage<Post>> getPostPage(@Validated PostQueryRequest request) {
        return CommonResult.success(PostConverter.INSTANCE.convert(postService.getPostPage(request)));
    }

}
