package com.rezdy.lunch.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeRepositoryIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    public void givenTwoNewRecipes_whenSaveRecipes_thenRecipesStored() {
        Recipe recipe1 = new Recipe("Omelette");
        Recipe recipe2 = new Recipe("Salad");

        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);
        List<Recipe> recipes = recipeRepository.findAll();

        assertNotNull(recipes);
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }

    @Test
    public void givenDateAndRecipes_whenLoadRecipes_thenOnlyNonExpiredRecipesFetched() {
        LocalDate date = LocalDate.parse("2030-02-02", formatter);
        Recipe recipe1 = new Recipe("Omelette")
                .setIngredients(Set.of(
                        createIngredient("Eggs","2030-12-31","2030-02-02"),
                        createIngredient("Milk","2030-12-31","2030-02-03")));
        Recipe recipe2 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2030-12-31","2030-02-02"),
                        createIngredient("Salad Dressing","2030-12-31","2030-02-03")));
        Recipe recipe3 = new Recipe("Hamburger")
                .setIngredients(Set.of(
                        createIngredient("Cheese","2030-12-31", "2030-01-01"),
                        createIngredient("Tomato","2030-12-31","2030-02-02")));
        recipeRepository.saveAll(Set.of(recipe1,recipe2,recipe3));

        List<Recipe> recipes = recipeRepository.loadNonExpiredRecipes(date);

        assertThat(recipes, hasSize(2));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }

    private Ingredient createIngredient(String name, String bestBeforeDate, String useByDate) {
        return new Ingredient(name,
                LocalDate.parse(bestBeforeDate, formatter),
                LocalDate.parse(useByDate, formatter));
    }

}
