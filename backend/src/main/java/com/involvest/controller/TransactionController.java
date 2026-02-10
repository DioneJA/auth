package com.involvest.controller;

import com.involvest.dto.TransactionRequest;
import com.involvest.dto.TransactionResponse;
import com.involvest.service.TransactionService;
import com.involvest.util.SecurityUtil;
import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody TransactionRequest request) {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    transactionService.create(familyId, request.typeId(), request.amount(), request.description(), request.occurredAt());
    return ResponseEntity.ok().build();
  }

  @GetMapping
  public ResponseEntity<List<TransactionResponse>> list(@RequestParam(required = false) String month) {
    long familyId = SecurityUtil.requirePrincipal().getFamilyId();
    YearMonth target = month == null ? YearMonth.now() : YearMonth.parse(month);
    return ResponseEntity.ok(transactionService.listByMonth(familyId, target));
  }
}
