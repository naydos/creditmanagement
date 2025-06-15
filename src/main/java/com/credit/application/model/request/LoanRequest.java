package com.credit.application.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequest {
    @NotNull(message = "Customer id cannot be null,it is required")
    private Long customerId;

    @NotNull(message = "Loan amount is required")
    @DecimalMin("0.01")
    private BigDecimal loanAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must not exceed 0.5")
    private BigDecimal interestRate;

    @NotNull(message = "Installment number is required. It must be between 6 and 24")
    @Min(value = 6)
    @Max(value = 24)
    private Integer numberOfInstallment;
}
