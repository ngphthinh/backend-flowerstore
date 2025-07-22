package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.RoleRequest;
import com.ngphthinh.flower.dto.response.RoleResponse;
import com.ngphthinh.flower.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    RoleResponse toRoleResponse(Role role);
}