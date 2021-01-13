package com.rezdy.lunch.service;

import com.rezdy.lunch.repository.Recipe;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LunchService {

    List<Recipe> getNonExpiredRecipesOnDate(LocalDate date);

    List<Recipe> getNonExpiredRecipesOnDate(LocalDate date, Set<String> filterOutIngredients);

    Optional<Recipe> findRecipeByTitle(String title);

}
