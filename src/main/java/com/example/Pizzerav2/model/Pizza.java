package com.example.Pizzerav2.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Pizza extends Prodotto {

    private List<String>  ingredienti=new ArrayList<String>();




    public Pizza(String nome, double prezzo) {
        super(nome, prezzo);
        ingredienti.add("base");


    }
    public void addIngred(String ingred){

        this.ingredienti.add(ingred);
    }
    public String listaIngredienti() {
        return String.join(", ", ingredienti);
    }

    @Override
    public String toString() {
        return "Pizza "
                + super.getNome()  +
                " ingredient: " + listaIngredienti() +
                " prezzo: " + super.getPrezzo()+"€";
    }

    public void setListaIngredienti(List<String> ingredientiPizza) {
        this.ingredienti = ingredientiPizza;

    }
}
