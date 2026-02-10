package com.involvest.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal balance,
    List<GoalResponse> goals
) {}
