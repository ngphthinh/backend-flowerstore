package com.ngphthinh.flower.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngphthinh.flower.dto.request.CreateOrderBaseRequest;
import com.ngphthinh.flower.dto.request.CreateOrderRequest;
import com.ngphthinh.flower.dto.request.DateRangeRequest;
import com.ngphthinh.flower.dto.response.OrderDetailResponse;
import com.ngphthinh.flower.dto.response.OrderResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.TotalPriceOrderResponse;
import com.ngphthinh.flower.entity.Order;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.enums.DeliveryMethod;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.OrderMapper;
import com.ngphthinh.flower.repo.OrderRepository;
import jakarta.validation.ConstraintViolation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final StoreService storeService;

    private final Validator validator;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    private final OrderMapper orderMapper;

    private final OrderDetailService orderDetailService;

    public OrderService(StoreService storeService, Validator validator, OrderRepository orderRepository, ObjectMapper objectMapper, OrderMapper orderMapper, OrderDetailService orderDetailService) {
        this.storeService = storeService;
        this.validator = validator;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
        this.orderDetailService = orderDetailService;
    }

    public OrderResponse createOrder(String jsonOrder, String jsonOrderDetail, List<MultipartFile> images, String jsonArrayImgIndexes) {
        try {
            // Convert data raw

            List<byte[]> imagesBytes = images != null ? buildImageBytes(images) : new ArrayList<>();

            List<Integer> imageIndexes = (jsonArrayImgIndexes != null) ? objectMapper
                    .readValue(jsonArrayImgIndexes, objectMapper.getTypeFactory().constructCollectionType(List.class, Integer.class)) : new ArrayList<>();

            CreateOrderRequest orderRequest = objectMapper.readValue(jsonOrder, CreateOrderRequest.class);

            validateRequest(orderRequest);

            // Create and save temporary order
            Order order = orderMapper.toOrder(orderRequest);
            order.setStore(storeService.getStoreEntityById(orderRequest.getStoreId()));
            order.setOrderDetails(new ArrayList<>());
            OrderResponse orderResponse = orderMapper.toOrderResponse(orderRepository.save(order));

            // create list order details
            List<OrderDetailResponse> orderDetailResponses = orderDetailService.createOrderDetails(order, jsonOrderDetail, imagesBytes, imageIndexes);

            double totalPrice = orderDetailResponses.stream().map(OrderDetailResponse::getSubtotal)
                    .reduce(0.0, Double::sum);

            // update prices and set data to respond
            orderResponse.setOrderDetailResponses(orderDetailResponses);
            order.setTotalPrice(totalPrice);
            orderRepository.save(order);
            orderResponse.setTotalPrice(order.getTotalPrice());
            return orderResponse;
        } catch (IOException e) {
            throw new AppException(ErrorCode.MAPPER_ERROR);
        }
    }

    private void validateRequest(CreateOrderRequest createOrderRequest) {
        Set<ConstraintViolation<CreateOrderRequest>> violations = validator.validate(createOrderRequest);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new AppException(ErrorCode.valueOf(message));
        }
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, "id", id.toString()));
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.getOrderDetailsByOrderId(id);

        var orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(orderDetailResponses);
        return orderResponse;
    }

    public List<OrderResponse> getAllInvoices() {

        List<OrderResponse> orderResponses = orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse).toList();

        orderResponses.forEach(e ->
                e.setOrderDetailResponses(orderDetailService.getOrderDetailsByOrderId(e.getOrderId()))
        );
        return orderResponses;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse).toList();

    }

    private List<byte[]> buildImageBytes(List<MultipartFile> images) {
        return images.stream()
                .map(image -> {
                    try {
                        return image.getBytes();
                    } catch (IOException e) {
                        throw new AppException(ErrorCode.MAPPER_ERROR);
                    }
                })
                .toList();
    }

    public OrderResponse deleteOrder(Long id) {
        OrderResponse orderResponse = getOrderById(id);
        orderDetailService.deleteOrderDetailByOrderId(id);
        orderRepository.deleteById(id);
        return orderResponse;
    }

    public OrderResponse createOrder(CreateOrderBaseRequest request) {

        Store store = storeService.getStoreEntityById(request.getStoreId());

        Order order = Order.builder()
                .customerName("Khách lẻ")
                .deliveryMethod(DeliveryMethod.PICKUP.toString())
                .orderDate(LocalDateTime.now())
                .note(request.getNote())
                .totalPrice(request.getPrice())
                .store(store)
                .build();

        orderRepository.save(order);
        OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetails(order, request.getProductName(), request.getPrice(), request.getQuantity());
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderDetailResponses(List.of(orderDetailResponse));
        return orderResponse;
    }

    public TotalPriceOrderResponse getTotalPriceBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        BigDecimal totalPriceByDateBetween = orderRepository.sumTotalPriceByDateBetween(startDate, endDate).orElse(BigDecimal.ZERO);
        return TotalPriceOrderResponse.builder()
                .totalAmount(totalPriceByDateBetween)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public TotalPriceOrderResponse getTotalPriceByDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);

        BigDecimal totalPriceByStoreIdAndOrderDateBetween = orderRepository
                .sumTotalPriceByDateBetween(startDate, endDate)
                .orElse(BigDecimal.ZERO);

        return TotalPriceOrderResponse.builder()
                .totalAmount(totalPriceByStoreIdAndOrderDateBetween)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public PagingResponse<OrderResponse> getOrderByBetweenDates(DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime enDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        int size = dateRangeRequest.getSize();
        int page = dateRangeRequest.getPage();

        if (size < 1 || page < 1) {
            throw new AppException(ErrorCode.INVALID_PAGINATION);
        }

        if (startDate.isAfter(enDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("orderDate").descending());

        var orderPage = orderRepository.findOrdersByOrderDateBetween(startDate, enDate, pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderMapper::toOrderResponse)
                .toList();

        return PagingResponse.<OrderResponse>builder()
                .content(orderResponses)
                .totalPages(orderPage.getTotalPages())
                .totalElements(orderPage.getTotalElements())
                .page(page)
                .size(size)
                .build();
    }



    public PagingResponse<OrderResponse> getAllOrderWithPaginate(int page, int size) {
        if (page < 1 || size < 1) {
            throw new AppException(ErrorCode.INVALID_PAGINATION);
        }

        int pageRaw = page - 1;

        Pageable pageable = PageRequest.of(pageRaw, size, Sort.by("orderDate").descending());

        var orderPage = orderRepository.findAll(pageable);

        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(orderMapper::toOrderResponse)
                .toList();

        return PagingResponse.<OrderResponse>builder()
                .content(orderResponses)
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .page(page)
                .page(size)
                .build();
    }

    public TotalPriceOrderResponse getTotalPriceByStoreIdAndDateRange(Long storeId, DateRangeRequest dateRangeRequest) {
        LocalDateTime startDate = dateRangeRequest.getStartDate().atStartOfDay();
        LocalDateTime endDate = dateRangeRequest.getEndDate().atTime(LocalTime.MAX);

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        if (storeId == null || !storeService.isStoreExist(storeId)) {
            throw new AppException(ErrorCode.STORE_NOT_FOUND);
        }

        BigDecimal totalPriceByStoreIdAndOrderDateBetween = orderRepository
                .sumTotalPriceByStoreIdAndOrderDateBetween(storeId, startDate, endDate)
                .orElse(BigDecimal.ZERO);

        return TotalPriceOrderResponse.builder()
                .totalAmount(totalPriceByStoreIdAndOrderDateBetween)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

    public TotalPriceOrderResponse getTotalPriceByStoreIdAndDate(Long storeId, LocalDate date) {

        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        if (storeId == null || !storeService.isStoreExist(storeId)) {
            throw new AppException(ErrorCode.STORE_NOT_FOUND);
        }

        BigDecimal totalPriceByStoreIdAndOrderDateBetween = orderRepository
                .sumTotalPriceByStoreIdAndOrderDateBetween(storeId, startDate, endDate)
                .orElse(BigDecimal.ZERO);

        return TotalPriceOrderResponse.builder()
                .totalAmount(totalPriceByStoreIdAndOrderDateBetween)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
