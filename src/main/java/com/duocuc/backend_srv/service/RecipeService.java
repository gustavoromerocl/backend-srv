package com.duocuc.backend_srv.service;

import com.duocuc.backend_srv.model.Comment;
import com.duocuc.backend_srv.model.Recipe;
import com.duocuc.backend_srv.repository.RecipeRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Obtener todas las recetas
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // Obtener una receta por su ID
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    // Crear una nueva receta
    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Actualizar una receta existente
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    // Eliminar una receta por su ID
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new IllegalArgumentException("Recipe with ID " + id + " does not exist.");
        }
        recipeRepository.deleteById(id);
    }

    public List<Comment> getCommentsForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        return recipe.getComments();
    }

    public Double getAverageRatingForRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        return recipe.getAverageRating();
    }

}
