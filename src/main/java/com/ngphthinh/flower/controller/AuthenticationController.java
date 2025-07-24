package com.ngphthinh.flower.controller;

import com.ngphthinh.flower.dto.request.AuthenticationRequest;
import com.ngphthinh.flower.dto.request.IntrospectRequest;
import com.ngphthinh.flower.dto.request.LogoutRequest;
import com.ngphthinh.flower.dto.request.RefreshTokenRequest;
import com.ngphthinh.flower.dto.response.ApiResponse;
import com.ngphthinh.flower.dto.response.AuthenticationResponse;
import com.ngphthinh.flower.dto.response.IntrospectResponse;
import com.ngphthinh.flower.enums.ResponseCode;
import com.ngphthinh.flower.serivce.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(ResponseCode.AUTHENTICATED.getCode())
                .message(ResponseCode.AUTHENTICATED.getMessage())
                .data(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .code(ResponseCode.INTROSPECTED.getCode())
                .message(ResponseCode.INTROSPECTED.getMessage())
                .data(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) {
        authenticationService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .code(ResponseCode.LOGGED_OUT.getCode())
                .message(ResponseCode.LOGGED_OUT.getMessage())
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(ResponseCode.REFRESHED.getCode())
                .message(ResponseCode.REFRESHED.getMessage())
                .data(authenticationService.refreshToken(request))
                .build();
    }

}
