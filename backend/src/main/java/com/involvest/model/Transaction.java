package com.involvest.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Transaction {
  private final long id;
  private final long familyId;
  private final long typeId;
  private final BigDecimal amount;
  private final String description;
  private final LocalDate occurredAt;
  private final OffsetDateTime createdAt;

  public Transaction(long id, long familyId, long typeId, BigDecimal amount, String description,
                     LocalDate occurredAt, OffsetDateTime createdAt) {
    this.id = id;
    this.familyId = familyId;
    this.typeId = typeId;
    this.amount = amount;
    this.description = description;
    this.occurredAt = occurredAt;
    this.createdAt = createdAt;
  }

  public long getId() {
    return id;
  }

  public long getFamilyId() {
    return familyId;
  }

  public long getTypeId() {
    return typeId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getOccurredAt() {
    return occurredAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
