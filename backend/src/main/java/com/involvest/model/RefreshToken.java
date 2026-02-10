package com.involvest.model;

import java.time.OffsetDateTime;

public class RefreshToken {
  private final long id;
  private final long userId;
  private final String tokenHash;
  private final OffsetDateTime expiresAt;
  private final OffsetDateTime revokedAt;
  private final OffsetDateTime createdAt;

  public RefreshToken(long id, long userId, String tokenHash, OffsetDateTime expiresAt,
                      OffsetDateTime revokedAt, OffsetDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
    this.revokedAt = revokedAt;
    this.createdAt = createdAt;
  }

  public long getId() {
    return id;
  }

  public long getUserId() {
    return userId;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public OffsetDateTime getRevokedAt() {
    return revokedAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}
