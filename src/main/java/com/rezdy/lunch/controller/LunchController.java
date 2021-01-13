package com.rezdy.lunch.controller;

import com.rezdy.java_tech_task.v0.generated.LunchApi;
import com.rezdy.java_tech_task.v0.model.generated.Recipe;
import com.rezdy.lunch.service.LunchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LunchController implements LunchApi {

    private LunchService lunchService;

    public LunchController(LunchService lunchService) {
        this.lunchService = lunchService;
    }

    public ResponseEntity<List<Recipe>> getRecipes(String date) {

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse(date)).stream()
                .map(this::mapRecipe)
                .collect(Collectors.toList());

        return ResponseEntity.ok(recipes);
    }

    private Recipe mapRecipe(com.rezdy.lunch.service.Recipe fromRecipe) {
        return new Recipe().title(fromRecipe.getTitle());
    }
}
