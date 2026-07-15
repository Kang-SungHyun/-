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
import org.example.commercebackoffice.item.dto.request.ItemStatusUpdateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemStockUpdateRequestDto;
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
        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 ID입니다."));

        Item item = new Item(admin, requestDto.getName(), requestDto.getCategory(),
                requestDto.getPrice(), requestDto.getStock(), requestDto.getStatus());
        return new ItemResponseDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 상품입니다. 상품 ID: " + itemId));
        return new ItemResponseDto(item);
    }

    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getAllItems(String keyword, String category, ItemStatus status, Pageable pageable) {
        return itemRepository.searchItems(keyword, category, status, pageable)
                .map(ItemResponseDto::new);
    }

    @Transactional
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequestDto requestDto) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 수정할 수 없는 상품입니다."));

        item.updateItem(requestDto.getName(), requestDto.getCategory(), requestDto.getPrice());
        return new ItemResponseDto(item);
    }

    // 1. 상태 전용 부분 수정
    @Transactional
    public ItemResponseDto updateItemStatus(Long itemId, ItemStatusUpdateRequestDto requestDto) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 수정할 수 없는 상품입니다."));

        item.updateStatus(requestDto.getStatus());
        return new ItemResponseDto(item);
    }

    // 2. 재고 전용 부분 수정
    @Transactional
    public ItemResponseDto updateItemStock(Long itemId, ItemStockUpdateRequestDto requestDto) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 수정할 수 없는 상품입니다."));

        item.updateStock(requestDto.getStock());
        return new ItemResponseDto(item);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
        item.discontinue();
    }
}