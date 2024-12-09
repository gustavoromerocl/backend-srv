package com.duocuc.backend_srv.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.duocuc.backend_srv.dto.SignUpRequest;
import com.duocuc.backend_srv.model.Role;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.repository.RoleRepository;
import com.duocuc.backend_srv.repository.UserRepository;
import com.duocuc.backend_srv.util.JwtUtils;

class UserServiceTests {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtUtils jwtUtils;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterUserSuccess() {

    String username = "testuser";
    String password = "password";
    String email = "test@example.com";
    Role role = new Role();
    role.setCode("ROLE_USER");
    User user = new User();

    when(roleRepository.findByCode("ROLE_USER")).thenReturn(Optional.of(role));
    when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    User registeredUser = userService.registerUser(username, password, email);

    assertNotNull(registeredUser);
    verify(roleRepository, times(1)).findByCode("ROLE_USER");
    verify(passwordEncoder, times(1)).encode(password);
    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  void testRegisterUserRoleNotFound() {

    when(roleRepository.findByCode("ROLE_USER")).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.registerUser("testuser", "password", "test@example.com");
    });

    assertEquals("Error: Role not found.", exception.getMessage());
    verify(roleRepository, times(1)).findByCode("ROLE_USER");
  }

  @Test
  void testFindByUsernameSuccess() {

    String username = "testuser";
    User user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    User foundUser = userService.findByUsername(username);

    assertNotNull(foundUser);
    assertEquals(username, foundUser.getUsername());
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void testFindByUsernameNotFound() {

    String username = "testuser";

    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      userService.findByUsername(username);
    });

    assertEquals("User not found with username: " + username, exception.getMessage());
    verify(userRepository, times(1)).findByUsername(username);
  }

  @Test
  void testExistsByUsername() {

    String username = "testuser";
    when(userRepository.existsByUsername(username)).thenReturn(true);

    boolean exists = userService.existsByUsername(username);

    assertTrue(exists);
    verify(userRepository, times(1)).existsByUsername(username);
  }

  @Test
  void testUpdateUserSuccess() {

    Long userId = 1L;
    User user = new User();
    SignUpRequest updateRequest = new SignUpRequest();
    updateRequest.setUsername("updatedUser");
    updateRequest.setEmail("updated@example.com");
    updateRequest.setPassword("newPassword");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    userService.updateUser(userId, updateRequest);

    verify(userRepository, times(1)).findById(userId);
    verify(passwordEncoder, times(1)).encode(updateRequest.getPassword());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testUpdateUserNotFound() {

    Long userId = 1L;
    SignUpRequest updateRequest = new SignUpRequest();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.updateUser(userId, updateRequest);
    });

    assertEquals("User not found", exception.getMessage());
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  void testDeleteUserSuccess() {

    Long userId = 1L;
    User user = new User();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    userService.deleteUser(userId);

    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).delete(user);
  }

  @Test
  void testDeleteUserNotFound() {

    Long userId = 1L;

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
      userService.deleteUser(userId);
    });

    assertEquals("User not found", exception.getMessage());
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, never()).delete(any(User.class));
  }

  @Test
  void testGetAuthenticatedUser() {

    String token = "valid-token";
    String username = "testuser";
    User user = new User();
    user.setUsername(username);

    when(jwtUtils.getAuthenticatedUsername(token)).thenReturn(username);
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    Optional<User> authenticatedUser = userService.getAuthenticatedUser(token);

    assertTrue(authenticatedUser.isPresent());
    assertEquals(username, authenticatedUser.get().getUsername());
    verify(jwtUtils, times(1)).getAuthenticatedUsername(token);
    verify(userRepository, times(1)).findByUsername(username);
  }
}
