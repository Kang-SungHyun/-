package org.example.commercebackoffice.order.domain.dto;

import lombok.Getter;
import org.example.commercebackoffice.order.domain.Order;
import org.example.commercebackoffice.order.domain.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
public class OrderListResponseDto {
    
    private final Long id;
    private final String orderNumber;
    private final String customerName;
    private final String itemName;
    private final Integer quantity;
    private final Long totalPrice;
    private final LocalDateTime orderedAt;
    private final OrderStatus status;
    private final String adminName;

    public OrderListResponseDto(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.itemName = order.getItem().getName();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.status = order.getStatus();
        this.adminName = order.getAdmin() != null ? order.getAdmin().getName() : null;
    }
}