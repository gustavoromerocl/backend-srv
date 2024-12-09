package com.duocuc.backend_srv.controller;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.duocuc.backend_srv.dto.SignUpRequest;
import com.duocuc.backend_srv.dto.UserProfileDto;
import com.duocuc.backend_srv.model.Role;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.service.UserService;
import com.duocuc.backend_srv.util.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

class UsersControllerTest {

  @InjectMocks
  private UsersController usersController;

  @Mock
  private UserService userService;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAuthenticatedUserProfileSuccess() {
    // Datos de prueba
    String token = "valid-token";
    User user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    Role role = new Role();
    role.setId(1L);
    role.setName("ROLE_USER");
    user.setRoles(Collections.singleton(role));

    when(jwtUtils.getJwtFromRequest(request)).thenReturn(token);
    when(userService.getAuthenticatedUser(token)).thenReturn(Optional.of(user));

    // Ejecutar el método
    ResponseEntity<?> response = usersController.getAuthenticatedUserProfile(request);

    // Verificaciones
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() instanceof UserProfileDto);

    UserProfileDto userProfile = (UserProfileDto) response.getBody();
    assertEquals(user.getId(), userProfile.getId());
    assertEquals(user.getUsername(), userProfile.getUsername());
    assertEquals(1, userProfile.getRoles().size());
    assertEquals("ROLE_USER", userProfile.getRoles().get(0).getName());

    verify(jwtUtils, times(1)).getJwtFromRequest(request);
    verify(userService, times(1)).getAuthenticatedUser(token);
  }

  @Test
  void testGetAuthenticatedUserProfileNotFound() {
    // Datos de prueba
    String token = "valid-token";

    when(jwtUtils.getJwtFromRequest(request)).thenReturn(token);
    when(userService.getAuthenticatedUser(token)).thenReturn(Optional.empty());

    // Ejecutar el método
    ResponseEntity<?> response = usersController.getAuthenticatedUserProfile(request);

    // Verificaciones
    assertNotNull(response);
    assertEquals(404, response.getStatusCode().value());
    assertEquals("User not found", response.getBody());

    verify(jwtUtils, times(1)).getJwtFromRequest(request);
    verify(userService, times(1)).getAuthenticatedUser(token);
  }

  @Test
  void testGetAuthenticatedUserProfileError() {
    when(jwtUtils.getJwtFromRequest(request)).thenThrow(new RuntimeException("Error decoding token"));

    // Ejecutar el método
    ResponseEntity<?> response = usersController.getAuthenticatedUserProfile(request);

    // Verificaciones
    assertNotNull(response);
    assertEquals(500, response.getStatusCode().value());
    assertEquals("Error retrieving user profile", response.getBody());

    verify(jwtUtils, times(1)).getJwtFromRequest(request);
    verify(userService, never()).getAuthenticatedUser(anyString());
  }

  @Test
  void testUpdateUserSuccess() {
    // Datos de prueba
    Long userId = 1L;
    SignUpRequest updateRequest = new SignUpRequest();
    updateRequest.setUsername("updatedUser");
    updateRequest.setEmail("updated@example.com");

    doNothing().when(userService).updateUser(userId, updateRequest);

    // Ejecutar el método
    ResponseEntity<?> response = usersController.updateUser(userId, updateRequest);

    // Verificaciones
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals("User updated successfully!", response.getBody());

    verify(userService, times(1)).updateUser(userId, updateRequest);
  }

  @Test
  void testUpdateUserError() {
    // Datos de prueba
    Long userId = 1L;
    SignUpRequest updateRequest = new SignUpRequest();

    doThrow(new RuntimeException("Error updating user")).when(userService).updateUser(userId, updateRequest);

    // Ejecutar el método
    ResponseEntity<?> response = usersController.updateUser(userId, updateRequest);

    // Verificaciones
    assertNotNull(response);
    assertEquals(500, response.getStatusCode().value());
    assertEquals("Error updating user.", response.getBody());

    verify(userService, times(1)).updateUser(userId, updateRequest);
  }

  @Test
  void testDeleteUserSuccess() {
    // Datos de prueba
    Long userId = 1L;

    doNothing().when(userService).deleteUser(userId);

    // Ejecutar el método
    ResponseEntity<?> response = usersController.deleteUser(userId);

    // Verificaciones
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals("User deleted successfully!", response.getBody());

    verify(userService, times(1)).deleteUser(userId);
  }

  @Test
  void testDeleteUserError() {
    // Datos de prueba
    Long userId = 1L;

    doThrow(new RuntimeException("Error deleting user")).when(userService).deleteUser(userId);

    // Ejecutar el método
    ResponseEntity<?> response = usersController.deleteUser(userId);

    // Verificaciones
    assertNotNull(response);
    assertEquals(500, response.getStatusCode().value());
    assertEquals("Error deleting user.", response.getBody());

    verify(userService, times(1)).deleteUser(userId);
  }
}
