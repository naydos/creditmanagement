package com.credit.application.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentResponse {

    private int paidInstallmentCount;
    private boolean isLoanPaid;
    private BigDecimal totalAmount;
}
