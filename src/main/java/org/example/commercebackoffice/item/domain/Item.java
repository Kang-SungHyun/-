package org.example.commercebackoffice.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.commercebackoffice.admin.domain.Admin;
import org.example.commercebackoffice.common.entity.BaseEntity;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;

@Getter
@Entity
@Table(name = "item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String name;
    private String category;
    private Long price;
    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    // 상품 생성을 위한 생성자
    public Item(Admin admin, String name, String category, Long price, Integer stock, ItemStatus status) {
        this.admin = admin;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    // 주문 기능(용범님)과 연동될 재고 관리 비즈니스 로직
    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.stock);
        }
        this.stock -= quantity;
    }

    //  상품 정보 수정 비즈니스 로직
    public void updateItem(String name, String category, Long price, Integer stock, ItemStatus status) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

}