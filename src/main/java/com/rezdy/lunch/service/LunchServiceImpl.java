package com.rezdy.lunch.service;

import com.rezdy.lunch.repository.Ingredient;
import com.rezdy.lunch.repository.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LunchServiceImpl implements LunchService {

    private final RecipeRepository recipeRepository;

    public LunchServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date) {
        return getNonExpiredRecipesOnDate(date, null);
    }

    public List<Recipe> getNonExpiredRecipesOnDate(LocalDate date, final Set<String> filterOutIngredients) {
        List<Recipe> nonExpiredRecipes = recipeRepository.loadNonExpiredRecipes(date);
        List<Recipe> recipes = filter(nonExpiredRecipes, filterOutIngredients);
        sortRecipes(recipes);
        return recipes;
    }

    @Override
    public Optional<Recipe> findRecipeByTitle(String title) {
        return recipeRepository.findById(title);
    }

    private void sortRecipes(List<Recipe> recipes ) {
        recipes.sort((r1, r2) -> {
                    LocalDate lowestR1 = r1.getIngredients().stream()
                            .map(Ingredient::getBestBefore)
                            .min(LocalDate::compareTo)
                            .orElse(LocalDate.MAX);
                    LocalDate lowestR2 = r2.getIngredients().stream()
                            .map(Ingredient::getBestBefore)
                            .min(LocalDate::compareTo)
                            .orElse(LocalDate.MAX);
                    return lowestR1.compareTo(lowestR2);
                }
        );
    }

    private List<Recipe> filter(List<Recipe> recipes, Set<String> filterOutIngredients) {
        if(filterOutIngredients == null) {
            return recipes;
        }
        return recipes.stream()
                .filter(recipe -> recipe.getIngredients().stream()
                        .map(Ingredient::getTitle)
                        .noneMatch(filterOutIngredients::contains))
                .collect(Collectors.toList());
    }

}
