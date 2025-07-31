package com.ngphthinh.flower.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String phoneNumber;
    private String fullName;
    private String storeName;
    private String email;
    private String roleAndPermissions;
}
