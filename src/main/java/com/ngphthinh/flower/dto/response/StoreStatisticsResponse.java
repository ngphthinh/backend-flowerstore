package com.ngphthinh.flower.dto.response;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Builder
public class StoreStatisticsResponse {
    private Long storeId;
    private String storeName;
    private Double totalRevenue;
    private Long totalOrders;
    private Double averageOrderValue;

    public StoreStatisticsResponse(Long storeId, String storeName, Double totalRevenue, Long totalOrders, Double averageOrderValue) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averageOrderValue = averageOrderValue;
    }

    public StoreStatisticsResponse(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalRevenue = 0.0;
        this.totalOrders = 0L;
        this.averageOrderValue = 0.0;
    }

    public StoreStatisticsResponse(Double totalRevenue, Long totalOrders, Double averageOrderValue) {
        this.storeId = null;
        this.storeName = null;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.averageOrderValue = averageOrderValue;
    }

}