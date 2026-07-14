package org.example.commercebackoffice.admin.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.admin.domain.Admin;
import org.example.commercebackoffice.admin.domain.dto.AdminSignupRequest;
import org.example.commercebackoffice.admin.domain.enums.AdminStatus;
import org.example.commercebackoffice.admin.repository.AdminRepository;
import org.example.commercebackoffice.config.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
//중복체크 -> 암호화 ->저장
@Service
@RequiredArgsConstructor
public class AdminService {
    private  final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; //암호화 부품

    @Transactional
    public void signup(AdminSignupRequest request) {
        //이메일 중복 여부 확인
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }
        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        //엔티티 생성
        //admin.of -> 내부에서 status는 자동으로 PENDING, CREATEDAT은  JPA auditing(@CreatedDate)에 의해 저장 시점에 자동으로 채워진다.
        Admin admin = Admin.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .status(AdminStatus.PENDING)
                .build();


//                request.getName(),
//                request.getEmail(),
//                encodedPassword, //원래 비밀번호 대신 암호문 넣기
//                request.getPhoneNumber(),
//                request.getRole(),
//                "PENDING",
//                LocalDateTime.now() //요구사항: 자동으로 현재 시간 설정

        adminRepository.save(admin);
    }


}
