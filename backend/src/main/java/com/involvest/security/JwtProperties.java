package com.involvest.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
  private String issuer;
  private String secret;
  private int accessTokenMinutes;
  private int refreshTokenDays;

  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public int getAccessTokenMinutes() {
    return accessTokenMinutes;
  }

  public void setAccessTokenMinutes(int accessTokenMinutes) {
    this.accessTokenMinutes = accessTokenMinutes;
  }

  public int getRefreshTokenDays() {
    return refreshTokenDays;
  }

  public void setRefreshTokenDays(int refreshTokenDays) {
    this.refreshTokenDays = refreshTokenDays;
  }
}
