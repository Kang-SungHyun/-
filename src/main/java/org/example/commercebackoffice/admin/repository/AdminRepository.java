package org.example.commercebackoffice.admin.repository;

import jakarta.persistence.Id;
import org.example.commercebackoffice.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByEmail(String email); //가입할 때 이메일 중복 체크를 위해 사용
}
