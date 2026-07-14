package org.example.commercebackoffice.item.service;

import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.admin.domain.Admin;
import org.example.commercebackoffice.admin.repository.AdminRepository;
import org.example.commercebackoffice.item.domain.Item;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.example.commercebackoffice.item.dto.request.ItemCreateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemUpdateRequestDto;
import org.example.commercebackoffice.item.dto.response.ItemResponseDto;
import org.example.commercebackoffice.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public ItemResponseDto createItem(ItemCreateRequestDto requestDto) {

        // 1. 재서님의 AdminRepository를 사용해, 요청으로 들어온 관리자 ID가 진짜 DB에 있는지 검증
        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 ID입니다."));

        // 2. 검증이 끝났다면, Entity 생성
        Item item = new Item(
                admin,
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getPrice(),
                requestDto.getStock(),
                ItemStatus.ON_SALE
        );
        Item savedItem = itemRepository.save(item);
        return new ItemResponseDto(savedItem);
    }

    // 상품 단건 조회 기능 추가
    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findByIdAndStatus(itemId, ItemStatus.ON_SALE)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 상품입니다. 상품 ID: " + itemId));

        return new ItemResponseDto(item);
    }

    // 전체 목록 조회 시 삭제된 상품(DISCONTINUED)은 제외하고 노출
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItems() {
        List<Item> itemList = itemRepository.findAllByStatus(ItemStatus.ON_SALE);

        return itemList.stream()
                .map(ItemResponseDto::new)
                .toList();
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequestDto requestDto) {
        // 수정 시에도 판매 중인 상품만 수정 가능하도록 검증
        Item item = itemRepository.findByIdAndStatus(itemId, ItemStatus.ON_SALE)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 수정할 수 없는 상품입니다. 상품 ID: " + itemId));

        item.updateItem(
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getPrice(),
                requestDto.getStock(),
                requestDto.getStatus()
        );

        return new ItemResponseDto(item);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        if (item.getStatus() == ItemStatus.DISCONTINUED) {
            throw new IllegalStateException("이미 삭제된 상품입니다.");
        }

        item.discontinue();
    }
}