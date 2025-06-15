package com.credit.application.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
        BigDecimal amount,
        BigDecimal paidAmount,
        LocalDate dueDate,
        LocalDate paymentDate,
        boolean isPaid
) {}