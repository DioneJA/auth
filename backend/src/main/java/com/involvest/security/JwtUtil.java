package com.involvest.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
  private final JwtProperties properties;
  private final ObjectMapper objectMapper;

  public JwtUtil(JwtProperties properties, ObjectMapper objectMapper) {
    this.properties = properties;
    this.objectMapper = objectMapper;
  }

  public String generateAccessToken(long userId, long familyId, String email) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("uid", userId);
    claims.put("fid", familyId);
    claims.put("email", email);
    return createToken(claims, properties.getAccessTokenMinutes() * 60L);
  }

  public String generateRefreshToken(long userId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("uid", userId);
    claims.put("type", "refresh");
    return createToken(claims, properties.getRefreshTokenDays() * 24L * 60L * 60L);
  }

  public Map<String, Object> validateAndGetClaims(String token) {
    String[] parts = token.split("\\.");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid token format");
    }

    String header = parts[0];
    String payload = parts[1];
    String signature = parts[2];

    String expected = sign(header + "." + payload);
    if (!MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8))) {
      throw new IllegalArgumentException("Invalid token signature");
    }

    Map<String, Object> claims = readJson(base64UrlDecode(payload));
    String issuer = (String) claims.get("iss");
    if (!properties.getIssuer().equals(issuer)) {
      throw new IllegalArgumentException("Invalid token issuer");
    }

    long now = Instant.now().getEpochSecond();
    Number exp = (Number) claims.get("exp");
    if (exp == null || exp.longValue() <= now) {
      throw new IllegalArgumentException("Token expired");
    }

    return claims;
  }

  private String createToken(Map<String, Object> claims, long ttlSeconds) {
    Map<String, Object> header = new HashMap<>();
    header.put("alg", "HS256");
    header.put("typ", "JWT");

    long now = Instant.now().getEpochSecond();
    Map<String, Object> payload = new HashMap<>(claims);
    payload.put("iss", properties.getIssuer());
    payload.put("iat", now);
    payload.put("exp", now + ttlSeconds);

    String headerJson = writeJson(header);
    String payloadJson = writeJson(payload);

    String encodedHeader = base64UrlEncode(headerJson);
    String encodedPayload = base64UrlEncode(payloadJson);

    String toSign = encodedHeader + "." + encodedPayload;
    String signature = sign(toSign);

    return toSign + "." + signature;
  }

  private String sign(String content) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(properties.getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
      byte[] raw = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to sign token", ex);
    }
  }

  private String base64UrlEncode(String value) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
  }

  private String base64UrlDecode(String value) {
    return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
  }

  private String writeJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to serialize JSON", ex);
    }
  }

  private Map<String, Object> readJson(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to parse JSON", ex);
    }
  }
}
