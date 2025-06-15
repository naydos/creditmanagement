package com.credit.application.manager;

import com.credit.application.LoanValidator;
import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.response.LoanResponse;
import com.credit.domain.dto.LoanDto;
import com.credit.domain.mapper.LoanMapper;
import com.credit.domain.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class LoanManagerTest {
    @Mock
    private LoanService loanService;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanManager loanManager;

    @Mock
    private LoanValidator loanValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_create_loan() {
        // given
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanAmount(BigDecimal.valueOf(1000));
        loanRequest.setInterestRate(BigDecimal.valueOf(0.2));
        loanRequest.setNumberOfInstallment(12);
        loanRequest.setCustomerId(1L);

        LoanDto loanDto = new LoanDto(null, 1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.2), 12, null, false);
        LoanDto createdLoan = new LoanDto(1L, 1L, BigDecimal.valueOf(1200), BigDecimal.valueOf(0.2), 12, LocalDate.now(), false);
        LoanResponse response = new LoanResponse(1L, BigDecimal.valueOf(1200), LocalDate.now(), 12, false);

        doNothing().when(loanValidator).validateLoan(loanRequest.getNumberOfInstallment());
        when(loanMapper.toLoanDto(loanRequest)).thenReturn(loanDto);
        when(loanService.createLoan(loanDto)).thenReturn(createdLoan);
        when(loanMapper.toLoanResponse(createdLoan)).thenReturn(response);

        // when
        LoanResponse result = loanManager.createLoan(loanRequest);

        // then
        assertNotNull(result);
        assertEquals(response.loanId(), result.loanId());
        verify(loanService).createLoan(loanDto);
        verify(loanMapper).toLoanResponse(createdLoan);
    }

    @Test
    void should_retrieve_loans_for_customer() {
        // given
        Long customerId = 2L;
        LoanDto loanDto = new LoanDto(1L, customerId, BigDecimal.valueOf(1500), BigDecimal.valueOf(0.2), 6, LocalDate.now(), false);
        LoanResponse response = new LoanResponse(1L, BigDecimal.valueOf(1800), LocalDate.now(), 6, false);

        when(loanService.retrieveLoansByCustomer(customerId)).thenReturn(List.of(loanDto));
        when(loanMapper.toLoanResponse(loanDto)).thenReturn(response);

        // when
        List<LoanResponse> results = loanManager.retrieveLoans(customerId);

        // then
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).loanId());
        verify(loanService).retrieveLoansByCustomer(customerId);
        verify(loanMapper).toLoanResponse(loanDto);
    }

}