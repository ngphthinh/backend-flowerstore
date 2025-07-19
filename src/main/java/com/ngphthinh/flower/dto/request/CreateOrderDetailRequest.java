package com.ngphthinh.flower.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDetailRequest {
    private String productName;
    private double price;
    private int quantity;
}
