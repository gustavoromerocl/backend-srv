package com.duocuc.backend_srv.controller;

import com.duocuc.backend_srv.dto.CommentDto;
import com.duocuc.backend_srv.model.*;
import com.duocuc.backend_srv.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeControllerTest {

  @InjectMocks
  private RecipeController recipeController;

  @Mock
  private RecipeService recipeService;

  @Mock
  private CommentService commentService;

  @Mock
  private RatingService ratingService;

  @Mock
  private UserService userService;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void testGetAllRecipes() {
    List<Recipe> recipes = Collections.singletonList(new Recipe());
    when(recipeService.getAllRecipes()).thenReturn(recipes);

    ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(recipes, response.getBody());

    verify(recipeService, times(1)).getAllRecipes();
  }

  @Test
  void testGetRecipeByIdFound() {
    Recipe recipe = new Recipe();
    when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));

    ResponseEntity<Recipe> response = recipeController.getRecipeById(1L);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(recipe, response.getBody());

    verify(recipeService, times(1)).getRecipeById(1L);
  }

  @Test
  void testGetRecipeByIdNotFound() {
    when(recipeService.getRecipeById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Recipe> response = recipeController.getRecipeById(1L);

    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
    assertNull(response.getBody());

    verify(recipeService, times(1)).getRecipeById(1L);
  }

  @Test
  void testCreateRecipe() {
    Recipe recipe = new Recipe();
    when(recipeService.addRecipe(recipe)).thenReturn(recipe);

    ResponseEntity<Recipe> response = recipeController.createRecipe(recipe);

    assertNotNull(response);
    assertEquals(201, response.getStatusCodeValue());
    assertEquals(recipe, response.getBody());

    verify(recipeService, times(1)).addRecipe(recipe);
  }

  @Test
  void testUpdateRecipeFound() {
    Recipe existingRecipe = new Recipe();
    Recipe updatedRecipe = new Recipe();
    updatedRecipe.setTitle("New Title");

    when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(existingRecipe));
    when(recipeService.updateRecipe(existingRecipe)).thenReturn(existingRecipe);

    ResponseEntity<Recipe> response = recipeController.updateRecipe(1L, updatedRecipe);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    verify(recipeService, times(1)).getRecipeById(1L);
    verify(recipeService, times(1)).updateRecipe(existingRecipe);
  }

  @Test
  void testUpdateRecipeNotFound() {
    when(recipeService.getRecipeById(1L)).thenReturn(Optional.empty());

    ResponseEntity<Recipe> response = recipeController.updateRecipe(1L, new Recipe());

    assertNotNull(response);
    assertEquals(404, response.getStatusCodeValue());
    assertNull(response.getBody());

    verify(recipeService, times(1)).getRecipeById(1L);
    verify(recipeService, never()).updateRecipe(any());
  }

  @Test
  void testDeleteRecipe() {
    doNothing().when(recipeService).deleteRecipe(1L);

    ResponseEntity<Void> response = recipeController.deleteRecipe(1L);

    assertNotNull(response);
    assertEquals(204, response.getStatusCodeValue());

    verify(recipeService, times(1)).deleteRecipe(1L);
  }

  @Test
  void testAddCommentToRecipe() {
    Recipe recipe = new Recipe();
    Comment comment = new Comment();
    User user = new User();
    CommentDto commentDto = new CommentDto();

    when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testUser");
    when(userService.findByUsername("testUser")).thenReturn(user);
    when(commentService.addComment(comment)).thenReturn(commentDto);

    ResponseEntity<CommentDto> response = recipeController.addComment(1L, comment);

    assertNotNull(response);
    assertEquals(201, response.getStatusCodeValue());
    assertEquals(commentDto, response.getBody());

    verify(recipeService, times(1)).getRecipeById(1L);
    verify(userService, times(1)).findByUsername("testUser");
    verify(commentService, times(1)).addComment(comment);
  }

  @Test
  void testAddRatingToRecipe() {
    Recipe recipe = new Recipe();
    Rating rating = new Rating();
    User user = new User();

    when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(recipe));
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn("testUser");
    when(userService.findByUsername("testUser")).thenReturn(user);
    when(ratingService.addRating(rating)).thenReturn(rating);

    ResponseEntity<Rating> response = recipeController.addRating(1L, rating);

    assertNotNull(response);
    assertEquals(201, response.getStatusCodeValue());
    assertEquals(rating, response.getBody());

    verify(recipeService, times(1)).getRecipeById(1L);
    verify(userService, times(1)).findByUsername("testUser");
    verify(ratingService, times(1)).addRating(rating);
  }
}
