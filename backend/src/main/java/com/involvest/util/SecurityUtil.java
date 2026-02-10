package com.involvest.util;

import com.involvest.exception.UnauthorizedException;
import com.involvest.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
  private SecurityUtil() {}

  public static UserPrincipal requirePrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
      throw new UnauthorizedException("Unauthenticated");
    }
    return (UserPrincipal) authentication.getPrincipal();
  }
}
