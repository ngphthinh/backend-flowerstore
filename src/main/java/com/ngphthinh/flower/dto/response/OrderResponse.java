package com.ngphthinh.flower.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private Long orderId;
    private String customerName;
    private String deliveryMethod;
    private LocalDateTime orderDate;
    private String note;
    private double totalPrice;
    private String storeName;
    private List<OrderDetailResponse> orderDetailResponses;
}
