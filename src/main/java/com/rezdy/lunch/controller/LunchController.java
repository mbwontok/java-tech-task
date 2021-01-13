package com.rezdy.lunch.controller;

import com.rezdy.java_tech_task.v0.generated.LunchApi;
import com.rezdy.java_tech_task.v0.model.generated.Recipe;
import com.rezdy.lunch.repository.Ingredient;
import com.rezdy.lunch.service.LunchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LunchController implements LunchApi {

    private final LunchService lunchService;

    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    public ResponseEntity<List<Recipe>> getRecipes(String date) {

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date)).stream()
                .map(this::mapRecipe)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    public ResponseEntity<Recipe> findRecipe(String title) {

        return lunchService.findRecipeByTitle(title)
            .map(this::mapRecipe)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Recipe>> searchRecipes(String date, List<String> ingredientsSubmission) {

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date), new HashSet<>(ingredientsSubmission))
                .stream()
                .map(this::mapRecipe)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipes);
    }

    private Recipe mapRecipe(com.rezdy.lunch.repository.Recipe fromRecipe) {
        Recipe recipe = new Recipe()
                .title(fromRecipe.getTitle());
        if(fromRecipe.getIngredients() != null) {
            recipe.ingredients(fromRecipe.getIngredients().stream().map(Ingredient::getTitle).collect(Collectors.toList()));
        }
        return recipe;
    }
}
