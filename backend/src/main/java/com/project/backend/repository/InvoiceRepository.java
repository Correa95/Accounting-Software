package com.project.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.backend.entity.Invoice;
import com.project.backend.entity.Payment;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByCompanyIdAndActiveTrue(Long companyId);

    Optional<Invoice> findByIdAndCompanyIdAndActiveTrue(Long invoiceId, Long companyId);

    Optional<Invoice> findByPaymentOrder(Payment paymentOrder);
}
