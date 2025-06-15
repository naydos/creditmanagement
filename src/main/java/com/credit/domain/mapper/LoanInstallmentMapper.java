package com.credit.domain.mapper;

import com.credit.application.model.response.InstallmentResponse;
import com.credit.domain.dto.LoanInstallmentDto;
import com.credit.domain.entity.LoanInstallment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoanInstallmentMapper {

    LoanInstallmentDto toInstallmentDto(LoanInstallment loanInstallment);

    List<LoanInstallmentDto> toInstallmentDtoList(List<LoanInstallment> loanInstallmentList);

    InstallmentResponse toInstallmentResponse(LoanInstallmentDto loanInstallmentDto);

    List<InstallmentResponse> toInstallmentResponseList(List<LoanInstallmentDto> loanInstallmentDtoList);
}
