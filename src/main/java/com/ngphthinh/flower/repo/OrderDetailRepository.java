package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderOrderId(Long orderId);
    List<OrderDetail> findByOrderOrderIdAndProductImageIdNotNull(Long orderId);
}
