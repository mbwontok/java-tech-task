package com.rezdy.lunch.repository;

import java.time.LocalDate;
import java.util.List;

public interface RecipeRepositoryCustom {

    List<Recipe> loadNonExpiredRecipes(LocalDate date);

}
