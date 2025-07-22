package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.UserCreationRequest;
import com.ngphthinh.flower.dto.response.UserResponse;
import com.ngphthinh.flower.entity.Permission;
import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "store", ignore = true)
    User toUser(UserCreationRequest request);


    @Mapping(target = "storeName", ignore = true)
    @Mapping(target = "roleAndPermissions", ignore = true)
    UserResponse toUserResponse(User user);

    @AfterMapping
    default void setStoreName(User user, @MappingTarget UserResponse userResponse) {
        if (user.getStore() != null) {
            userResponse.setStoreName(user.getStore().getName());
        }
    }

    @AfterMapping
    default void setRoleAndPermissions(User user, @MappingTarget UserResponse userResponse) {
        if (user.getRole() != null) {
            Role userRole = user.getRole();
            String roleName = "ROLE_" + userRole.getName();
            String permissions = userRole.getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.joining(" "));
            userResponse.setRoleAndPermissions(roleName + " " + permissions);
        }
    }
}