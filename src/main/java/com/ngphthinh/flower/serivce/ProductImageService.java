package com.ngphthinh.flower.serivce;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.ngphthinh.flower.entity.ProductImage;
import com.ngphthinh.flower.exception.AppException;
import com.ngphthinh.flower.exception.ErrorCode;
import com.ngphthinh.flower.repo.ProductImageRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.Normalizer;
import java.util.Map;

@Service
public class ProductImageService {
    private static final String SECURE_URL = "secure_url";
    private static final String PUBLIC_ID = "public_id";
    private final ProductImageRepository productImageRepository;
    private final Cloudinary cloudinary;

    public ProductImageService(ProductImageRepository productImageRepository, Cloudinary cloudinary) {
        this.productImageRepository = productImageRepository;
        this.cloudinary = cloudinary;
    }

    public ProductImage saveProductImage(byte[] imageBytes, String productName) {

        Map<?, ?> uploadParams = ObjectUtils.asMap(
                "folder", "flower",
                "transformation", new Transformation<>().width(300).height(300).crop("fill").gravity("auto")
        );

        Map<?, ?> uploadResult;
        try {
            uploadResult = cloudinary.uploader().upload(imageBytes, uploadParams);
        } catch (IOException e) {
            throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }

        ProductImage productImage = ProductImage.builder()
                .id(buildIdImage(productName))
                .url(uploadResult.get(SECURE_URL).toString())
                .publicId(uploadResult.get(PUBLIC_ID).toString())
                .build();

        return productImageRepository.save(productImage);
    }

    private String buildIdImage(String productName) {
        String random = String.valueOf(System.currentTimeMillis() % 100);

        String normalized = Normalizer.normalize(productName, Normalizer.Form.NFD);
        String noAccent = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String productNameAfterConvert = noAccent.replaceAll("\\s+", "");

        return productNameAfterConvert + random;
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new AppException(ErrorCode.DELETE_IMAGE_FAILED);
        }
    }
}
