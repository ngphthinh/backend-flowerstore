package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidUpperCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @Email(message = "INVALID_EMAIL_REGEX", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @ValidUpperCase(message = "INVALID_ROLE_UPPERCASE")
    @NotEmpty(message = "INVALID_ROLE_NAME")
    private String role;

    @NotNull(message = "INVALID_STORE_ID")
    private Long storeId;
}
