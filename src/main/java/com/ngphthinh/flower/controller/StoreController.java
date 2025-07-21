package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.StoreRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.StoreResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.StoreService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{id}")
    public ApiResponse<StoreResponse> getStoreById(@PathVariable("id") Long id) {
        return ApiResponse.<StoreResponse>builder()
                .code(ResponseCode.GET_STORE.getCode())
                .data(storeService.getStoreById(id))
                .message(ResponseCode.GET_STORE.getMessage())
                .build();
    }


    @GetMapping
    public ApiResponse<List<StoreResponse>> getAllStores() {
        return ApiResponse.<List<StoreResponse>>builder()
                .code(ResponseCode.GET_STORE_LIST.getCode())
                .message(ResponseCode.GET_STORE_LIST.getMessage())
                .data(storeService.getAllStore())
                .build();
    }

    @PostMapping
    public ApiResponse<StoreResponse> createStore(@Valid @RequestBody StoreRequest request) {
        return ApiResponse.<StoreResponse>builder()
                .code(ResponseCode.CREATE_STORE.getCode())
                .message(ResponseCode.CREATE_STORE.getMessage())
                .data(storeService.createStore(request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<StoreResponse> updateStore(@PathVariable("id") Long id) {
        return ApiResponse.<StoreResponse>builder()
                .code(ResponseCode.DELETE_STORE.getCode())
                .message(ResponseCode.DELETE_STORE.getMessage())
                .data(storeService.deleteById(id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<StoreResponse> updateStore(@PathVariable("id") Long id, @Valid @RequestBody StoreRequest request) {
        return ApiResponse.<StoreResponse>builder()
                .code(ResponseCode.UPDATE_STORE.getCode())
                .message(ResponseCode.UPDATE_STORE.getMessage())
                .data(storeService.updateStore(id, request))
                .build();
    }

}
