package com.involvest.controller;

import com.involvest.dto.DashboardResponse;
import com.involvest.service.DashboardService;
import com.involvest.util.SecurityUtil;
import java.time.YearMonth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
  private final DashboardService dashboardService;

  public DashboardController(DashboardService dashboardService) {
    this.dashboardService = dashboardService;
  }

  @GetMapping
  public ResponseEntity<DashboardResponse> getSummary(@RequestParam(required = false) String month) {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    YearMonth target = month == null ? YearMonth.now() : YearMonth.parse(month);
    return ResponseEntity.ok(dashboardService.getMonthlySummary(familyId, target));
  }
}
