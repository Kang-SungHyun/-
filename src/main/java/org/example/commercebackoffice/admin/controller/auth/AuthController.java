package org.example.commercebackoffice.admin.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.commercebackoffice.admin.controller.auth.dto.request.SignupRequest;
import org.example.commercebackoffice.admin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth") //이 클래스의 모든 메서드는 이걸로 시작
@RequiredArgsConstructor //final이 붙은 서비스 객체를 생성자로 자동으로 주입
public class AuthController {
    private final AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        adminService.signup(request);

        return  ResponseEntity.ok("회원가입 신청이 완료되었습니다. 승인을 기다려주세요");
    }

}
