package com.rezdy.lunch.repository;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Recipe {

    @Id
    @Column(name = "TITLE")
    private String title;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe"),
            inverseJoinColumns = @JoinColumn(name = "ingredient"))
    private Set<Ingredient> ingredients;

    public Recipe() {}

    public Recipe(String title) {
        Assert.notNull(title, "title is missing");
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }
}
