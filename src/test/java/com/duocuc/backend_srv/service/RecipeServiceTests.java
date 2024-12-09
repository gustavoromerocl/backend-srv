package com.duocuc.backend_srv.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.model.Recipe;
import com.duocuc.backend_srv.repository.RecipeRepository;

import jakarta.persistence.EntityNotFoundException;

class RecipeServiceTests {

  @InjectMocks
  private RecipeService recipeService;

  @Mock
  private RecipeRepository recipeRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllRecipes() {
    // Dado
    List<Recipe> mockRecipes = Arrays.asList(new Recipe(), new Recipe());
    when(recipeRepository.findAll()).thenReturn(mockRecipes);

    // Cuando
    List<Recipe> recipes = recipeService.getAllRecipes();

    // Entonces
    assertNotNull(recipes);
    assertEquals(2, recipes.size());
    verify(recipeRepository, times(1)).findAll();
  }

  @Test
  void testGetRecipeById() {
    // Dado
    Long recipeId = 1L;
    Recipe mockRecipe = new Recipe();
    mockRecipe.setId(recipeId);
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

    // Cuando
    Optional<Recipe> recipe = recipeService.getRecipeById(recipeId);

    // Entonces
    assertTrue(recipe.isPresent());
    assertEquals(recipeId, recipe.get().getId());
    verify(recipeRepository, times(1)).findById(recipeId);
  }

  @Test
  void testAddRecipe() {
    // Dado
    Recipe newRecipe = new Recipe();
    newRecipe.setTitle("Test Recipe");
    when(recipeRepository.save(newRecipe)).thenReturn(newRecipe);

    // Cuando
    Recipe savedRecipe = recipeService.addRecipe(newRecipe);

    // Entonces
    assertNotNull(savedRecipe);
    assertEquals("Test Recipe", savedRecipe.getTitle());
    verify(recipeRepository, times(1)).save(newRecipe);
  }

  @Test
  void testUpdateRecipe() {
    // Dado
    Recipe updatedRecipe = new Recipe();
    updatedRecipe.setId(1L);
    updatedRecipe.setTitle("Updated Recipe");
    when(recipeRepository.save(updatedRecipe)).thenReturn(updatedRecipe);

    // Cuando
    Recipe result = recipeService.updateRecipe(updatedRecipe);

    // Entonces
    assertNotNull(result);
    assertEquals("Updated Recipe", result.getTitle());
    verify(recipeRepository, times(1)).save(updatedRecipe);
  }

  @Test
  void testDeleteRecipe() {
    // Dado
    Long recipeId = 1L;
    when(recipeRepository.existsById(recipeId)).thenReturn(true);

    // Cuando
    recipeService.deleteRecipe(recipeId);

    // Entonces
    verify(recipeRepository, times(1)).existsById(recipeId);
    verify(recipeRepository, times(1)).deleteById(recipeId);
  }

  @Test
  void testDeleteRecipeNotFound() {
    // Dado
    Long recipeId = 1L;
    when(recipeRepository.existsById(recipeId)).thenReturn(false);

    // Cuando & Entonces
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      recipeService.deleteRecipe(recipeId);
    });
    assertEquals("Recipe with ID " + recipeId + " does not exist.", exception.getMessage());
    verify(recipeRepository, times(1)).existsById(recipeId);
    verify(recipeRepository, never()).deleteById(recipeId);
  }

  @Test
  void testGetCommentsForRecipe() {
    // Dado
    Long recipeId = 1L;
    Recipe recipe = new Recipe();
    Comment comment1 = new Comment();
    Comment comment2 = new Comment();
    recipe.setComments(Arrays.asList(comment1, comment2));
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

    // Cuando
    List<Comment> comments = recipeService.getCommentsForRecipe(recipeId);

    // Entonces
    assertNotNull(comments);
    assertEquals(2, comments.size());
    verify(recipeRepository, times(1)).findById(recipeId);
  }

  @Test
  void testGetCommentsForRecipeNotFound() {
    // Dado
    Long recipeId = 1L;
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

    // Cuando & Entonces
    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      recipeService.getCommentsForRecipe(recipeId);
    });
    assertEquals("Recipe not found", exception.getMessage());
    verify(recipeRepository, times(1)).findById(recipeId);
  }

  @Test
  void testGetAverageRatingForRecipe() {
    // Dado
    Long recipeId = 1L;
    Recipe recipe = mock(Recipe.class);
    when(recipe.getAverageRating()).thenReturn(4.5);
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

    // Cuando
    Double averageRating = recipeService.getAverageRatingForRecipe(recipeId);

    // Entonces
    assertNotNull(averageRating);
    assertEquals(4.5, averageRating);
    verify(recipeRepository, times(1)).findById(recipeId);
    verify(recipe, times(1)).getAverageRating();
  }

  @Test
  void testGetAverageRatingForRecipeNotFound() {
    // Dado
    Long recipeId = 1L;
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

    // Cuando & Entonces
    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      recipeService.getAverageRatingForRecipe(recipeId);
    });
    assertEquals("Recipe not found", exception.getMessage());
    verify(recipeRepository, times(1)).findById(recipeId);
  }
}
