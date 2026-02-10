package com.involvest.model;

import java.time.OffsetDateTime;

public class MovementType {
  private final long id;
  private final long familyId;
  private final String name;
  private final MovementKind kind;
  private final OffsetDateTime createdAt;

  public MovementType(long id, long familyId, String name, MovementKind kind, OffsetDateTime createdAt) {
    this.id = id;
    this.familyId = familyId;
    this.name = name;
    this.kind = kind;
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

  public MovementKind getKind() {
    return kind;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
