package com.credit.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanInstallmentDto(BigDecimal amount,
                                 BigDecimal paidAmount,
                                 LocalDate dueDate,
                                 LocalDate paymentDate,
                                 boolean isPaid,
                                 Long loanId) {
}
