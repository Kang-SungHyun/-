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

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final AdminRepository adminRepository;

    // 상품 등록 로직
    @Transactional
    public ItemResponseDto createItem(ItemCreateRequestDto requestDto) {
        // 1. 관리자 검증: 요청받은 관리자 ID가 진짜 DB에 있는지 확인합니다.
        Admin admin = adminRepository.findById(requestDto.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 ID입니다."));

        // 2. 문제 없으면 Entity(실제 데이터 객체)를 만들어 DB에 저장합니다.
        Item item = new Item(
                admin,
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getPrice(),
                requestDto.getStock(),
                requestDto.getStatus() // DTO에서 받아온 초기 상태값 적용
        );
        Item savedItem = itemRepository.save(item);

        // 3. 손님(Client)에게 보여줄 영수증(DTO)으로 변환해서 리턴합니다.
        return new ItemResponseDto(savedItem);
    }

    // 상품 단건 조회
    @Transactional(readOnly = true) // 데이터 변경 없이 읽기만 하므로 최적화를 위해 readOnly 옵션 켬!
    public ItemResponseDto getItem(Long itemId) {
        // 단종(DISCONTINUED) 상태인 건 빼고 가져옵니다.
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 삭제된 상품입니다. 상품 ID: " + itemId));

        return new ItemResponseDto(item);
    }

    // 전체 목록 다중 검색 (페이징 적용)
    @Transactional(readOnly = true)
    public Page<ItemResponseDto> getAllItems(String keyword, String category, ItemStatus status, Pageable pageable) {
        // Repository의 똑똑한 만능 쿼리를 호출해서 조건에 맞는 데이터만 쏙 뽑아옵니다.
        Page<Item> itemPage = itemRepository.searchItems(keyword, category, status, pageable);

        // Page<Item> 을 Client가 보기 좋은 Page<ItemResponseDto> 형태로 매핑(변환)해줍니다.
        return itemPage.map(ItemResponseDto::new);
    }

    // 상품 정보 수정
    @Transactional // 메서드가 끝날 때 영속성 컨텍스트(1차 캐시)가 변경을 감지해서 자동으로 UPDATE 쿼리를 날려줍니다!
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequestDto requestDto) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 수정할 수 없는 상품입니다. 상품 ID: " + itemId));

        // 엔티티 내부에 만들어둔 안전한 메서드로만 값 수정 (캡슐화)
        item.updateItem(
                requestDto.getName(),
                requestDto.getCategory(),
                requestDto.getPrice()
        );
        return new ItemResponseDto(item);
    }

    // 상품 상태만 수동 변경
    @Transactional
    public ItemResponseDto updateItemStatus(Long itemId, ItemStatus newStatus) {
        Item item = itemRepository.findByIdAndStatusNot(itemId, ItemStatus.DISCONTINUED)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않거나 상태를 변경할 수 없는 상품입니다. 상품 ID: " + itemId));

        item.updateStatus(newStatus);
        return new ItemResponseDto(item);
    }

    // 상품 삭제 (소프트 딜리트)
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        // 단종 로직 실행 (상태 변경 및 삭제 시간 기록)
        item.discontinue();
    }
}