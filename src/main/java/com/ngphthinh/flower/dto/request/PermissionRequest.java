package com.ngphthinh.flower.dto.request;


import com.ngphthinh.flower.validator.ValidUpperCase;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
public class PermissionRequest {

    @ValidUpperCase(message = "INVALID_PERMISSION_UPPERCASE")
    @NotEmpty(message = "INVALID_PERMISSION_NAME")
    private String name;
    private String description;
}