package org.example.commercebackoffice.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.example.commercebackoffice.item.dto.request.ItemCreateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemUpdateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemStatusUpdateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemStockUpdateRequestDto;
import org.example.commercebackoffice.item.dto.response.ItemResponseDto;
import org.example.commercebackoffice.item.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 등록 (Create)
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(requestDto));
    }

    // 상품 단건 조회 (Read)
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItem(itemId));
    }

    // 상품 전체조회 (Search & Paging)
    @GetMapping
    public ResponseEntity<Page<ItemResponseDto>> getAllItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ItemStatus status,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItems(keyword, category, status, pageable));
    }

    // 상품 기본 정보 전체 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequestDto requestDto) {
        return ResponseEntity.ok(itemService.updateItem(itemId, requestDto));
    }

    // 상품 상태 단독 수정 (상태 변경)
    @PatchMapping("/{itemId}/status")
    public ResponseEntity<ItemResponseDto> updateItemStatus(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemStatusUpdateRequestDto requestDto) {
        return ResponseEntity.ok(itemService.updateItemStatus(itemId, requestDto));
    }

    // 상품 재고 단독 수정 (재고 변경)
    @PatchMapping("/{itemId}/stock")
    public ResponseEntity<ItemResponseDto> updateItemStock(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemStockUpdateRequestDto requestDto) {
        return ResponseEntity.ok(itemService.updateItemStock(itemId, requestDto));
    }

    // 상품 삭제 (Soft Delete)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}