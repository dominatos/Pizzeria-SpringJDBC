package com.example.Pizzerav2.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Drink extends Prodotto {

    private double gradazione;


    public Drink(String nome,Double gradazione, double prezzo) {
        super(nome, prezzo);



    }

    @Override
    public String toString() {
        return "Drink "
                + super.getNome()  +
                " Gradazione: " + gradazione +
                " prezzo: " + super.getPrezzo()+"\n";
    }
}
