package com.credit.application.model.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Standard error response")
public record ErrorResponse(
        @Schema(description = "Timestamp of the error")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error summary", example = "Bad Request")
        String error,

        @Schema(description = "Detailed error message", example = "Loan amount must be greater than 0")
        String message
) {}
