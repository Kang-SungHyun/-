package org.example.commercebackoffice.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 상품입니다. 상품 ID: " + itemId));

        return new ItemResponseDto(item);
    }

    // 전체 목록 조회 시 삭제 상태가 아닌 모든 상품(판매중, 품절 등) 조회
    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getAllItems(Pageable pageable) {
        // Repository에서 List가 아닌 Page로 받아옵니다.
        Page<Item> itemPage = itemRepository.findAllByStatusNot(ItemStatus.DISCONTINUED, pageable);

    // Page 객체는 stream() 없이도 내부 데이터를 변환하는 map() 기능을 지원합니다. -> AI 추천 자바 문법
        return itemPage.map(ItemResponseDto::new);
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequestDto requestDto) {
        // 품절(SOLD_OUT) 상태인 상품도 수정할 수 있게 변경
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
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