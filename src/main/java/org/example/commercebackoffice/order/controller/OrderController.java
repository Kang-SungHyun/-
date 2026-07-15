package org.example.commercebackoffice.order.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.order.domain.dto.*;
import org.example.commercebackoffice.order.domain.enums.OrderStatus;
import org.example.commercebackoffice.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderCreateRequestDto requestDto,
            HttpSession session
    ) {
        Long loginAdminId = (Long) session.getAttribute("adminId");
        OrderResponseDto response = orderService.createOrder(requestDto, loginAdminId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<OrderListResponseDto>> getOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderedAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(required = false) OrderStatus status
    ) {
        PageResponseDto<OrderListResponseDto> response =
                orderService.getOrders(keyword, page, size, sort, order, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrder(@PathVariable Long orderId) {
        OrderDetailResponseDto response = orderService.getOrder(orderId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto requestDto
    ) {
        OrderResponseDto response = orderService.updateOrderStatus(orderId, requestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderCancelRequestDto requestDto
    ) {
        OrderResponseDto response = orderService.cancelOrder(orderId, requestDto);
        return ResponseEntity.ok(response);
    }
}