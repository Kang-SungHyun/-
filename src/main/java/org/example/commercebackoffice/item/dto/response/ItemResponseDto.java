package org.example.commercebackoffice.item.dto.response;

import lombok.Getter;
import org.example.commercebackoffice.item.domain.Item;
import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    public ItemResponseDto(Item item) {
        this.id = item.getId();
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
        this.createdAt = item.getCreatedAt();
    }
}