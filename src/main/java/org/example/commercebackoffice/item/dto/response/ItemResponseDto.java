package org.example.commercebackoffice.item.dto.response;

import lombok.Getter;
import org.example.commercebackoffice.item.domain.Item;

@Getter
public class ItemResponseDto {
    private Long id;
    private Long adminId;
    private String name;
    private String category;
    private Long price;
    private Integer stock;
    private String status;

    // Entity를 DTO로 변환해주는 생성자
    public ItemResponseDto(Item item) {
        this.id = item.getId();
        //  Admin 객체가 존재하면 ID를 가져오고, 없으면 null 처리
        this.adminId = item.getAdmin() != null ? item.getAdmin().getId() : null;
        this.name = item.getName();
        this.category = item.getCategory();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.status = item.getStatus().name();
    }
}