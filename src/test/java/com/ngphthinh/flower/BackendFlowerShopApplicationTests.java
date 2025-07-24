package com.ngphthinh.flower;


import com.ngphthinh.flower.dto.request.RefreshTokenRequest;
import com.ngphthinh.flower.serivce.RefreshTokenService;
import com.ngphthinh.flower.serivce.TokenBlackListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BackendFlowerShopApplicationTests {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Test
    void contextLoads() {
        refreshTokenService.saveRefreshToken("refreshTokenTest","1");

        System.out.println( "User id: "+refreshTokenService.getUserId("refreshTokenTest"));

//        refreshTokenService.deleteRefreshToken("refreshTokenTest");

    }

}
