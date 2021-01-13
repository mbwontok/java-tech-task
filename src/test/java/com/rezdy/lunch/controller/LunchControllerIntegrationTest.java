package com.rezdy.lunch.controller;

import com.rezdy.lunch.repository.Recipe;
import com.rezdy.lunch.service.LunchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LunchController.class)
class LunchControllerIntegrationTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LunchService lunchService;

    @Test
    public void givenRecipes_whenGetRecipes_thenReturnJsonArray() throws Exception {
        LocalDate date = LocalDate.parse("2020-06-06", formatter);
        Recipe recipe1 = new Recipe("Omelette");
        Recipe recipe2 = new Recipe("Salad");
        when(lunchService.getNonExpiredRecipesOnDate(any(LocalDate.class)))
                .thenReturn(Arrays.asList(recipe1,recipe2));

        mvc.perform(get("/lunch?date=" + date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(recipe1.getTitle())))
                .andExpect(jsonPath("$[1].title", is(recipe2.getTitle())));
    }
}
