package com.ngphthinh.flower.scheduled;

import com.ngphthinh.flower.serivce.ProductImageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AutoDeleteProductImage {

    private final ProductImageService productImageService;

    public AutoDeleteProductImage(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @Scheduled(cron = "0 0 2 * * SUN")
    public void deleteOldRecords() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(30);
        productImageService.getPublicIdSByCreatedAtBefore(threshold).forEach(productImageService::deleteImage);
    }

}
