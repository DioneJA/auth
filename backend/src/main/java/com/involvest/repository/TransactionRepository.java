package com.involvest.repository;

import com.involvest.dto.TransactionResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {
  private final JdbcTemplate jdbcTemplate;

  public TransactionRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public long save(long familyId, long typeId, BigDecimal amount, String description, LocalDate occurredAt) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO transactions (family_id, type_id, amount, description, occurred_at) VALUES (?, ?, ?, ?, ?) RETURNING id",
        Long.class,
        familyId,
        typeId,
        amount,
        description,
        occurredAt
    );
    return id == null ? 0 : id;
  }

  public List<TransactionResponse> findByFamilyAndMonth(long familyId, LocalDate start, LocalDate end) {
    return jdbcTemplate.query(
        "SELECT t.id, t.type_id, mt.name as type_name, mt.kind, t.amount, t.description, t.occurred_at " +
            "FROM transactions t JOIN movement_types mt ON mt.id = t.type_id " +
            "WHERE t.family_id = ? AND t.occurred_at BETWEEN ? AND ? ORDER BY t.occurred_at DESC",
        (rs, rowNum) -> new TransactionResponse(
            rs.getLong("id"),
            rs.getLong("type_id"),
            rs.getString("type_name"),
            rs.getString("kind"),
            rs.getBigDecimal("amount"),
            rs.getString("description"),
            rs.getObject("occurred_at", java.time.LocalDate.class)
        ),
        familyId, start, end
    );
  }

  public BigDecimal sumByKind(long familyId, String kind, LocalDate start, LocalDate end) {
    BigDecimal sum = jdbcTemplate.queryForObject(
        "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t " +
            "JOIN movement_types mt ON mt.id = t.type_id " +
            "WHERE t.family_id = ? AND mt.kind = ? AND t.occurred_at BETWEEN ? AND ?",
        BigDecimal.class,
        familyId, kind, start, end
    );
    return sum == null ? BigDecimal.ZERO : sum;
  }

  public BigDecimal balanceAllTime(long familyId) {
    BigDecimal balance = jdbcTemplate.queryForObject(
        "SELECT COALESCE(SUM(CASE WHEN mt.kind = 'INCOME' THEN t.amount ELSE -t.amount END), 0) " +
            "FROM transactions t JOIN movement_types mt ON mt.id = t.type_id WHERE t.family_id = ?",
        BigDecimal.class,
        familyId
    );
    return balance == null ? BigDecimal.ZERO : balance;
  }
}
