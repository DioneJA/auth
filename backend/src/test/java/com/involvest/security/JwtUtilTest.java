package com.involvest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JwtUtilTest {
  @Test
  void generateAndValidateToken() {
    JwtProperties properties = new JwtProperties();
    properties.setIssuer("involvest-test");
    properties.setSecret("TEST_SECRET_ABCDEFGHIJKLMNOPQRSTUVWXYZ_1234567890_ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    properties.setAccessTokenMinutes(5);
    properties.setRefreshTokenDays(1);

    JwtUtil jwtUtil = new JwtUtil(properties, new ObjectMapper());
    String token = jwtUtil.generateAccessToken(10L, 22L, "user@test.com");

    Map<String, Object> claims = jwtUtil.validateAndGetClaims(token);
    assertNotNull(claims);
    assertEquals(10L, ((Number) claims.get("uid")).longValue());
    assertEquals(22L, ((Number) claims.get("fid")).longValue());
    assertEquals("user@test.com", claims.get("email"));
  }
}
