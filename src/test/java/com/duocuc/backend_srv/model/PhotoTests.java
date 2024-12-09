package com.duocuc.backend_srv.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class PhotoTests {

  @Test
  void testPhotoConstructorAndGetters() {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Photo photo = new Photo(1L, "http://example.com/photo.jpg", "Sample description", recipe);

    assertEquals(1L, photo.getId());
    assertEquals("http://example.com/photo.jpg", photo.getUrl());
    assertEquals("Sample description", photo.getDescription());
    assertEquals(recipe, photo.getRecipe());
  }

  @Test
  void testPhotoSetters() {
    Recipe recipe = new Recipe();
    recipe.setId(2L);

    Photo photo = new Photo();
    photo.setId(2L);
    photo.setUrl("http://example.com/newphoto.jpg");
    photo.setDescription("New description");
    photo.setRecipe(recipe);

    assertEquals(2L, photo.getId());
    assertEquals("http://example.com/newphoto.jpg", photo.getUrl());
    assertEquals("New description", photo.getDescription());
    assertEquals(recipe, photo.getRecipe());
  }

  @Test
  void testPhotoNoArgsConstructor() {
    Photo photo = new Photo();
    assertNull(photo.getId());
    assertNull(photo.getUrl());
    assertNull(photo.getDescription());
    assertNull(photo.getRecipe());
  }
}
