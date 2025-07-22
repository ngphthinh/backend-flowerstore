package com.ngphthinh.flower.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordRequest {
    private String phoneNumber;
    private String oldPassword;
    private String newPassword;
}
