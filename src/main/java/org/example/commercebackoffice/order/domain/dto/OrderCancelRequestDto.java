package org.example.commercebackoffice.order.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelRequestDto {

    @NotBlank
    private String cancelReason;
}