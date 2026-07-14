package org.example.commercebackoffice.item.controller;

import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.example.commercebackoffice.item.dto.request.ItemCreateRequestDto;
import org.example.commercebackoffice.item.dto.request.ItemUpdateRequestDto;
import org.example.commercebackoffice.item.dto.response.ItemResponseDto;
import org.example.commercebackoffice.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor

public class ItemController {

    private final ItemService itemService;

    // 상품 등록 (Create) API
    @PostMapping
    public ResponseEntity<ItemResponseDto> createItem(@Valid @RequestBody ItemCreateRequestDto requestDto) {

        ItemResponseDto responseDto = itemService.createItem(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 상품 단건 조회 API 추가
    @GetMapping("/{itemId}") // ex) GET/api/items/1
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long itemId) {
        ItemResponseDto responseDto = itemService.getItem(itemId);
        return ResponseEntity.ok(responseDto);
    }

    // 상품 전체 목록 조회 API 추가 (페이징 적용)
    @GetMapping // 예: GET /api/items?keyword=맥북&category=전자제품&page=0
    public ResponseEntity<Page<ItemResponseDto>> getAllItems(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ItemStatus status,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<ItemResponseDto> responseDtoPage = itemService.getAllItems(keyword, category, status, pageable);
        return ResponseEntity.ok(responseDtoPage);
    }

    // 상품 수정 API 추가
    @PutMapping("/{itemId}") // 예: PUT /api/items/1
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequestDto requestDto) {

        ItemResponseDto responseDto = itemService.updateItem(itemId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 상품 상태만 단독으로 변경하는 전용 API
    @PatchMapping("/{itemId}/status") // ex) PATCH /api/items/1/status
    public ResponseEntity<ItemResponseDto> updateItemStatus(
            @PathVariable Long itemId,
            @RequestParam ItemStatus status) {

        ItemResponseDto responseDto = itemService.updateItemStatus(itemId, status);
        return ResponseEntity.ok(responseDto);
    }


    // 상품 삭제 API 추가
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
}

