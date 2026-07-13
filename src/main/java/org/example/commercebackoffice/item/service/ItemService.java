package org.example.commercebackoffice.item.service;

import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.admin.domain.Admin;
import org.example.commercebackoffice.admin.repository.AdminRepository;
import org.example.commercebackoffice.item.domain.Item;
import org.example.commercebackoffice.item.domain.enums.ItemStatus;
import org.example.commercebackoffice.item.dto.request.ItemCreateRequestDto;
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
}