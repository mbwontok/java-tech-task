package com.rezdy.lunch.controller;

import com.rezdy.lunch.service.LunchServiceImpl;
import com.rezdy.lunch.repository.Recipe;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class LunchControllerTest {

    @Mock
    private LunchServiceImpl lunchService;

    @InjectMocks
    private LunchController lunchController;

    @Test
    public void givenRecipes_whenGetRecipes_thenReturnJsonArray() {
        Recipe recipe1 = new Recipe("Omelette");
        Recipe recipe2 = new Recipe("Salad");
        when(lunchService.getNonExpiredRecipesOnDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(recipe1,recipe2));

        ResponseEntity<List<com.rezdy.java_tech_task.v0.model.generated.Recipe>> response =
                lunchController.getRecipes(LocalDate.now().toString());

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));
        List<com.rezdy.java_tech_task.v0.model.generated.Recipe> responseBody = response.getBody();
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.size(), is(2));
        assertThat(responseBody.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(responseBody.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }

    @Test
    public void givenNoRecipe_whenGetRecipesWithAnyName_thenReturnNotFound() {
        when(lunchService.findRecipeByTitle(any(String.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<com.rezdy.java_tech_task.v0.model.generated.Recipe> response =
                lunchController.findRecipe("Salad");

        assertThat(response.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void givenRecipes_whenSearchRecipes_thenReturnJsonArray() {
        Recipe recipe1 = new Recipe("Omelette");
        Recipe recipe2 = new Recipe("Salad");
        when(lunchService.getNonExpiredRecipesOnDate(any(LocalDate.class), anySet()))
                .thenReturn(Arrays.asList(recipe1,recipe2));

        ResponseEntity<List<com.rezdy.java_tech_task.v0.model.generated.Recipe>> response =
                lunchController.searchRecipes(LocalDate.now().toString(), List.of("Hamburger"));

        assertThat(response.getStatusCodeValue(), is(HttpStatus.OK.value()));
        List<com.rezdy.java_tech_task.v0.model.generated.Recipe> responseBody = response.getBody();
        assertThat(responseBody, notNullValue());
        assertThat(responseBody.size(), is(2));
        assertThat(responseBody.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe1.getTitle())), is(true));
        assertThat(responseBody.stream().anyMatch(recipe -> recipe.getTitle().equals(recipe2.getTitle())), is(true));
    }
}
