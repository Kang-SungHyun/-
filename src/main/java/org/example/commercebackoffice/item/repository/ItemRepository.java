package org.example.commercebackoffice.item.repository;

import org.example.commercebackoffice.item.domain.Item;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 단건 조회 시, ID와 상태값이 ON_SALE인 것만 조회
    Optional<Item> findByIdAndStatus(Long id, ItemStatus status);

    // 전체 조회 시, 판매 중(ON_SALE)인 상품만 리스트로 조회
    List<Item> findAllByStatus(ItemStatus status);

}
