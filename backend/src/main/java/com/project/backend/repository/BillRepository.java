package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByCompanyIdAndActiveTrue(Long companyId);
    Optional<Bill> findByIdAndCompanyIdAndActiveTrue(Long billId, Long companyId);
}
