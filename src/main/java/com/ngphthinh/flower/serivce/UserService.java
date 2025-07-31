package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.UserCreationRequest;
import com.ngphthinh.flower.dto.request.UserAdminUpdateRequest;
import com.ngphthinh.flower.dto.request.UserUpdateRequest;
import com.ngphthinh.flower.dto.response.PagingResponse;
import com.ngphthinh.flower.dto.response.UserResponse;
import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.entity.Store;
import com.ngphthinh.flower.entity.User;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.mapper.UserMapper;
import com.ngphthinh.flower.repo.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Value("${flower.default.password}")
    private String PASSWORD_DEFAULT;
    private static final String USER_ROLE = "USER";
    private static final Logger log = LogManager.getLogger(UserService.class);

    private static final String PHONE_KEY = "phoneNumber";

    private final StoreService storeService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    private final UserMapper userMapper;

    public UserService(StoreService storeService, UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, UserMapper userMapper) {
        this.storeService = storeService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(UserCreationRequest request) {

        Store store = storeService.getStoreEntityById(request.getStoreId());

        User user = userMapper.toUser(request);

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTS);
        }
        // get role user
        Role role = roleService.getRoleEntityById(USER_ROLE);
        if (role == null) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        user.setRole(role);
        user.setStore(store);

        user.setPassword(passwordEncoder.encode(PASSWORD_DEFAULT));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PagingResponse<UserResponse> getAllUsers(Pageable pageable, String roleName, String phoneNumber) {

        validatePaginate(pageable);

        if (roleName != null && roleName.isBlank()) {
            roleName = null;
        }
        if (phoneNumber != null && phoneNumber.isBlank()) {
            phoneNumber = null;
        }

        var pageResult = userRepository.findAllByPhoneNumberContainingIgnoreCaseOrRoleNameContainingIgnoreCase(phoneNumber, roleName, pageable);

        List<UserResponse> userResponses = pageResult.getContent().stream()
                .map(userMapper::toUserResponse)
                .toList();

        return PagingResponse.<UserResponse>builder()
                .size(pageable.getPageSize())
                .page(pageable.getPageNumber())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .content(userResponses)
                .build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse deleteUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, phoneNumber));
        userRepository.delete(user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String phoneNumber, UserAdminUpdateRequest request) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, phoneNumber));

        Store store = storeService.getStoreEntityById(request.getStoreId());
        Role role = roleService.getRoleEntityById(request.getRole());


        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setStore(store);
        return userMapper.toUserResponse(userRepository.save(user));
    }

//    public UserChangePasswordResponse changePassword(UserChangePasswordRequest request) {
//        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, request.getPhoneNumber()));
//
//        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
//            throw new AppException(ErrorCode.VERIFY_PASSWORD_FAILED);
//        }
//
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        return UserChangePasswordResponse.builder()
//                .success(true)
//                .build();
//    }

    public UserResponse getProfile() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, phoneNumber));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, phoneNumber));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("#phone == authentication.name")
    public UserResponse updateProfile(String phone, UserUpdateRequest request){
        User user = userRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, PHONE_KEY, phone));

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    private void validatePaginate(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();
        if (page < 0) {
            throw new AppException(ErrorCode.INVALID_PAGE);
        }

        if (size < 1 || size > 60) {
            throw new AppException(ErrorCode.INVALID_PAGE_SIZE);
        }
    }
}
