package com.involvest.repository;

import com.involvest.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  private final JdbcTemplate jdbcTemplate;

  public UserRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Optional<User> findByEmail(String email) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, email, password_hash, created_at FROM users WHERE email = ?",
        new UserRowMapper(),
        email
    ).stream().findFirst();
  }

  public Optional<User> findById(long id) {
    return jdbcTemplate.query(
        "SELECT id, family_id, name, email, password_hash, created_at FROM users WHERE id = ?",
        new UserRowMapper(),
        id
    ).stream().findFirst();
  }

  public boolean existsByEmail(String email) {
    Integer count = jdbcTemplate.queryForObject(
        "SELECT COUNT(1) FROM users WHERE email = ?",
        Integer.class,
        email
    );
    return count != null && count > 0;
  }

  public User save(long familyId, String name, String email, String passwordHash) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO users (family_id, name, email, password_hash) VALUES (?, ?, ?, ?) RETURNING id",
        Long.class,
        familyId,
        name,
        email,
        passwordHash
    );
    return findById(id).orElseThrow();
  }

  private static class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new User(
          rs.getLong("id"),
          rs.getLong("family_id"),
          rs.getString("name"),
          rs.getString("email"),
          rs.getString("password_hash"),
          rs.getObject("created_at", java.time.OffsetDateTime.class)
      );
    }
  }
}
