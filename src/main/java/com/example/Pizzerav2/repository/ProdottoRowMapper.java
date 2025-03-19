package com.example.Pizzerav2.repository;

import com.example.Pizzerav2.model.Drink;
import com.example.Pizzerav2.model.Menu;
import com.example.Pizzerav2.model.Pizza;
import com.example.Pizzerav2.model.Prodotto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProdottoRowMapper implements RowMapper<Prodotto> {
    @Override
    public Prodotto mapRow(ResultSet rs, int rowNum) throws SQLException {
        String tipo = rs.getString("tipo_prodotto");

        if ("pizza".equalsIgnoreCase(tipo)) {
            Pizza pizza = new Pizza();
            pizza.setId(rs.getLong("id_prodotto"));
            pizza.setNome(rs.getString("prodotto_name"));
            pizza.setListaIngredienti(parseIngredienti(rs.getString("ingredienti_pizza")));
            pizza.setPrezzo(rs.getDouble("prezzo"));// Парсим ингредиенты
            return pizza;
        } else if ("drink".equalsIgnoreCase(tipo)) {
            Drink drink = new Drink();
            drink.setId(rs.getLong("id_prodotto"));
            drink.setNome(rs.getString("prodotto_name"));
            drink.setGradazione(rs.getDouble("gradi_drink")); //
            drink.setPrezzo(rs.getDouble("prezzo"));// Устанавливаем градусы напитка
            return drink;
        } else {
            // Общий случай для объектов типа Prodotto
            Prodotto prodotto = new Prodotto();
            prodotto.setId(rs.getLong("id_prodotto"));
            prodotto.setNome(rs.getString("prodotto_name"));
            prodotto.setPrezzo(rs.getDouble("prezzo"));
            return prodotto;
        }
    }

    private List<String> parseIngredienti(String ingredienti) {
        return ingredienti != null ? Arrays.asList(ingredienti.split(", ")) : new ArrayList<>();
    }
}

