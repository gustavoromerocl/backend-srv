package com.duocuc.backend_srv.controller;

import com.duocuc.backend_srv.dto.JwtResponse;
import com.duocuc.backend_srv.dto.LoginRequest;
import com.duocuc.backend_srv.dto.SignUpRequest;
import com.duocuc.backend_srv.exception.UserAlreadyExistsException;
import com.duocuc.backend_srv.service.UserService;
import com.duocuc.backend_srv.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

  @InjectMocks
  private AuthController authController;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserService userService;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private Authentication authentication;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAuthenticateUser() {
    // Datos de prueba
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("password");

    String token = "jwt-token";

    // Configurar mocks
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(authentication)).thenReturn(token);

    // Ejecutar el método a probar
    ResponseEntity<?> response = authController.authenticateUser(loginRequest);

    // Verificaciones
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody() instanceof JwtResponse);

    JwtResponse jwtResponse = (JwtResponse) response.getBody();
    assertEquals(token, jwtResponse.getToken());

    verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtUtils, times(1)).generateJwtToken(authentication);
  }

  @Test
  void testRegisterUserSuccess() {
    // Datos de prueba
    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername("newuser");
    signUpRequest.setPassword("password");
    signUpRequest.setEmail("newuser@example.com");

    // Configurar mocks
    when(userService.existsByUsername(signUpRequest.getUsername())).thenReturn(false);

    // Ejecutar el método a probar
    ResponseEntity<?> response = authController.registerUser(signUpRequest);

    // Verificaciones
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals("User registered successfully!", response.getBody());

    verify(userService, times(1)).existsByUsername(signUpRequest.getUsername());
    verify(userService, times(1)).registerUser(
        signUpRequest.getUsername(),
        signUpRequest.getPassword(),
        signUpRequest.getEmail());
  }

  @Test
  void testRegisterUserAlreadyExists() {
    // Datos de prueba
    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername("existinguser");
    signUpRequest.setPassword("password");
    signUpRequest.setEmail("existinguser@example.com");

    // Configurar mocks
    when(userService.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

    // Ejecutar y verificar excepción
    UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
        () -> authController.registerUser(signUpRequest));

    assertEquals("Error: Username is already taken!", exception.getMessage());

    verify(userService, times(1)).existsByUsername(signUpRequest.getUsername());
    verify(userService, never()).registerUser(anyString(), anyString(), anyString());
  }
}
