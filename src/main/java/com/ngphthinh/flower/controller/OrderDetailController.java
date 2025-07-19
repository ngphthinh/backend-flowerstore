package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.OrderDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-detail")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }


    @GetMapping("/{id}")
    public ApiResponse<List<OrderDetailResponse>> getOrderDetailByOrderId(@PathVariable("id") Long id) {
        return ApiResponse.<List<OrderDetailResponse>>builder()
                .code(ResponseCode.GET_ORDER.getCode())
                .message(ResponseCode.GET_ORDER.getMessage())
                .data(orderDetailService.getOrderDetailsByOrderId(id))
                .build();
    }
}
