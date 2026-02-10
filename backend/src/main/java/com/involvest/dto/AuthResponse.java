package com.involvest.dto;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    long userId,
    long familyId,
    String userName
) {}
