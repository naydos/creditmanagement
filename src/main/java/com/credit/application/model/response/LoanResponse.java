package com.credit.application.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Response object containing loan details")
public record LoanResponse(

        @Schema(description = "Unique identifier for the loan", example = "12345")
        Long loanId,

        @Schema(description = "Total amount of the loan", example = "5000.00")
        BigDecimal totalLoanAmount,

        @Schema(description = "Date when the loan was created", example = "2023-01-01")
        LocalDate createDate,

        @Schema(description = "Number of installments for the loan", example = "12")
        Integer numberOfInstallment,

        @Schema(description = "Loan payment status", example = "false")
        boolean isPaid
) {}
