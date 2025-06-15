package com.credit.application.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PaymentRequest {

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than 0")
    private BigDecimal amount;
}
