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

    // 상품과 관리자는 N:1 관계. 지연 로딩(LAZY)으로 성능 최적화
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String name;
    private String category;
    private Long price;
    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    // 최초 생성자
    public Item(Admin admin, String name, String category, Long price, Integer stock, ItemStatus status) {
        this.admin = admin;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    // [핵심 로직 1] 주문 발생 시 호출되는 재고 감소 로직
    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.stock);
        }
        this.stock -= quantity;
        updateStatusByStock(); // 재고 감소 후 자동으로 상태 동기화
    }

    // 재고 수량에 따라 판매 상태를 자동으로 바꿔주는 내부 로직 (단종 상품은 제외)
    private void updateStatusByStock() {
        if (this.status == ItemStatus.DISCONTINUED) return;

        if (this.stock <= 0) {
            this.status = ItemStatus.SOLD_OUT;
        } else {
            this.status = ItemStatus.ON_SALE;
        }
    }

    // 기본 정보(이름, 카테고리, 가격) 수정
    public void updateItem(String name, String category, Long price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // [핵심 로직 2] 수동 상태 변경
    public void updateStatus(ItemStatus newStatus) {
        if (this.status == ItemStatus.DISCONTINUED) {
            throw new IllegalStateException("단종된 상품은 상태를 변경할 수 없습니다.");
        }
        this.status = newStatus;
    }

    // [핵심 로직 3] 수동 재고 변경
    public void updateStock(Integer newStock) {
        if (this.status == ItemStatus.DISCONTINUED) {
            throw new IllegalStateException("단종된 상품은 재고를 변경할 수 없습니다.");
        }
        this.stock = newStock;
        updateStatusByStock(); // 재고가 수동으로 변경되면 이에 따라 상태도 자동 갱신
    }

    // 상품 삭제 (데이터 날리지 않고 단종 처리 Soft Delete)
    public void discontinue() {
        this.status = ItemStatus.DISCONTINUED;
        super.delete();
    }
}