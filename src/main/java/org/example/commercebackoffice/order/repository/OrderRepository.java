package org.example.commercebackoffice.order.repository;

import org.example.commercebackoffice.order.domain.Order;
import org.example.commercebackoffice.order.domain.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o " +
            "WHERE (:status IS NULL OR o.status = :status) " +
            "AND (:keyword IS NULL " +
            "     OR o.orderNumber LIKE %:keyword% " +
            "     OR o.customer.name LIKE %:keyword%)")
    Page<Order> search(@Param("status") OrderStatus status,
                       @Param("keyword") String keyword,
                       Pageable pageable);
}
