package org.example.commercebackoffice.item.repository;

import org.example.commercebackoffice.item.domain.Item;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 특정 ID로 찾되, 단종(DISCONTINUED) 상태가 아닌 것만 가져오는 메서드
    Optional<Item> findByIdAndStatusNot(Long id, ItemStatus status);

    // [발표 핵심 포인트] 동적 다중 필터 검색 쿼리
    // 사용자가 키워드, 카테고리, 상태 중 무엇을 검색할지 모릅니다.
    // 값이 안 들어오면(IS NULL) 해당 조건은 무시하고, 값이 들어왔을 때만(LIKE 또는 =) 조건절이 발동하는 쿼리입니다.
    @Query("SELECT i FROM Item i WHERE i.status != 'DISCONTINUED' " +
            "AND (:keyword IS NULL OR i.name LIKE %:keyword%) " +
            "AND (:category IS NULL OR i.category = :category) " +
            "AND (:status IS NULL OR i.status = :status)")
    Page<Item> searchItems(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("status") ItemStatus status,
            Pageable pageable
    );
}
