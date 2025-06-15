package com.credit.application.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Request object for making a payment")
@Setter
@Getter
public class PaymentRequest {

    @Schema(description = "The amount of the loan payment being made", example = "200.00")
    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than 0")
    private BigDecimal amount;
}
