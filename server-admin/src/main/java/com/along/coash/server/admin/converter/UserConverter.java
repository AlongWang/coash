package com.along.coash.server.admin.converter;

import com.along.coash.server.admin.contract.profile.UserProfile;
import com.along.coash.server.admin.contract.profile.UserProfileUpdateRequest;
import com.along.coash.server.admin.contract.user.UserCreateRequest;
import com.along.coash.server.admin.contract.user.UserPageItem;
import com.along.coash.server.admin.contract.user.UserSimple;
import com.along.coash.server.admin.contract.user.UserUpdateRequest;
import com.along.coash.server.admin.entities.AdminUserDO;
import com.along.coash.server.admin.entities.DeptDO;
import com.along.coash.server.admin.entities.PostDO;
import com.along.coash.server.admin.entities.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserPageItem convert(AdminUserDO userDO);

    UserPageItem.Dept convert(DeptDO deptDO);

    AdminUserDO convert(UserCreateRequest request);

    AdminUserDO convert(UserUpdateRequest request);

    AdminUserDO convert(UserProfileUpdateRequest request);

    List<UserSimple> convert(List<AdminUserDO> list);

    UserProfile.Dept convertDept(DeptDO bean);

    List<UserProfile.Post> convertPosts(List<PostDO> list);

    List<UserProfile.Role> convertRoles(List<RoleDO> roles);

    UserProfile.Role convertRole(RoleDO roleDO);

    UserProfile.Post convertPost(PostDO postDO);

    UserSimple adminUserDOToUserSimpleRespVO(AdminUserDO adminUserDO);

    default UserProfile convertToUserProfile(AdminUserDO userDO, List<RoleDO> userRoles, DeptDO dept, List<PostDO> posts) {
        if (userDO == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(userDO.getUsername());
        userProfile.setNickname(userDO.getNickname());
        userProfile.setRemark(userDO.getRemark());
        if (userDO.getDeptId() != null) {
            userProfile.setDeptId(String.valueOf(userDO.getDeptId()));
        }
        if (userDO.getPostIds() != null) {
            userProfile.setPostIds(userDO.getPostIds().stream().map(String::valueOf).collect(Collectors.toSet()));
        }
        userProfile.setEmail(userDO.getEmail());
        userProfile.setMobile(userDO.getMobile());
        userProfile.setSex(userDO.getSex());
        userProfile.setAvatar(userDO.getAvatar());
        if (userDO.getId() != null) {
            userProfile.setId(String.valueOf(userDO.getId()));
        }
        userProfile.setStatus(userDO.getStatus());
        userProfile.setLoginIp(userDO.getLoginIp());
        userProfile.setLoginDate(userDO.getLoginDate());
        userProfile.setCreateTime(userDO.getCreateTime());

        userProfile.setDept(INSTANCE.convertDept(dept));
        userProfile.setRoles(INSTANCE.convertRoles(userRoles));
        userProfile.setPosts(INSTANCE.convertPosts(posts));

        return userProfile;
    }

}
