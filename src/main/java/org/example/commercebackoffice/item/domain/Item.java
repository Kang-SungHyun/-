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
        updateStatusByStock();
    }
    private void updateStatusByStock() {
        // 단종 상태면 상태 변경 무시
        if (this.status == ItemStatus.DISCONTINUED) {
            return;
        }
        // 재고가 0 이하면 품절, 1 이상이면 판매중 처리
        if (this.stock <= 0) {
            this.status = ItemStatus.SOLD_OUT;
        } else {
            this.status = ItemStatus.ON_SALE;
        }
    }

    //  상품 정보 수정 비즈니스 로직
    public void updateItem(String name, String category, Long price, Integer stock, ItemStatus status) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    // Item 삭제 기능 비즈니스 로직 (Soft Delete)
    public void discontinue() {
        this.status = ItemStatus.DISCONTINUED;
        super.delete(); // BaseEntity의 삭제 시간(deletedAt)과 deleted=true도 함께 업데이트
    }

}
