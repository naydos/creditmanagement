package com.credit.domain.mapper;

import com.credit.application.model.request.LoanRequest;
import com.credit.application.model.response.LoanResponse;
import com.credit.domain.dto.LoanDto;
import com.credit.domain.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {

    LoanDto toLoanDto(LoanRequest request);

    @Mapping(source = "id", target = "loanId")
    @Mapping(source = "loanAmount", target = "totalLoanAmount")
    LoanResponse toLoanResponse(LoanDto dto);

    LoanDto toLoanDto(Loan loan);
}
