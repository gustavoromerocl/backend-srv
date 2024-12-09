package com.duocuc.backend_srv.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class UserTests {

  @Test
  void testUserConstructorAndGetters() {
    User user = new User("testuser", "password123", "test@example.com");

    assertNull(user.getId());
    assertEquals("testuser", user.getUsername());
    assertEquals("password123", user.getPassword());
    assertEquals("test@example.com", user.getEmail());
    assertNotNull(user.getRoles());
    assertTrue(user.getRoles().isEmpty());
  }

  @Test
  void testSetters() {
    User user = new User();
    user.setId(1L);
    user.setUsername("newuser");
    user.setPassword("newpassword");
    user.setEmail("new@example.com");

    assertEquals(1L, user.getId());
    assertEquals("newuser", user.getUsername());
    assertEquals("newpassword", user.getPassword());
    assertEquals("new@example.com", user.getEmail());
  }

  @Test
  void testAddRole() {
    User user = new User();
    Role role = new Role("USER", "ROLE_USER");
    user.addRole(role);

    assertNotNull(user.getRoles());
    assertTrue(user.getRoles().contains(role));
  }

  @Test
  void testRemoveRole() {
    User user = new User();
    Role role = new Role("USER", "ROLE_USER");
    user.addRole(role);
    user.removeRole(role);

    assertNotNull(user.getRoles());
    assertFalse(user.getRoles().contains(role));
  }

  @Test
  void testToString() {
    User user = new User("testuser", "password123", "test@example.com");
    Role role = new Role("USER", "ROLE_USER");
    user.addRole(role);

    String expected = "User{id=null, username='testuser', email='test@example.com', roles=[Role{id=null, name='USER', code='ROLE_USER'}]}";
    assertEquals(expected, user.toString());
  }
}
