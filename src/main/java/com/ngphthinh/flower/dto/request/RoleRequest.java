package com.ngphthinh.flower.dto.request;

import java.util.Set;

import com.ngphthinh.flower.validator.ValidUpperCase;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleRequest {

    @ValidUpperCase(message = "INVALID_ROLE_UPPERCASE")
    @NotEmpty(message = "INVALID_ROLE_NAME")
    private String name;
    private String description;
    private Set<String> permissions;


}