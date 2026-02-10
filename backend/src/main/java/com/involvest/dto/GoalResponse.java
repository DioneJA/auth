package com.involvest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GoalResponse(
    long id,
    String name,
    BigDecimal targetAmount,
    LocalDate targetDate,
    BigDecimal progressAmount,
    double progressPercent
) {}
