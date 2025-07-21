package com.ngphthinh.flower.serivce;

import com.ngphthinh.flower.entity.OrderDetail;
import com.ngphthinh.flower.entity.ProductImage;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.repo.OrderDetailRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class ImageAsyncService {

    private static final Logger log = LogManager.getLogger(ImageAsyncService.class);
    private final OrderDetailRepository orderDetailRepository;
    private final ProductImageService productImageService;

    public ImageAsyncService(OrderDetailRepository orderDetailRepository, ProductImageService productImageService) {
        this.orderDetailRepository = orderDetailRepository;
        this.productImageService = productImageService;
    }


    @Async
    public void uploadImageForProduct(byte[] imageBytes, Long orderDetailId) {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND, "id", orderDetailId.toString()));

        ProductImage productImage = productImageService.saveProductImage(imageBytes, orderDetail.getProductName());

        orderDetail.setProductImage(productImage);
        orderDetailRepository.save(orderDetail);
    }


    @Async
    public void deleteImage(String publicId) {
        productImageService.deleteImage(publicId);
    }
}