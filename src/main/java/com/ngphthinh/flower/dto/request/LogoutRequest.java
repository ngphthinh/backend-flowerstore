package com.ngphthinh.flower.dto.request;

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
public class LogoutRequest {
    @NotBlank(message = "INVALID_ACCESS_TOKEN")
    private String accessToken;
    @NotNull(message = "INVALID_REFRESH_TOKEN")
    private String refreshToken;
}
