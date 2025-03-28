package com.example.Pizzerav2.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prodotto  {
    private Long id;
    private String nome;
    private Double prezzo;
    private Menu menu;

    public Prodotto(String nome, Double prezzo, Menu menu) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.menu = menu;
    }

    public Prodotto(String nome, double prezzo) {
    }

}
