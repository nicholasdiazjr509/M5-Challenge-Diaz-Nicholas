package com.trilogyed.gamestorecatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private long id;
    @NotEmpty(message = "title for the console is required.")
    private  String title;
    @NotEmpty(message = "ESRB Rating of the game is required.")
    private  String esrbRating;
    @NotEmpty(message = "description is required.")
    private  String description;
    @NotNull(message = "Price for console is required.")
    @DecimalMax(value = "999.99", inclusive = true, message = "price for the item is above $999.99.")
    @DecimalMin(value = "1.00", inclusive = true, message = "The price for this items is less than $1.00.")
    private  BigDecimal price;
    @NotEmpty(message = "studio for the console is required.")
    private  String studio;
    @NotNull(message = "A quantity for the console is required.")
    @Max(value = 50000, message = "The maximum for quantity is 50000")
    @Min(value = 1, message = "The minimum quantity of 1 is required.")
    private  long quantity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && quantity == game.quantity && Objects.equals(title, game.title) && Objects.equals(esrbRating, game.esrbRating) && Objects.equals(description, game.description) && Objects.equals(price, game.price) && Objects.equals(studio, game.studio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, esrbRating, description, price, studio, quantity);
    }
}
