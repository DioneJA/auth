package com.involvest.dto;

import jakarta.validation.constraints.NotBlank;

public record FamilyRequest(@NotBlank String name) {}
