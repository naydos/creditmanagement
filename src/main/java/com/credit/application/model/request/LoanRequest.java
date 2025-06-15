package com.credit.application.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Request object for creating a new loan")
@Getter
@Setter
public class LoanRequest {

    @Schema(description = "Customer id who takes the loan", example = "1")
    @NotNull(message = "Customer id cannot be null,it is required")
    private Long customerId;

    @Schema(description = "Loan amount", example = "4500.00")
    @NotNull(message = "Loan amount is required")
    @DecimalMin("0.01")
    private BigDecimal loanAmount;

    @Schema(description = "Interest rate", example = "0.2")
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.1", message = "Interest rate must be at least 0.1")
    @DecimalMax(value = "0.5", message = "Interest rate must not exceed 0.5")
    private BigDecimal interestRate;

    @Schema(description = "Number of installments", example = "6")
    @NotNull(message = "Installment number is required. It must be between 6 and 24")
    @Min(value = 6)
    @Max(value = 24)
    private Integer numberOfInstallment;
}
