package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalPriceOrderResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.serivce.OrderService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private static final Logger log = LogManager.getLogger(OrderController.class);
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


    @GetMapping("/date")
    public ApiResponse<PagingResponse<OrderResponse>> getOrderByBetweenDates(@Valid @RequestBody DateRangeRequest dateRangeRequest) {
        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDER_BETWEEN_DATE.getCode())
                .message(ResponseCode.GET_ORDER_BETWEEN_DATE.getMessage())
                .data(orderService.getOrderByBetweenDates(dateRangeRequest))
                .build();
    }

    @GetMapping("/paginate")
    public ApiResponse<PagingResponse<OrderResponse>> getOrdersPaginate(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return ApiResponse.<PagingResponse<OrderResponse>>builder()
                .code(ResponseCode.GET_ORDERS_PAGINATE.getCode())
                .message(ResponseCode.GET_ORDERS_PAGINATE.getMessage())
                .data(orderService.getAllOrderWithPaginate(page, size))
                .build();
    }

    @GetMapping("/total/date")
    public ApiResponse<TotalPriceOrderResponse> getTotalPriceByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ApiResponse.<TotalPriceOrderResponse>builder()
                .code(ResponseCode.GET_TOTAL_ORDER_DATE.getCode())
                .message(ResponseCode.GET_TOTAL_ORDER_DATE.getMessage())
                .data(orderService.getTotalPriceByDate(date))
                .build();
    }

    @GetMapping("/total/store/dates")
    public ApiResponse<TotalPriceOrderResponse> getTotalPriceByStoreIdAndDateBetween(
            @RequestParam("storeId") Long storeId,
            @Valid @RequestBody DateRangeRequest dateRangeRequest
    ) {
        return ApiResponse.<TotalPriceOrderResponse>builder()
                .code(ResponseCode.GET_TOTAL_ORDER_DATE.getCode())
                .message(ResponseCode.GET_TOTAL_ORDER_DATE.getMessage())
                .data(orderService.getTotalPriceByStoreIdAndDateRange(storeId, dateRangeRequest))
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


}
