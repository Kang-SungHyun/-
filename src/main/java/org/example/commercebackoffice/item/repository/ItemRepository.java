package org.example.commercebackoffice.item.repository;

import org.example.commercebackoffice.item.domain.Item;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // 단건 조회 시
    Optional<Item> findByIdAndStatusNot(Long id, ItemStatus status);
    // 전체 조회 시
    List<Item> findAllByStatusNot(ItemStatus status);

}
