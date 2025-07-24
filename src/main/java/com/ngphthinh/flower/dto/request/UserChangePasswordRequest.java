package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidPassword;
import com.ngphthinh.flower.validator.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordRequest {
    @ValidPhone(message = "INVALID_PHONE_REGEX")
    @NotBlank(message = "INVALID_PHONE_NUMBER")
    private String phoneNumber;

    @ValidPassword(message = "INVALID_PASSWORD_REGEX")
    private String oldPassword;

    @ValidPassword(message = "INVALID_PASSWORD_REGEX")
    private String newPassword;
}
