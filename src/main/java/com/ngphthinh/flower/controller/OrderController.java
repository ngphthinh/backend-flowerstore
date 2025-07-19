package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.OrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ApiResponse<OrderResponse> createOrder(
            @RequestPart("order") String jsonOrder,
            @RequestPart("orderDetails") String jsonOrderDetail,
            @RequestPart("images") List<MultipartFile> images,
            @RequestPart("imageIndexes") String imageIndexes
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.CREATE_ORDER_SUCCESS.getCode())
                .message(ResponseCode.CREATE_ORDER_SUCCESS.getMessage())
                .data(orderService.createOrder(jsonOrder, jsonOrderDetail, images, imageIndexes))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.GET_ORDER.getCode())
                .message(ResponseCode.GET_ORDER.getMessage())
                .data(orderService.getOrderById(id))
                .build();
    }

    @GetMapping("/invoice")
    public ApiResponse<List<OrderResponse>> getInvoices() {
        return ApiResponse.<List<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_LIST.getCode())
                .message(ResponseCode.GET_ORDER_LIST.getMessage())
                .data(orderService.getAllInvoices())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<OrderResponse> deleteOrder(@PathVariable("id") Long id) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.DELETE_ORDER.getCode())
                .message(ResponseCode.DELETE_ORDER.getMessage())
                .data(orderService.deleteOrder(id))
                .build();
    }

    @GetMapping()
    public ApiResponse<List<OrderResponse>> getOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_LIST.getCode())
                .message(ResponseCode.GET_ORDER_LIST.getMessage())
                .data(orderService.getAllOrders())
                .build();
    }

    @PostMapping("/product")
    public ApiResponse<OrderResponse> createOrders(@RequestBody CreateOrderBaseRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.CREATE_ORDER_SUCCESS.getCode())
                .message(ResponseCode.CREATE_ORDER_SUCCESS.getMessage())
                .data(orderService.createOrder(request))
                .build();
    }
}
