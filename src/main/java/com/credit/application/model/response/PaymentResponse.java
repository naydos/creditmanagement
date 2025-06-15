package com.credit.application.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Response object for a payment transaction")
@Getter
@Setter
public class PaymentResponse {

    @Schema(description = "Count of paid installments", example = "2")
    private int paidInstallmentCount;

    @Schema(description = "Loan installment payment status", example = "true")
    private boolean isLoanPaid;

    @Schema(description = "Total amount paid in the transaction", example = "1500.00")
    private BigDecimal totalAmount;
}
