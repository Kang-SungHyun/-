package org.example.commercebackoffice.admin.domain.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.admin.domain.dto.AdminSignupRequest;
import org.example.commercebackoffice.admin.domain.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins") //이 클래스의 모든 메서드는 이걸로 시작
@RequiredArgsConstructor //final이 붙은 서비스 객체를 생성자로 자동으로 주입
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody AdminSignupRequest request) {
        adminService.signup(request);

        return  ResponseEntity.ok("회원가입 신청이 완료되었습니다. 승인을 기다려주세요");
    }

}
