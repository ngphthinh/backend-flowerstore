package com.ngphthinh.flower.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenRequest {
    @NotBlank(message = "INVALID_REFRESH_TOKEN")
    private String refreshToken;
}