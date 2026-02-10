package com.involvest.controller;

import com.involvest.dto.MovementTypeRequest;
import com.involvest.dto.MovementTypeResponse;
import com.involvest.model.MovementType;
import com.involvest.service.MovementTypeService;
import com.involvest.util.SecurityUtil;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/types")
public class MovementTypeController {
  private final MovementTypeService movementTypeService;

  public MovementTypeController(MovementTypeService movementTypeService) {
    this.movementTypeService = movementTypeService;
  }

  @PostMapping
  public ResponseEntity<MovementTypeResponse> create(@Valid @RequestBody MovementTypeRequest request) {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    MovementType type = movementTypeService.create(familyId, request.name(), request.kind());
    return ResponseEntity.ok(new MovementTypeResponse(type.getId(), type.getName(), type.getKind()));
  }

  @GetMapping
  public ResponseEntity<List<MovementTypeResponse>> list() {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    List<MovementTypeResponse> response = movementTypeService.list(familyId).stream()
        .map(type -> new MovementTypeResponse(type.getId(), type.getName(), type.getKind()))
        .toList();
    return ResponseEntity.ok(response);
  }
}
