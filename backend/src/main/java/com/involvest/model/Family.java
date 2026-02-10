package com.involvest.model;

import java.time.OffsetDateTime;

public class Family {
  private final long id;
  private final String name;
  private final OffsetDateTime createdAt;

  public Family(long id, String name, OffsetDateTime createdAt) {
    this.id = id;
    this.name = name;
    this.createdAt = createdAt;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
