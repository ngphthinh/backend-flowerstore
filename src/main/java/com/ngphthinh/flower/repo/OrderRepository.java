package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :startDate and :endDate")
    Optional<BigDecimal> sumTotalPriceByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Order> findOrdersByOrderDateBetween(LocalDateTime orderDateAfter, LocalDateTime orderDateBefore, Pageable pageable);
}
