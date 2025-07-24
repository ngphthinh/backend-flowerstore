package com.ngphthinh.flower.dto.request;

import com.ngphthinh.flower.validator.ValidPhone;
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

    @NotBlank(message = "INVALID_PHONE_NUMBER")
    @ValidPhone
    private String phoneNumber;

    @NotNull(message = "STORE_ID_NULL")
    private Long storeId;

}
