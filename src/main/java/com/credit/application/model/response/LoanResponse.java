package com.credit.application.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponse(
        Long loanId,
        BigDecimal totalLoanAmount,
        LocalDate createDate,
        Integer numberOfInstallment,
        boolean isPaid
) {
}
