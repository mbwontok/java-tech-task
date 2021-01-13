package com.rezdy.lunch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, String>, RecipeRepositoryCustom {

}
