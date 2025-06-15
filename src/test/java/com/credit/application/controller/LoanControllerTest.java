package com.credit.application.controller;

import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.request.PaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testuser")
    void should_retrieve_loans() throws Exception {
        mockMvc.perform(get("/loans")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanId").exists());
    }

    @Test
    @WithMockUser(username = "testuser")
    void should_create_loan() throws Exception {

        LoanRequest request = new LoanRequest();
        request.setCustomerId(1L);
        request.setLoanAmount(BigDecimal.valueOf(1000));
        request.setInterestRate(BigDecimal.valueOf(0.2));
        request.setNumberOfInstallment(12);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalLoanAmount").value(BigDecimal.valueOf(1200.0)))
                .andExpect(jsonPath("$.loanId").isNumber())
                .andExpect(jsonPath("$.numberOfInstallment").value(12L))
                .andExpect(jsonPath("$.isPaid").value(false))
                .andExpect(jsonPath("$.createDate").value(LocalDate.now().toString()));
    }

    @Test
    @WithMockUser(username = "testuser")
    void should_pay_loan() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setAmount(BigDecimal.valueOf(100));


        mockMvc.perform(post("/loans/{loanId}/pay", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paidInstallmentCount").isNumber());
    }

    @Test
    @WithMockUser(username = "testuser")
    void should_retrieve_installments() throws Exception {
        mockMvc.perform(get("/loans/1/installments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].amount").value(BigDecimal.valueOf(2000.0)))
                .andExpect(jsonPath("$[0].isPaid").value(false));
    }
}