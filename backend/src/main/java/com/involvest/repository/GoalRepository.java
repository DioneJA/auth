package com.involvest.repository;

import com.involvest.model.Goal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GoalRepository {
  private final JdbcTemplate jdbcTemplate;

  public GoalRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Goal save(long familyId, String name, java.math.BigDecimal targetAmount, java.time.LocalDate targetDate) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO goals (family_id, name, target_amount, target_date) VALUES (?, ?, ?, ?) RETURNING id",
        Long.class,
        familyId,
        name,
        targetAmount,
        targetDate
    );
    return findByIdAndFamily(id, familyId).orElseThrow();
  }

  public List<Goal> findByFamily(long familyId) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, target_amount, target_date, created_at FROM goals WHERE family_id = ? ORDER BY created_at DESC",
        new GoalRowMapper(),
        familyId
    );
  }

  public java.util.Optional<Goal> findByIdAndFamily(long id, long familyId) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, target_amount, target_date, created_at FROM goals WHERE id = ? AND family_id = ?",
        new GoalRowMapper(),
        id, familyId
    ).stream().findFirst();
  }

  private static class GoalRowMapper implements RowMapper<Goal> {
    @Override
    public Goal mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Goal(
          rs.getLong("id"),
          rs.getLong("family_id"),
          rs.getString("name"),
          rs.getBigDecimal("target_amount"),
          rs.getObject("target_date", java.time.LocalDate.class),
          rs.getObject("created_at", java.time.OffsetDateTime.class)
      );
    }
  }
}
