package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreationRequest {
    private String fullName;

    @Email(message = "INVALID_EMAIL_REGEX", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @NotBlank(message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "INVALID_PHONE_NUMBER")
    @ValidPhone(message = "INVALID_PHONE_REGEX")
    private String phoneNumber;

    @NotNull(message = "STORE_ID_NULL")
    private Long storeId;

}
