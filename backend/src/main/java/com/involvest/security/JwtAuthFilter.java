package com.involvest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
  private final JwtUtil jwtUtil;

  public JwtAuthFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        Map<String, Object> claims = jwtUtil.validateAndGetClaims(token);
        if (claims.containsKey("fid") && claims.containsKey("email")) {
          long userId = ((Number) claims.get("uid")).longValue();
          long familyId = ((Number) claims.get("fid")).longValue();
          String email = (String) claims.get("email");
          UserPrincipal principal = new UserPrincipal(userId, familyId, email);
          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception ex) {
        logger.debug("JWT validation failed: {}", ex.getMessage());
      }
    }
    filterChain.doFilter(request, response);
  }
}
