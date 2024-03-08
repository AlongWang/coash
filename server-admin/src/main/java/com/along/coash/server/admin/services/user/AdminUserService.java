package com.along.coash.server.admin.services.user;

import com.along.coash.framework.contract.CommonStatusEnum;
import com.along.coash.framework.contract.ServiceException;
import com.along.coash.server.admin.contract.dict.dictData.enums.ErrorCodeConstants;
import com.along.coash.server.admin.contract.profile.UserProfileUpdatePasswordRequest;
import com.along.coash.server.admin.contract.profile.UserProfileUpdateRequest;
import com.along.coash.server.admin.contract.user.UserCreateRequest;
import com.along.coash.server.admin.contract.user.UserQueryRequest;
import com.along.coash.server.admin.contract.user.UserUpdateRequest;
import com.along.coash.server.admin.converter.UserConverter;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.entities.UserPostDO;
import com.along.coash.server.admin.mapper.AdminUserMapper;
import com.along.coash.server.admin.mapper.UserPostMapper;
import com.along.coash.server.admin.services.dept.DeptService;
import com.along.coash.server.admin.services.dept.PostService;
import com.along.coash.server.admin.services.permission.PermissionService;
import com.along.coash.server.admin.utils.AuthUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserMapper userMapper;

    @Autowired
    private UserPostMapper userPostMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PostService postService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateRequest reqVO) {
        // 校验正确性
        validateUserForCreateOrUpdate(null, reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                Long.parseLong(reqVO.getDeptId()),
                reqVO.getPostIds() == null ? null : reqVO.getPostIds().stream().map(Long::parseLong)
                        .collect(Collectors.toSet()));
        // 插入用户
        AdminUserDO user = UserConverter.INSTANCE.convert(reqVO);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(reqVO.getPassword())); // 加密密码
        user.setCreateTime(LocalDateTime.now());
        if (AuthUtils.getLoginUserId() != null) {
            user.setCreator(String.valueOf(AuthUtils.getLoginUserId()));
        }
        userMapper.insert(user);
        // 插入关联岗位
        if (!CollectionUtils.isEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(user.getPostIds().stream().map(postId -> {
                UserPostDO userPostDO = new UserPostDO();
                userPostDO.setUserId(user.getId());
                userPostDO.setPostId(postId);
                return userPostDO;
            }).collect(Collectors.toList()));
        }
        return user.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateRequest reqVO) {
        // 校验正确性
        validateUserForCreateOrUpdate(Long.parseLong(reqVO.getId()), reqVO.getUsername(), reqVO.getMobile(), reqVO.getEmail(),
                Long.parseLong(reqVO.getDeptId()),
                reqVO.getPostIds() == null ? null : reqVO.getPostIds().stream().map(Long::valueOf).collect(Collectors.toSet()));
        // 更新用户
        AdminUserDO updateObj = UserConverter.INSTANCE.convert(reqVO);
        userMapper.updateById(updateObj);
        // 更新岗位
        updateUserPost(reqVO, updateObj);
    }

    private void updateUserPost(UserUpdateRequest reqVO, AdminUserDO updateObj) {
        Set<Long> postIds = updateObj.getPostIds();
        if (postIds == null) {
            return;
        }
        Long userId = Long.parseLong(reqVO.getId());
        Set<Long> dbPostIds = userPostMapper.selectListByUserId(userId).stream()
                .map(UserPostDO::getPostId).collect(Collectors.toSet());
        // 计算新增和删除的岗位编号
        Collection<Long> createPostIds = new HashSet<>(postIds);
        createPostIds.removeAll(dbPostIds);
        Collection<Long> deletePostIds = new HashSet<>(dbPostIds);
        deletePostIds.removeAll(postIds);

        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtils.isEmpty(createPostIds)) {
            userPostMapper.insertBatch(createPostIds.stream().map(postId -> {
                UserPostDO userPostDO = new UserPostDO();
                userPostDO.setUserId(userId);
                userPostDO.setPostId(postId);
                return userPostDO;
            }).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(deletePostIds)) {
            userPostMapper.deleteByUserIdAndPostId(userId, deletePostIds);
        }
    }

    public void updateUserLogin(Long id, String loginIp) {
        AdminUserDO adminUserDO = new AdminUserDO();
        adminUserDO.setId(id);
        adminUserDO.setLoginIp(loginIp);
        adminUserDO.setLoginDate(LocalDateTime.now());
        adminUserDO.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(adminUserDO);
    }

    public void updateUserProfile(Long id, UserProfileUpdateRequest reqVO) {
        // 校验正确性
        validateUserExists(id);
        validateEmailUnique(id, reqVO.getEmail());
        validateMobileUnique(id, reqVO.getMobile());

        AdminUserDO updateObj = UserConverter.INSTANCE.convert(reqVO);
        updateObj.setId(id);
        // 执行更新
        userMapper.updateById(updateObj);
    }

    public void updateUserPassword(Long id, UserProfileUpdatePasswordRequest reqVO) {
        // 校验旧密码密码
        validateOldPassword(id, reqVO.getOldPassword());
        // 执行更新
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setPassword(encodePassword(reqVO.getNewPassword())); // 加密密码
        userMapper.updateById(updateObj);
    }

    public void updateUserPassword(Long id, String password) {
        // 校验用户存在
        validateUserExists(id);
        // 更新密码
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setPassword(encodePassword(password)); // 加密密码
        userMapper.updateById(updateObj);
    }

    public void updateUserStatus(Long id, Integer status) {
        // 校验用户存在
        validateUserExists(id);
        // 更新状态
        AdminUserDO updateObj = new AdminUserDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        updateObj.setUpdateTime(LocalDateTime.now());
        if (AuthUtils.getLoginUserId() != null) {
            updateObj.setUpdater(String.valueOf(AuthUtils.getLoginUserId()));
        }
        userMapper.updateById(updateObj);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 校验用户存在
        validateUserExists(id);
        // 删除用户
        userMapper.deleteById(id);
        // 删除用户关联数据
        permissionService.processUserDeleted(id);
        // 删除用户岗位
        userPostMapper.deleteByUserId(id);
    }

    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }


    public AdminUserDO getUserByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    public IPage<AdminUserDO> getUserPage(UserQueryRequest reqVO) {
        return userMapper.selectPage(reqVO, getDeptCondition(reqVO.getDeptId() == null ?
                null : Long.parseLong(reqVO.getDeptId())));
    }

    public AdminUserDO getUser(Long id) {
        return userMapper.selectById(id);
    }

    public List<AdminUserDO> getUserListByDeptIds(Collection<Long> deptIds) {
        if (CollectionUtils.isEmpty(deptIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectListByDeptIds(deptIds);
    }

    public List<AdminUserDO> getUserListByPostIds(Collection<Long> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        Set<Long> userIds = userPostMapper.selectListByPostIds(postIds).stream().map(UserPostDO::getUserId)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(userIds);
    }

    public List<AdminUserDO> getUserList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.selectBatchIds(ids);
    }

    public void validateUserList(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<AdminUserDO> users = userMapper.selectBatchIds(ids);
        Map<Long, AdminUserDO> userMap = users.stream().collect(Collectors.toMap(AdminUserDO::getId, p -> p));
        // 校验
        ids.forEach(id -> {
            AdminUserDO user = userMap.get(id);
            if (user == null) {
                throw new ServiceException(ErrorCodeConstants.USER_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw new ServiceException(ErrorCodeConstants.USER_IS_DISABLE, user.getNickname());
            }
        });
    }

    public List<AdminUserDO> getUserListByNickname(String nickname) {
        return userMapper.selectListByNickname(nickname);
    }


    private Set<Long> getDeptCondition(Long deptId) {
        if (deptId == null) {
            return Collections.emptySet();
        }
        Set<Long> deptIds = deptService.getChildDeptList(deptId).stream().map(DeptDO::getId)
                .collect(Collectors.toSet());
        deptIds.add(deptId); // 包括自身
        return deptIds;
    }

    private void validateUserForCreateOrUpdate(Long id, String username, String mobile, String email, Long deptId, Set<Long> postIds) {
        // 校验用户存在
        validateUserExists(id);
        // 校验用户名唯一
        validateUsernameUnique(id, username);
        // 校验手机号唯一
        validateMobileUnique(id, mobile);
        // 校验邮箱唯一
        validateEmailUnique(id, email);
        // 校验部门处于开启状态
        deptService.validateDeptList(Collections.singleton(deptId));
        // 校验岗位处于开启状态
        postService.validatePostList(postIds);
    }

    void validateUserExists(Long id) {
        if (id == null) {
            return;
        }
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(ErrorCodeConstants.USER_NOT_EXISTS);
        }
    }

    void validateUsernameUnique(Long id, String username) {
        if (!StringUtils.hasLength(username)) {
            return;
        }
        AdminUserDO user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.USER_USERNAME_EXISTS);
        }
    }

    void validateEmailUnique(Long id, String email) {
        if (!StringUtils.hasLength(email)) {
            return;
        }
        AdminUserDO user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.USER_EMAIL_EXISTS);
        }
    }

    void validateMobileUnique(Long id, String mobile) {
        if (!StringUtils.hasLength(mobile)) {
            return;
        }
        AdminUserDO user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        if (id == null) {
            throw new ServiceException(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw new ServiceException(ErrorCodeConstants.USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     *
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    void validateOldPassword(Long id, String oldPassword) {
        AdminUserDO user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw new ServiceException(ErrorCodeConstants.USER_PASSWORD_FAILED);
        }
    }

    public List<AdminUserDO> getUserListByStatus(Integer status) {
        return userMapper.selectListByStatus(status);
    }

    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Map<Long, AdminUserDO> getUserMap(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new HashMap<>();
        }
        return getUserList(ids).stream().collect(Collectors.toMap(AdminUserDO::getId, p -> p));
    }
}
