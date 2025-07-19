package com.ngphthinh.flower.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
    private Long id;
    private String name;
    private String address;
}
