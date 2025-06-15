package com.credit.application.manager;

import com.credit.application.LoanValidator;
import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.request.PaymentRequest;
import com.credit.application.model.response.InstallmentResponse;
import com.credit.application.model.response.LoanResponse;
import com.credit.application.model.response.PaymentResponse;
import com.credit.domain.dto.LoanDto;
import com.credit.domain.dto.LoanInstallmentDto;
import com.credit.domain.dto.PaymentDto;
import com.credit.domain.mapper.LoanInstallmentMapper;
import com.credit.domain.mapper.LoanMapper;
import com.credit.domain.mapper.LoanPaymentMapper;
import com.credit.domain.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoanManager {

    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final LoanInstallmentMapper loanInstallmentMapper;
    private final LoanPaymentMapper loanPaymentMapper;
    private final LoanValidator loanValidator;

    public List<LoanResponse> retrieveLoans(Long customerId) {
        log.info("Retrieving loans for customer: {}", customerId);
        List<LoanDto> loans = loanService.retrieveLoansByCustomer(customerId);
        return loans.stream()
                .map(loanMapper::toLoanResponse)
                .toList();
    }

    public LoanResponse createLoan(LoanRequest loanRequest) {
        log.info("Creating loan for customer: {}", loanRequest.getCustomerId());
        loanValidator.validateLoan(loanRequest.getNumberOfInstallment());
        LoanDto loanDto = loanMapper.toLoanDto(loanRequest);
        return loanMapper.toLoanResponse(loanService.createLoan(loanDto));
    }


    public List<InstallmentResponse> retrieveInstallments(Long loanId) {
        log.info("Retrieving installments for loan: {}", loanId);
        List<LoanInstallmentDto> loanInstallmentDtoList = loanService.retrieveInstallments(loanId);
        return loanInstallmentDtoList.stream().map(loanInstallmentMapper::toInstallmentResponse).toList();
    }


    public PaymentResponse payLoan(Long loanId, PaymentRequest request) {
        log.info("Processing payment for loan: {}", loanId);
        PaymentDto dto = loanPaymentMapper.toPaymentDto(loanId, request);
        return loanPaymentMapper.toPaymentResponse(loanService.payLoan(dto));
    }
}
