package com.involvest.repository;

import com.involvest.model.RefreshToken;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository {
  private final JdbcTemplate jdbcTemplate;

  public RefreshTokenRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public RefreshToken save(long userId, String tokenHash, OffsetDateTime expiresAt) {
    Long id = jdbcTemplate.queryForObject(
        "INSERT INTO refresh_tokens (user_id, token_hash, expires_at) VALUES (?, ?, ?) RETURNING id",
        Long.class,
        userId,
        tokenHash,
        expiresAt
    );
    return findById(id).orElseThrow();
  }

  public Optional<RefreshToken> findValidByHash(String tokenHash) {
    return jdbcTemplate.query(
        "SELECT id, user_id, token_hash, expires_at, revoked_at, created_at FROM refresh_tokens " +
            "WHERE token_hash = ? AND revoked_at IS NULL AND expires_at > NOW()",
        new RefreshTokenRowMapper(),
        tokenHash
    ).stream().findFirst();
  }

  public void revokeByUser(long userId) {
    jdbcTemplate.update(
        "UPDATE refresh_tokens SET revoked_at = NOW() WHERE user_id = ? AND revoked_at IS NULL",
        userId
    );
  }

  private Optional<RefreshToken> findById(Long id) {
    return jdbcTemplate.query(
        "SELECT id, user_id, token_hash, expires_at, revoked_at, created_at FROM refresh_tokens WHERE id = ?",
        new RefreshTokenRowMapper(),
        id
    ).stream().findFirst();
  }

  private static class RefreshTokenRowMapper implements RowMapper<RefreshToken> {
    @Override
    public RefreshToken mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new RefreshToken(
          rs.getLong("id"),
          rs.getLong("user_id"),
          rs.getString("token_hash"),
          rs.getObject("expires_at", java.time.OffsetDateTime.class),
          rs.getObject("revoked_at", java.time.OffsetDateTime.class),
          rs.getObject("created_at", java.time.OffsetDateTime.class)
      );
    }
  }
}
