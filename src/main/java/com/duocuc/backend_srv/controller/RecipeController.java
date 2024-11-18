package com.duocuc.backend_srv.controller;

import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.model.Photo;
import com.duocuc.backend_srv.model.Rating;
import com.duocuc.backend_srv.model.Recipe;
import com.duocuc.backend_srv.model.User;
import com.duocuc.backend_srv.service.CommentService;
import com.duocuc.backend_srv.service.RatingService;
import com.duocuc.backend_srv.service.RecipeService;
import com.duocuc.backend_srv.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

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

    // POST /recipes/{id}/comments
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long id, @RequestBody Comment comment) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtener el usuario logueado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Obtener nombre del usuario logueado
        User user = userService.findByUsername(username); // Método en UserService para buscar usuario

        comment.setUser(user);
        comment.setRecipe(recipeOpt.get());

        Comment savedComment = commentService.addComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    // POST /recipes/{id}/ratings
    @PostMapping("/{id}/ratings")
    public ResponseEntity<Rating> addRating(@PathVariable Long id, @RequestBody Rating rating) {
        Optional<Recipe> recipeOpt = recipeService.getRecipeById(id);
        if (recipeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Obtener el usuario logueado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        rating.setUser(user);
        rating.setRecipe(recipeOpt.get());

        Rating savedRating = ratingService.addRating(rating);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRating);
    }
}
