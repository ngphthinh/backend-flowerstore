package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.response.*;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    @GetMapping("/invoice")
    public ApiResponse<PagingResponse<OrderResponse>> getInvoices(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            Pageable pageable
    ) {

        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_LIST.getCode())
                .message(ResponseCode.GET_ORDER_LIST.getMessage())
                .data(orderService.getAllInvoices(storeId, startDate, endDate, pageable))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<PagingResponse<OrderResponse>> getInvoices(
            @RequestParam(value = "inputKey", required = false) String inputKey,
            Pageable pageable
    ) {

        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_LIST.getCode())
                .message(ResponseCode.GET_ORDER_LIST.getMessage())
                .data(orderService.getAllInvoicesByCustomerNameOrCustomerPhoneOrIdContainIgnoreCase(inputKey, pageable))
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

    @PostMapping("/product")
    public ApiResponse<OrderResponse> createBaseOrder(@Valid @RequestBody CreateOrderBaseRequest request) {
        return ApiResponse.<OrderResponse>builder()
                .code(ResponseCode.CREATE_ORDER.getCode())
                .message(ResponseCode.CREATE_ORDER.getMessage())
                .data(orderService.createOrder(request))
                .build();
    }


    @GetMapping("/total")
    public ApiResponse<TotalPriceOrderResponse> getTotalPriceByStoreIdAndDate(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ApiResponse.<TotalPriceOrderResponse>builder()
                .code(ResponseCode.GET_TOTAL_ORDER_DATE.getCode())
                .message(ResponseCode.GET_TOTAL_ORDER_DATE.getMessage())
                .data(orderService.getTotalPriceByStoreIdAndDate(storeId, startDate, endDate))
                .build();
    }

    @GetMapping("/statistics/price")
    public ApiResponse<List<OrderTotalByDateResponse>> getOrderTotalByDate(
            @RequestParam String stateStatisticsDay
        ) {
        return ApiResponse.<List<OrderTotalByDateResponse>>builder()
                .code(ResponseCode.GET_TOTAL_ORDER_DATE.getCode())
                .message(ResponseCode.GET_TOTAL_ORDER_DATE.getMessage())
                .data(orderService.getTotalByOrderDate(stateStatisticsDay))
                .build();
    }
}
