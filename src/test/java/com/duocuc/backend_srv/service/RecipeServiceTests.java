package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.model.Rating;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.model.Recipe;
import com.duocuc.backend_srv.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RatingServiceTest {

  @InjectMocks
  private RatingService ratingService;

  @Mock
  private RatingRepository ratingRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddRatingSuccess() {
    // Mock User
    User mockUser = new User();
    mockUser.setId(1L);
    mockUser.setUsername("testuser");

    // Mock Recipe
    Recipe mockRecipe = new Recipe();
    mockRecipe.setId(1L);
    mockRecipe.setTitle("Test Recipe");

    // Mock Rating
    Rating mockRating = new Rating();
    mockRating.setId(1L);
    mockRating.setUser(mockUser);
    mockRating.setRecipe(mockRecipe);
    mockRating.setValue(5);

    // Simulate repository save
    when(ratingRepository.save(any(Rating.class))).thenReturn(mockRating);

    // Call the method to test
    Rating result = ratingService.addRating(mockRating);

    // Verify repository interaction
    verify(ratingRepository, times(1)).save(mockRating);

    // Assert the returned Rating
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals(5, result.getValue());
    assertEquals("testuser", result.getUser().getUsername());
    assertEquals("Test Recipe", result.getRecipe().getTitle());
  }
}
