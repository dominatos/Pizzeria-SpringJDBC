package com.example.Pizzerav2.model;

import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class Menu {
    private static Long id;
    List<Pizza> pizzaList = new ArrayList<>();
    List<Drink> drinkList= new ArrayList<>();

    private String nome;



    public Menu() {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Pizza> getPizzaList() {
        return this.pizzaList;
    }

    public void setPizzaList(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    public List<Drink> getDrinkList() {
        return this.drinkList;
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
    }


}
