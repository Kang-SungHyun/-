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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 (프록시 생성을 위해 PROTECTED 사용)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // [멘토의 팁] 상품과 관리자의 관계는 N:1 (상품 여러 개를 관리자 한 명이 등록 가능)
    // FetchType.LAZY(지연 로딩)로 설정해서, 상품만 조회할 때 굳이 관리자 정보까지 DB에서 다 끌고 오지 않게 만듭니다. (성능 최적화)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String name;
    private String category;
    private Long price;
    private Integer stock;

    @Enumerated(EnumType.STRING) // DB에 숫자가 아닌 문자열(ON_SALE 등)로 저장되게 하여 가독성을 높임
    private ItemStatus status;

    // 상품 최초 생성을 위한 생성자
    public Item(Admin admin, String name, String category, Long price, Integer stock, ItemStatus status) {
        this.admin = admin;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    // [핵심 로직] 주문 기능과 연동될 재고 감소 비즈니스 로직 (캡슐화 원칙 적용!)
    // 밖에서 함부로 stock 값을 바꾸지 못하게 하고, 오직 이 메서드로만 재고를 줄입니다.
    public void decreaseStock(Integer quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + this.stock);
        }
        this.stock -= quantity;
        updateStatusByStock(); // 재고가 줄어들면 상태도 갱신되어야 하는지 확인
    }

    // 재고 수량에 따라 판매 상태를 자동으로 바꿔주는 똑똑한 내부 로직
    private void updateStatusByStock() {
        if (this.status == ItemStatus.DISCONTINUED) {
            return; // 이미 단종된 상품은 부활시키지 않음
        }
        // 재고가 0 이하면 품절, 1 이상이면 판매중
        if (this.stock <= 0) {
            this.status = ItemStatus.SOLD_OUT;
        } else {
            this.status = ItemStatus.ON_SALE;
        }
    }

    // 상품 기본 정보 수정 로직 (재고나 상태는 여기서 수정 못하도록 분리함)
    public void updateItem(String name, String category, Long price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // 관리자가 수동으로 상품 상태만 바꿀 때 사용하는 로직
    public void updateStatus(ItemStatus newStatus) {
        if (this.status == ItemStatus.DISCONTINUED) {
            throw new IllegalStateException("단종된 상품은 상태를 변경할 수 없습니다.");
        }
        this.status = newStatus;
    }

    // 상품 삭제 (데이터를 아예 날리지 않고 '단종' 처리하는 Soft Delete 기법)
    public void discontinue() {
        this.status = ItemStatus.DISCONTINUED;
        super.delete(); // BaseEntity의 deletedAt, deleted=true 업데이트
    }
}