package com.awd.user;

import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/users")
class UserController {
  @GetMapping("/health") Map<String, String> health() { return Map.of("status", "user-service running", "identityProvider", "Keycloak"); }
  @GetMapping("/me") Map<String, Object> me(@AuthenticationPrincipal Jwt jwt) {
    return Map.of("id", jwt.getSubject(), "username", jwt.getClaimAsString("preferred_username"), "email", jwt.getClaimAsString("email"), "roles", jwt.getClaimAsMap("realm_access"));
  }
}
