package com.credit.application.controller;

import com.credit.application.manager.LoanManager;
import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.request.PaymentRequest;
import com.credit.application.model.response.ErrorResponse;
import com.credit.application.model.response.InstallmentResponse;
import com.credit.application.model.response.LoanResponse;
import com.credit.application.model.response.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Loans", description = "Operations related to loans")
@RequiredArgsConstructor
@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanManager loanManager;

    @Operation(summary = "Create a new loan", description = "Creates a new loan for a given customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody @Valid LoanRequest loanRequest) {
        return ResponseEntity.ok(loanManager.createLoan(loanRequest));
    }

    @Operation(summary = "Retrieve loans", description = "Retrieves all loans for a given customer id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid customer id", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<LoanResponse>> retrieveLoans(@RequestParam Long customerId) {
        return ResponseEntity.ok(loanManager.retrieveLoans(customerId));
    }

    @Operation(summary = "Retrieve loan installments", description = "Retrieves all installments for a given loan id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Installments retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid loan id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<InstallmentResponse>> retrieveInstallments(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanManager.retrieveInstallments(loanId));
    }

    @Operation(summary = "Pay a loan", description = "Processes a payment for the given loan id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid loan id or payment request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{loanId}/pay")
    public ResponseEntity<PaymentResponse> payLoan(@PathVariable Long loanId,
                                                   @RequestBody @Valid PaymentRequest paymentRequest) {
        return ResponseEntity.ok(loanManager.payLoan(loanId, paymentRequest));
    }

}
