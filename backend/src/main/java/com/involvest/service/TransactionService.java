package com.involvest.service;

import com.involvest.dto.TransactionResponse;
import com.involvest.exception.BadRequestException;
import com.involvest.model.MovementType;
import com.involvest.repository.MovementTypeRepository;
import com.involvest.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
  private final TransactionRepository transactionRepository;
  private final MovementTypeRepository movementTypeRepository;

  public TransactionService(TransactionRepository transactionRepository, MovementTypeRepository movementTypeRepository) {
    this.transactionRepository = transactionRepository;
    this.movementTypeRepository = movementTypeRepository;
  }

  public long create(long familyId, long typeId, BigDecimal amount, String description, LocalDate occurredAt) {
    MovementType type = movementTypeRepository.findByIdAndFamily(typeId, familyId)
        .orElseThrow(() -> new BadRequestException("Movement type not found"));

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new BadRequestException("Amount must be greater than zero");
    }

    return transactionRepository.save(familyId, type.getId(), amount, description, occurredAt);
  }

  public List<TransactionResponse> listByMonth(long familyId, YearMonth month) {
    LocalDate start = month.atDay(1);
    LocalDate end = month.atEndOfMonth();
    return transactionRepository.findByFamilyAndMonth(familyId, start, end);
  }
}
