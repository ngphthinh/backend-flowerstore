package com.ngphthinh.flower.mapper;

import com.ngphthinh.flower.dto.request.PermissionRequest;
import com.ngphthinh.flower.dto.response.PermissionResponse;
import com.ngphthinh.flower.entity.Permission;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}