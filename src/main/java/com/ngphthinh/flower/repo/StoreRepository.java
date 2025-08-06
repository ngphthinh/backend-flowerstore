package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.dto.response.StoreStatisticsResponse;
import com.ngphthinh.flower.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("""
    SELECT new com.ngphthinh.flower.dto.response.StoreStatisticsResponse(
        s.id,
        s.name,
        SUM(o.totalPrice),
        COUNT(o.orderId),
        AVG(o.totalPrice)
    )
    FROM Store s
    JOIN Order o ON o.store.id = s.id
    WHERE o.createdAt BETWEEN :startDate AND :endDate
    GROUP BY s.id, s.name
    """)
    List<StoreStatisticsResponse> getStoreStatistics(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
    SELECT new com.ngphthinh.flower.dto.response.StoreStatisticsResponse(
        SUM(o.totalPrice),
        COUNT(o.orderId),
        AVG(o.totalPrice)
    )
    FROM Store s
    JOIN Order o ON o.store.id = s.id
    WHERE o.createdAt BETWEEN :startDate AND :endDate
    """)
    StoreStatisticsResponse getStoreStatisticsAll(LocalDateTime startDate, LocalDateTime endDate);



}
