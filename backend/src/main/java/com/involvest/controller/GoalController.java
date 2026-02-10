package com.involvest.controller;

import com.involvest.dto.GoalRequest;
import com.involvest.dto.GoalResponse;
import com.involvest.model.Goal;
import com.involvest.service.GoalService;
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
@RequestMapping("/goals")
public class GoalController {
  private final GoalService goalService;

  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  @PostMapping
  public ResponseEntity<GoalResponse> create(@Valid @RequestBody GoalRequest request) {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    Goal goal = goalService.create(familyId, request.name(), request.targetAmount(), request.targetDate());
    GoalResponse response = new GoalResponse(goal.getId(), goal.getName(), goal.getTargetAmount(),
        goal.getTargetDate(), java.math.BigDecimal.ZERO, 0.0);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<GoalResponse>> list() {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    return ResponseEntity.ok(goalService.listWithProgress(familyId));
  }
}
