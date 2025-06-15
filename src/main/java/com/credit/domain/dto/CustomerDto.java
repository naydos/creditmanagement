package com.credit.domain.dto;

import java.math.BigDecimal;

public record CustomerDto(String name,
                          String surname,
                          BigDecimal creditLimit,
                          BigDecimal usedCreditLimit) {
}
