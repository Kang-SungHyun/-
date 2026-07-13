package org.example.commercebackoffice.item.controller;

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

    // 상품 전체 목록 조회 API 추가
    @GetMapping // 예: GET /api/items
    public ResponseEntity<java.util.List<ItemResponseDto>> getAllItems() {
        java.util.List<ItemResponseDto> responseDtoList = itemService.getAllItems();
        return ResponseEntity.ok(responseDtoList);
    }

    // 상품 수정 API 추가
    @PutMapping("/{itemId}") // 예: PUT /api/items/1
    public ResponseEntity<ItemResponseDto> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequestDto requestDto) {

        ItemResponseDto responseDto = itemService.updateItem(itemId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
