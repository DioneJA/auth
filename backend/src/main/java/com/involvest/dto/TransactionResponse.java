package com.involvest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
    long id,
    long typeId,
    String typeName,
    String kind,
    BigDecimal amount,
    String description,
    LocalDate occurredAt
) {}
