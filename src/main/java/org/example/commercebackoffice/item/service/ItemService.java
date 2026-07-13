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
        // 1. 창고(Repository)에서 ID 번호로 상품을 찾습니다. 없다면 에러를 던집니다!
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. 상품 ID: " + itemId));

        return new ItemResponseDto(item);
    }

    // 상품 전체 목록 조회 기능 추가
    @Transactional(readOnly = true)
    public java.util.List<ItemResponseDto> getAllItems() {
        // 1. DB에 있는 모든 상품 Entity 리스트를 꺼내옵니다.
        java.util.List<Item> itemList = itemRepository.findAll();

        // 2. Entity 리스트를 DTO 리스트로 변환 후 리턴 -> AI 추천 자바 문법
        return itemList.stream()
                .map(ItemResponseDto::new)
                .toList();
    }

    // 상품 수정 로직 추가
    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequestDto requestDto) {
        // 1. 창고에서 수정할 상품을 찾아옵니다.
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. 상품 ID: " + itemId));

        // 2. 찾아온 상품의 정보를 변경,
        item.updateItem(
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getPrice(),
                requestDto.getStock(),
                requestDto.getStatus()
        );

        return new ItemResponseDto(item);
    }

}