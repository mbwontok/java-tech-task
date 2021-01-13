package com.rezdy.lunch.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class RecipeRepositoryImpl implements RecipeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Recipe> loadNonExpiredRecipes(LocalDate date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Recipe> criteriaQuery = cb.createQuery(Recipe.class);
        Root<Recipe> recipeRoot = criteriaQuery.from(Recipe.class);

        CriteriaQuery<Recipe> query = criteriaQuery.select(recipeRoot);

        Subquery<Recipe> nonExpiredIngredientSubquery = query.subquery(Recipe.class);
        Root<Recipe> nonExpiredIngredient = nonExpiredIngredientSubquery.from(Recipe.class);
        nonExpiredIngredientSubquery.select(nonExpiredIngredient);

        Predicate matchingRecipe = cb.equal(nonExpiredIngredient.get("title"), recipeRoot.get("title"));
        Predicate expiredIngredient = cb.greaterThan(nonExpiredIngredient.join("ingredients").get("useBy"), date);

        Predicate allNonExpiredIngredients = cb.exists(nonExpiredIngredientSubquery.where(matchingRecipe, expiredIngredient));

        return entityManager.createQuery(query.where(allNonExpiredIngredients)).getResultList();
    }
}
