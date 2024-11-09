package com.duocuc.backend_srv.controller;

import com.duocuc.backend_srv.model.Photo;
import com.duocuc.backend_srv.model.Recipe;
import com.duocuc.backend_srv.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // Obtener todas las recetas
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    // Obtener una receta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(id);
        return recipeOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // Crear una nueva receta
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe createdRecipe = recipeService.addRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    // Actualizar una receta existente
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @RequestBody Recipe updatedRecipe) {

        Optional<Recipe> existingRecipeOpt = recipeService.getRecipeById(id);
        if (existingRecipeOpt.isPresent()) {
            Recipe existingRecipe = existingRecipeOpt.get();

            // Actualizar detalles de la receta
            existingRecipe.setTitle(updatedRecipe.getTitle());
            existingRecipe.setDescription(updatedRecipe.getDescription());
            existingRecipe.setCookTime(updatedRecipe.getCookTime());
            existingRecipe.setIngredients(updatedRecipe.getIngredients());
            existingRecipe.setInstructions(updatedRecipe.getInstructions());
            existingRecipe.setDifficulty(updatedRecipe.getDifficulty());

            // Actualizar foto si existe
            Photo photo = updatedRecipe.getPhoto();
            if (photo != null) {
                existingRecipe.setPhoto(photo);
            }

            Recipe updated = recipeService.updateRecipe(existingRecipe);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar una receta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
