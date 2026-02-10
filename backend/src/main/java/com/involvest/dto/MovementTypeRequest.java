package com.involvest.dto;

import com.involvest.model.MovementKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovementTypeRequest(
    @NotBlank String name,
    @NotNull MovementKind kind
) {}
