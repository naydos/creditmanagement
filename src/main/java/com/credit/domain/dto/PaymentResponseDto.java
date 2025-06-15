package com.credit.domain.dto;

import java.math.BigDecimal;

public record PaymentResponseDto(int paidInstallmentCount,
                                 boolean isLoanPaid,
                                 BigDecimal totalAmount) {
}
