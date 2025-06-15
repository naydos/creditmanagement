package com.credit.domain.service;

import com.credit.domain.dto.LoanDto;
import com.credit.domain.dto.LoanInstallmentDto;
import com.credit.domain.dto.PaymentDto;
import com.credit.domain.dto.PaymentResponseDto;
import com.credit.domain.entity.Customer;
import com.credit.domain.entity.Loan;
import com.credit.domain.entity.LoanInstallment;
import com.credit.domain.exception.BusinessException;
import com.credit.domain.mapper.LoanInstallmentMapper;
import com.credit.domain.mapper.LoanMapper;
import com.credit.domain.repository.LoanInstallmentRepository;
import com.credit.domain.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoanService {

    private final CustomerService customerService;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final LoanInstallmentRepository loanInstallmentRepository;
    private final LoanInstallmentMapper loanInstallmentMapper;

    @Transactional
    public LoanDto createLoan(LoanDto loanDto) {
        Customer customer = customerService.retrieveCustomerById(loanDto.customerId());
        BigDecimal totalAmount = loanDto.loanAmount().multiply(BigDecimal.ONE.add(loanDto.interestRate()));

        validateCustomerCredit(customer, totalAmount);

        Loan loan = new Loan();
        loan.setLoanAmount(totalAmount);
        loan.setCreateDate(LocalDate.now());
        loan.setPaid(false);
        loan.setCustomer(customer);
        loan.setNumberOfInstallment(loanDto.numberOfInstallment());

        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(loanDto.numberOfInstallment()), 2, BigDecimal.ROUND_HALF_UP);
        LocalDate dueDate = LocalDate.now().plusMonths(1).with(firstDayOfMonth());

        List<LoanInstallment> installments = createInstallments(loan, installmentAmount, dueDate, loanDto.numberOfInstallment());
        loan.setInstallments(installments);

        loan = loanRepository.save(loan);
        customerService.updateCustomerUsedLimit(customer, totalAmount);

        return loanMapper.toLoanDto(loan);
    }

    public List<LoanDto> retrieveLoansByCustomer(Long customerId) {
        List<Loan> loanList = loanRepository.findByCustomerId(customerId);
        return loanList.stream().map(loanMapper::toLoanDto).toList();
    }

    public List<LoanInstallmentDto> retrieveInstallments(Long loanId) {
        List<LoanInstallment> installments = loanInstallmentRepository.findByLoanIdOrderByDueDateAsc(loanId);
        return installments.stream().map(loanInstallmentMapper::toInstallmentDto).toList();
    }

    public PaymentResponseDto payLoan(PaymentDto dto) {
        return payLoan(dto.loanId(), dto.amount());
    }

    @Transactional
    public PaymentResponseDto payLoan(Long loanId, BigDecimal paymentAmount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new BusinessException("Loan not found"));

        List<LoanInstallment> unpaidInstallments = loanInstallmentRepository
                .findByLoanIdOrderByDueDateAsc(loanId)
                .stream()
                .filter(i -> !i.isPaid())
                .filter(i -> i.getDueDate().isBefore(LocalDate.now().plusMonths(4))) //TODO move constant
                .toList();

        int paidCount = 0;
        BigDecimal totalSpent = BigDecimal.ZERO;

        for (LoanInstallment installment : unpaidInstallments) {
            BigDecimal installmentAmount = installment.getAmount();

            if (paymentAmount.compareTo(installmentAmount) >= 0) {
                installment.setPaid(true);
                installment.setPaidAmount(installmentAmount); // bonus kısmında güncellenecek
                installment.setPaymentDate(LocalDate.now());

                paymentAmount = paymentAmount.subtract(installmentAmount);
                totalSpent = totalSpent.add(installmentAmount);
                paidCount++;
            } else {
                break;
            }
        }

        loanInstallmentRepository.saveAll(unpaidInstallments);

        boolean allPaid = unpaidInstallments.stream().allMatch(LoanInstallment::isPaid);
        if (allPaid) {
            loan.setPaid(true);
            loanRepository.save(loan);
        }

        return new PaymentResponseDto(paidCount, loan.isPaid(), totalSpent);
    }

    private List<LoanInstallment> createInstallments(Loan loan, BigDecimal installmentAmount, LocalDate dueDate, int numberOfInstallments) {
        List<LoanInstallment> installments = new ArrayList<>();
        for (int i = 0; i < numberOfInstallments; i++) {
            LoanInstallment loanInstallment = new LoanInstallment();
            loanInstallment.setLoan(loan);
            loanInstallment.setDueDate(dueDate.plusMonths(i));
            loanInstallment.setPaid(false);
            loanInstallment.setAmount(installmentAmount);
            loanInstallment.setPaidAmount(BigDecimal.ZERO);
            installments.add(loanInstallment);
        }
        return installments;
    }

    private void validateCustomerCredit(Customer customer, BigDecimal totalAmount) {
        if (customer.getCreditLimit().subtract(customer.getUsedCreditLimit()).compareTo(totalAmount) < 0) {
            throw new BusinessException("Not enough credit limit for customer: " + customer.getId());
        }
    }
}
