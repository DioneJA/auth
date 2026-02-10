package com.involvest.model;

import java.time.OffsetDateTime;

public class User {
  private final long id;
  private final long familyId;
  private final String name;
  private final String email;
  private final String passwordHash;
  private final OffsetDateTime createdAt;

  public User(long id, long familyId, String name, String email, String passwordHash, OffsetDateTime createdAt) {
    this.id = id;
    this.familyId = familyId;
    this.name = name;
    this.email = email;
    this.passwordHash = passwordHash;
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

  public String getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
