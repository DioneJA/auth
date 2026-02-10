package com.involvest.service;

import com.involvest.dto.GoalResponse;
import com.involvest.model.Goal;
import com.involvest.repository.GoalRepository;
import com.involvest.repository.TransactionRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GoalService {
  private final GoalRepository goalRepository;
  private final TransactionRepository transactionRepository;

  public GoalService(GoalRepository goalRepository, TransactionRepository transactionRepository) {
    this.goalRepository = goalRepository;
    this.transactionRepository = transactionRepository;
  }

  public Goal create(long familyId, String name, BigDecimal targetAmount, java.time.LocalDate targetDate) {
    return goalRepository.save(familyId, name, targetAmount, targetDate);
  }

  public List<GoalResponse> listWithProgress(long familyId) {
    BigDecimal balance = transactionRepository.balanceAllTime(familyId);
    return goalRepository.findByFamily(familyId).stream()
        .map(goal -> toResponse(goal, balance))
        .toList();
  }

  private GoalResponse toResponse(Goal goal, BigDecimal balance) {
    BigDecimal progress = balance.max(BigDecimal.ZERO);
    BigDecimal target = goal.getTargetAmount();
    double percent = target.compareTo(BigDecimal.ZERO) == 0 ? 0.0 :
        progress.divide(target, 4, java.math.RoundingMode.HALF_UP).min(BigDecimal.ONE).doubleValue() * 100.0;
    return new GoalResponse(
        goal.getId(),
        goal.getName(),
        goal.getTargetAmount(),
        goal.getTargetDate(),
        progress,
        percent
    );
  }
}
