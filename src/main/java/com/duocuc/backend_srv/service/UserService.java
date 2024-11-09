package com.duocuc.backend_srv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.duocuc.backend_srv.dto.SignUpRequest;
import com.duocuc.backend_srv.model.Role;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.repository.RoleRepository;
import com.duocuc.backend_srv.repository.UserRepository;
import com.duocuc.backend_srv.util.JwtUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtils jwtUtils;

  // Método para registrar un usuario con un rol predeterminado
  public User registerUser(String username, String password, String email) {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEmail(email);
    System.out.println("TODOS LOS ROLES" + roleRepository.findAll());
    Role userRole = roleRepository.findByCode("ROLE_USER")
        .orElseThrow(() -> new RuntimeException("Error: Role not found."));

    Set<Role> roles = new HashSet<>();
    roles.add(userRole);
    user.setRoles(roles);

    return userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public void updateUser(Long id, SignUpRequest updateRequest) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    user.setUsername(updateRequest.getUsername());
    user.setEmail(updateRequest.getEmail());
    user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

    userRepository.save(user);
  }

  public void deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    userRepository.delete(user);
  }

  public Optional<User> getAuthenticatedUser(String token) {
    String username = jwtUtils.getAuthenticatedUsername(token);
    return userRepository.findByUsername(username); // Busca al usuario en la base de datos
  }
}
