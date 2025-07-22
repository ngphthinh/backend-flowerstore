package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.UserChangePasswordRequest;
import com.ngphthinh.flower.dto.request.UserCreationRequest;
import com.ngphthinh.flower.dto.request.UserUpdateRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.UserChangePasswordResponse;
import com.ngphthinh.flower.dto.response.UserResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(ResponseCode.CREATE_USER.getCode())
                .message(ResponseCode.CREATE_USER.getMessage())
                .data(userService.createUser(request))
                .build();
    }

    @GetMapping
    public ApiResponse<PagingResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "1", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        return ApiResponse.<PagingResponse<UserResponse>>builder()
                .code(ResponseCode.GET_USER_LIST.getCode())
                .message(ResponseCode.GET_USER_LIST.getMessage())
                .data(userService.getAllUsers(page, size))
                .build();
    }


    @GetMapping("/{phone}")
    public ApiResponse<UserResponse> getUserByPhone(@PathVariable String phone) {
        return ApiResponse.<UserResponse>builder()
                .code(ResponseCode.GET_USER_BY_PHONE.getCode())
                .message(ResponseCode.GET_USER_BY_PHONE.getMessage())
                .data(userService.getUserByPhoneNumber(phone))
                .build();
    }

    @PutMapping("/{phone}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable String phone,
            @RequestBody UserUpdateRequest request
    ) {
        return ApiResponse.<UserResponse>builder()
                .code(ResponseCode.UPDATE_USER.getCode())
                .message(ResponseCode.UPDATE_USER.getMessage())
                .data(userService.updateUser(phone, request))
                .build();
    }

    @DeleteMapping("/{phone}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable String phone) {
        return ApiResponse.<UserResponse>builder()
                .code(ResponseCode.DELETE_USER.getCode())
                .message(ResponseCode.DELETE_USER.getMessage())
                .data(userService.deleteUserByPhoneNumber(phone))
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<UserChangePasswordResponse> changePassword(@Valid @RequestBody UserChangePasswordRequest request) {
        return ApiResponse.<UserChangePasswordResponse>builder()
                .code(ResponseCode.CHANGE_PASSWORD.getCode())
                .message(ResponseCode.CHANGE_PASSWORD.getMessage())
                .data(userService.changePassword(request))
                .build();
    }

}
