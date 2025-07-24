package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidUpperCase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminUpdateRequest {

    private String fullName;

    @ValidUpperCase(message = "INVALID_ROLE_UPPERCASE")
    @NotEmpty(message = "INVALID_ROLE_NAME")
    private String role;

    @NotNull(message = "INVALID_STORE_ID")
    private Long storeId;
}
