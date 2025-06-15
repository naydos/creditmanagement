package com.credit.application.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Response object for a loan installment")
public record InstallmentResponse(
        @Schema(description = "The amount of the installment")
        BigDecimal amount,

        @Schema(description = "The amount that has been paid")
        BigDecimal paidAmount,

        @Schema(description = "The due date of the installment")
        LocalDate dueDate,

        @Schema(description = "The payment date of the installment")
        LocalDate paymentDate,

        @Schema(description = "Indicates whether the installment is paid")
        boolean isPaid
) {
}