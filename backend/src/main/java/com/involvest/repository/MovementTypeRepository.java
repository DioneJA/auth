package com.involvest.repository;

import com.involvest.model.MovementKind;
import com.involvest.model.MovementType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MovementTypeRepository {
  private final JdbcTemplate jdbcTemplate;

  public MovementTypeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public MovementType save(long familyId, String name, MovementKind kind) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO movement_types (family_id, name, kind) VALUES (?, ?, ?) RETURNING id",
        Long.class,
        familyId,
        name,
        kind.name()
    );
    return findByIdAndFamily(id, familyId).orElseThrow();
  }

  public List<MovementType> findAllByFamily(long familyId) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, kind, created_at FROM movement_types WHERE family_id = ? ORDER BY name",
        new MovementTypeRowMapper(),
        familyId
    );
  }

  public Optional<MovementType> findByIdAndFamily(long id, long familyId) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, kind, created_at FROM movement_types WHERE id = ? AND family_id = ?",
        new MovementTypeRowMapper(),
        id, familyId
    ).stream().findFirst();
  }

  private static class MovementTypeRowMapper implements RowMapper<MovementType> {
    @Override
    public MovementType mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new MovementType(
          rs.getLong("id"),
          rs.getLong("family_id"),
          rs.getString("name"),
          MovementKind.valueOf(rs.getString("kind")),
          rs.getObject("created_at", java.time.OffsetDateTime.class)
      );
    }
  }
}
