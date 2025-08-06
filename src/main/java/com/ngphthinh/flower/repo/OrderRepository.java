package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.dto.response.OrderTotalByDateResponse;
import com.ngphthinh.flower.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE (o.store.id = :storeId or :storeId is null ) AND o.orderDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumTotalPriceByStoreIdAndOrderDateBetween(Long storeId, LocalDateTime startDate, LocalDateTime endDate);


    @Query("""
    SELECT o.orderId FROM Order o
    WHERE (:storeId IS NULL OR o.store.id = :storeId)
      AND (
            (:startDate IS NULL OR :endDate IS NULL)
            OR o.orderDate BETWEEN :startDate AND :endDate
          )
    ORDER BY o.orderDate DESC
    """)
    Page<Long> findAllByStoreIdOrderByOrderDateDesc(Long storeId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("""
            select distinct o from Order o
                join fetch o.store
                left join fetch o.orderDetails od
                left join fetch od.productImage
                where o.orderId in :ids
                order by o.orderDate desc""")
    List<Order> findOrderWithOrderDetailByIds(List<Long> ids);


    @Query("""
        select o.orderId from Order o
        where
             upper(o.customerPhone) like upper(concat('%', :inputKey, '%'))
            or upper(o.customerName) like upper(concat('%', :inputKey, '%'))
        order by o.orderDate desc
    """)
    Page<Long> findAllByOrCustomerPhoneContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String inputKey, Pageable pageable);

    @Query("""
            select new com.ngphthinh.flower.dto.response.OrderTotalByDateResponse (
                    cast(CAST(o.orderDate AS localdate) as string),
                    cast( sum(o.totalPrice) as bigdecimal),
                    count(o.orderId))
            from Order o
            where o.orderDate between :startDate and :endDate
            group by cast(CAST(o.orderDate AS localdate) as string)
            order by cast(CAST(o.orderDate AS localdate) as string) asc
            """)
   List<OrderTotalByDateResponse> findTotalByOrderDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        select new com.ngphthinh.flower.dto.response.OrderTotalByDateResponse (
                Cast(FUNCTION('QUARTER', o.orderDate) as string),
                cast(sum(o.totalPrice) as bigdecimal ),
                count (o.orderId))
        from Order o
        where FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', :now)
        group by CAST(FUNCTION('QUARTER', o.orderDate) AS string)
        order by CAST(FUNCTION('QUARTER', o.orderDate) AS string) asc
        """)
    List<OrderTotalByDateResponse> findTotalByQuarter( LocalDateTime now);


    @EntityGraph(attributePaths = {"store", "orderDetails"})
    List<Order> findAllByOrderDateBetween(LocalDateTime orderDateAfter, LocalDateTime orderDateBefore);
}
