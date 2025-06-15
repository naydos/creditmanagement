package com.credit.domain.service;

import com.credit.domain.dto.LoanDto;
import com.credit.domain.dto.LoanInstallmentDto;
import com.credit.domain.dto.PaymentResponseDto;
import com.credit.domain.entity.Customer;
import com.credit.domain.entity.Loan;
import com.credit.domain.entity.LoanInstallment;
import com.credit.domain.exception.BusinessException;
import com.credit.domain.mapper.LoanInstallmentMapper;
import com.credit.domain.mapper.LoanMapper;
import com.credit.domain.repository.LoanInstallmentRepository;
import com.credit.domain.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LoanServiceTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository loanInstallmentRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private LoanInstallmentMapper loanInstallmentMapper;

    @InjectMocks
    private LoanService loanService;

    @Test
    void should_create_Loan() {
        // Arrange
        LoanDto loanDto = new LoanDto(null, 2L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.2), 6, null, false);

        Customer customer = new Customer();
        customer.setCreditLimit(BigDecimal.valueOf(3456));
        customer.setUsedCreditLimit(BigDecimal.ZERO);
        customer.setId(2L);

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(1000));
        loan.setNumberOfInstallment(12);
        loan.setCustomer(customer);

        when(customerService.retrieveCustomerById(2L)).thenReturn(customer);
        when(loanRepository.save(ArgumentMatchers.<Loan>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(loanMapper.toLoanDto(ArgumentMatchers.<Loan>any())).thenReturn(loanDto);

        // Act
        LoanDto result = loanService.createLoan(loanDto);

        // Assert
        assertNotNull(result);
        assertEquals(loanDto.customerId(), result.customerId());
        verify(customerService).updateCustomerUsedLimit(customer, BigDecimal.valueOf(1200.0));
    }

    @Test
    void should_retrieve_loans() {
        // Arrange
        Customer customer = new Customer();
        customer.setCreditLimit(BigDecimal.valueOf(3456));
        customer.setUsedCreditLimit(BigDecimal.ZERO);
        customer.setId(1L);

        LoanDto loanDto = new LoanDto(null, 1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(0.2), 6, null, false);

        Loan loan = new Loan();
        loan.setLoanAmount(BigDecimal.valueOf(1000));
        loan.setNumberOfInstallment(6);
        loan.setCustomer(customer);

        when(loanRepository.findByCustomerId(1L)).thenReturn(List.of(loan));
        when(loanMapper.toLoanDto(loan)).thenReturn(loanDto);

        // Act
        List<LoanDto> loans = loanService.retrieveLoansByCustomer(1L);

        // Assert
        assertNotNull(loans);
        assertEquals(1, loans.size());
        assertEquals(1L, loans.get(0).customerId());
    }

    @Test
    void should_throw_exception_when_credit_limit_insufficient() {
        // arrange
        LoanDto loanDto = new LoanDto(null, 1L, BigDecimal.valueOf(5000), BigDecimal.valueOf(0.2), 6, null, false);
        Customer customer = new Customer();
        customer.setCreditLimit(BigDecimal.valueOf(1000));
        customer.setUsedCreditLimit(BigDecimal.ZERO);

        when(customerService.retrieveCustomerById(1L)).thenReturn(customer);

        assertThrows(BusinessException.class, () -> loanService.createLoan(loanDto));
    }

    @Test
    void should_retrieve_installments() {
        // Arrange
        Long loanId = 1L;
        LoanInstallment installment = new LoanInstallment();
        installment.setAmount(BigDecimal.valueOf(100));

        LoanInstallmentDto dto = new LoanInstallmentDto(BigDecimal.valueOf(100), BigDecimal.valueOf(100), LocalDate.now(), LocalDate.now(), true, 1L);

        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(List.of(installment));
        when(loanInstallmentMapper.toInstallmentDto(installment)).thenReturn(dto);

        // Act
        List<LoanInstallmentDto> installments = loanService.retrieveInstallments(loanId);

        // Assert
        assertNotNull(installments);
        assertEquals(1, installments.size());
        assertEquals(BigDecimal.valueOf(100), installments.get(0).amount());
    }

    @Test
    void should_pay_loan() {
        // Arrange
        Long loanId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(100);
        Loan loan = new Loan();
        loan.setPaid(false);

        LoanInstallment installment = new LoanInstallment();
        installment.setAmount(paymentAmount);
        installment.setPaid(false);
        installment.setDueDate(LocalDate.now());
        installment.setLoan(loan);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(List.of(installment));

        // Act
        PaymentResponseDto response = loanService.payLoan(loanId, paymentAmount);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.paidInstallmentCount());
        assertTrue(response.isLoanPaid());
        assertEquals(paymentAmount, response.totalAmount());
    }

    @Test
    void should_pay_multiple_installments() {
        // Arrange
        Long loanId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(290); // two installments can be paid

        Loan loan = new Loan();
        loan.setPaid(false);

        LoanInstallment inst1 = new LoanInstallment();
        inst1.setAmount(BigDecimal.valueOf(100));
        inst1.setPaid(false);
        inst1.setDueDate(LocalDate.now());

        LoanInstallment inst2 = new LoanInstallment();
        inst2.setAmount(BigDecimal.valueOf(100));
        inst2.setPaid(false);
        inst2.setDueDate(LocalDate.now().plusMonths(1));

        LoanInstallment inst3 = new LoanInstallment();
        inst3.setAmount(BigDecimal.valueOf(100));
        inst3.setPaid(false);
        inst3.setDueDate(LocalDate.now().plusMonths(2));

        List<LoanInstallment> installments = List.of(inst1, inst2, inst3);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId)).thenReturn(installments);

        // Act
        PaymentResponseDto response = loanService.payLoan(loanId, paymentAmount);

        // Assert
        assertNotNull(response);
        assertEquals(3, installments.size());
        assertTrue(inst1.isPaid());
        assertTrue(inst2.isPaid());
        assertFalse(inst3.isPaid());
        assertEquals(2, response.paidInstallmentCount());
        assertEquals(BigDecimal.valueOf(200), response.totalAmount());
        assertFalse(response.isLoanPaid());
    }

    @Test
    void pay_loan_when_all_installments_already_paid_should_return_zero() {
        // Arrange
        Long loanId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(500);

        Loan loan = new Loan();
        loan.setPaid(true);

        LoanInstallment installment1 = new LoanInstallment();
        installment1.setPaid(true);
        installment1.setAmount(BigDecimal.valueOf(250));

        LoanInstallment installment2 = new LoanInstallment();
        installment2.setPaid(true);
        installment2.setAmount(BigDecimal.valueOf(250));

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId))
                .thenReturn(List.of(installment1, installment2));

        // Act
        PaymentResponseDto response = loanService.payLoan(loanId, paymentAmount);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.paidInstallmentCount());
        assertEquals(BigDecimal.ZERO, response.totalAmount());
        assertTrue(response.isLoanPaid());
    }

    @Test
    void pay_loan_when_loan_not_found_throw_exception() {
        // Arrange
        Long loanId = 99L;
        BigDecimal paymentAmount = BigDecimal.valueOf(1000);

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(BusinessException.class, () -> {
            loanService.payLoan(loanId, paymentAmount);
        });

        assertEquals("Loan not found", exception.getMessage());
    }

    @Test
    void pay_loan_when_payment_more_than_total_pay_all() {
        // Arrange
        Long loanId = 1L;
        BigDecimal installmentAmount = BigDecimal.valueOf(100);
        BigDecimal overPayment = BigDecimal.valueOf(500);

        Loan loan = new Loan();
        loan.setPaid(false);

        LoanInstallment inst1 = new LoanInstallment();
        inst1.setAmount(installmentAmount);
        inst1.setPaid(false);
        inst1.setDueDate(LocalDate.now().plusMonths(1));

        LoanInstallment inst2 = new LoanInstallment();
        inst2.setAmount(installmentAmount);
        inst2.setPaid(false);
        inst2.setDueDate(LocalDate.now().plusMonths(2));

        LoanInstallment inst3 = new LoanInstallment();
        inst3.setAmount(installmentAmount);
        inst3.setPaid(false);
        inst3.setDueDate(LocalDate.now().plusMonths(3));

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId))
                .thenReturn(List.of(inst1, inst2, inst3));

        // Act
        PaymentResponseDto response = loanService.payLoan(loanId, overPayment);

        // Assert
        assertNotNull(response);
        assertEquals(3, response.paidInstallmentCount());
        assertTrue(response.isLoanPaid());
        assertEquals(BigDecimal.valueOf(300), response.totalAmount());

        assertTrue(inst1.isPaid());
        assertTrue(inst2.isPaid());
        assertTrue(inst3.isPaid());
    }
}