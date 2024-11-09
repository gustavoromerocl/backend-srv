package com.duocuc.backend_srv.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.duocuc.backend_srv.dto.RoleDto;
import com.duocuc.backend_srv.dto.SignUpRequest;
import com.duocuc.backend_srv.dto.UserProfileDto;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.service.UserService;
import com.duocuc.backend_srv.util.JwtUtils;

@RestController
@RequestMapping("/api/users")
public class UsersController {
  @Autowired
  private UserService userService;

  @Autowired
  private JwtUtils jwtUtils;

  @GetMapping("/profile")
  public ResponseEntity<?> getAuthenticatedUserProfile(HttpServletRequest request) {
    try {
      String token = jwtUtils.getJwtFromRequest(request);
      Optional<User> userOpt = userService.getAuthenticatedUser(token);

      if (userOpt.isPresent()) {
        User user = userOpt.get();

        // Mapear los roles a RoleDto
        List<RoleDto> roles = user.getRoles().stream()
            .map(role -> new RoleDto(role.getId(), role.getName()))
            .collect(Collectors.toList());

        // Crear el UserProfileDto
        UserProfileDto userProfile = new UserProfileDto(user.getId(), user.getUsername(), roles);

        return ResponseEntity.ok(userProfile);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving user profile");
    }
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody SignUpRequest updateRequest) {
    try {
      userService.updateUser(id, updateRequest);
      return ResponseEntity.ok("User updated successfully!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user.");
    }
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    try {
      userService.deleteUser(id);
      return ResponseEntity.ok("User deleted successfully!");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user.");
    }
  }
}