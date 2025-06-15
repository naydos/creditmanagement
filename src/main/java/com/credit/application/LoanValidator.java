package com.credit.application;

import com.credit.domain.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanValidator {

    public void validateLoan(int installmentNumber) {
        List<Integer> validInstallments = List.of(6, 9, 12, 24);
        if (!validInstallments.contains(installmentNumber)) {
            throw new BusinessException("Installment count must be 6, 9, 12 or 24");
        }
    }
}
