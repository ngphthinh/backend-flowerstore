package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
