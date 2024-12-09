package com.duocuc.backend_srv.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class CommentTest {

  @Test
  void testConstructorAndDefaultValues() {
    Comment comment = new Comment();
    assertNotNull(comment.getCreatedAt());
    assertNull(comment.getUser());
    assertNull(comment.getRecipe());
    assertNull(comment.getContent());
    assertNull(comment.getUsername());
  }

  @Test
  void testSetAndGetId() {
    Comment comment = new Comment();
    comment.setId(1L);
    assertEquals(1L, comment.getId());
  }

  @Test
  void testSetAndGetUser() {
    Comment comment = new Comment();
    User user = new User();
    user.setUsername("testuser");

    comment.setUser(user);

    assertNotNull(comment.getUser());
    assertEquals("testuser", comment.getUser().getUsername());
    assertEquals("testuser", comment.getUsername());
  }

  @Test
  void testSetAndGetRecipe() {
    Comment comment = new Comment();
    Recipe recipe = new Recipe();
    recipe.setTitle("Spaghetti Bolognese");

    comment.setRecipe(recipe);

    assertNotNull(comment.getRecipe());
    assertEquals("Spaghetti Bolognese", comment.getRecipe().getTitle());
  }

  @Test
  void testSetAndGetContent() {
    Comment comment = new Comment();
    comment.setContent("This recipe is great!");
    assertEquals("This recipe is great!", comment.getContent());
  }

  @Test
  void testSetAndGetCreatedAt() {
    Comment comment = new Comment();
    LocalDateTime now = LocalDateTime.now();
    comment.setCreatedAt(now);

    assertEquals(now, comment.getCreatedAt());
  }

  @Test
  void testPopulateTransientFields() {
    Comment comment = new Comment();
    User user = new User();
    user.setUsername("testuser");

    comment.setUser(user);
    comment.populateTransientFields();

    assertEquals("testuser", comment.getUsername());
  }

  @Test
  void testTransientUsernameWhenUserIsNull() {
    Comment comment = new Comment();
    comment.populateTransientFields();

    assertNull(comment.getUsername());
  }
}
