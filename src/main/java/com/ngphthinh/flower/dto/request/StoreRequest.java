package com.ngphthinh.flower.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRequest {

    @NotBlank(message = "INVALID_NAME_STORE")
    private String name;

    @NotBlank(message = "INVALID_ADDRESS_STORE")
    private String address;
}
