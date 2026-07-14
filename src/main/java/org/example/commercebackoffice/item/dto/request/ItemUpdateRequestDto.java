package org.example.commercebackoffice.item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;

@Getter
public class ItemUpdateRequestDto {

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Long price;

    @NotNull(message = "재고 수량은 필수 입력 값입니다.")
    @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
    private Integer stock;

    @NotNull(message = "상품 상태는 필수 입력 값입니다.")
    private ItemStatus status;

}
