package com.involvest.controller;

import com.involvest.dto.FamilyRequest;
import com.involvest.dto.FamilyResponse;
import com.involvest.model.Family;
import com.involvest.service.FamilyService;
import com.involvest.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/families")
public class FamilyController {
  private final FamilyService familyService;

  public FamilyController(FamilyService familyService) {
    this.familyService = familyService;
  }

  @PostMapping
  public ResponseEntity<FamilyResponse> create(@Valid @RequestBody FamilyRequest request) {
    Family family = familyService.create(request.name());
    return ResponseEntity.ok(new FamilyResponse(family.getId(), family.getName()));
  }

  @GetMapping("/me")
  public ResponseEntity<FamilyResponse> getMyFamily() {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    Family family = familyService.getById(familyId);
    return ResponseEntity.ok(new FamilyResponse(family.getId(), family.getName()));
  }
}
