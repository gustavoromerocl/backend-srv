package com.duocuc.backend_srv.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.duocuc.backend_srv.model.Role;
import com.duocuc.backend_srv.model.User;

class UserPrincipalTests {

  private User user;
  private UserPrincipal userPrincipal;

  @BeforeEach
  void setUp() {
    Role roleUser = new Role();
    roleUser.setName("ROLE_USER");

    Role roleAdmin = new Role();
    roleAdmin.setName("ROLE_ADMIN");

    user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setPassword("password");

    Set<Role> roles = new HashSet<>();
    roles.add(roleUser);
    roles.add(roleAdmin);
    user.setRoles(roles);

    userPrincipal = UserPrincipal.create(user);
  }

  @Test
  void testGetAuthorities() {
    Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

    // Imprimir las autoridades para depuración
    System.out.println("Authorities: " + authorities);

    assertNotNull(authorities);
    assertEquals(2, authorities.size());
    assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
  }

  @Test
  void testGetPassword() {
    assertEquals("password", userPrincipal.getPassword());
  }

  @Test
  void testGetUsername() {
    assertEquals("testuser", userPrincipal.getUsername());
  }

  @Test
  void testIsAccountNonExpired() {
    assertTrue(userPrincipal.isAccountNonExpired());
  }

  @Test
  void testIsAccountNonLocked() {
    assertTrue(userPrincipal.isAccountNonLocked());
  }

  @Test
  void testIsCredentialsNonExpired() {
    assertTrue(userPrincipal.isCredentialsNonExpired());
  }

  @Test
  void testIsEnabled() {
    assertTrue(userPrincipal.isEnabled());
  }
}
