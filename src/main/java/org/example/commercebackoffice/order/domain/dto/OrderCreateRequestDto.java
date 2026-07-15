package org.example.commercebackoffice.order.domain.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequestDto {

    @NotNull
    private Long customerId;

    @NotNull
    private Long itemId;

    @NotNull
    @Min(1)
    private Integer quantity;
}
