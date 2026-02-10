package com.involvest.service;

import com.involvest.dto.DashboardResponse;
import com.involvest.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
  private final TransactionRepository transactionRepository;
  private final GoalService goalService;

  public DashboardService(TransactionRepository transactionRepository, GoalService goalService) {
    this.transactionRepository = transactionRepository;
    this.goalService = goalService;
  }

  public DashboardResponse getMonthlySummary(long familyId, YearMonth month) {
    LocalDate start = month.atDay(1);
    LocalDate end = month.atEndOfMonth();

    BigDecimal income = transactionRepository.sumByKind(familyId, "INCOME", start, end);
    BigDecimal expense = transactionRepository.sumByKind(familyId, "EXPENSE", start, end);
    BigDecimal balance = income.subtract(expense);

    return new DashboardResponse(income, expense, balance, goalService.listWithProgress(familyId));
  }
}
