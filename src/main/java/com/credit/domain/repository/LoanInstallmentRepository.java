package com.credit.domain.repository;

import com.credit.domain.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {

    List<LoanInstallment> findByLoanIdOrderByDueDateAsc(Long loanId);
}
