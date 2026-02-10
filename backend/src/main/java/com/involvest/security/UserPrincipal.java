package com.involvest.security;

public class UserPrincipal {
  private final long userId;
  private final long familyId;
  private final String email;

  public UserPrincipal(long userId, long familyId, String email) {
    this.userId = userId;
    this.familyId = familyId;
    this.email = email;
  }

  public long getUserId() {
    return userId;
  }

  public long getFamilyId() {
    return familyId;
  }

  public String getEmail() {
    return email;
  }
}
