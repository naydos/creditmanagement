package com.credit.application.controller;

import com.credit.application.manager.LoanManager;
import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.request.PaymentRequest;
import com.credit.application.model.response.InstallmentResponse;
import com.credit.application.model.response.LoanResponse;
import com.credit.application.model.response.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanManager loanManager;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody @Valid LoanRequest loanRequest) {
        return ResponseEntity.ok(loanManager.createLoan(loanRequest));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponse>> retrieveLoans(@RequestParam Long customerId) {
        return ResponseEntity.ok(loanManager.retrieveLoans(customerId));
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<InstallmentResponse>> retrieveInstallments(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanManager.retrieveInstallments(loanId));
    }

    @PostMapping("/{loanId}/pay")
    public ResponseEntity<PaymentResponse> payLoan(@PathVariable Long loanId,
                                                   @RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.ok(loanManager.payLoan(loanId, paymentRequest));
    }
}
