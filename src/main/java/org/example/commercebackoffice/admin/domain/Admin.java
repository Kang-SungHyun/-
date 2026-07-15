package org.example.commercebackoffice.admin.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.commercebackoffice.admin.domain.enums.AdminRole;
import org.example.commercebackoffice.admin.domain.enums.AdminStatus;
import org.example.commercebackoffice.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false , unique = true ,length = 100)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;
    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private AdminRole role;

    @Enumerated(EnumType.STRING) //db에 저장 시 문자열 그대로 저장 -> 유지보수성 향상
    @Column(nullable = false)
    private AdminStatus status;
    @Column(nullable = false)
    private LocalDateTime approvedAt;

    private LocalDateTime rejectedAt;
    private String rejectionReason;


    @Builder
    public Admin (String name, String email, String password, String phoneNumber,AdminRole role, AdminStatus status){
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.status = status;

    }
}