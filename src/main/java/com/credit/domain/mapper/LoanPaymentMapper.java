package com.credit.domain.mapper;

import com.credit.application.model.request.PaymentRequest;
import com.credit.application.model.response.PaymentResponse;
import com.credit.domain.dto.PaymentDto;
import com.credit.domain.dto.PaymentResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanPaymentMapper {

    PaymentDto toPaymentDto(Long loanId, PaymentRequest request);

    PaymentResponse toPaymentResponse(PaymentResponseDto paymentResponseDto);

}
