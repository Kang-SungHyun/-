package org.example.commercebackoffice.item.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ItemUpdateRequestDto {

    // [멘토의 다이어트] 명세서에 "상품명, 카테고리, 가격"만 수정 가능하다고 되어 있어 stock, status 필드 삭제!
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "카테고리는 필수 입력 값입니다.")
    private String category;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    @Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
    private Long price;
}