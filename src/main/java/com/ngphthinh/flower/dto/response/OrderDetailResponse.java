package com.ngphthinh.flower.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailResponse {
    private Long id;
    private String productName;
    private double price;
    private double subtotal;
    private int quantity;
    private String url;
}
