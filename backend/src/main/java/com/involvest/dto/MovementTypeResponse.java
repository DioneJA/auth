package com.involvest.dto;

import com.involvest.model.MovementKind;

public record MovementTypeResponse(long id, String name, MovementKind kind) {}
