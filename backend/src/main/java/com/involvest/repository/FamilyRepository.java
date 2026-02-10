package com.involvest.repository;

import com.involvest.model.Family;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FamilyRepository {
  private final JdbcTemplate jdbcTemplate;

  public FamilyRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Family save(String name) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO families (name) VALUES (?) RETURNING id",
        Long.class,
        name
    );
    return findById(id).orElseThrow();
  }

  public Optional<Family> findById(long id) {
    return jdbcTemplate.query(
        "SELECT id, name, created_at FROM families WHERE id = ?",
        new FamilyRowMapper(),
        id
    ).stream().findFirst();
  }

  private static class FamilyRowMapper implements RowMapper<Family> {
    @Override
    public Family mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Family(
          rs.getLong("id"),
          rs.getString("name"),
          rs.getObject("created_at", java.time.OffsetDateTime.class)
      );
    }
  }
}
