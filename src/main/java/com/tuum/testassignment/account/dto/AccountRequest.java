package com.tuum.testassignment.account.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.tuum.testassignment.common.Country;
import com.tuum.testassignment.common.CustomCurrency;

import java.util.Set;

public record AccountRequest(@NotNull @Positive Long customerId,
                             @NotNull Country country,
                             @NotNull Set<CustomCurrency> currencies) {
}
