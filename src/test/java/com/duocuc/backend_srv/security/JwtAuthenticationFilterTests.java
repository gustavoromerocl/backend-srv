package com.duocuc.backend_srv.security;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.duocuc.backend_srv.util.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
    // Reinicia el contexto de seguridad antes de cada prueba
    SecurityContextHolder.setContext(new SecurityContextImpl());
  }

  @AfterEach
  void tearDown() {
    // Limpia el contexto de seguridad después de cada prueba
    SecurityContextHolder.clearContext();
  }

  @Test
  void testDoFilterInternalWithValidToken() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String jwtToken = "validToken";
    request.addHeader("Authorization", "Bearer " + jwtToken);

    Claims claims = mock(Claims.class);
    when(jwtUtils.getJwtFromRequest(request)).thenReturn(jwtToken);
    when(jwtUtils.validateToken(jwtToken)).thenReturn(true);
    when(jwtUtils.getClaimsFromToken(jwtToken)).thenReturn(claims);
    when(claims.getSubject()).thenReturn("testuser");
    when(claims.get("roles")).thenReturn("ROLE_USER,ROLE_ADMIN");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    assertEquals(2, SecurityContextHolder.getContext().getAuthentication().getAuthorities().size());
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testDoFilterInternalWithInvalidToken() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    String jwtToken = "invalidToken";
    request.addHeader("Authorization", "Bearer " + jwtToken);

    when(jwtUtils.getJwtFromRequest(request)).thenReturn(jwtToken);
    when(jwtUtils.validateToken(jwtToken)).thenReturn(false);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  void testDoFilterInternalWithoutToken() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(jwtUtils.getJwtFromRequest(request)).thenReturn(null);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain, times(1)).doFilter(request, response);
  }
}
