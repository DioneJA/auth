package com.involvest.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Goal {
  private final long id;
  private final long familyId;
  private final String name;
  private final BigDecimal targetAmount;
  private final LocalDate targetDate;
  private final OffsetDateTime createdAt;

  public Goal(long id, long familyId, String name, BigDecimal targetAmount, LocalDate targetDate, OffsetDateTime createdAt) {
    this.id = id;
    this.familyId = familyId;
    this.name = name;
    this.targetAmount = targetAmount;
    this.targetDate = targetDate;
    this.createdAt = createdAt;
  }

  public long getId() {
    return id;
  }

  public long getFamilyId() {
    return familyId;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getTargetAmount() {
    return targetAmount;
  }

  public LocalDate getTargetDate() {
    return targetDate;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
