package com.duocuc.backend_srv.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.duocuc.backend_srv.config.JwtConfig;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

  @InjectMocks
  private JwtUtils jwtUtils;

  @Mock
  private JwtConfig jwtConfig;

  private Key signingKey;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    String secret = Base64.getEncoder().encodeToString(
        "NkxhR2NFdDJ5VGxzNkd3aG9MYmVkdG1qSDFucEJIZnBxSGQ0SkxCVHVxZnN3b1RlMjhiM2dQQzRScmxzOFZUUg==".getBytes());
    signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    when(jwtConfig.getSecret()).thenReturn(secret);
    when(jwtConfig.getExpirationMs()).thenReturn(3600000);
  }

  @Test
  void testGenerateJwtToken() {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        "testuser",
        null,
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    String token = jwtUtils.generateJwtToken(authentication);

    assertNotNull(token);
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();

    assertEquals("testuser", claims.getSubject());
    assertEquals("ROLE_USER", claims.get("roles"));
  }

  @Test
  void testValidateTokenValid() {
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(signingKey)
        .compact();

    boolean isValid = jwtUtils.validateToken(token);

    assertTrue(isValid);
  }

  @Test
  void testValidateTokenExpired() {
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date(System.currentTimeMillis() - 7200000))
        .setExpiration(new Date(System.currentTimeMillis() - 3600000))
        .signWith(signingKey)
        .compact();

    boolean isValid = jwtUtils.validateToken(token);

    assertFalse(isValid);
  }

  @Test
  void testGetClaimsFromToken() {
    String token = Jwts.builder()
        .setSubject("testuser")
        .claim("roles", "ROLE_USER")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(signingKey)
        .compact();

    Claims claims = jwtUtils.getClaimsFromToken(token);

    assertNotNull(claims);
    assertEquals("testuser", claims.getSubject());
    assertEquals("ROLE_USER", claims.get("roles"));
  }

  @Test
  void testGetJwtFromRequest() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn("Bearer test-jwt-token");

    String token = jwtUtils.getJwtFromRequest(request);

    assertNotNull(token);
    assertEquals("test-jwt-token", token);
  }

  @Test
  void testGetJwtFromRequestNoBearer() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getHeader("Authorization")).thenReturn("test-jwt-token");

    String token = jwtUtils.getJwtFromRequest(request);

    assertNull(token);
  }

  @Test
  void testGetAuthenticatedUsername() {
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        .signWith(signingKey)
        .compact();

    String username = jwtUtils.getAuthenticatedUsername(token);

    assertNotNull(username);
    assertEquals("testuser", username);
  }
}
