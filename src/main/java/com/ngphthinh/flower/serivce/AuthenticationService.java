package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.dto.request.AuthenticationRequest;
import com.ngphthinh.flower.dto.request.IntrospectRequest;
import com.ngphthinh.flower.dto.request.LogoutRequest;
import com.ngphthinh.flower.dto.response.AuthenticationResponse;
import com.ngphthinh.flower.dto.response.IntrospectResponse;
import com.ngphthinh.flower.entity.ActiveToken;
import com.ngphthinh.flower.entity.Permission;
import com.ngphthinh.flower.entity.Role;
import com.ngphthinh.flower.entity.User;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.repo.ActiveTokenRepository;
import com.ngphthinh.flower.repo.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ActiveTokenRepository activeTokenRepository;

    @Value("${jwt.valid-duration}")
    private Long VALID_DURATION;

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, ActiveTokenRepository activeTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activeTokenRepository = activeTokenRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "phoneNumber", request.getPhoneNumber()));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String token = generateAccessToken(user);
        return AuthenticationResponse.builder()
                .accessToken(token)
                .phoneNumber(user.getPhoneNumber())
                .roleAndPermission(buildScope(user))
                .refreshToken("refresh-token-placeholder")
                .build();

    }

    private String generateAccessToken(User user) {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS512)
                .type(JOSEObjectType.JWT)
                .build();

        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .subject(user.getPhoneNumber())
                .issuer("ngphthinh")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);


        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            // Store the token in the active token repository
            saveActiveToken(claimSet);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.GENERATE_TOKEN_FAILED);
        }
    }

    private void saveActiveToken(JWTClaimsSet claimSet) {
        ActiveToken activeToken = ActiveToken.builder()
                .id(claimSet.getJWTID())
                .expiryTime(LocalDateTime.ofInstant(claimSet.getExpirationTime().toInstant(), ZoneId.systemDefault()))
                .build();
        activeTokenRepository.save(activeToken);
    }


    public IntrospectResponse introspect(IntrospectRequest request) {

        String token = request.getAccessToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .isValid(isValid)
                .build();

    }

    private SignedJWT verifyToken(String token, boolean isRefresh) {
        try {
            JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());

            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean verify = signedJWT.verify(jwsVerifier);

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (!(verify && expirationTime.after(new Date()))) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            if (!activeTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT;
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.INTROSPECT_FAILED);
        }
    }

    public void logout(LogoutRequest request) {
        try {
            SignedJWT signedJWT = verifyToken(request.getAccessToken(), false);
            activeTokenRepository.deleteById(signedJWT.getJWTClaimsSet().getJWTID());
        } catch (AppException | ParseException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }


    private String buildScope(User user) {
        if (user.getRole() != null) {
            Role userRole = user.getRole();
            String roleName = "ROLE_" + userRole.getName();
            String permissions = userRole.getPermissions().stream()
                    .map(Permission::getName)
                    .collect(Collectors.joining(" "));
            return roleName + " " + permissions;
        }
        return "";
    }


}
