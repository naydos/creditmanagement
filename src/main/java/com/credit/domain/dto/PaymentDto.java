package com.credit.domain.dto;

import java.math.BigDecimal;

public record PaymentDto(Long loanId,
                         BigDecimal amount) {
}
