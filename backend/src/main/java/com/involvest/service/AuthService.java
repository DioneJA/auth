package com.involvest.service;

import com.involvest.dto.AuthResponse;
import com.involvest.dto.RegisterRequest;
import com.involvest.exception.BadRequestException;
import com.involvest.exception.UnauthorizedException;
import com.involvest.model.User;
import com.involvest.repository.FamilyRepository;
import com.involvest.repository.RefreshTokenRepository;
import com.involvest.repository.UserRepository;
import com.involvest.security.JwtProperties;
import com.involvest.security.JwtUtil;
import com.involvest.util.HashUtil;
import java.time.OffsetDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final FamilyRepository familyRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final JwtProperties jwtProperties;

  public AuthService(UserRepository userRepository,
                     FamilyRepository familyRepository,
                     RefreshTokenRepository refreshTokenRepository,
                     PasswordEncoder passwordEncoder,
                     JwtUtil jwtUtil,
                     JwtProperties jwtProperties) {
    this.userRepository = userRepository;
    this.familyRepository = familyRepository;
    this.refreshTokenRepository = refreshTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.jwtProperties = jwtProperties;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.email())) {
      throw new BadRequestException("Email already registered");
    }

    var family = familyRepository.save(request.familyName());
    var user = userRepository.save(family.getId(), request.name(), request.email(),
        passwordEncoder.encode(request.password()));

    return issueTokens(user);
  }

  public AuthResponse login(String email, String password) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

    if (!passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid credentials");
    }

    return issueTokens(user);
  }

  public AuthResponse refresh(String refreshToken) {
    var claims = jwtUtil.validateAndGetClaims(refreshToken);
    String type = (String) claims.get("type");
    if (!"refresh".equals(type)) {
      throw new UnauthorizedException("Invalid refresh token");
    }
    long userId = ((Number) claims.get("uid")).longValue();

    String tokenHash = HashUtil.sha256(refreshToken);
    refreshTokenRepository.findValidByHash(tokenHash)
        .orElseThrow(() -> new UnauthorizedException("Refresh token revoked or expired"));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UnauthorizedException("User not found"));

    refreshTokenRepository.revokeByUser(userId);
    return issueTokens(user);
  }

  private AuthResponse issueTokens(User user) {
    String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getFamilyId(), user.getEmail());
    String refreshToken = jwtUtil.generateRefreshToken(user.getId());
    String tokenHash = HashUtil.sha256(refreshToken);
    OffsetDateTime expiresAt = OffsetDateTime.now().plusDays(jwtProperties.getRefreshTokenDays());
    refreshTokenRepository.save(user.getId(), tokenHash, expiresAt);
    return new AuthResponse(accessToken, refreshToken, user.getId(), user.getFamilyId(), user.getName());
  }
}
