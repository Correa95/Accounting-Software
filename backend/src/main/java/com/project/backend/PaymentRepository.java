package com.project.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCompanyIdAndActiveTrue(Long companyId);
    Optional<Payment> findByIdAndCompanyIdAndActiveTrue(Long paymentId, Long companyId);
}
