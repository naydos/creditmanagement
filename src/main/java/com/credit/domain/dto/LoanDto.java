package com.credit.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanDto( Long id,
                       Long customerId,
                       BigDecimal loanAmount,
                       BigDecimal interestRate,
                       Integer numberOfInstallment,
                       LocalDate createDate,
                       boolean isPaid) {
}
