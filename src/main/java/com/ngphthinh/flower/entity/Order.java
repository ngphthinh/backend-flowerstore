package com.ngphthinh.flower.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private String customerName;
    private String  customerPhone;
    private String deliveryMethod;
    private LocalDateTime orderDate;
    private String note;
    private double totalPrice;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails;

    public Order() {
        this.orderDetails = new ArrayList<>();
    }

    public boolean addOrderDetail(OrderDetail od) {

        if (od == null) {
            return false;
        }
        od.setOrder(this);
        orderDetails.add(od);
        return true;
    }

    public boolean removeOrderDetail(OrderDetail od) {

        if (od == null) {
            return false;
        }
        od.setOrder(null);
        orderDetails.remove(od);
        return true;
    }

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
