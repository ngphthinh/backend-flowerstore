package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("select p.publicId from ProductImage p where p.createdAt < :createdAtBefore")
    List<String> findAllPublicIdByCreatedAtBefore(LocalDateTime createdAtBefore);

    void deleteByPublicId(String publicId);
}
