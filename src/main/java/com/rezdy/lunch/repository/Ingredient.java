package com.rezdy.lunch.repository;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Ingredient {

    @Id
    @Column(name = "TITLE")
    private String title;

    @Column(name = "BEST_BEFORE")
    private LocalDate bestBefore;

    @Column(name = "USE_BY")
    private LocalDate useBy;

    public Ingredient() {}

    public Ingredient(String title, LocalDate bestBefore, LocalDate useBy) {
        Assert.notNull(title, "title is missing");
        this.title = title;
        this.bestBefore = bestBefore;
        this.useBy = useBy;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    public LocalDate getUseBy() {
        return useBy;
    }
}
