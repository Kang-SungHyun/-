package org.example.commercebackoffice.item.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;

@Getter
public class ItemStatusUpdateRequestDto {
    @NotNull(message = "변경할 상태값을 입력해주세요.")
    private ItemStatus status;
}