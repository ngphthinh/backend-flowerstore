package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalPriceOrderResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.OrderService;
import jakarta.validation.Valid;
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
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "imageIndexes", required = false) String imageIndexes
    ) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.CREATE_ORDER.getCode())
                .message(ResponseCode.CREATE_ORDER.getMessage())
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

    @GetMapping("/all")
    public ApiResponse<List<OrderResponse>> getOrders() {
        return ApiResponse.<List<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_LIST.getCode())
                .message(ResponseCode.GET_ORDER_LIST.getMessage())
                .data(orderService.getAllOrders())
                .build();
    }

    @PostMapping("/product")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderBaseRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.CREATE_ORDER.getCode())
                .message(ResponseCode.CREATE_ORDER.getMessage())
                .data(orderService.createOrder(request))
                .build();
    }

    @GetMapping("/total")
    public ApiResponse<TotalPriceOrderResponse> getTotalPriceByBetweenDates(@RequestBody DateRangeRequest dateRangeRequest) {
        return ApiResponse.<TotalPriceOrderResponse>builder()
                .code(ResponseCode.GET_TOTAL_EXPENSE.getCode())
                .message(ResponseCode.GET_TOTAL_EXPENSE.getMessage())
                .data(orderService.getTotalPriceBetweenDates(dateRangeRequest))
                .build();
    }

    @GetMapping("/date")
    public ApiResponse<PagingResponse<OrderResponse>> getOrderByBetweenDates(@RequestBody DateRangeRequest dateRangeRequest) {
        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_BETWEEN_DATE.getCode())
                .message(ResponseCode.GET_ORDER_BETWEEN_DATE.getMessage())
                .data(orderService.getOrderByDate(dateRangeRequest))
                .build();
    }

    @GetMapping("/paginate")
    public ApiResponse<PagingResponse<OrderResponse>> getOrdersPaginate(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDERS_PAGINATE.getCode())
                .message(ResponseCode.GET_ORDERS_PAGINATE.getMessage())
                .data(orderService.getAllOrderWithPaginate(page, size))
                .build();
    }
}
