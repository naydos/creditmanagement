package com.credit.domain.service;

import com.credit.domain.entity.Customer;
import com.credit.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;


    public Customer retrieveCustomerById(Long customerId) {
        log.info("Retrieving customer with id: {}", customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));
    }

    public void updateCustomerUsedLimit(Customer customer, BigDecimal amount) {
       customer.setUsedCreditLimit(customer.getUsedCreditLimit().add(amount));
       customerRepository.save(customer);
    }
}
