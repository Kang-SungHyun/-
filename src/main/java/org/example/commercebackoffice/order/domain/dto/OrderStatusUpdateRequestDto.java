package org.example.commercebackoffice.order.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.commercebackoffice.order.domain.enums.OrderStatus;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequestDto {

    @NotNull
    private OrderStatus status;
}