package org.example.commercebackoffice.order.domain.dto;

import lombok.Getter;
import org.example.commercebackoffice.admin.domain.Admin;
import org.example.commercebackoffice.order.domain.Order;
import org.example.commercebackoffice.order.domain.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
public class OrderDetailResponseDto {

    private final String orderNumber;
    private final String customerName;
    private final String customerEmail;
    private final String itemName;
    private final Integer quantity;
    private final Long totalPrice;
    private final LocalDateTime orderedAt;
    private final OrderStatus status;
    private final String adminName;
    private final String adminEmail;
    private final String adminRole;

    public OrderDetailResponseDto(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.customerEmail = order.getCustomer().getEmail();
        this.itemName = order.getItem().getName();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getOrderedAt();
        this.status = order.getStatus();

        Admin admin = order.getAdmin();
        this.adminName = admin != null ? admin.getName() : null;
        this.adminEmail = admin != null ? admin.getEmail() : null;
        this.adminRole = admin != null ? admin.getRole().name() : null;
    }
}