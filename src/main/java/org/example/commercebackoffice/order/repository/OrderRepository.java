package org.example.commercebackoffice.order.repository;

import org.example.commercebackoffice.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //git push
}
