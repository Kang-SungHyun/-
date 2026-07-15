package org.example.commercebackoffice.item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemStockUpdateRequestDto {
    @NotNull(message = "변경할 재고 수량을 입력해주세요.")
    @Min(value = 0, message = "재고 수량은 0개 이상이어야 합니다.")
    private Integer stock;
}