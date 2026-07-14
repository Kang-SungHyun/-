package org.example.commercebackoffice.item.dto.response;

import lombok.Getter;
import org.example.commercebackoffice.item.domain.Item;

@Getter
public class ItemResponseDto {
    private Long id;
    private Long adminId;
    private String adminName;
    private String adminEmail;
    private String name;
    private String category;
    private Long price;
    private Integer stock;
    private String status;

    // Entity를 DTO로 변환해주는 생성자
    public ItemResponseDto(Item item) {
        this.id = item.getId();
        //  Admin 객체가 존재하면 ID, 이름, 이메일을 모두 가져옵니다
        if (item.getAdmin() != null) {
            this.adminId = item.getAdmin().getId();
            this.adminName = item.getAdmin().getName();
            this.adminEmail = item.getAdmin().getEmail();
        }
        this.name = item.getName();
        this.category = item.getCategory();
        this.price = item.getPrice();
        this.stock = item.getStock();
        this.status = item.getStatus().name();
    }
}