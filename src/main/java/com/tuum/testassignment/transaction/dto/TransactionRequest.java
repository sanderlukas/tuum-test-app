package com.tuum.testassignment.transaction.dto;

import jakarta.validation.constraints.*;

import com.tuum.testassignment.common.CustomCurrency;
import com.tuum.testassignment.common.TransactionDirection;

public record TransactionRequest(@NotNull Long accountId,
                                 @NotNull @Pattern(regexp="^[1-9][0-9]*$", message = "Requested amount has to be provided in cents") String amount,
                                 @NotNull CustomCurrency currency,
                                 @NotNull TransactionDirection direction,
                                 @NotEmpty @NotBlank String description) {
}
