package com.rezdy.lunch.service;

import com.rezdy.lunch.repository.Ingredient;
import com.rezdy.lunch.repository.Recipe;
import com.rezdy.lunch.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LunchServiceIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private LunchService lunchService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    public void givenNonExpiredRecipes_whenGetRecipesOnDate_thenRecipesAvailable() {
        Recipe recipe1 = new Recipe("Hamburger")
                .setIngredients(singleton(createIngredient("Cheese","2030-12-31", "2030-01-01")));
        Recipe recipe2 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2030-12-31","2030-01-01"),
                        createIngredient("Tomato","2030-12-31","2030-01-01")));
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse("2000-01-01", formatter));

        assertNotNull(recipes);
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }

    @Test
    public void givenExpiredRecipes_whenGetRecipesOnDate_thenNoRecipesAvailable() {
        Recipe recipe1 = new Recipe("Hamburger")
                .setIngredients(singleton(createIngredient("Cheese","2030-12-31", "2030-01-01")));
        Recipe recipe2 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2030-12-31","2030-01-01"),
                        createIngredient("Tomato","2030-12-31","2030-01-01")));
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse("2030-01-02", formatter), null);

        assertThat(recipes, hasSize(0));
    }

    @Test
    public void givenNonExpiredRecipes_whenGetRecipesOnDateWithFilter_thenReturnNonExpiredRecipeOnly() {
        Recipe recipe1 = new Recipe("Hamburger")
                .setIngredients(singleton(createIngredient("Cheese","2030-12-31", "2030-01-01")));
        Recipe recipe2 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2030-12-31","2030-01-01"),
                        createIngredient("Tomato","2030-12-31","2030-01-01")));
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse("2000-01-01", formatter), singleton("Lettuce"));

        assertThat(recipes, hasSize(1));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
    }

    @Test
    public void givenUnsortedRecipes_whenGetRecipes_thenRecipesSortedByBestBeforeAreAvailable() {
        LocalDate date = LocalDate.parse("2020-03-01", formatter);
        Recipe recipe1 = new Recipe("Hamburger")
                .setIngredients(Set.of(
                        createIngredient("Ham","2020-04-01","2030-01-01"),
                        createIngredient("Cheese","2020-04-02","2030-01-01")));
        Recipe recipe2 = new Recipe("Omelette")
                .setIngredients(Set.of(
                        createIngredient("Milk","2020-03-01","2030-01-01"),
                        createIngredient("Eggs","2020-03-02","2030-01-01")));
        Recipe recipe3 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2020-01-01","2030-01-01"),
                        createIngredient("Tomato","2020-01-02","2030-01-01")));
        Recipe recipe4 = new Recipe("Hotdog")
                .setIngredients(Set.of(
                        createIngredient("Cucumber","2020-02-01","2030-01-01"),
                        createIngredient("Ketchup","2020-02-02","2030-01-01")));
        recipeRepository.saveAll(Set.of(recipe1, recipe2, recipe3, recipe4));

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(date);

        assertThat(recipes, hasSize(4));
        assertThat(recipes.get(0).getTitle(), is("Salad"));
        assertThat(recipes.get(1).getTitle(), is("Hotdog"));
        assertThat(recipes.get(2).getTitle(), is("Omelette"));
        assertThat(recipes.get(3).getTitle(), is("Hamburger"));
    }

    @Test
    public void givenRecipesWithTheSameIngredient_whenGetRecipesOnDate_thenRecipesAvailable() {
        Recipe recipe1 = new Recipe("Hamburger")
                .setIngredients(Set.of(
                        createIngredient("Cheese","2030-12-31", "2030-01-01"),
                        createIngredient("Eggs","2030-12-31","2030-01-01")));
        Recipe recipe2 = new Recipe("Salad")
                .setIngredients(Set.of(
                        createIngredient("Lettuce","2030-12-31","2030-01-01"),
                        createIngredient("Eggs","2030-12-31","2030-01-01")));
        recipeRepository.save(recipe1);
        recipeRepository.save(recipe2);

        List<Recipe> recipes = lunchService.getNonExpiredRecipesOnDate(LocalDate.parse("2000-01-01", formatter));

        assertNotNull(recipes);
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(recipes.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }

    private Ingredient createIngredient(String name, String bestBeforeDate, String useByDate) {
        return new Ingredient(name,
                LocalDate.parse(bestBeforeDate, formatter),
                LocalDate.parse(useByDate, formatter));
    }

}
