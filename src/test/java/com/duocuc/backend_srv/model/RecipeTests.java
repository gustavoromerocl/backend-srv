package com.duocuc.backend_srv.model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RecipeTest {

  @Test
  void testConstructorAndGetters() {
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    recipe.setTitle("Spaghetti Bolognese");
    recipe.setDescription("A classic Italian pasta dish.");
    recipe.setCookTime(30);
    recipe.setDifficulty(Difficulty.MEDIUM);

    assertEquals(1L, recipe.getId());
    assertEquals("Spaghetti Bolognese", recipe.getTitle());
    assertEquals("A classic Italian pasta dish.", recipe.getDescription());
    assertEquals(30, recipe.getCookTime());
    assertEquals(Difficulty.MEDIUM, recipe.getDifficulty());
  }

  @Test
  void testSetIngredients() {
    Recipe recipe = new Recipe();
    List<String> ingredients = List.of("Spaghetti", "Ground beef", "Tomato sauce");
    recipe.setIngredients(ingredients);

    assertNotNull(recipe.getIngredients());
    assertEquals(3, recipe.getIngredients().size());
    assertTrue(recipe.getIngredients().contains("Spaghetti"));
  }

  @Test
  void testSetInstructions() {
    Recipe recipe = new Recipe();
    recipe.setInstructions("1. Boil spaghetti. 2. Cook beef. 3. Mix with sauce.");

    assertEquals("1. Boil spaghetti. 2. Cook beef. 3. Mix with sauce.", recipe.getInstructions());
  }

  @Test
  void testSetPhoto() {
    Recipe recipe = new Recipe();
    Photo photo = new Photo(1L, "photo-url", "Delicious spaghetti", null);
    recipe.setPhoto(photo);

    assertNotNull(recipe.getPhoto());
    assertEquals("photo-url", recipe.getPhoto().getUrl());
    assertEquals("Delicious spaghetti", recipe.getPhoto().getDescription());
  }

  @Test
  void testSetRatings() {
    Recipe recipe = new Recipe();
    Rating rating1 = new Rating();
    rating1.setValue(5);
    Rating rating2 = new Rating();
    rating2.setValue(4);

    List<Rating> ratings = List.of(rating1, rating2);
    recipe.setRatings(ratings);

    assertNotNull(recipe.getRatings());
    assertEquals(2, recipe.getRatings().size());
    assertEquals(5, recipe.getRatings().get(0).getValue());
    assertEquals(4, recipe.getRatings().get(1).getValue());
  }

  @Test
  void testGetAverageRating() {
    Recipe recipe = new Recipe();
    Rating rating1 = new Rating();
    rating1.setValue(5);
    Rating rating2 = new Rating();
    rating2.setValue(3);

    List<Rating> ratings = List.of(rating1, rating2);
    recipe.setRatings(ratings);

    assertEquals(4.0, recipe.getAverageRating());
  }

  @Test
  void testSetComments() {
    Recipe recipe = new Recipe();
    Comment comment1 = new Comment();
    comment1.setContent("Great recipe!");
    Comment comment2 = new Comment();
    comment2.setContent("Easy to follow!");

    List<Comment> comments = List.of(comment1, comment2);
    recipe.setComments(comments);

    assertNotNull(recipe.getComments());
    assertEquals(2, recipe.getComments().size());
    assertEquals("Great recipe!", recipe.getComments().get(0).getContent());
    assertEquals("Easy to follow!", recipe.getComments().get(1).getContent());
  }
}
