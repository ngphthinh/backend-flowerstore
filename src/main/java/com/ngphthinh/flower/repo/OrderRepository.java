package com.ngphthinh.flower.repo;

import com.ngphthinh.flower.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :startDate and :endDate")
    Optional<BigDecimal> sumTotalPriceByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Order> findOrdersByOrderDateBetween(LocalDateTime orderDateAfter, LocalDateTime orderDateBefore, Pageable pageable);

    Page<Order> findAllByStoreIdAndOrderDateBetween(Long storeId, LocalDateTime orderDateAfter, LocalDateTime orderDateBefore, Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE (o.store.id = :storeId or :storeId is null ) AND o.orderDate BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> sumTotalPriceByStoreIdAndOrderDateBetween(Long storeId, LocalDateTime startDate, LocalDateTime endDate);

    Optional<Integer> countOrderByOrderDateBetweenAndStoreId(LocalDateTime orderDateAfter, LocalDateTime orderDateBefore, Long storeId);

    Optional<Integer> countOrderByOrderDateBetween(LocalDateTime orderDateAfter, LocalDateTime orderDateBefore);

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



}
